package Model;

import java.util.ArrayList;
import java.util.List;


/**
 * API: https://app.swaggerhub.com/apis/cloud-perf/SkiDataAPI/1.0.2#/ResortsList
 */
public class ResortsList {
    private List<ResortsListResort> resortsListResorts;

    public ResortsList(List<ResortsListResort> resortsListResorts) {
        this.resortsListResorts = resortsListResorts;
    }

    public ResortsList() {
        this.resortsListResorts = new ArrayList<ResortsListResort>();
    }

    public void addResort(ResortsListResort resortsListResort) {
        this.resortsListResorts.add(resortsListResort);
    }
}

