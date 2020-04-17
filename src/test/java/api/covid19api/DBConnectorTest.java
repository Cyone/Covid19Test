package api.covid19api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBConnectorTest {
    private final static String user = "root";
    private final static String password = "";
    private final static String dbName = "Covid19";
    private final static String hostName = "localhost";
    private final static int port = 3306;
    private Connection connection;

    @Before
    public void setConnection() throws Exception {
        connection = new DBConnector().init().getConnection();

    }

    @Test
    public void testConnection() throws Exception {
        assertNotNull(connection);
    }

    @Test
    public void testNoDB() throws Exception {
        DBConnector dbc = new DBConnector(user, password, "testCovid", hostName, port);
        SQLException thrown = assertThrows(SQLException.class, () -> dbc.init());
        assertEquals(1049, thrown.getErrorCode());
    }

    @Test
    public void testWrongLocation() {
        DBConnector dbc = new DBConnector(user, password, dbName, "hostName", port);
        SQLException thrown = assertThrows(SQLException.class, () -> dbc.init());
        assertEquals(-1, thrown.getErrorCode());
    }

    public int countRows(ResultSet rs) {
        int rowCount = 0;
        try {
            rs.last();
            rowCount = rs.getRow();
        } catch (Exception e) {
            rowCount = 0;
        }
        return rowCount;
    }

    @Test
    public void sqlQueryTest() throws SQLException {
        String selectGlobal = "SELECT * FROM global";
        String selectCountries = "SELECT * FROM countries";
        ResultSet rsGlobal = connection.prepareStatement(selectGlobal).executeQuery();
        ResultSet rsCountries = connection.prepareStatement(selectCountries).executeQuery();
        assertEquals(1, countRows(rsGlobal));
        assertEquals(247, countRows(rsCountries));

    }

    @Test
    public void testCredentials() {
        DBConnector dbc = new DBConnector(user, "password", dbName, hostName, port);
        SQLException thrown = assertThrows(SQLException.class, () -> dbc.init());
        assertEquals(1045, thrown.getErrorCode());
    }

    @After
    public void closeConnection() throws Exception {
        connection.close();
    }
}
