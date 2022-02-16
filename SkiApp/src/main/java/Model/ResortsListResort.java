package Model;

/**
 * API:
 * https://app.swaggerhub.com/apis/cloud-perf/SkiDataAPI/1.0.2#/ResortsList
 * line 287-297
 */
public class ResortsListResort {
    private String resortName;
    private int resortID;

    public ResortsListResort(String resortName, int resortID) {
        this.resortName = resortName;
        this.resortID = resortID;
    }

    public String getResortName() {
        return resortName;
    }

    public int getResortID() {
        return resortID;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public void setResortID(int resortID) {
        this.resortID = resortID;
    }
}
