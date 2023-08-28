import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/progmatic";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            initializeSummerSportsTable(connection);
            initializePersonTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeSummerSportsTable(Connection connection) throws SQLException {
        createTableIfNotExists(connection,
                "summer_sports",
                "(id INT AUTO_INCREMENT PRIMARY KEY, sport_name VARCHAR(255))");

        if (isTableEmpty(connection, "summer_sports")) {
            insertSummerSportsData(connection);
        } else {
            System.out.println("Table 'summer_sports' already initialized.");
        }
    }

    private static void initializePersonTable(Connection connection) throws SQLException {
        createTableIfNotExists(connection,
                "person",
                "(id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), favorite_sport_id INT)");

        if (isTableEmpty(connection, "person")) {
            insertPersonData(connection);
        } else {
            System.out.println("Table 'person' already initialized.");
        }
    }

    private static void createTableIfNotExists(Connection connection, String tableName, String createQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " " + createQuery);
            System.out.println("Table '" + tableName + "' created or already exists.");
        }
    }

    private static boolean isTableEmpty(Connection connection, String tableName) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            int rowCount = resultSet.getInt(1);
            return rowCount == 0;
        } catch (SQLException e) {
            return true;
        }
    }

    private static void insertSummerSportsData(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO summer_sports (sport_name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, "Swimming");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "Cycling");
            preparedStatement.executeUpdate();
        }
    }

    private static void insertPersonData(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO person (name, favorite_sport_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, "Alice");
            preparedStatement.setInt(2, 1);
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "Bob");
            preparedStatement.setInt(2, 2);
            preparedStatement.executeUpdate();
        }
    }
}
