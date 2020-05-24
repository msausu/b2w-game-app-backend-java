package com.b2w.game.external.swapi.client;

import com.b2w.game.external.swapi.queue.RequestQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test is SLOW subject to ReqQueue.FREQ and sample size!
 *
 * @author msa
 */
public class PlanetCountSvcTest {

    private final PlanetFilmCountSvc swapi = new PlanetFilmCountSvc();
    private static final String X_PLANET = "X_" + Math.abs(new java.util.Random().nextInt());
    private static final Object[][] DATA = {{"Coruscant"}, {"Champala"}, {"Haruun Kal"}, {"Tatooine"}, {X_PLANET}};
    private static final long TIMEOUT = Math.round(1.2 * RequestQueue.FREQ * DATA.length); // nw dep
    private static final boolean ENABLED = true;

    @BeforeClass
    public void setup() {
        Logger.getAnonymousLogger().log(
                Level.INFO, 
                "{0} is slow due to cache warmup: {1} tests will require {2} sec", 
                new Object[] { PlanetCountSvcTest.class.getName(), DATA.length + 1, (int)(TIMEOUT / 1000) }
        );
        List<String> data = new ArrayList<>();
        for (Object[] arr : DATA) {
            data.add((String) arr[0]);
        }
        swapi.warmCache(data);
        try {
            Thread.sleep(TIMEOUT);          // wait!
        } catch (InterruptedException ex) { // ignore
        }
    }

    @DataProvider(name = "planet")
    public Object[][] validPlanetNames() {
        return DATA;
    }

    @Test(enabled = ENABLED, dataProvider = "planet", threadPoolSize = 3, timeOut = 1_000L)
    public void svcCacheTest(String planet) {
        assert swapi.getFilmCount(planet).isPresent() : "failed to fetch cached film count for " + planet;
    }
    
    @Test(enabled = ENABLED, dependsOnMethods = "svcCacheTest")
    public void svcCacheValueTest() {
        assert swapi.getFilmCount(X_PLANET).isPresent() 
                && swapi.getFilmCount(X_PLANET).get() == PlanetFilmCountSvc.INEXISTENT : "failed to get exact value for non existing " + X_PLANET;
    }
}
