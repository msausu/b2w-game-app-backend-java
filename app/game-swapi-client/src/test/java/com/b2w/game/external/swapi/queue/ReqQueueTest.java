
package com.b2w.game.external.swapi.queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
public class ReqQueueTest {

    private final ConcurrentMap cache = new ConcurrentHashMap<String, CacheEntry>();
    private final ReqQueue queue = new ReqQueue(cache);
    private static final boolean ENABLED = true;
    private static final Object[][] VALID_DATA = new Object[][] { { "Coruscant" }, { "Champala" }, { "Haruun Kal" } };
    private static final int VALID_DATA_SIZE = 3; // VALID_DATA[0].length; 
    private static final long EXTERNAL_TMO = ReqQueue.FREQ * VALID_DATA_SIZE + 3000, INTERNAL_TMO = 30L * VALID_DATA_SIZE + 30; // hw/nw dependant
    
    @BeforeClass
    public void setup() {
        Logger.getAnonymousLogger().log(Level.INFO, "tests will require at least {0} sec", EXTERNAL_TMO);
    }
    
    @DataProvider(name = "valid-planet")
    public Object[][] validPlanetNames() {
        return VALID_DATA;
    }
    
    @Test(enabled = ENABLED, threadPoolSize = 3, dataProvider = "valid-planet", timeOut = INTERNAL_TMO)
    public void reqMissingTest(final String planet) {
        queue.refresh(planet);
        assert !cache.containsKey(planet) : "cold cache should not have planet " + planet;
    }

    // external: do not test for exact number, count my change     
    @Test(enabled = ENABLED, dependsOnMethods = "reqMissingTest", timeOut = INTERNAL_TMO)
    public void reqCountTest() {
        assert queue.size() > 0 : "queue should not be empty";
    }
    
    @Test(enabled = ENABLED, dependsOnMethods = "reqCountTest", timeOut = EXTERNAL_TMO)
    public void dummyDelay() {
        try {
            Thread.sleep(EXTERNAL_TMO - 1000);
        } catch (InterruptedException e) {
            Logger.getLogger(ReqQueueTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @Test(enabled = ENABLED, dataProvider = "valid-planet", dependsOnMethods = "dummyDelay", timeOut = EXTERNAL_TMO)
    public void reqPresentTest(final String planet) {
        assert cache.containsKey(planet) : "cache presence for requested " + planet + " failed";
     }
}