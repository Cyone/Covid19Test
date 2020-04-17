package api.covid19api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class ApiCaller {
    public static void main(String[] args) throws SQLException {
        DBConnector dbConnector = new DBConnector();
        dbConnector.init();
        DownloadSummary ds = new DownloadSummary();
        Summary sum = ds.getSummary(ds.downloadSummary());
        dbConnector.storeSummary(sum);

        List<Country> countries = sum.getCountries();
        long affected = countries.stream().filter(c -> c.getTotalConfirmed() > 0).count();
        System.out.println("Total countries affected (have confirmed cases): " + affected);
        Global global = sum.getGlobal();
        double recoveryPercent = (double) global.getTotalRecovered() / (double) global.getTotalConfirmed();
        System.out.println("Global recovery rate is: " +
                new BigDecimal(recoveryPercent * 100.0).setScale(2, RoundingMode.HALF_UP).doubleValue() + "%");
    }
}
