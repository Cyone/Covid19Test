package api.covid19api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DownloadSummaryTest {
    private String summaryJson;
    private DownloadSummary downloadSummary;

    @Test
    @Before
    public void testDownload() {
        downloadSummary = new DownloadSummary();
        String summary = downloadSummary.downloadSummary();
        summaryJson = summary;
        assertNotNull(summary);
    }

    @Test
    public void testSummaryDataObject() {
        Global global = new Global(1, 1, 1, 1, 1, 1);
        Country country = new Country("", "", "", 1, 1, 1, 1, 1, 1, new Date());
        List<Country> countries = new ArrayList<>();
        countries.add(country);
        Summary summary = new Summary(global, countries, new Date());
        assertNotNull(global);
        assertNotNull(summary);
        assertNotNull(country);

        assertEquals(country.getCountry(), "");
        assertEquals(global.getNewConfirmed(), 1);
        assertEquals(summary.getCountries().get(0), country);

        assertNotEquals(global.getNewDeaths(), 0);
    }

    @Test
    public void testSummary() {
        Summary summary = downloadSummary.getSummary(summaryJson);

        assertNotNull(summary);
        assertNotNull(summary.getCountries());
        assertNotNull(summary.getGlobal());
        assertNotNull(summary.getDate());

        assertEquals(summary.getCountries().size(), 247);

    }

    @Test
    public void testGlobal() throws Exception {
        Global global = downloadSummary.getSummary(summaryJson).getGlobal();

        assertTrue(global.getSqlInsert().contains("INSERT"));
        assertTrue(global.getSqlUpdate().contains("UPDATE"));

        assertTrue(global.getValues().length == 6);
        for (Object o : global.getValues()) {
            assertNotNull(o);
        }

        String offlineJson = Files.readString(Paths.get("src\\main\\java\\api\\covid19api\\json.json"));
        long offlineGlobal = new Gson().fromJson(offlineJson, JsonObject.class).getAsJsonObject("Global").getAsJsonPrimitive("TotalRecovered").getAsLong();
        assertEquals(global.getTotalRecovered(), offlineGlobal);
        assertEquals(global.getTotalConfirmed(), 2150422);
        assertEquals(9619, global.getNewDeaths());
        assertEquals(global.getNewRecovered(), 31084);

    }

    @Test
    public void testCountries() {
        List<Country> countries = downloadSummary.getSummary(summaryJson).getCountries();
        assertNotNull(countries);

        assertEquals(247, countries.size());
        assertTrue(countries.get(1).getInsertWithUpdate().contains("INSERT"));
        assertTrue(countries.get(100).getInsertWithUpdate().contains("UPDATE"));

    }
}
