import java.util.*;
import java.util.Scanner;
import BudgetTracker.MenuOptions;
import java.io.*;
public class BudgetTracker {
    private double currentBalance;
    private double desiredBudget;
    private Scanner scanner;
    List<String> transactions = new ArrayList<>();

    public BudgetTracker() {
        scanner = new Scanner(System.in);
    }
    public void start() {
        System.out.println("Options for your Budget Tracker:");
        for(int i =0; i<MenuOptions.trackerOptions.length; i++){
            System.out.println(MenuOptions.trackerOptions[i]);

        }
        System.out.println("Choose one: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                setDesiredBudgetAgain();
                break;
            case 2:
                addTransactions();
                break;
            case 3:
                viewTransactions();
                break;
            case 4:
                viewCurrentBalance();
                break;
            case 5:
                editTransactions();
                break;
            case 6:
                deleteTransaction();
                break;
            case 7:
                saveDataName();
                break;
            case 8:
                loadDataName();
                break;
        }
    }
    public void setDesiredBudgetAgain(){
        System.out.println("Unfortunately, you cannot set the budget again, stick to your word!");
        System.out.print("Reset the terminal or Press enter to go back to main menu.");
        scanner.nextLine();
        start();
    }
    public void setDesiredBudget() {
        System.out.println("Enter in your desired budget: ");
        double budget = scanner.nextDouble();
        desiredBudget = budget;
        currentBalance = budget;
        System.out.println("Your desired budget is : $" + desiredBudget);
        System.out.println("Your current balance has been updated to : $" + currentBalance);
        scanner.nextLine();
        start();
    }

    public void addTransactions() {
        System.out.println("Enter price: $");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        currentBalance -= price;
        transactions.add("$" + price + ": " + description);
        System.out.println("Your current balance has been updated to : $" + currentBalance);
        System.out.println("$" + price + " , " + description);
        System.out.println("Do you wish to add more: 1. yes 2. no");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                addTransactions();
                break;
            case 2:
                start();
        }
    }

    public void viewTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to view.");
        } else {
            System.out.println("Here is your transaction log: ");
            int count = 1;
            for (String transaction : transactions) {
                System.out.println(count + ". " + transaction);
                count++;
            }
        }
        System.out.println("Press 1 to go back to menu. 2 to view again.");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            start();

        } else {
            viewTransactions();
        }
    }

    public void viewCurrentBalance() {
        System.out.println("Here is your current balance: $" + currentBalance);
        System.out.println("Press 1 to go back to menu. 2 to view again.");
        int choice = scanner.nextInt();
        if (choice == 1) {
            start();
        } else {
            viewCurrentBalance();
        }
    }

    public void editOptions(int index, String currentTransaction) {
        for (int i = 1; i < MenuOptions.editMenuOptions.length + 1; i++) {
            System.out.println(i + ". " + MenuOptions.editMenuOptions[i - 1]);
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
                System.out.println("Enter new price: $");
                double newPrice = scanner.nextDouble();
                scanner.nextLine();
                String description = currentTransaction.split(":")[1].trim();
                double oldPrice = extractAmountFromTransaction(currentTransaction);
                transactions.set(index, "$" + newPrice + ": " + description);
                currentBalance += oldPrice - newPrice;
                System.out.println("Price updated. Current balance adjusted.");
                start();
                break;

            case 2:
                System.out.println("Enter new description: ");
                String newDescription = scanner.nextLine();
                double price = extractAmountFromTransaction(currentTransaction);
                transactions.set(index, "$" + price + ": " + newDescription);
                System.out.println("Description updated.");
                start();
                break;

            case 3:
                System.out.println("Enter new price: $");
                double newPriceBoth = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Enter new description: ");
                String updatedDescription = scanner.nextLine();
                double oldPriceBoth = extractAmountFromTransaction(currentTransaction);
                transactions.set(index, "$" + newPriceBoth + ": " + updatedDescription);
                currentBalance += (oldPriceBoth - newPriceBoth);
                System.out.println("Transaction updated. Current balance adjusted.");
                start();
                break;
        }
    }

    public void editTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to edit.");
            return;
        }
        System.out.println("Transaction log: ");
        List<String> transactionList = new ArrayList<>(transactions);
        for (int i = 0; i < transactionList.size(); i++) {
            System.out.println((i + 1) + ". " + transactionList.get(i));
        }
        System.out.println("Which transaction would you like to edit? ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > transactionList.size()) {
            System.out.println("Invalid transaction number");
            return;
        }
        System.out.println("Would you like to edit: ");
        int index = choice - 1;
        String currentTransaction = transactions.get(index);
        editOptions(index, currentTransaction);
    }

    private double extractAmountFromTransaction(String transaction) {
        try {
            //format is "$amount: description"
            String amountString = transaction.split(":")[0].trim();
            if (amountString.startsWith("$")) {
                amountString = amountString.substring(1).trim();
            }
            return Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing transaction amount.");
            return 0.0;
        }
    }

    public void deleteTransaction() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to delete.");
            start();
            return;
        }
        System.out.println("Which transaction would you like to delete? ");
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println("" + (i + 1) + ". " + transactions.get(i));
        }
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > transactions.size()) {
            System.out.println("Invalid choice. Returning to menu.");
            start();
            return;
        }

        String deletedChoice = transactions.get(choice-1);
        double amount = extractAmountFromTransaction(deletedChoice);

        transactions.remove(choice - 1);
        currentBalance += amount;

        System.out.println("You have deleted: " + deletedChoice);
        System.out.println("Your current balance has been updated to: $" + currentBalance);

        System.out.println("Would you like to delete again: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int yesNo = scanner.nextInt();
        scanner.nextLine();
        switch (yesNo) {
            case 1:
                deleteTransaction();
                break;
            case 2:
                start();
                break;
        }
    }

    public void saveDataName(){
        System.out.println("Enter in the file name you want to save data to: ");
        String fileName = scanner.nextLine().trim();
        if(fileName.isEmpty()) {
            fileName = "Budget Info.txt";
        }
        if(!fileName.endsWith(".txt")){
            fileName +=  ".txt";
        }
        System.out.println("Saving data to file: " + fileName);
        saveDataToFile(fileName);
    }

    public void saveDataToFile(String fileName){
        try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Budget: " + desiredBudget);
            writer.println("Current Balance: "+ currentBalance);
            writer.println("Transaction log: ");
            for(String transaction: transactions) {
                writer.println(transaction);
            }
            System.out.println("Data saved successfully.");
        }
        catch(IOException e){
            System.out.println("An error occurred while saving.");
            e.printStackTrace();
        }
        start();
    }

    public void loadDataName() {
        String loadFileName = "";
        boolean validFileName = false;
        while (!validFileName) {
            System.out.println("Enter the file you want to load data from: ");
            loadFileName = scanner.nextLine().trim();

            if (loadFileName.isEmpty()) {
                System.out.println("File name cannot be empty. Try again.");
            } else {
                loadFileName = loadFileName.endsWith(".txt") ? loadFileName : loadFileName + ".txt";
                File file = new File(loadFileName);

                if (!file.exists()) {
                    System.out.println("The file " + loadFileName + " does not exist.");
                    System.out.println("Do you want to try again? 1. Yes 2. No");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice == 2) {
                        start();
                        return;
                    } else if (choice != 1) {
                        System.out.println("Invalid choice. Returning to menu.");
                        start();
                        return;
                    }
                }
                else{
                    loadDataFromFile(file);
                    validFileName = true;
                }
            }
        }
    }

    public void loadDataFromFile(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("Budget: ")) {
                desiredBudget = Double.parseDouble(line.substring("Budget: ".length()));
                System.out.println("Budget: " + desiredBudget);
            }

            line = reader.readLine();
            if (line != null && line.startsWith("Current Balance: ")) {
                currentBalance = Double.parseDouble(line.substring("Current Balance: ".length()));
                System.out.println("Current Balance: " + currentBalance);
            }

            System.out.println("Transactions: ");
            transactions.clear();
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
                System.out.println(line);
            }

            System.out.println("Data loaded successfully.");
        }
        catch (IOException e) {
            System.out.println("An error occurred while loading data.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numerical data from the file.");
            e.printStackTrace();
        }
        returnToMenu();
    }
    public void returnToMenu() {
        System.out.println("Press Enter to return to the menu...");
        scanner.nextLine();
        start();
    }

    public static void main(String[] args) {
        BudgetTracker tracker = new BudgetTracker();
        tracker.setDesiredBudget();
        tracker.start();
    }
}

