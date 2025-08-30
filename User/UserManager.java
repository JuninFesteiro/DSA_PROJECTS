package User; // User package define karta hai

import Event.EventManager; // EventManager class import karta hai
import Pet.PetManager; // PetManager class import karta hai
import Sanctuary.Sanctuary; // Sanctuary class import karta hai
import Pet.PetInteraction; // PetInteraction class import karta hai
import java.io.*; // File handling ke liye import
import java.util.ArrayList; // ArrayList class import karta hai
import java.util.List; // List interface import karta hai
import java.util.Scanner; // Scanner class import karta hai

public class UserManager { // UserManager class define karta hai

    private ArrayList<User> usersLists; // Users ke liye dynamic storage
    private PetManager petManager; // PetManager ka reference store karta hai
    private EventManager eventManager; // EventManager ka reference store karta hai
    private static Manager managerInstance; // Singleton Manager instance
    private UserNodeBST root; // Email lookup ke liye BST root

    // Constructor
    public UserManager(PetManager petManager, EventManager eventManager) { // UserManager constructor PetManager aur EventManager ke saath
        this.usersLists = new ArrayList<>(); // Users list initialize karta hai
        this.root = null; // BST root ko null set karta hai
        this.petManager = petManager; // PetManager reference set karta hai
        this.eventManager = eventManager; // EventManager reference set karta hai
        loadUsers("users.txt"); // Users ko file se load karta hai
    }

    // Register a New User
    public void register(String userName, String email, String password, int age, char role) { // Naya user register karne ka method
        if (isEmailRegistered(email)) { // Check karta hai ke email pehle se registered hai
            System.out.println("Error! Email is already registered: " + email); // Error message print karta hai
            return; // Method se return karta hai
        }

        if ((role == 'M' || role == 'm') && managerInstance != null) { // Check karta hai ke manager pehle se exist karta hai
            System.out.println("A Manager already exists! Cannot register another."); // Error message print karta hai
            return; // Method se return karta hai
        }

        User user = null; // User object initialize karta hai
        if (role == 'C' || role == 'c') { // Agar role CareTaker hai
            user = new CareTaker(userName, email, password, age, role); // CareTaker object banata hai
        } else if (role == 'M' || role == 'm') { // Agar role Manager hai
            user = new Manager(userName, email, password, age, role); // Manager object banata hai
            managerInstance = (Manager) user; // Manager instance set karta hai
        }

        if (user != null) { // Agar user object bana hai
            usersLists.add(user); // User ko list mein add karta hai
            root = insertBST(root, user); // User ko BST mein insert karta hai
            saveUsers("users.txt"); // Users ko file mein save karta hai
            System.out.println("Registration successful for " + role + ": " + userName + ", instance: " + user); // Success message with instance
        } else { // Agar user object nahi bana
            System.out.println("Error! User creation failed."); // Error message print karta hai
        }
    }

    // Insert in BST
    public UserNodeBST insertBST(UserNodeBST node, User user) { // BST mein user insert karne ka method
        if (node == null) { // Agar node null hai
            return new UserNodeBST(user); // Naya node banata hai
        }

        int compare = user.getEmail().compareToIgnoreCase(node.getEmail()); // Email compare karta hai
        if (compare < 0) { // Agar email chhota hai
            node.setLeft(insertBST(node.getLeft(), user)); // Left subtree mein insert karta hai
        } else if (compare > 0) { // Agar email bada hai
            node.setRight(insertBST(node.getRight(), user)); // Right subtree mein insert karta hai
        }
        return node; // Node return karta hai
    }

    // Login a User
    public User login(String email, String password, char role) { // User login karne ka method
        UserNodeBST node = searchBST(root, email); // BST mein email search karta hai
        if (node == null) { // Agar node nahi mila
            System.out.println("Invalid credentials, email not found: " + email); // Error message print karta hai
            return null; // Null return karta hai
        }
        User user = node.getData(); // Node se user data leta hai

        if (user != null && user.getPassword().equalsIgnoreCase(password) && user.getRol() == role) { // Credentials check karta hai
            user.setLastLogin(System.currentTimeMillis()); // Last login time set karta hai
            System.out.println("Login successful, user instance: " + user); // Success message with instance
            return user; // User object return karta hai
        }

        System.out.println("Invalid credentials for email: " + email + ", role: " + role); // Error message print karta hai
        return null; // Null return karta hai
    }

    // Search in BST
    private UserNodeBST searchBST(UserNodeBST node, String email) { // BST mein email search karne ka method
        if (node == null || node.getEmail().equalsIgnoreCase(email)) { // Agar node null hai ya email match karta hai
            return node; // Node return karta hai
        }

        int compare = email.compareToIgnoreCase(node.getEmail()); // Email compare karta hai
        if (compare < 0) { // Agar email chhota hai
            return searchBST(node.getLeft(), email); // Left subtree mein search karta hai
        }
        return searchBST(node.getRight(), email); // Right subtree mein search karta hai
    }

    // Get User by Email
    public User getUser(String email) { // Email se user dhoondhne ka method
        UserNodeBST node = searchBST(root, email); // BST mein email search karta hai
        if (node == null) { // Agar node nahi mila
            System.out.println("Not found: " + email); // Error message print karta hai
            return null; // Null return karta hai
        }
        System.out.println("Found user: " + node.getData().getUserName() + ", instance: " + node.getData()); // User found message with instance
        return node.getData(); // User object return karta hai
    }

    // Check if user is registered with email
    public boolean isEmailRegistered(String email) { // Email registered hai ya nahi check karta hai
        return searchBST(root, email) != null; // BST search result return karta hai
    }

    // Remove user by email
    public boolean removeUser(String email) { // Email se user remove karne ka method
        if (!isEmailRegistered(email)) { // Check karta hai ke email registered hai
            System.out.println("User Not found: " + email); // Error message print karta hai
            return false; // False return karta hai
        }

        usersLists.removeIf(user -> user.getEmail().equalsIgnoreCase(email)); // List se user remove karta hai
        root = deleteBST(root, email); // BST se user remove karta hai

        if (managerInstance != null && managerInstance.getEmail().equalsIgnoreCase(email)) { // Agar manager remove ho raha hai
            managerInstance = null; // Manager instance null karta hai
        }
        saveUsers("users.txt"); // Users ko file mein save karta hai
        System.out.println("Removed user successfully: " + email); // Success message print karta hai
        return true; // True return karta hai
    }

    // Delete BST
    public UserNodeBST deleteBST(UserNodeBST node, String email) { // BST se node delete karne ka method
        if (node == null) { // Agar node null hai
            return null; // Null return karta hai
        }

        int compare = email.compareToIgnoreCase(node.getEmail()); // Email compare karta hai
        if (compare < 0) { // Agar email chhota hai
            node.setLeft(deleteBST(node.getLeft(), email)); // Left subtree se delete karta hai
        } else if (compare > 0) { // Agar email bada hai
            node.setRight(deleteBST(node.getRight(), email)); // Right subtree se delete karta hai
        } else { // Agar email match karta hai
            if (node.getLeft() == null) { // Agar left child nahi hai
                return node.getRight(); // Right child return karta hai
            } else if (node.getRight() == null) { // Agar right child nahi hai
                return node.getLeft(); // Left child return karta hai
            }
            UserNodeBST minNode = findMin(node.getRight()); // Right subtree ka minimum node dhoondta hai
            node.setData(minNode.getData()); // Node ka data replace karta hai
            node.setRight(deleteBST(node.getRight(), minNode.getEmail())); // Minimum node ko delete karta hai
        }
        return node; // Updated node return karta hai
    }

    // Find minimum node
    private UserNodeBST findMin(UserNodeBST node) { // BST ka minimum node dhoondne ka method
        while (node.getLeft() != null) { // Jab tak left child hai
            node = node.getLeft(); // Left child pe jata hai
        }
        return node; // Minimum node return karta hai
    }

    // Load users from file
    public void loadUsers(String fileName) { // Users ko file se load karne ka method
        usersLists.clear(); // Users list clear karta hai
        root = null; // BST root clear karta hai
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) { // File reader open karta hai
            String line; // Line variable banata hai
            while ((line = reader.readLine()) != null) { // File se har line padhta hai
                try {
                    String[] values = line.split(","); // Line ko comma se split karta hai
                    if (values.length == 5) { // Agar line mein 5 fields hain
                        String userName = values[0]; // Username extract karta hai
                        String email = values[1].toLowerCase(); // Email extract karta hai aur lowercase karta hai
                        String password = values[2]; // Password extract karta hai
                        int age = Integer.parseInt(values[3]); // Age extract karta hai
                        char role = values[4].charAt(0); // Role extract karta hai
                        User user; // User object banata hai
                        if (role == 'C' || role == 'c') { // Agar role CareTaker hai
                            user = new CareTaker(userName, email, password, age, role); // CareTaker object banata hai
                        } else { // Agar role Manager hai
                            user = new Manager(userName, email, password, age, role); // Manager object banata hai
                            managerInstance = (Manager) user; // Manager instance set karta hai
                        }
                        usersLists.add(user); // User ko list mein add karta hai
                        root = insertBST(root, user); // User ko BST mein insert karta hai
                        System.out.println("Loaded user: " + userName + ", email: " + email + ", instance: " + user); // Debug message print karta hai
                    } else { // Agar line invalid hai
                        System.out.println("Invalid user data in file: " + line); // Error message print karta hai
                    }
                } catch (Exception e) { // Agar parsing error hota hai
                    System.out.println("Error parsing line: " + line + ", Error: " + e.getMessage()); // Error message print karta hai
                }
            }
        } catch (FileNotFoundException e) { // Agar file nahi milti
            System.out.println("No users file found, starting fresh."); // Message print karta hai
        } catch (IOException e) { // Agar IO error hota hai
            System.out.println("Error loading users: " + e.getMessage()); // Error message print karta hai
        }
    }

    // Save users to file
    public void saveUsers(String fileName) { // Users ko file mein save karne ka method
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) { // File writer open karta hai
            for (User user : usersLists) { // Har user ke liye loop
                writer.write(user.getUserName() + "," + user.getEmail().toLowerCase() + "," + user.getPassword() + ","
                        + user.getAge() + "," + user.getRol()); // User data file mein likhta hai
                writer.newLine(); // Nayi line add karta hai
            }
            System.out.println("Users saved successfully."); // Success message print karta hai
        } catch (IOException e) { // Agar IO error hota hai
            System.out.println("Error saving users: " + e.getMessage()); // Error message print karta hai
        }
    }

    // Get Total User Count
    public int getUserCount() { // Total users ki ginti lene ka method
        return usersLists.size(); // Users list ka size return karta hai
    }

    // Login Menu
    public void loginMenu(PetManager petManager, EventManager eventManager, Sanctuary sanctuary) { // Login menu display karne ka method
        Scanner input = new Scanner(System.in); // Scanner object banata hai
        System.out.println("\nLogin User"); // Login menu title print karta hai
        System.out.print("Enter Email: "); // Email input ke liye prompt
        String email = input.nextLine(); // Email input leta hai

        while (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) { // Email format check karta hai
            System.out.print("Invalid Email Format. Enter again: "); // Error message print karta hai
            email = input.nextLine(); // Naya email input leta hai
        }

        System.out.print("Enter Password: "); // Password input ke liye prompt
        String password = input.nextLine(); // Password input leta hai

        while (password.length() < 8) { // Password length check karta hai
            System.out.print("Password must be at least 8 characters. Enter again: "); // Error message print karta hai
            password = input.nextLine(); // Naya password input leta hai
        }

        char role; // Role variable banata hai
        do { // Loop chalata hai jab tak valid role nahi milta
            System.out.print("Enter Role (C for CareTaker, M for Manager): "); // Role input ke liye prompt
            role = Character.toUpperCase(input.next().charAt(0)); // Role input leta hai aur uppercase karta hai
        } while (role != 'C' && role != 'M'); // Valid role check karta hai

        User user = login(email, password, role); // Login method call karta hai

        if (user != null) { // Agar login successful hai
            System.out.println("Login Successful! Welcome, " + user.getUserName()); // Success message print karta hai
            if (user instanceof CareTaker) { // Agar user CareTaker hai
                CareTaker careTaker = (CareTaker) user; // User ko CareTaker mein cast karta hai
                System.out.println("CareTaker instance: " + careTaker + ", email: " + careTaker.getEmail()); // Debug message print karta hai
                petManager.loadAssignmentsFromFile("assignments.txt"); // Assignments load karta hai login ke baad
                careTaker.loadCareTakerMenu(petManager, eventManager); // CareTaker menu kholta hai
            } else if (user instanceof Manager) { // Agar user Manager hai
                Manager manager = (Manager) user; // User ko Manager mein cast karta hai
                System.out.println("Manager instance: " + manager + ", email: " + manager.getEmail()); // Debug message print karta hai
                petManager.loadAssignmentsFromFile("assignments.txt"); // Assignments load karta hai login ke baad
                manager.loadManagerMenu(petManager, eventManager, new PetInteraction(10), sanctuary); // Manager menu kholta hai
            }
        }
    }

    // Register Menu
    public void registerMenu() { // Register menu display karne ka method
        Scanner input = new Scanner(System.in); // Scanner object banata hai

        System.out.println("\n🔹 Register New User"); // Register menu title print karta hai
        System.out.print("Enter User Name: "); // Username input ke liye prompt
        String userName = input.nextLine(); // Username input leta hai

        System.out.print("Enter Email: "); // Email input ke liye prompt
        String email = input.nextLine(); // Email input leta hai

        while (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) { // Email format check karta hai
            System.out.print("Invalid Email Format. Enter again: "); // Error message print karta hai
            email = input.nextLine(); // Naya email input leta hai
        }

        System.out.print("Enter Password: "); // Password input ke liye prompt
        String password = input.nextLine(); // Password input leta hai

        while (password.length() < 8) { // Password length check karta hai
            System.out.print("Password must be at least 8 characters. Enter again: "); // Error message print karta hai
            password = input.nextLine(); // Naya password input leta hai
        }

        System.out.print("Enter Age: "); // Age input ke liye prompt
        while (!input.hasNextInt()) { // Age valid number hai ya nahi check karta hai
            System.out.print("Invalid age. Enter a number: "); // Error message print karta hai
            input.next(); // Invalid input skip karta hai
        }
        int age = input.nextInt(); // Age input leta hai

        while (age < 18 || age > 50) { // Age range check karta hai
            System.out.print("Age must be between 18 and 50. Enter again: "); // Error message print karta hai
            age = input.nextInt(); // Naya age input leta hai
        }

        char role; // Role variable banata hai
        do { // Loop chalata hai jab tak valid role nahi milta
            System.out.print("Enter Role (C for CareTaker, M for Manager): "); // Role input ke liye prompt
            role = Character.toUpperCase(input.next().charAt(0)); // Role input leta hai aur uppercase karta hai
        } while (role != 'C' && role != 'M'); // Valid role check karta hai

        register(userName, email, password, age, role); // Register method call karta hai
    }

    // Get the Singleton Manager Instance
    public static Manager getManagerInstance() { // Singleton Manager instance return karne ka method
        return managerInstance; // Manager instance return karta hai
    }

    public ArrayList<User> getUsers() { // Users ki list return karne ka method
        return usersLists; // Users list return karta hai
    }

    // Find user by email prefix
    public ArrayList<User> findUserEmailPrefix(String prefix) { // Email prefix se users dhoondhne ka method
        ArrayList<User> result = new ArrayList<>(); // Result list banata hai
        findUserEmailPrefixHelper(root, prefix.toLowerCase(), result); // Helper method call karta hai
        if (result.isEmpty()) { // Agar koi user nahi mila
            System.out.println("No users found with this email: " + prefix); // Error message print karta hai
        } else { // Agar users mile
            for (User user : result) { // Har user ke liye
                System.out.println(user); // User details print karta hai
            }
        }
        return result; // Result list return karta hai
    }

    // Find user by email prefix helper
    public void findUserEmailPrefixHelper(UserNodeBST node, String prefix, ArrayList<User> result) { // Email prefix se users dhoondhne ka helper method
        if (node == null) { // Agar node null hai
            return; // Return karta hai
        }
        findUserEmailPrefixHelper(node.getLeft(), prefix, result); // Left subtree mein search karta hai
        if (node.getEmail().toLowerCase().startsWith(prefix)) { // Agar email prefix se match karta hai
            result.add(node.getData()); // User ko result list mein add karta hai
        }
        findUserEmailPrefixHelper(node.getRight(), prefix, result); // Right subtree mein search karta hai
    }

    // Count Active users
    public int countActiveUser(long timeThreshold) { // Active users count karne ka method
        ArrayList<User> activeUsers = new ArrayList<>(); // Active users ki list banata hai
        countActiveUserHelper(root, timeThreshold, activeUsers); // Helper method call karta hai
        System.out.println("Active users in the last " + (System.currentTimeMillis() - timeThreshold) / (1000 * 60 * 60) + " hours: " + activeUsers.size()); // Active users ka count print karta hai
        return activeUsers.size(); // Active users ka count return karta hai
    }

    // Helper method for counting active users
    private void countActiveUserHelper(UserNodeBST node, long timeThreshold, ArrayList<User> result) { // Active users count karne ka helper method
        if (node == null) { // Agar node null hai
            return; // Return karta hai
        }
        countActiveUserHelper(node.getLeft(), timeThreshold, result); // Left subtree mein search karta hai
        if (node.getData().getLastLogin() >= timeThreshold) { // Agar user recently active tha
            result.add(node.getData()); // User ko result list mein add karta hai
        }
        countActiveUserHelper(node.getRight(), timeThreshold, result); // Right subtree mein search karta hai
    }
}