import java.util.Scanner;

public class ATM {


    public static void main(String[] args) {

        // Initializing Scanner
        Scanner sc = new Scanner(System.in);

        // Initializing Bank
        Bank theBank = new Bank("Den Norske Bank");

        // Adding user for testing so that there is no need to create new user everytime.
        User aUser = theBank.addUser("Ola", "Nordman", "2020");

        // Adding a checking account for the user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;

        // continue looping forever
        while (true) {

            // stay in login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);

        }

    }

    /**
     * Printing the ATM login menu
     *
     * @param theBank the Bank object whose accounts to use.
     * @param sc      Scanner object to use for user input
     * @return
     */
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        // inits
        String userID;
        String pin;
        User authUser;

        // Prompt for ID and Pin until a correct one is reached.
        do {

            System.out.printf("\n\nVelkommen til %s\n\n", theBank.getName());
            System.out.print("Tast inn bruker ID: ");
            userID = sc.nextLine();
            System.out.print("Tast inn pin kode: ");
            pin = sc.nextLine();

            // Trying to get the user  object corresponding to the ID and pin combination.
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Feil ID eller PIN koden. " +
                        "Prøv Igjen");
            }

        } while(authUser == null); //Continue looping until successfull login.

        return authUser;

    }

    public static void printUserMenu(User theUser, Scanner sc) {

        // print a summary of the user's accounts
        theUser.printAccountsSummary();

        // init
        int choice;

        // user menu
        do {

            System.out.println("What would you like to do?");
            System.out.println("  1) Vis konto transaksjonshistorie");
            System.out.println("  2) Uttak");
            System.out.println("  3) Innskudd");
            System.out.println("  4) Overfør");
            System.out.println("  5) Avslutt");
            System.out.println();
            System.out.print("Tast inn et valg: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Ugyldig valg. Velg noe mellom 1-5.");
            }

        } while (choice < 1 || choice > 5);

        // process the choice
        switch (choice) {

            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                // gobble up rest of previous input
                sc.nextLine();
                break;
        }

        // redisplay this menu unless the user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }

    }

    /**
     * Transferring funds from account to another.
     *
     * @param theUser the logged in User object
     * @param sc      Scanner object used for user input.
     */
    public static void transferFunds(User theUser, Scanner sc) {

        int fromAccount;
        int toAccount;
        double amount;
        double accountBalance;

        // get account to transfer from
        do {
            System.out.printf("Tast nummeret til konten (1-%d) som det skal sendes fra. " +
                    "Sendes fra: ", theUser.numAccounts());
            fromAccount = sc.nextInt()-1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto. Prøv igjen.");
            }
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        accountBalance = theUser.getAcctBalance(fromAccount);

        // get account to transfer to
        do {
            System.out.printf("Tast nummeret (1-%d) til kontoen som " +
                    "skal overføres til: ", theUser.numAccounts());
            toAccount = sc.nextInt()-1;
            if (toAccount < 0 || toAccount >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto. Prøv igjen.");
            }
        } while (toAccount < 0 || toAccount >= theUser.numAccounts());

        // get amount to transfer
        do {
            System.out.printf("Tast inn mengden som skal overføres (max Kr %.02f): $",
                    accountBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Møngden må være større enn 0.");
            } else if (amount > accountBalance) {
                System.out.printf("Mengden kan ikke være større enn hva som er på konto " +
                        "of Kr %.02f.\n", accountBalance);
            }
        } while (amount < 0 || amount > accountBalance);

        // finally, do the transfer
        theUser.addAcctTransaction(fromAccount, -1*amount, String.format(
                "Overfør til konto %s", theUser.getAcctUUID(toAccount)));
        theUser.addAcctTransaction(toAccount, amount, String.format(
                "Overfør fra konto %s", theUser.getAcctUUID(fromAccount)));

    }

    /**
     * Process a fund to withdraw from an account
     *
     * @param theUser Logged in User object
     * @param sc      Scanner objest user for user input
     */
    public static void withdrawFunds(User theUser, Scanner sc) {

        int fromAccount;
        double amount;
        double accountBalance;
        String memo;

        // get account to withdraw from
        do {
            System.out.printf("Tast in nummeret (1-%d) til kontoen som " +
                    "uttakes fra: ", theUser.numAccounts());
            fromAccount = sc.nextInt()-1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto. Prøv igjen.");
            }
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        accountBalance = theUser.getAcctBalance(fromAccount);

// Get the account to transfer from
        do {
            System.out.printf("Tast inn mengden som skal tas i uttakk (max Kr %.02f): Kr",
                    accountBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Mengden må være høyere enn 0.");
            } else if (amount > accountBalance) {
                System.out.printf("Mengden kan ikke være større en hva som er på kontoen " +
                        "som er Kr %.02f.\n", accountBalance);
            }
        } while (amount < 0 || amount > accountBalance);

        // Remove rest of previous inpu
        sc.nextLine();

        System.out.print("Tast inn en melding: ");
        memo = sc.nextLine();

        // Do the withdrwal
        theUser.addAcctTransaction(fromAccount, -1*amount, memo);

    }

    /**
     * Process deposit to account
     *
     * @param theUser	the logged-in User object
     * @param sc		the Scanner object used for user input
     */
    public static void depositFunds(User theUser, Scanner sc) {

        int toAccount;
        double amount;
        String memo;

        // get account to withdraw from
        do {
            System.out.printf("Tast inn nummeret (1-%d) til kontoen som skal få et innskudd " +
                    "på konto: ", theUser.numAccounts());
            toAccount = sc.nextInt()-1;
            if (toAccount < 0 || toAccount >= theUser.numAccounts()) {
                System.out.println("gyldig konto. Prøv igjen");
            }
        } while (toAccount < 0 || toAccount >= theUser.numAccounts());

        // get amount to transfer
        do {
            System.out.printf("Enter the amount to deposit: $");
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        // gobble up rest of previous input
        sc.nextLine();

        // get a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // do the deposit
        theUser.addAcctTransaction(toAccount, amount, memo);

    }

    /**
     * Show the transaction history for an account.
     * @param theUser	the logged-in User object
     * @param sc		the Scanner object used for user input
     */
    public static void showTransHistory(User theUser, Scanner sc) {

        int theAccountt;

        // get account whose transactions to print
        do {
            System.out.printf("Enter the number (1-%d) of the account\nwhose " +
                    "transactions you want to see: ", theUser.numAccounts());
            theAccountt = sc.nextInt()-1;
            if (theAccountt < 0 || theAccountt >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAccountt < 0 || theAccountt >= theUser.numAccounts());

        // print the transaction history
        theUser.printAcctTransHistory(theAccountt);

    }

}