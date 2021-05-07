package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;

import java.util.List;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station createStation(String name) {
        Station station = new Station(name);
        long id = stationDao.save(station);
        return stationDao.findById(id);
    }

    public List<Station> findAll() {
        return stationDao.findAll();
    }

    public void deleteById(long id) {
        stationDao.deleteById(id);
    }
}
