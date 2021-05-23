package Database;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class Database {
    private static final String DB_URL = "";

    private final Connection conn;
    private static Database instance;
    private static int lastModifiedRowsCount;

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL);
        }

        catch (SQLException exception) {
           throw new RuntimeException("Couldn't initialize the connection: " + exception);
        }

        catch (ClassNotFoundException exception) {
            throw new RuntimeException("Couldn't find the JDBC driver: " + exception);
        }

        lastModifiedRowsCount = -1;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public int getLastModifiedRowsCount() {
        return lastModifiedRowsCount;
    }

    public ResultSet sendQuery(String query, List<?> values) {
        try {
            PreparedStatement statement = instance.conn.prepareStatement(query);

            for (int i = 1; i <= values.size(); ++i) {
                Object item = values.get(i - 1);

                if (Objects.isNull(item)) {
                    statement.setNull(i, Types.BINARY);
                    continue;
                }

                switch (item.getClass().getSimpleName()) {
                    case "String" -> statement.setString(i, (String) item);
                    case "Double" -> statement.setDouble(i, (Double) item);
                    case "Integer" -> statement.setInt(i, (int) item);
                    case "Boolean" -> statement.setBoolean(i, (boolean) item);
                    case "Timestamp" -> statement.setTimestamp(i, (Timestamp) item);
                }
            }

            statement.execute();

            lastModifiedRowsCount = statement.getUpdateCount();

            return statement.getResultSet();
        }

        catch (Exception exception) {
            throw new RuntimeException("Couldn't send query: " + exception);
        }
    }
}
