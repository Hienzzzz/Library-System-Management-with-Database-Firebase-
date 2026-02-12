package project.Firebase_backend.User_backend;

public class User {

    private String id;
    private String role;
    private String email;
    private String fullName;
    private String password;
    private String status;
    private long createdAt;

    // Required empty constructor for Firebase
    public User() {}

    public User(String id, String role, String email,
                String fullName, String password) {

        this.id = id;
        this.role = role;
        this.email = email.toLowerCase();
        this.fullName = capitalizeWords(fullName);
        this.password = password;
        this.status = null;
        this.createdAt = System.currentTimeMillis();
    }

    // Capitalize helper
    private String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
        }

        return result.toString().trim();
    }

    // Getters
    public String getId() { return id; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getStatus() { return status; }
}
