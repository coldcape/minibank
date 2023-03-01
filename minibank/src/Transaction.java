import java.util.Date;

public class Transaction {

    private double amount;
    private Date timestamp;
    private String memo;

    private Account inAccount;


    /**
     * Creating a new transaction
     *
     * @param amount    The amount in the transaction.
     * @param inAccount Account that the transaction belongs to.
     */
    public Transaction(double amount, Account inAccount) {

        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    public Transaction(double amount, String memo, Account inAccount) {

        // Call the two-arguement constructor first so that if there is any changes in the first constructor then this will be updated. Less work.
        this(amount, inAccount);

        // Set memo
        this.memo = memo;

    }

    /**
     * Get the amount of the transaction
     *
     * @return the amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Get string summarizing transaction
     * @return summary string
     */
    public String getSummaryLine() {

        if (this.amount >= 0) {
            return String.format("%s : Kr %.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : Kr %.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        }

    }


}
