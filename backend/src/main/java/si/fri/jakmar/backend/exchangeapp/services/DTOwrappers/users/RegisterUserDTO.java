package si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users;

public class RegisterUserDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String studentNumber;
    private String description;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(String name, String surname, String email, String password, String studentNumber, String description) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.studentNumber = studentNumber;
        this.description = description;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
