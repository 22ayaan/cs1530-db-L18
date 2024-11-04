/**
 * A helper class for querying the Store example table.
 * The class abstracts SQL query strings passed to JDBC
 * and provides common simple queries that might be used.
 *
 * @author Brian T. Nixon
 */

package cs1530.dbdemo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class StoreQueryHelper {
    private static final int QUERY_TIMEOUT = 30;
    private Connection databaseConnection;

    /**
     * The constructor for the StoreQueryHelper class, which
     * attempts to build and initialize the STORE table in the
     * H2 database with some sample data
     *
     * @param conn The database connection for executing modifications
     *             and queries
     */
    public StoreQueryHelper(Connection conn) {
        this.databaseConnection = conn;
        try (Statement st = databaseConnection.createStatement()) {
            st.setQueryTimeout(QUERY_TIMEOUT); // set timeout to 30 seconds
            st.executeUpdate("DROP TABLE IF EXISTS STORE CASCADE;");
            st.executeUpdate("CREATE TABLE STORE (storeNumber integer PRIMARY KEY, " +
                    "name varchar(50)," +
                    "storeType varchar(50)," +
                    "street varchar(50)," +
                    "city varchar(50)," +
                    "state varchar(20));");
            // Can execute each update as a single statement
            st.executeUpdate("INSERT INTO STORE VALUES (1, 'Test Store', 'sitting', 'Forbes', 'Pittsburgh', 'Pennsylvania');");
            st.executeUpdate("INSERT INTO STORE VALUES (2, 'Test Store2', 'sitting', 'Fifth', 'Pittsburgh', 'Pennsylvania');");

            // Or execute multiple updates within a single statement
            st.executeUpdate("INSERT INTO STORE VALUES (3, 'Test Store 3', 'drive-through', 'Lincoln', 'Seattle', 'Washington')," +
                    "(4, 'Test Store 4', 'digital', 'Pike', 'Los Angeles', 'California')," +
                    "(5, 'Test Store 5', 'digital', 'Park Place', 'Hollywood', 'California')," +
                    "(6, 'Test Store 6', 'sitting', 'Madison Square', 'New York City', 'New York')," +
                    "(7, 'Test Store 7', 'digital', 'Washington Ave', 'New York City', 'New York')," +
                    "(8, 'Test Store 8', 'drive-through', 'Lincoln Place', 'Albany', 'New York')," +
                    "(9, 'Test Store 9', 'digital', 'Forbes', 'Pittsburgh', 'Pennsylvania')," +
                    "(10, 'Test Store 10', 'drive-through', 'Boulevard of the Allies', 'Pittsburgh', 'Pennsylvania');");
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * A helper function for querying all rows within a single table
     *
     * @return A List of all Stores in the H2 database
     */
    protected List<RowInterface> findAll() {
        List<RowInterface> storeList = new ArrayList<>();
        try (Statement st = databaseConnection.createStatement()) {
            st.setQueryTimeout(QUERY_TIMEOUT); // set timeout to 30 seconds
            // TODO: Replace the Null assignment below with the SQL query to retrieve all stores
            ResultSet rs = st.executeQuery("SELECT * FROM STORE");
            while (rs != null && rs.next()) {
                Store currentStore = buildStoreFromRow(rs);
                storeList.add(currentStore);
            }
        } catch (SQLException e) {
            handleError(e);
        }
        return storeList;
    }

    /**
     * A helper function for querying the Stores with the specified
     * storeNumber
     *
     * @param storeNumber The storeNumber that the queried rows should have
     * @return A list of all Stores in the H2 database with the specified storeNumber
     */
    protected List<RowInterface> findByStoreNumber(int storeNumber) {
        List<RowInterface> storeList = new ArrayList<>();
        // TODO: Fill-In the SQL statement below to filter for rows with a given storeNumber
        try (PreparedStatement st = databaseConnection.prepareStatement("SELECT * FROM STORE WHERE storeNumber = ?")) {
            st.setQueryTimeout(QUERY_TIMEOUT);
            // TODO: Given the updated SQL statement, set the storeNumber parameter
            st.setInt(1, storeNumber);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Store currentStore = buildStoreFromRow(rs);
                storeList.add(currentStore);
            }
        } catch (SQLException e) {
            handleError(e);
        }
        return storeList;
    }

    /**
     * A helper function for querying the Stores with the specified
     * storeNumber
     *
     * @param storeType The storeType that the queried rows should have
     * @return A list of all Stores in the H2 database with the specified storeType
     */
    protected List<RowInterface> findByStoreType(String storeType) {
        List<RowInterface> storeList = new ArrayList<>();
        // TODO: Fill-In the SQL statement below to filter for rows with a given storeType
        try (PreparedStatement st = databaseConnection.prepareStatement("SELECT * FROM STORE WHERE storeType = ?")) {
            st.setQueryTimeout(QUERY_TIMEOUT);
            // TODO: Given the updated SQL statement, set the storeType parameter
            st.setString(1, storeType);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Store currentStore = buildStoreFromRow(rs);
                storeList.add(currentStore);
            }
        } catch (SQLException e) {
            handleError(e);
        }
        return storeList;
    }

    /**
     * A helper function for querying the Stores with the specified
     * city and state
     *
     * @param city The city that the queried rows should have
     * @param state The state that the queried rows should have
     * @return A list of all Stores in the H2 database with the specified
     *          city and state
     */
    protected List<RowInterface> findByCityAndState(String city, String state) {
        List<RowInterface> storeList = new ArrayList<>();
        // TODO: Fill-In the SQL statement below to filter for rows with a given city and state
        try (PreparedStatement st = databaseConnection.prepareStatement("SELECT * FROM STORE WHERE city = ? AND state = ?")) {
            st.setQueryTimeout(QUERY_TIMEOUT);
            // TODO: Given the updated SQL statement, set the city and state parameters
            st.setString(1, city);
            st.setString(2, state);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Store currentStore = buildStoreFromRow(rs);
                storeList.add(currentStore);
            }
        } catch (SQLException e) {
            handleError(e);
        }
        return storeList;
    }

    /**
     * A private helper function for converting a row from ResultSet (JDBC's
     * standard return type for a query) to an instance of the Store class.
     *
     * @param rs The ResultSet being iterated from executing a query
     * @return The Store instance that matches the ResultSet's current row
     * @throws SQLException when the ResultSet is closed or another exception occurs
     *                      such as trying to access a column that is not part of the ResultSet
     */
    private static Store buildStoreFromRow(ResultSet rs) throws SQLException {
        return new Store(rs.getInt("storeNumber"),
                rs.getString("name"),
                rs.getString("storeType"),
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("state"));
    }

    /**
     * A helper function for handling errors that prints the error message,
     * SQL State, and the SQL Code for the error
     * @param err The SQLException being handled
     */
    private static void handleError(SQLException err) {
        System.err.println("The following error occurred while executing the query/update:");
        System.err.println("Message = " + err.getMessage());
        System.err.println("SQLState = " + err.getSQLState());
        System.err.println("SQL Code = " + err.getErrorCode());
    }
}
