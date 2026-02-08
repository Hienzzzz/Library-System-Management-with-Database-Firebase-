package project.Firebase_backend.Studnet_backend;

public class Student {

    private String studentId;
    private String fullName;
    private String email;
    private String course;
    private int yearLevel;
    private int borrowedCount;
    private String status; // ACTIVE / BLOCKED

    public Student() {}

    public Student(String studentId, String fullName, String email,
                   String course, int yearLevel) {

        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.course = course;
        this.yearLevel = yearLevel;
        this.borrowedCount = 0;
        this.status = "ACTIVE";
    }

    // ===== GETTERS =====
    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public int getYearLevel() { return yearLevel; }
    public int getBorrowedCount() { return borrowedCount; }
    public String getStatus() { return status; }

    // ===== SETTERS =====
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
    public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }
    public void setBorrowedCount(int borrowedCount) { this.borrowedCount = borrowedCount; }
    public void setStatus(String status) { this.status = status; }
}
