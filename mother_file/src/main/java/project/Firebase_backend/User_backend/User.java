package project.Firebase_backend.User_backend;

/* =========================================================
 * ======================== USER MODEL =====================
 * =========================================================
 * Represents a User entity stored in Firebase.
 * Used for:
 * - Registration
 * - Profile storage
 * - Role management
 * ========================================================= */

public class User {

    /* =====================================================
     * ===================== IDENTIFIERS ====================
     * ===================================================== */

    // 🔑 Student ID / Internal ID
    private String id;

    // 🔐 Firebase UID
    private String uid;


    /* =====================================================
     * ===================== ACCOUNT INFO ===================
     * ===================================================== */

    private String role;
    private String email;
    private String status;
    private long createdAt;


    /* =====================================================
     * ===================== PERSONAL INFO ==================
     * ===================================================== */

    private String firstName;
    private String lastName;
    private String fullName;


    /* =====================================================
     * ===================== PROFILE DATA ===================
     * ===================================================== */

    private String profileImageUrl;


    /* =====================================================
     * ===================== CONSTRUCTORS ===================
     * ===================================================== */

    // 🔴 Required empty constructor for Firebase
    public User() {}


    public User(String id,
                String role,
                String email,
                String firstName,
                String lastName) {

        this.id = id;
        this.role = role;
        this.email = email.toLowerCase();

        this.firstName = capitalizeWords(firstName);
        this.lastName = capitalizeWords(lastName);
        this.fullName = this.firstName + " " + this.lastName;

        this.status = "ACTIVE";
        this.createdAt = System.currentTimeMillis();
        this.profileImageUrl = null;
    }


    /* =====================================================
     * ===================== HELPER METHODS =================
     * ===================================================== */

    // Capitalize first letter of each word
    private String capitalizeWords(String input) {

        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim()
                              .toLowerCase()
                              .split("\\s+");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
        }

        return result.toString().trim();
    }


    /* =====================================================
     * ======================== GETTERS =====================
     * ===================================================== */

    // ===== ID =====
    public String getId() {
        return id;
    }

    // ===== UID =====
    public String getUid() {
        return uid;
    }

    // ===== Role =====
    public String getRole() {
        return role;
    }

    // ===== Email =====
    public String getEmail() {
        return email;
    }

    // ===== Full Name =====
    public String getFullName() {
        return fullName;
    }

    // ===== First Name =====
    public String getFirstName() {
        return firstName;
    }

    // ===== Last Name =====
    public String getLastName() {
        return lastName;
    }

    // ===== Status =====
    public String getStatus() {
        return status;
    }

    // ===== Profile Image =====
    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    /* =====================================================
     * ======================== SETTERS =====================
     * ===================================================== */

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
