package api.covid19api;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class DBConnector {
    private static final String TABLE_GLOBAL = "global";
    private static final String TABLE_COUNTRIES = "countries";
    private String user = "root";
    private String password = "";
    private String dbName = "Covid19";
    private String hostName = "localhost";
    private int port = 3306;
    private Connection connection = null;

    /**
     * DBConnector with default values
     */
    public DBConnector() {

    }

    public DBConnector(String user, String password, String dbName, String hostName, int port) {
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
        this.hostName = hostName;
    }

    /**
     * Establish connection. Use @getConnection to get usable connection
     */
    public DBConnector init() throws SQLException {
        try {
            connection = makeConnection();
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 1049 && !"testCovid".equals(dbName)) {
                createDB();
                connection = makeConnection();
            }
            throw sqlException;
        }
        createTableGlobal();
        createTableCountries();
        return this;
    }

    /**
     *
     * @return Connection ready to execute SQL
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * New DataSource with values Different from default
     *
     * @param hostName
     * @param port
     * @param dbName
     * @return
     */
    private DataSource getDataSource(String hostName, int port, String dbName) {
        return new MariaDbDataSource(hostName, port, dbName);

    }

    private DataSource getDataSource() {
        return new MariaDbDataSource(hostName, port, dbName);
    }

    /**
     * Tries to create DB.
     * Used if SQLException 1049 is thrown
     * @throws SQLException
     */
    private void createDB() throws SQLException {
        Connection con = null;
        try {
            DataSource dataSource = getDataSource(hostName, port, "");
            con = dataSource.getConnection(user, password);
            con.prepareStatement("create database " + dbName).execute();
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == -1) {
                System.err.println("Can't connect given hostname: " + hostName + ", and port: " + port);
                sqlException.printStackTrace();
            }
        } finally {
            if (con != null) con.close();
        }
    }

    /**
     * Connects to db with Default values
     *
     * @return
     * @throws SQLException
     */
    private Connection makeConnection() throws SQLException {
        return getDataSource().getConnection(user, password);
    }

    /**
     * Prints results from db
      * @param resultSet
     * @throws SQLException
     */
    protected void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colNum = metaData.getColumnCount();
        for (int i = 1; i <= colNum; i++) {
            System.out.print(metaData.getColumnName(i) + "    ");
        }
        System.out.println();
        while (resultSet.next()) {
            for (int i = 1; i <= colNum; i++) {
                String colValue = resultSet.getString(i);
                System.out.print(colValue);
            }
            System.out.println();
        }
    }

    private void createTableCountries() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COUNTRIES + " (\n" +
                "    Country VARCHAR(255) NOT NULL ,\n" +
                "    CountryCode CHAR(2) NOT NULL ,\n" +
                "    Slug VARCHAR(255),\n" +
                "    NewConfirmed BIGINT DEFAULT 0,\n" +
                "    TotalConfirmed BIGINT DEFAULT 0,\n" +
                "    NewDeaths BIGINT DEFAULT 0,\n" +
                "    TotalDeaths BIGINT DEFAULT 0,\n" +
                "    NewRecovered BIGINT DEFAULT 0,\n" +
                "    TotalRecovered BIGINT DEFAULT 0,\n" +
                "    Date DATETIME ,\n" +
                "    PRIMARY KEY (CountryCode)\n" +
                ");";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

    }

    /**
     * Boolean lock to always have 1 row
     */
    private void createTableGlobal() throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_GLOBAL + "\n" +
                "(   IsGlobal       BOOLEAN NOT NULL,\n" +
                "    NewConfirmed   BIGINT,\n" +
                "    TotalConfirmed BIGINT,\n" +
                "    NewDeaths       BIGINT,\n" +
                "    TotalDeaths    BIGINT,\n" +
                "    NewRecovered   BIGINT,\n" +
                "    TotalRecovered BIGINT,\n" +
                "    Date DATETIME, \n" +
                "    PRIMARY KEY (IsGlobal)\n" +
                ");\n";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
    }

    public void storeSummary(Summary summary) throws SQLException {

        String checkIfEmpty = "SELECT * FROM " + TABLE_GLOBAL;
        ResultSet rs = connection.createStatement().executeQuery(checkIfEmpty);
        int rowCount = 0;
        try {
            rs.last();
            rowCount = rs.getRow();
        } catch (Exception e) {
            rowCount = 0;
        }
        if (rowCount == 1) {
            PreparedStatement ps = connection.prepareStatement(summary.getGlobal().getSqlUpdate());
            ps.setObject(1, summary.getDateForSql());
            ps.executeUpdate();
        } else {
            PreparedStatement ps = connection.prepareStatement(summary.getGlobal().getSqlInsert());
            ps.setObject(1, summary.getDateForSql());
            ps.executeQuery();
        }

        for (Country country : summary.getCountries()) {
            PreparedStatement ps = connection.prepareStatement(country.getInsertWithUpdate());
            ps.executeQuery();
        }
    }
}
