package project.Firebase_backend.Studnet_backend;

import project.Firebase_backend.User_backend.User;


public class Student extends User {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_RESTRICTED = "RESTRICTED";
    public static final String STATUS_BLOCKED = "BLOCKED";
    public static final String STATUS_OVERDUE = "OVERDUE";

    private int borrowedCount;
    private double penaltyAmount;
    private String status; // ACTIVE, RESTRICTED, BLOCKED
    private int offenseCount;
    private long restrictionUntil;
    private boolean blocked;

    public Student() {
        super();
    }

    public Student(
        String firstName,
        String surname,
        String email,
        String id,
        String password
    ) {
        super(firstName, surname, email, id, password);
        this.borrowedCount = 0;
        this.penaltyAmount = 0.0;
        this.offenseCount = 0;
        this.restrictionUntil = 0;
        this.blocked = false;
        this.status = "ACTIVE";
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

    public boolean isBlocked() {
        return blocked;
    }
    public boolean isRestricted() {
        return !blocked && System.currentTimeMillis() < restrictionUntil;
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

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
