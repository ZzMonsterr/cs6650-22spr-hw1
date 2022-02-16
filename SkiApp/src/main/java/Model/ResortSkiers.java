package Model;

public class ResortSkiers {
    String time;
    int numSKiers;

    public ResortSkiers(String time, int numSKiers) {
        this.time = time;
        this.numSKiers = numSKiers;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumSKiers() {
        return numSKiers;
    }

    public void setNumSKiers(int numSKiers) {
        this.numSKiers = numSKiers;
    }
}
