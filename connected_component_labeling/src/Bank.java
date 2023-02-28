import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    /**
     * Creating new Bank object with empty lists of users & accounts
     *
     * @param name The bank's name
     */
    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
        this.users = new ArrayList<User>();
    }

    /**
     * @return
     */
    public String getNewUserUUID() {

        String uuid;
        Random rng = new Random();
        int len = 12;
        boolean nonUnique = false;

        // Continue looping until we get a unique ID
        do {

            // Generating the number by adding a digit between 0-9 to the uuid string
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += (Integer) rng.nextInt(10);
            }

            // Making sure it's unique
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;

    }

    public String getNewAccountUUID() {

        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique = false;

        // Continue looping until we get a unique ID
        do {

            // Generating the number by adding a digit between 0-9 to the uuid string
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += (Integer) rng.nextInt(10);
            }

            // Making sure it's unique
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
     * Add an account
     *
     * @param anAcct The account that is added
     */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
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

        // Create a savings account for new user and add to User and Bank account lists
        // So if anything changes with the account be it a new transaction or balance then the lists will be updated.
        // By performing Add to holder and bank lists. Each list will have a pointer to the same object.
        Account newAccount = new Account("Sparekonto", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);

        return newUser;
    }

    /**
     * @param userID UUID of the user to log in with
     * @param pin    The pin of the user
     * @return The user object, if the login was succesful. Null if it is not successful.
     */
    public User userLogin(String userID, String pin) {

        for (User u : this.users) {

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
    public String getName(){
        return this.name;
    }

}
