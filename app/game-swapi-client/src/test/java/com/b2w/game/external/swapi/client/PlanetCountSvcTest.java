package com.b2w.game.external.swapi.client;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author msa
 */
public class PlanetCountSvcTest {

    private final PlanetFilmCountSvc swapi = new PlanetFilmCountSvc();
    private static final boolean ENABLED = true, MOCK = true;
    private static final long TIMEOUT = 30000L, R_CACHE = 1000; // hw & nw dependant
    private static final Object[][] DATA = {{"Coruscant"}, {"Champala"}, {"Haruun Kal"}, {"Tatooine"}};

    @BeforeClass
    public void setup() {
        if (!MOCK) {
            return;
        }
        List<String> data = new ArrayList<>();
        for (Object[] arr : DATA) {
            data.add((String) arr[0]);
        }
        swapi.warmCache(data);
    }

    @DataProvider(name = "planet")
    public Object[][] validPlanetNames() {
        return DATA;
    }

    @Test(enabled = ENABLED, dataProvider = "planet", timeOut = TIMEOUT)
    public void svcTest(String planet) {
        assert swapi.getFilmCount(planet).isPresent() : "failed to fetch film count for " + planet;
    }

    @Test(enabled = ENABLED, dataProvider = "planet", timeOut = TIMEOUT / R_CACHE, dependsOnMethods = "svcTest", threadPoolSize = 3, invocationCount = 100)
    public void svcCacheTest(String planet) {
        assert swapi.getFilmCount(planet).isPresent() : "failed to fetch cached film count for " + planet;
    }
}
