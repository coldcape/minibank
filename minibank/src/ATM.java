import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {

        // Initializing Scanner
        Scanner sc = new Scanner(System.in);

        // Initializing Bank
        Bank theBank = new Bank("Den Norske Bank");

        // Adding user for testing so that there is no need to create new user everytime.
        User aUser = theBank.addUser("Ola", "Nordman", "2021");

        // Adding a checking account for the user
        Account newAccount = new Account("Traansaksjonskonto", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {

            // Staying in the login prompt until a successfull login happens.
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // Stay in main menu until user quits
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

        // Initializing
        String userID;
        String pin;
        User authUser;

        do {

            // Promt for ID and Pin until a correct one is reached.

            System.out.printf("\n\n Velkommen til %s \n\n", theBank.getName());
            System.out.print("Tast inn bruker ID: ");

            userID = sc.nextLine();
            System.out.printf("Tast inn pin koden: ");
            pin = sc.nextLine();

            // Trying to get the user  object corresponding to the ID and pin combination.
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Feil ID eller pin kode. " + "Prøv igjen. ");
            }


        } while (authUser == null); //Continue looping until successfull login.

        return authUser;


    }

    public static void printUserMenu(User theUser, Scanner sc) {

        // Print a summary of user's accounts
        theUser.printAccountsSummary();

        // Initialize
        int choice;

        // user menu
        do {
            System.out.printf("Velkommen %s, hva ønsker du ønsker du å gjøre?", theUser.getFirstName());
            System.out.println(" 1) Vis transaksjonshistorie");
            System.out.println(" 2) Uttak");
            System.out.println(" 3) Innskudd");
            System.out.println(" 4) Overføring");
            System.out.println(" 5) Avslutt");
            System.out.println();
            System.out.println("Gjør et valg");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Ugyldig valg, velg en av alternativene ovenfor");
            }
        } while (choice < 1 || choice > 5);

        // Handle the choice

        switch (choice) {

            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawlFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);

        }

        // Redisplay this menu unless the user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }
    }

    /**
     * @param theUser the logged in User object
     * @param sc      Scanner object used for user input.
     */
    public static void showTransHistory(User theUser, Scanner sc) {

        int theAcct;

        do {

            System.out.printf("Velg konto fra listen ved å taste inn et tall fra (1-%d) for å vise transaksjonshistorien den gjeldene kontoen", theUser.numAccounts());

            theAcct = sc.nextInt();
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto, prøv igjen");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        // Printing transaction history
        theUser.printAcctTransHistory(theAcct);


    }


    /**
     * Process transferring funds from one account to another
     *
     * @param theUser Logged in User object
     * @param sc      Scanner object used for user input
     */
    public static void transferFunds(User theUser, Scanner sc) {

        // Initializing
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // Get the account to transfer from
        do {
            System.out.printf("Velg kontoen du ønsker å sende penger fra listen ved å taste inn et tall fra (1-%d)\n");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto, prøv igjen");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);

        // Get the account to transfer to
        do {
            System.out.printf("Velg kontoen du ønsker å sende penger til listen ved å taste inn et tall fra (1-%d)\n");
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto, prøv igjen");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        // get the amount to transfer
        do {

            System.out.printf("Tast in mengden som du ønsker å overføre (Max: Kr%.02f", acctBal);
            amount = sc.nextDouble();

            if (amount < 0) {

                System.out.println("Mengden må være høyere enn 0");

            } else if (amount > acctBal) {

                System.out.printf("Mengden som skal overføres kan i være større enn\n" + "Balansen Kr%.02f\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // Proceed with the transfer
        theUser.addAcctTransaction(fromAcct, -1 * amount, String.format("Overfør til konto %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(fromAcct, amount, String.format("Overfør til konto %s", theUser.getAcctUUID(fromAcct)));

    }

    /**
     * Process a fund to withdraw from an account
     * @param theUser   Logged in User object
     * @param sc    Scanner objest user for user input
     */
    public static void withdrawlFunds(User theUser, Scanner sc) {

        // Initializing
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        // Get the account to transfer from
        do {
            System.out.printf("Velg kontoen du ønsker å sende penger fra listen ved å taste inn et tall fra (1-%d)\n");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Ugyldig konto, prøv igjen");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);

        // get the amount to transfer
        do {

            System.out.printf("Tast in mengden som du ønsker å overføre (Max: Kr%.02f", acctBal);
            amount = sc.nextDouble();

            if (amount < 0) {
                System.out.println("Mengden må være høyere enn 0");
            } else if (amount > acctBal) {
                System.out.printf("Mengden som skal overføres kan i være større enn\n" + "Balansen Kr%.02f\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // Gobbling up rest of previous input
        sc.nextLine();

        // Get a memo
        System.out.println("Legg til melding: ");
        memo = sc.nextLine();

        // Do withdrawal
        theUser.addAcctTransaction(fromAcct, -1*amount, memo);

    }
}
