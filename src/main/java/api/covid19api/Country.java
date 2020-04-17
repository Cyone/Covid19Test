package api.covid19api;

import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

class Country {

    private String Country;
    private String CountryCode;
    private String Slug;
    private long NewConfirmed;
    private long TotalConfirmed;
    private long NewDeaths;
    private long TotalDeaths;
    private long NewRecovered;
    private long TotalRecovered;
    private Date Date;
    @Expose(serialize = false, deserialize = false)
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:MM:SS");

    public Country(String country, String countryCode, String slug, long newConfirmed, long totalConfirmed, long newDeaths, long totalDeaths, long newRecovered, long totalRecovered, Date date) {
        this.Country = country;
        this.CountryCode = countryCode;
        this.Slug = slug;
        NewConfirmed = newConfirmed;
        TotalConfirmed = totalConfirmed;
        NewDeaths = newDeaths;
        TotalDeaths = totalDeaths;
        NewRecovered = newRecovered;
        TotalRecovered = totalRecovered;
        this.Date = date;
    }

    public String getCountry() {
        return Country;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getSlug() {
        return Slug;
    }

    public long getNewConfirmed() {
        return NewConfirmed;
    }

    public long getTotalConfirmed() {
        return TotalConfirmed;
    }

    public long getNewDeaths() {
        return NewDeaths;
    }

    public long getTotalDeaths() {
        return TotalDeaths;
    }

    public long getNewRecovered() {
        return NewRecovered;
    }

    public long getTotalRecovered() {
        return TotalRecovered;
    }

    public Date getDate() {
        return Date;
    }

    /**
     * Formated INSERT ON DUPLICATE KEY UPDATE this object's data
     * Updates every column in the table
     * @return
     */
    public String getInsertWithUpdate() {
        String countryInsert = "INSERT INTO countries\n" +
                "values ('%s', '%s', '%s', %d, %d, %d, %d, %d, %d, '%s')" +
                "ON DUPLICATE KEY UPDATE ";

        return String.format(countryInsert, getValues()) + String.format(toStringForSql(),getValues());

    }

    /**
     * Arrays of this object's datafields. Strings with ' are escaped to ''
     * @return
     */
    protected Object[] getValues() {
        Object[] objects = new Object[]{Country,
                CountryCode,
                Slug,
                NewConfirmed,
                TotalConfirmed,
                NewDeaths,
                TotalDeaths,
                NewRecovered,
                TotalRecovered,
                dateFormat.format(Date)
        };
        for (int i =0; i<objects.length; i++){
            if (objects[i] instanceof String){
                objects[i] = ((String) objects[i]).replace("'","''");
            }
        }
        return objects;
    }

    private String toStringForSql() {
        return
                "Country='%s'" +
                        ", CountryCode='%s'" +
                        ", Slug='%s'" +
                        ", NewConfirmed=%d" +
                        ", TotalConfirmed=%d" +
                        ", NewDeaths=%d" +
                        ", TotalDeaths=%d" +
                        ", NewRecovered=%d" +
                        ", TotalRecovered=%d" +
                        ", Date='%s'";
    }

    @Override
    public String toString() {
        return "Country{" +
                "Country='" + Country + '\'' +
                ", CountryCode='" + CountryCode + '\'' +
                ", Slug='" + Slug + '\'' +
                ", NewConfirmed=" + NewConfirmed +
                ", TotalConfirmed=" + TotalConfirmed +
                ", NewDeaths=" + NewDeaths +
                ", TotalDeaths=" + TotalDeaths +
                ", NewRecovered=" + NewRecovered +
                ", TotalRecovered=" + TotalRecovered +
                ", Date=" + Date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return NewConfirmed == country.NewConfirmed &&
                TotalConfirmed == country.TotalConfirmed &&
                NewDeaths == country.NewDeaths &&
                TotalDeaths == country.TotalDeaths &&
                NewRecovered == country.NewRecovered &&
                TotalRecovered == country.TotalRecovered &&
                Objects.equals(Country, country.Country) &&
                Objects.equals(CountryCode, country.CountryCode) &&
                Objects.equals(Slug, country.Slug) &&
                Objects.equals(Date, country.Date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Country, CountryCode, Slug, NewConfirmed, TotalConfirmed, NewDeaths, TotalDeaths, NewRecovered, TotalRecovered, Date);
    }
}
