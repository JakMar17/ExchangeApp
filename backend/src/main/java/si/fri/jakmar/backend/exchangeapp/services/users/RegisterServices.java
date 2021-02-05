package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserRegistrationStage;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserTypeEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.RegisterUserDTO;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserExistsException;

@Component
public class RegisterServices {

    @Autowired
    private UserRepository userRepository;

    public Boolean registerNewUser(RegisterUserDTO user) throws UserExistsException {
        if(userRepository.findUsersByEmail(user.getEmail()).size() != 0)
            throw new UserExistsException("Uporabnik s tem epoštnim naslovom že obstaja");

        if(user.getStudentNumber() != null && userRepository.findUsersByPersonalNumber(user.getStudentNumber()).size() != 0)
            throw new UserExistsException("Študent s to vpisno številko že obstaja");

        userRepository.save(this.registerUserDTOToUserEntity(user));
        return true;
    }

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

    private String generatePersonalNumberForProfessor() {
        var profs = userRepository.getLastInsertedProfessor();
        if(profs == null || profs.size() == 0)
            return "p_000001";

        String lastId = profs.get(0).getPersonalNumber();
        int intPart = Integer.parseInt(lastId.substring(lastId.length() - 6));

        return "p_" + String.format("%06d", (intPart+1));
    }
}
