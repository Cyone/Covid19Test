package api.covid19api;

/**
 * Data Class
 */
class Global {
    private long NewConfirmed;
    private long TotalConfirmed;
    private long NewDeaths;
    private long TotalDeaths;
    private long NewRecovered;
    private long TotalRecovered;

    public Global(long newConfirmed, long totalConfirmed, long newDeath, long totalDeath, long newRecovered, long totalRecovered) {
        NewConfirmed = newConfirmed;
        TotalConfirmed = totalConfirmed;
        NewDeaths = newDeath;
        TotalDeaths = totalDeath;
        NewRecovered = newRecovered;
        TotalRecovered = totalRecovered;
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

    /**
     * SQL Update statement with this object's data
     * @return
     */
    public String getSqlUpdate() {
        String update = "UPDATE global\n" +
                "SET IsGlobal = true, NewConfirmed = %d,\n" +
                "    TotalConfirmed =    %d,\n" +
                "    NewDeaths= %d,\n" +
                "    TotalDeaths=%d,\n" +
                "    NewRecovered=%d,\n" +
                "    TotalRecovered=%d,\n" +
                "Date = ?\n"+
                "WHERE IsGlobal = true;\n";
        return String.format(update, getValues());
    }

    /**
     * SQL Insert statement with this object's data
     * @return
     */
    public String getSqlInsert() {
        String insert = "INSERT INTO global(IsGlobal,NewConfirmed, TotalConfirmed, NewDeaths, TotalDeaths, NewRecovered, TotalRecovered, Date)\n" +
                "values (true,\n" +
                "        %d,\n" +
                "        %d,\n" +
                "        %d,\n" +
                "        %d,\n" +
                "        %d,\n" +
                "        %d,\n" +
                "        ?);\n";
        return String.format(insert, getValues());
    }

    protected Object[] getValues(){
        return new Object[]{NewConfirmed, TotalConfirmed, NewDeaths, TotalDeaths, NewRecovered, TotalRecovered};
    }
    @Override
    public String toString() {
        return "Global{" +
                "NewConfirmed=" + NewConfirmed +
                ", TotalConfirmed=" + TotalConfirmed +
                ", NewDeath=" + NewDeaths +
                ", TotalDeath=" + TotalDeaths +
                ", NewRecovered=" + NewRecovered +
                ", TotalRecovered=" + TotalRecovered +
                '}';
    }
}
