package wooteco.subway.line.web;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.line.Line;
import wooteco.subway.station.web.StationResponse;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse create(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), new ArrayList<>());
    }
}
