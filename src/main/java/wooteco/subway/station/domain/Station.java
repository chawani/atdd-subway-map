package wooteco.subway.station.domain;

import java.util.Objects;
import wooteco.subway.station.controller.dto.StationRequest;

public class Station {

    private final Long id;
    private final String name;

    public Station(StationRequest stationRequest) {
        this(null, stationRequest.getName());
    }

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public final Long getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return id.equals(station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
