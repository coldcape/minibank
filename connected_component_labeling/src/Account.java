import java.security.MessageDigest;
import java.util.ArrayList;

public class Account {
    //    Name of the account
    private String name;
    //  The current balance of the account.
    private double balance;
    //    ID number of the account.
    private String uuid;
    //    The User that owns this account.
    private User holder;
    //    List of all transactions for this account.
    private ArrayList<Transaction> transactions;

    /**
     *
     * @param name  Name of the account.
     * @param holder    The user object that is holding this account.
     * @param theBank   The bank that iss issuing this account.
     */
    public Account(String name, User holder, Bank theBank) {
        //
        this.name = name;
        this.holder = holder;

        // get new account uuid
        this.uuid = theBank.getNewAccountUUID();

        // initialize transactions
        this.transactions = new ArrayList<Transaction>();


    }

    /**
     *  Get the account UUID/ID
     * @return  uuid
     */
    public String getUUID(){
        return this.uuid;
    }


}
