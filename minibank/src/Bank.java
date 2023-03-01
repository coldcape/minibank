
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Bank {

    /**
     * The name of the bank.
     */
    private String name;

    /**
     * The account holders of the bank.
     */
    private ArrayList<User> users;

    /**
     * The accounts of the bank.
     */
    private ArrayList<Account> accounts;

    /**
     * Create a new Bank object with empty lists of users and accounts.
     */
    public Bank(String name) {

        this.name = name;

        // init users and accounts
        users = new ArrayList<User>();
        accounts = new ArrayList<Account>();

    }

    /**
     * Generate a new universally unique ID for a user.
     * @return	the uuid
     */
    public String getNewUserUUID() {

        // inits
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        // continue looping until we get a unique ID
        do {

            // generate the number
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // check to make sure it's unique
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;
    }

    /**
     * Generate a new UUID for an account.
     * @return	the uuid
     */
    public String getNewAccountUUID() {

        // inits
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique = false;

        // continue looping until we get a unique ID
        do {

            // generate the number
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // check to make sure it's unique
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;

    }

    /**
     * @param firstName user's first name
     * @param lastName  user's last name
     * @param pin       the user's pin
     * @return the new User object
     */
    public User addUser(String firstName, String lastName, String pin) {

        // Creating User object and adding to our list.
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // Create a savings account for new user and add to User and Bank account lists
        // So if anything changes with the account be it a new transaction or balance then the lists will be updated.
        // By performing Add to holder and bank lists. Each list will have a pointer to the same object.
        Account newAccount = new Account("Sparekonto", newUser, this);
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return newUser;

    }

    /**
     * Add an existing account for a particular User.
     * @param newAccount	the account
     */
    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    /**
     * @param userID UUID of the user to log in with
     * @param pin    The pin of the user
     * @return The user object, if the login was succesful. Null if it is not successful.
     */
    public User userLogin(String userID, String pin) {

        // search through list of users
        for (User u : this.users) {

            // if we find the user, and the pin is correct, return User object
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }

        // Returning null if we haven't found the user or have incorrect pin.
        return null;

    }

    /**
     * Getting the name of the bank
     *
     * @return name of the bank
     */
    public String getName() {
        return this.name;
    }

}