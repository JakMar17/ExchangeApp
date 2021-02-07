package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users;

import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.courses.CourseDTO;

import java.util.List;

public class UserDTO {
    private String email;
    private String name;
    private String surname;
    private String personalNumber;
    private String userType;
    private List<CourseDTO> myCourses;

    public UserDTO() {
    }

    public UserDTO(String email, String name, String surname, String personalNumber, String userType, List<CourseDTO> myCourses) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.personalNumber = personalNumber;
        this.userType = userType;
        this.myCourses = myCourses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<CourseDTO> getMyCourses() {
        return myCourses;
    }

    public void setMyCourses(List<CourseDTO> myCourses) {
        this.myCourses = myCourses;
    }
}
