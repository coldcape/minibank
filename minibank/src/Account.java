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

    /**
     * Get summary line for the account
     *
     * @return String summary
     */
    public String getSummaryLine(){

        // Get the account balance
        double balance = this.getBalance();

        // format the summary line, depending on the whether balance is negative
        if (balance >= 0){
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);

        }
    }

    public double getBalance(){

        double balance = 0;
        for (Transaction t : this.transactions){
            balance += t.getAmount();
        }
        return balance;

    }

}
