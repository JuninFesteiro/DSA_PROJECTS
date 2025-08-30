package User;

// No semicolon after package
public class UserNodeBST { // User Node BST class

    // Fields
    private User data; // User ka data
    private String email; // BST ordering ke liye key
    private UserNodeBST left; // Left child
    private UserNodeBST right; // Right child

    // Constructors
    public UserNodeBST(User data) { // Constructor
        if (data == null || data.getEmail() == null) {
            throw new IllegalArgumentException("User or email cannot be null");
        }
        this.data = data;
        this.email = data.getEmail();
        this.left = null;
        this.right = null;
    }

    // Methods
    // Getters and Setters
    public User getData() { // Data lena
        return data;
    }

    public void setData(User data) { // Data set karna
        if (data == null || data.getEmail() == null) {
            throw new IllegalArgumentException("User or email cannot be null");
        }
        this.data = data;
        this.email = data.getEmail();
    }

    public String getEmail() { // Email lena
        return email;
    }

    public UserNodeBST getLeft() { // Left child lena
        return left;
    }

    public void setLeft(UserNodeBST left) { // Left child set karna
        this.left = left;
    }

    public UserNodeBST getRight() { // Right child lena
        return right;
    }

    public void setRight(UserNodeBST right) { // Right child set karna
        this.right = right;
    }
}