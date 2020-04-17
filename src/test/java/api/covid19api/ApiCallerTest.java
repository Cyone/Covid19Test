package api.covid19api;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class ApiCallerTest {
    DBConnector dbc;
    DownloadSummary ds;
    Summary summary;

    @Before
    public void init() throws SQLException {
        dbc = new DBConnector();
        dbc.init();
        ds = new DownloadSummary();
        summary = ds.getSummary(ds.downloadSummary());

    }

    @Test
    public void testCountriesSum() throws SQLException {
        List<Country> countries = summary.getCountries();
        long affected = countries.stream().filter(c -> c.getTotalConfirmed() > 0).count();
        assertEquals(affected,182);

    }

    @Test
    public void testPercent() {
        Global global = summary.getGlobal();
        double recoveryPercent = (double) global.getTotalRecovered() / (double) global.getTotalConfirmed();
        assertTrue(recoveryPercent*100.0 > 20);
        assertTrue(recoveryPercent*100.0 <=30);
    }

    @Test
    public void testConnection(){
        assertNotNull(dbc.getConnection());
    }
}
