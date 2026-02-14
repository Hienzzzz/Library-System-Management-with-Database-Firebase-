package project.Firebase_backend.User_backend;

public class User {

    private String id;
    private String role;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String status;
    private long createdAt;
    private String profileImageUrl;
    private String uid;


    // Required empty constructor for Firebase
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
    public String getId() { 
        return id; 
    }
    public String getRole() { 
        return role;
     }
    public String getEmail() { 
        return email; 
    }
    public String getFullName() {
        return fullName; 
    }
    public String getStatus() {
         return status; 
        }

    public String getProfileImageUrl() {
    return profileImageUrl;
    }
    public String getFirstName() {
    return firstName;
}

    public String getLastName() {
        return lastName;
    }

    public String getUid() {
        return uid;
    }



    public void setStatus(String status){
        this.status = status;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}




/*

Do you think this is possible to code using pure Java and Firebase?

I have an idea: I will use a JTable with a scroll bar, and for the card design, I’ll use a plain card background as a JLabel background or a JPanel. What do you think? Do you have any suggestions?*/