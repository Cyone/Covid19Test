package api.covid19api;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Data Class
 */
public class Summary {
    @SerializedName("Global")
    private Global global;
    @SerializedName("Countries")
    private List<Country> countries;
    @SerializedName("Date")
    private Date date;

    public Summary(Global global, List<Country> countries, Date date) {
        this.global = global;
        this.countries = countries;
        this.date = date;
    }

    public Global getGlobal() {
        return global;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public Date getDate() {
        return date;
    }

    public String getDateForSql(){
        return new SimpleDateFormat("YYYY-MM-dd HH:MM:SS").format(date);
    }
    @Override
    public String toString() {
        return "Summary{" +
                "global=" + global +
                ", countries=" + countries +
                ", date=" + date +
                '}';
    }


}
