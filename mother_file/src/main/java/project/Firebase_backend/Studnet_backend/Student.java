package project.Firebase_backend.Studnet_backend;

import project.Firebase_backend.User_backend.User;

public class Student extends User {

    // ===== STATUS CONSTANTS =====
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_RESTRICTED = "RESTRICTED";
    public static final String STATUS_BLOCKED = "BLOCKED";
    public static final String STATUS_OVERDUE = "OVERDUE";

    // ===== STUDENT-SPECIFIC FIELDS =====
    private int borrowedCount;
    private double penaltyAmount;
    private String status;              // ACTIVE, RESTRICTED, BLOCKED, OVERDUE
    private int offenseCount;
    private long restrictionUntil;      // epoch millis (0 = no restriction)

    // ===== REQUIRED FOR FIREBASE =====
    public Student() {
        super();
    }

    // ===== MAIN CONSTRUCTOR =====
    public Student(
            String firstName,
            String surname,
            String email,
            String studentId,
            String password
    ) {
        super(firstName, surname, email, studentId, password);
        this.borrowedCount = 0;
        this.penaltyAmount = 0.0;
        this.offenseCount = 0;
        this.restrictionUntil = 0;
        this.status = STATUS_ACTIVE;
    }

    // ===== GETTERS =====
    public int getBorrowedCount() {
        return borrowedCount;
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public String getStatus() {
        return status;
    }

    public int getOffenseCount() {
        return offenseCount;
    }

    public long getRestrictionUntil() {
        return restrictionUntil;
    }

    // ===== STATUS HELPERS =====
    public boolean isBlocked() {
        return STATUS_BLOCKED.equals(status);
    }

    public boolean isRestricted() {
        return STATUS_RESTRICTED.equals(status)
                && System.currentTimeMillis() < restrictionUntil;
    }

    public boolean isActive() {
        return STATUS_ACTIVE.equals(status);
    }

    public boolean canBorrow() {
        return isActive()
                && borrowedCount < 3
                && penaltyAmount <= 0;
    }

    // ===== SETTERS =====
    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public void setPenaltyAmount(double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOffenseCount(int offenseCount) {
        this.offenseCount = offenseCount;
    }

    public void setRestrictionUntil(long restrictionUntil) {
        this.restrictionUntil = restrictionUntil;
    }
}
