package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * API: https://app.swaggerhub.com/apis/cloud-perf/SkiDataAPI/1.0.2#/SkierVertical
 */
public class SkierVertical {
    private List<SkierVerticalResort> skierVerticalResorts;

    public SkierVertical() {
        this.skierVerticalResorts = new ArrayList<>();
    }

    public SkierVertical(List<SkierVerticalResort> skierVerticalResorts) {
        this.skierVerticalResorts = skierVerticalResorts;
    }

    public List<SkierVerticalResort> getVerticals() {
        return skierVerticalResorts;
    }

    public void setVerticals(List<SkierVerticalResort> skierVerticalResorts) {
        this.skierVerticalResorts = skierVerticalResorts;
    }

    public void add(SkierVerticalResort v) {
        this.skierVerticalResorts.add(v);
    }
}
