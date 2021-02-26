package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.constants.UrlConstants;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserPasswordResetEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserRegistrationStage;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserTypeEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.user.UserPasswordResetRepository;
import si.fri.jakmar.backend.exchangeapp.database.repositories.user.UserRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.users.RegisterUserDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.MailException;
import si.fri.jakmar.backend.exchangeapp.exceptions.RequestInvalidException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.functions.RandomizerService;
import si.fri.jakmar.backend.exchangeapp.functions.RequestEmailCreator;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserExistsException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.StreamSupport;

@Component
public class RegisterServices {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPasswordResetRepository userPasswordResetRepository;
    @Autowired
    private RequestEmailCreator requestEmailCreator;

    /**
     * takes register data, checks them and registers user
     * TODO: send email for confirmation
     * @param user register data
     * @return true if user has been registered in system
     * @throws UserExistsException user with given data already exists
     */
    public Boolean registerNewUser(RegisterUserDTO user) throws Exception {
        if(userRepository.findUsersByEmail(user.getEmail()).size() != 0)
            throw new UserExistsException("Uporabnik s tem epoštnim naslovom že obstaja");

        if(user.getStudentNumber() != null && userRepository.findUsersByPersonalNumber(user.getStudentNumber()).size() != 0)
            throw new UserExistsException("Študent s to vpisno številko že obstaja");

        var userEntity = this.registerUserDTOToUserEntity(user);
        requestEmailCreator.sendEmailConfirmation(userEntity.getEmail(), userEntity.getConfirmationString());
        userRepository.save(userEntity);

        return true;
    }

    public void confirmEmail(String confirmationId) throws DataNotFoundException {
        var user = userRepository.getUserByConfirmationString(confirmationId);
        if(user == null)
            throw new DataNotFoundException("Uporabnik bodisi ne obstaja ali je registracijo že potrdil");
        user.setConfirmationString(null);
        user.setRegistrationStatus(new UserRegistrationStage(2));
        userRepository.save(user);
    }

    public void createPasswordResetRequest(String email) throws DataNotFoundException, IOException, MailException {
        var users = userRepository.findUsersByEmail(email);
        if(users.isEmpty())
            throw new DataNotFoundException("Uporabnik s podanim emailom ne obstaja");
        var user = users.get(0);
        var requestId = RandomizerService.createRandomStringWithLength(
                StreamSupport.stream(userPasswordResetRepository.findAll().spliterator(), false)
                    .map(UserPasswordResetEntity::getResetKey),
                64
        );
        var resetRequest = new UserPasswordResetEntity(requestId, user);
        requestEmailCreator.sendEmailPasswordReset(email, requestId);
        userPasswordResetRepository.save(resetRequest);
    }

    public void updatePasswordForUser(String email, String resetId, String newPassword) throws DataNotFoundException, RequestInvalidException {
        var users = userRepository.findUsersByEmail(email);
        if(users.isEmpty())
            throw new DataNotFoundException("Ne najdem uporabnika");

        var resetRequest = userPasswordResetRepository.getByResetKey(resetId).orElseThrow(() -> new RequestInvalidException("Zahteva za spremebo gela ne obstaja"));
        
        if(!resetRequest.isActive() || !email.equals(resetRequest.getUser().getEmail()))
            throw new RequestInvalidException("Napaka pri obdelavi zahteve");

        var user = users.get(0);
        user.setPassword(newPassword);
        userRepository.save(user);
        resetRequest.setUsed(true);
        userPasswordResetRepository.save(resetRequest);
    }

    /**
     * casts RegisterUserDTO to UserEntity
     * @param registerUserDTO DTO to cast
     * @return casted UserEntity
     */
    private UserEntity registerUserDTOToUserEntity(RegisterUserDTO registerUserDTO) throws Exception {
        if(registerUserDTO.getName() == null || registerUserDTO.getSurname() == null || registerUserDTO.getEmail() == null)
            throw new Exception("Niso izpoljnjeni vsi potrebni podatki");

        int userType = 4;
        if(registerUserDTO.getEmail().contains("@student.uni-lj.si"))
            userType = 3;
        else if(registerUserDTO.getEmail().contains("@fri1.uni-lj.si"))
            userType = 2;

        return new UserEntity(
                registerUserDTO.getEmail(),
                registerUserDTO.getName(),
                registerUserDTO.getSurname(),
                registerUserDTO.getPassword(),
                registerUserDTO.getStudentNumber() != null ? registerUserDTO.getStudentNumber() : generatePersonalNumberForProfessor(),
                generateConfirmationId(),
                new UserTypeEntity(userType),
                new UserRegistrationStage(1)
        );
    }

    /**
     * generates personal number for PROFESSORs, "p_" + 6 digits
     * @return generated personal number
     */
    private String generatePersonalNumberForProfessor() {
        var profs = userRepository.getLastInsertedProfessor();
        if(profs == null || profs.size() == 0)
            return "p_000001";

        String lastId = profs.get(0).getPersonalNumber();
        int intPart = Integer.parseInt(lastId.substring(lastId.length() - 6));

        return "p_" + String.format("%06d", (intPart+1));
    }



    private String generateConfirmationId() {
        var confirmationIds = StreamSupport
                .stream(userRepository.findAll().spliterator(), false)
                .map(UserEntity::getConfirmationString);

        return RandomizerService.createRandomStringWithLength(confirmationIds, 32);
    }
}
