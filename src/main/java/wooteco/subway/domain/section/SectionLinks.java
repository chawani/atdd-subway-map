package wooteco.subway.domain.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SectionLinks {

    private final Map<Long, Long> sections;

    private SectionLinks(Map<Long, Long> sections) {
        this.sections = new HashMap<>(sections);
    }

    static SectionLinks from(List<Section> sections) {
        Map<Long, Long> stationIds = new HashMap<>();
        for (Section existingSection : sections) {
            stationIds.put(existingSection.getUpStationId(), existingSection.getDownStationId());
        }
        return new SectionLinks(stationIds);
    }

    public List<Long> getAllStationId() {
        Set<Long> ids = new HashSet<>(sections.keySet());
        ids.addAll(sections.values());
        return new ArrayList<>(ids);
    }

    boolean isNotExistMatchedStation(Section section) {
        return isNotExistStation(section.getUpStationId()) && isNotExistStation(section.getDownStationId());
    }

    boolean isAllMatchedStation(Section section) {
        return !isNotExistStation(section.getUpStationId()) && !isNotExistStation(section.getDownStationId());
    }

    boolean isEndSection(Section section) {
        return (isExistUpStation(section.getDownStationId()) && !isExistDownStation(section.getDownStationId()))
            || (isExistDownStation(section.getUpStationId()) && !isExistUpStation(section.getUpStationId()));
    }

    boolean isEndStation(Long stationId) {
        return (isExistDownStation(stationId) && !isExistUpStation(stationId))
            || (!isExistDownStation(stationId) && isExistUpStation(stationId));
    }

    boolean isNotExistStation(Long id) {
        return !isExistUpStation(id) && !isExistDownStation(id);
    }

    boolean isExistUpStation(Long id) {
        return sections.containsKey(id);
    }

    private boolean isExistDownStation(Long id) {
        return sections.containsValue(id);
    }
}
