package com.b2w.game.external.swapi.queue;

import com.b2w.game.external.swapi.model.StarWarsResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author msa
 */
public class ExternalSPITest {

    private static final boolean ENABLED = true;
    private static final long EXTERNAL_TMO = 10_000L; // nw dependant
    private static final Object[][] DATA_VALID = {{"Coruscant"}, {"Champala"}, {"Haruun Kal"}};
    private static final Object[][] DATA_INVALID = {{"Koruscant"}, {"Shampala"}, {"Harun Cal"}};
    private static final int VALID_DATA_SIZE = DATA_VALID.length;
    private final SWAPIClient sw = new SWAPIClient();

    @BeforeClass
    public void setup() {
        Logger.getAnonymousLogger().log(
                Level.INFO,
                "{0}: {1} external tests may take up to {1} sec",
                new Object[]{ ExternalSPITest.class.getName(), 2 * VALID_DATA_SIZE + 1, (int)(3 * EXTERNAL_TMO / 1000) }
        );
    }

    @DataProvider(name = "valid-planet")
    public Object[][] validPlanetNames() {
        return DATA_VALID;
    }

    @DataProvider(name = "invalid-planet")
    public Object[][] invalidPlanetNames() {
        return DATA_INVALID;
    }

    @Test(enabled = ENABLED, timeOut = EXTERNAL_TMO / 3)
    public void checkEndpointTest() {
        assert sw.getStarWarsPlanets("X") != null : "endpoint request failed";
    }

    // external: do not test for exact number, count my change     
    @Test(enabled = ENABLED, dataProvider = "valid-planet", dependsOnMethods = "checkEndpointTest", timeOut = EXTERNAL_TMO)
    public void externalSPIValidPlanetTest(final String planet) {
        StarWarsResponse r = sw.getStarWarsPlanets(planet);
        assert r.getResults() != null : "request for " + planet + " missing response";
        assert r.getResults().length > 0 : "request for " + planet + " empty response";
        assert r.getResults()[0].getName().equals(planet) : "request match for valid " + planet + " is incorrect";
    }

    @Test(enabled = ENABLED, dataProvider = "invalid-planet", dependsOnMethods = "checkEndpointTest", timeOut = EXTERNAL_TMO)
    public void externalSPIInvalidPlanetTest(final String planet) {
        assert sw.getStarWarsPlanets(planet).getResults().length == 0 : "request for invalid " + planet + " should have failed";
    }
}
