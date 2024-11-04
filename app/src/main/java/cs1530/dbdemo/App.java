/*
 * A simple Java client app for connecting to and interacting with
 * an H2 database.
 *
 * @author Brian T. Nixon
 */
package cs1530.dbdemo;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        /*
          Attempt to establish a connection to the database
          Java try-with-resources will create a connection and
          autoclose at the end of the try block
         */
        Properties props = new Properties();
        props.setProperty("user", "testUser");
        props.setProperty("password", "testPassword");

        // The URL can be updated for other DBMSs (provided that a supported driver exists)
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./database/h2_data_storage", props);
             Scanner scanner = new Scanner(System.in)) {
            int menu = -1;

            // Helper Classes for querying the H2 database using JDBC
            StoreQueryHelper storeHelper = new StoreQueryHelper(conn);
            CoffeeQueryHelper coffeeHelper = new CoffeeQueryHelper(conn);
            ReceiptQueryHelper receiptHelper = new ReceiptQueryHelper(conn);

            // For storing the result rows from queries
            List<RowInterface> queryResults;

            // A simple menu allowing the user to select queries
            while (menu != 0) {
                System.out.println("Please enter the number for the query you would like to run from the list below: ");
                displayMenu();
                menu = scanner.nextInt();
                // When using nextInt, the entire buffer is not consumed... so ensure nothing is dangling
                scanner.nextLine();
                switch (menu) {
                    case 1:
                        System.out.println("You've selected to find all stores:");
                        // TODO: Retrieve all stores using the storeHelper by replacing the Null assignment below
                        queryResults = storeHelper.findAll();
                        printResultRows(queryResults);
                        break;
                    case 2:
                        System.out.println("You've selected to find a specific store number, please enter the store number:");
                        /*
                        TODO: Retrieve input from the user for the storeNumber to be queried
                              Then retrieve all stores with the storeNumber by replacing the Null assignment below
                         */
                        int storeByNumber = scanner.nextInt();
                        scanner.nextLine();
                        queryResults = storeHelper.findByStoreNumber(storeByNumber);
                        printResultRows(queryResults);
                        break;
                    case 3:
                        System.out.println("You've selected to find all stores with a specific store type, please enter the store type:");
                        /*
                        TODO: Retrieve input from the user for the store type to be queried
                              Then retrieve all stores with the storeType by replacing the Null assignment below
                         */
                        String storeType = scanner.nextLine();

                        queryResults = storeHelper.findByStoreType(storeType);
                        printResultRows(queryResults);
                        break;
                    case 4:
                        System.out.println("You've selected to find all stores with a specific city and state. Please enter the city:");
                        /*
                        TODO: Retrieve input from the user for the city, then prompt the user for the state
                              Then retrieve all stores within the city and state by replacing the Null assignment below
                         */
                        String city = scanner.nextLine();
                        System.out.println("Please enter the state: ");
                        String state = scanner.nextLine();

                        queryResults = storeHelper.findByCityAndState(city, state);
                        printResultRows(queryResults);
                        break;
                    case 5:
                        System.out.println("You've selected to find all coffees:");
                        queryResults = coffeeHelper.findAll();
                        printResultRows(queryResults);
                        break;
                    case 6:
                        System.out.println("You've selected to find all coffees with a given coffee name: Please enter the name");
                        String coffeeNameToQuery = scanner.nextLine();

                        queryResults = coffeeHelper.findByName(coffeeNameToQuery);
                        printResultRows(queryResults);
                        break;
                    case 7:
                        System.out.println("You've selected to find all coffees with a given intensity: Please enter the intensity");
                        int coffeeIntensityToQuery = scanner.nextInt();
                        scanner.nextLine();

                        queryResults = coffeeHelper.findByIntensity(coffeeIntensityToQuery);
                        printResultRows(queryResults);
                        break;
                    case 8:
                        System.out.println("You've selected to find all coffees within a given price range: Please enter the lower price of the range");
                        double lowerPriceBound = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Please enter the upper price of the range");
                        double upperPriceBound = scanner.nextDouble();
                        scanner.nextLine();

                        queryResults = coffeeHelper.findCoffeeInPriceRange(BigDecimal.valueOf(lowerPriceBound), BigDecimal.valueOf(upperPriceBound));
                        printResultRows(queryResults);
                        break;
                    case 9:
                        System.out.println("You've selected to find all receipts:");

                        queryResults = receiptHelper.findAll();
                        printResultRows(queryResults);
                        break;
                    case 10:
                        System.out.println("You've selected to find all receipts with a given receiptID: Please enter the receiptID");
                        int receiptIDToQuery = scanner.nextInt();
                        scanner.nextLine();

                        queryResults = receiptHelper.findByReceiptID(receiptIDToQuery);
                        printResultRows(queryResults);
                        break;
                    case 11:
                        System.out.println("You've selected to find all receipts for a given storeNumber: Please enter the storeNumber");
                        int storeNumberToQuery = scanner.nextInt();
                        scanner.nextLine();
                        queryResults = receiptHelper.findByStoreNumber(storeNumberToQuery);
                        printResultRows(queryResults);
                        break;
                    case 12:
                        System.out.println("You've selected to find all receipts for a given coffeeID. Please enter the coffeeID");
                        int coffeeIDToQuery = scanner.nextInt();
                        scanner.nextLine();
                        queryResults = receiptHelper.findByCoffeeID(coffeeIDToQuery);
                        printResultRows(queryResults);
                        break;
                    case 13:
                        System.out.println("You've selected to find all receipts within a given quantity range. Please enter the lower quantity of the range");
                        int lowerQuantity = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Please enter the upper quantity of the range");
                        int upperQuantity = scanner.nextInt();
                        scanner.nextLine();

                        queryResults = receiptHelper.findReceiptInQuantityRange(lowerQuantity, upperQuantity);
                        printResultRows(queryResults);
                        break;
                    case 14:
                        System.out.println("You've selected to find all receipts within a given date range.\n" +
                                "Please enter the lower date of the range using the following format: YYYY-MM-DD");
                        String lowerRange = scanner.nextLine();
                        System.out.println("Please enter the upper date of the range using the following format: YYYY-MM-DD");
                        String upperRange = scanner.nextLine();

                        // Convert Strings to Date type
                        Date lowerDate = Date.valueOf(lowerRange);
                        Date upperDate = Date.valueOf(upperRange);

                        queryResults = receiptHelper.findReceiptInDateRange(lowerDate, upperDate);
                        printResultRows(queryResults);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Message = " + e.getMessage());
            System.err.println("SQLState = " + e.getSQLState());
            System.err.println("SQL Code = " + e.getErrorCode());
        }
    }

    private static void displayMenu() {
        // Text Block added in Java 15
        String menuString = """
                +------------------------------------------------------+
                | (0)  Exit the Application                            |
                | (1)  Find All Stores                                 |
                | (2)  Find All Stores with a given store number       |
                | (3)  Find All Stores with a given store type         |
                | (4)  Find All Stores in a given city and state       |
                | (5)  Find All Coffees                                |
                | (6)  Find All Coffees with a given coffee name       |
                | (7)  Find All Coffees for a given intensity          |
                | (8)  Find All Coffees within a given price range     |
                | (9)  Find All Receipts                               |
                | (10) Find All Receipts with a given receiptID        |
                | (11) Find All Receipts for a given storeNumber       |
                | (12) Find All Receipts for a given coffeeID          |
                | (13) Find All Receipts within a given quantity range |
                | (14) Find All Receipts within a given date range     |
                +---------------------------------------------------+""";
        System.out.println(menuString);
    }

    private static void printResultRows(List<RowInterface> queryResults) {
        if (queryResults == null) {
            return;
        }

        // Print the name of the table beforehand
        if (!queryResults.isEmpty()) {
            String tableName;
            if (queryResults.getFirst() instanceof Store) {
                tableName = """
                        +----------------------------------------------------------------+
                        |                             Store                              |
                        +----------------------------------------------------------------+""";
            } else if (queryResults.getFirst() instanceof Coffee) {
                tableName = """
                        +----------------------------------------------------------------+
                        |                             Coffee                             |
                        +----------------------------------------------------------------+""";
            } else {
                tableName = """
                        +----------------------------------------------------------------+
                        |                              Receipt                              |
                        +----------------------------------------------------------------+""";
            }
            System.out.println(tableName);
        }
        for (RowInterface queryResult : queryResults) {
            System.out.println(queryResult);
        }
        System.out.println("\n");
    }
}
