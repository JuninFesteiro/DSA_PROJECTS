package User;

import Pet.Pet;

public abstract class User implements Interactable {

    // Static 
    private static int totalUser = 0; // Kul users ki ginti

    // Fields
    protected String userName; // User ka naam
    protected String password; // User ka password
    protected String email; // User ka email
    protected int age; // User ki umar
    protected char role; // User ka role
    protected long lastLogin; // User ka akhri login time

    // Constructors
    public User(String userName, String password, String email, int age, char role) { // Constructor
        // boolean result = CheckUser(email); // Email check karne ka commented code
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = role;
        this.lastLogin = 0;
        totalUser++;
    }

    // Getters
    public String getUserName() { // User ka naam lena
        return userName;
    }

    public String getPassword() { // User ka password lena
        return password;
    }

    public int getAge() { // User ki umar lena
        return age;
    }

    public char getRol() { // User ka role lena
        return role;
    }

    public String getEmail() { // User ka email lena
        return email;
    }

    public long getLastLogin() { // User ka akhri login time lena
        return lastLogin;
    }

    // Setters
    public void setUserName(String userName) { // User ka naam set karna
        this.userName = userName;
    }

    public void setEmail(String email) { // User ka email set karna
        this.email = email;
    }

    public void setPassword(String password) { // User ka password set karna
        this.password = password;
    }

    public void setAge(int age) { // User ki umar set karna
        this.age = age;
    }

    public void setRol(char role) { // User ka role set karna
        this.role = role;
    }

    public void setLastLogin(long lastLogin) { // User ka akhri login time set karna
        this.lastLogin = lastLogin;
    }

    // Static
    public static int getTotalUser() { // Kul users ki ginti lena
        return totalUser;
    }

    // Abstract
    // Interact with pet
    @Override
    public abstract void interactWithPet(Pet pet); // Pet ke saath interact karne ka abstract method

    @Override
    public String toString() { // User ki details display karna
        return "\n___User Details__\n Name: " + userName + ", Email: " + email + ", Password: " + password + ", Age: " + age + ", Role: " + role;
    }
}