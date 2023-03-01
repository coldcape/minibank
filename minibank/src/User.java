import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {
    //    First name of the user.
    private String firstName;
    //    Last name of the user.
    private String lastName;
    //    Universally unique identifier to give users an unique ID
    private String uuid;
    //    MD5 hashed pin of the user
    private byte pinHash[];
    //    List of bank accounts a user may have
    private ArrayList<Account> accounts;

    /**
     * @param firstName
     * @param lastName
     * @param pin
     * @param theBank
     */
    public User(String firstName, String lastName, String pin, Bank theBank) {

        // Set user's name
        this.firstName = firstName;
        this.lastName = lastName;

        // Storing the pin MD5 hash, not the original value.
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // Get a new unique universal ID for user.
        this.uuid = theBank.getNewUserUUID();

        // Create empty list of accounts
        this.accounts = new ArrayList<Account>();

        // print Log message
        System.out.printf("Ny bruker %s, %s med ID %s har blitt oprettet.\n", lastName, firstName, this.uuid);
    }

    /**
     * Adds an account for the user
     *
     * @param anAcct The account that is added
     */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /**
     * Returns the user's UUID
     *
     * @return the uuid
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Testing if a give pin matches the true pin of a User
     *
     * @param aPin the pin that was input that will be checked
     * @return Checking if the pin is valid or not
     */
    public boolean validatePin(String aPin) {


        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;


//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
//        } catch (NoSuchAlgorithmException e) {
//            System.err.println("Error, caught NoSuchAlgorithmException");
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        return false;
    }

    public String getFirstName() {
        return this.firstName;
    }


    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts() {
        return this.accounts.size();
    }

    public void printAcctTransHistory(int accountIndex){
        this.accounts.get(accountIndex).printTransHistory();
    }

    /**
     * Get the balance of a specific account
     *
     * @param accountIndex   index of the account to use
     * @return
     */
    public double getAcctBalance(int accountIndex){
        return this.accounts.get(accountIndex).getBalance();
    }

    /**
     *
     * @param accountIndex   Index of the account to be used
     * @return UUID of the account
     */
    public String getAcctUUID(int accountIndex){
        return this.accounts.get(accountIndex).getUUID();
    }

    public void addAcctTransaction(int accountIndex, double amount, String memo){

        this.accounts.get(accountIndex).addTransaction(amount, memo);

    }
}
