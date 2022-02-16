package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * API: https://app.swaggerhub.com/apis/cloud-perf/SkiDataAPI/1.0.2#/SeasonsList
 */
public class SeasonsList {
    private List<String> seasons;

    public SeasonsList() {
        this.seasons = new ArrayList<String>();
    }

    public SeasonsList(List<String> seasons) {
        this.seasons = seasons;
    }

    public List<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }
}
