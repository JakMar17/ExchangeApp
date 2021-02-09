package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserRegistrationStage;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserTypeEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.users.RegisterUserDTO;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserExistsException;

@Component
public class RegisterServices {

    @Autowired
    private UserRepository userRepository;

    /**
     * takes register data, checks them and registers user
     * TODO: send email for confirmation
     * @param user register data
     * @return true if user has been registered in system
     * @throws UserExistsException user with given data already exists
     */
    public Boolean registerNewUser(RegisterUserDTO user) throws UserExistsException {
        if(userRepository.findUsersByEmail(user.getEmail()).size() != 0)
            throw new UserExistsException("Uporabnik s tem epoštnim naslovom že obstaja");

        if(user.getStudentNumber() != null && userRepository.findUsersByPersonalNumber(user.getStudentNumber()).size() != 0)
            throw new UserExistsException("Študent s to vpisno številko že obstaja");

        userRepository.save(this.registerUserDTOToUserEntity(user));
        return true;
    }

    /**
     * casts RegisterUserDTO to UserEntity
     * @param registerUserDTO DTO to cast
     * @return casted UserEntity
     */
    private UserEntity registerUserDTOToUserEntity(RegisterUserDTO registerUserDTO) {
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
                new UserRegistrationStage(1),
                new UserTypeEntity(userType)
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
}
