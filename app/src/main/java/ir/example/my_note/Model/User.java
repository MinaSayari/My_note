package ir.example.my_note.Model;

public class User {

    private String username;
    private String email;
    private String password;
    private String id;

    public User(){

    }

    // Constructor
    public User(String username, String email, String password, String id) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
    }


    // Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

