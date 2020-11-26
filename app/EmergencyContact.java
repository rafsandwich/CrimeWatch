package com.example.crimewatch;

public class EmergencyContact {

    private String EmergencyNum;
    private String WebReport;


    public EmergencyContact(String EmergencyNum, String WebReport) {
        this.EmergencyNum = EmergencyNum;
        this.WebReport = WebReport;
    }

    public String DisplayContact()
    {
       return "Emergency number: " + EmergencyNum + "The following link can be used to report a crime: " + WebReport;
    }

    public String getEmergencyNum()
    {
      return EmergencyNum;
    }

    public void setEmergencyNum(String emergencyNum) {
        EmergencyNum = emergencyNum;
    }

    public String getWebReport() {
        return WebReport;
    }

    public void setWebReport(String webReport) {
        WebReport = webReport;
    }
}
