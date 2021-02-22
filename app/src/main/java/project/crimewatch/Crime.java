package project.crimewatch;

public class Crime
{

    static private int count;
    private int UID;

    private String crimeID;
    private String date;
    private String reportedBy;
    private String fallsWithin;
    private String latitude;
    private String longitude;
    private String location;
    private String LSOACode;
    private String LSOAName;
    private String crimeType;


    public Crime()
    {

    }

    // TODO lat and long need to switch positions to be correct.
    public Crime(String crimeID, String date, String reportedBy, String fallsWithin,
                 String latitude, String longitude, String location, String LSOACode,
                 String LSOAName, String crimeType)
    {
        this.crimeID = crimeID;
        this.date = date;
        this.reportedBy = reportedBy;
        this.fallsWithin = fallsWithin;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.LSOACode = LSOACode;
        this.LSOAName = LSOAName;
        this.crimeType = crimeType;

        UID = count;
        count++;
    }

    public String displayCrimeData()
    {
        return "CrimeUID: " + UID +
                ", Month: " + date +
                ", latitude: " + latitude +
                ", longitude: " + longitude +
                ", Crime Type: " + crimeType;
    }

    public String getCrimeID() {
        return crimeID;
    }

    public void setCrimeID(String crimeID) {
        this.crimeID = crimeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getFallsWithin() {
        return fallsWithin;
    }

    public void setFallsWithin(String fallsWithin) {
        this.fallsWithin = fallsWithin;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLSOACode() {
        return LSOACode;
    }

    public void setLSOACode(String LSOACode) {
        this.LSOACode = LSOACode;
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

    public static int getCount() { return count; }

    public int getUID() { return UID; }

    public void setUID(int id) { this.UID = id; }
}
