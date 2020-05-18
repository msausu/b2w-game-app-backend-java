package com.b2w.game.planet.dao;

import com.b2w.game.planet.model.Planet;
import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author msa
 */
public class PlanetDaoLoadIT {

    private PlanetDao dao;
    
    private Planet planet(int i) {
        return new Planet("" + i, "xx" + i, "xxx" + i);
    }
    
    @BeforeClass
    void setup() {
        dao = new PlanetDao();
        dao.initPersistence();
    }
    
    @Test(enabled = true, singleThreaded = true, timeOut = (PlanetDao.MAX_RESULTS * 50L)) // hw dependant
    void loadWriteTest() {
        IntStream.rangeClosed(1, PlanetDao.MAX_RESULTS).parallel().forEach(p -> dao.create(planet(p)));
    }
    
    @Test(enabled = true, dependsOnMethods = {"loadWriteTest"}, timeOut = (PlanetDao.MAX_RESULTS * 20L))
    void loadReadTest() {
        List<Planet> list = dao.list();
        assert list.size() == PlanetDao.MAX_RESULTS : "load test failed size " + list.size() + " != " + PlanetDao.MAX_RESULTS;
    }

}
