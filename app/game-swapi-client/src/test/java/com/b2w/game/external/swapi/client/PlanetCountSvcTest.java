package com.b2w.game.external.swapi.client;

import com.b2w.game.external.swapi.queue.ReqQueue;
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
    private static final boolean ENABLED = true;
    private static final String X_PLANET = "X_" + Math.abs(new java.util.Random().nextInt());
    private static final Object[][] DATA = {{"Coruscant"}, {"Champala"}, {"Haruun Kal"}, {"Tatooine"}, {X_PLANET}};
    private static final long DATA_SIZE = 5; // DATA[0].length;
    private static final long TIMEOUT = Math.round(ReqQueue.FREQ * DATA_SIZE * 1.2); // must tune nw dep 

    @BeforeClass
    public void setup() {
        Logger.getAnonymousLogger().log(Level.INFO, "tests will require {0} sec", TIMEOUT / 1000);
        List<String> data = new ArrayList<>();
        for (Object[] arr : DATA) {
            data.add((String) arr[0]);
        }
        swapi.warmCache(data);
        try {
            Thread.sleep(TIMEOUT);          // wait!
        } catch (InterruptedException ex) { // ok to ignore
        }
    }

    @DataProvider(name = "planet")
    public Object[][] validPlanetNames() {
        return DATA;
    }

    @Test(enabled = ENABLED, dataProvider = "planet", threadPoolSize = 3, timeOut = 1000L)
    public void svcCacheTest(String planet) {
        assert swapi.getFilmCount(planet).isPresent() : "failed to fetch cached film count for " + planet;
    }
    
    @Test(enabled = ENABLED, dependsOnMethods = "svcCacheTest")
    public void svcCacheValueTest() {
        assert swapi.getFilmCount(X_PLANET).isPresent() 
                && swapi.getFilmCount(X_PLANET).get() == -1 : "failed to get exact value for non existing " + X_PLANET;
    }
}
