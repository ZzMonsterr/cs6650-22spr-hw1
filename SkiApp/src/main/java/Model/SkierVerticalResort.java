package Model;

public class SkierVerticalResort {
    private String seasonID;
    private int totalVert;

    public SkierVerticalResort(String seasonID, int totalVert) {
        this.seasonID = seasonID;
        this.totalVert = totalVert;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public int getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(int totalVert) {
        this.totalVert = totalVert;
    }
}
