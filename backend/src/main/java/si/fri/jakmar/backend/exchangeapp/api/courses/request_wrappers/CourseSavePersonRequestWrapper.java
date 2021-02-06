package si.fri.jakmar.backend.exchangeapp.api.courses.request_wrappers;

public class CourseSavePersonRequestWrapper {
    private String email;
    private String studentNumber;

    public CourseSavePersonRequestWrapper() {
    }

    public CourseSavePersonRequestWrapper(String email, String studentNumber) {
        this.email = email;
        this.studentNumber = studentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
