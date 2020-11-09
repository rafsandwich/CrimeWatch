package project.crimewatch;

public class Crime
{
    private String crimeID;
    private String month;
    private double latitude;
    private double longitude;
    private String LSOAName;
    private String crimeType;
    private int lastOutcome;

    Crime(){};

    public String getCrimeID() {
        return crimeID;
    }

    public void setCrimeID(String crimeID) {
        this.crimeID = crimeID;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLSOAName() {
        return LSOAName;
    }

    public void setLSOAName(String LSOAName) {
        this.LSOAName = LSOAName;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public int getLastOutcome() {
        return lastOutcome;
    }

    public void setLastOutcome(int lastOutcome) {
        this.lastOutcome = lastOutcome;
    }

}
