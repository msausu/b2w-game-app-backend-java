
package com.b2w.game.external.swapi.queue;

import com.b2w.game.external.swapi.model.StarWarsResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author msa
 */
public class ExternalSPITest {

    private final boolean ENABLED = true;
    private final SWAPI sw = new SWAPI();
    private static final long TIMEOUT = 10000L; // nw dependant
    
    @DataProvider(name = "valid-planet")
    public Object[][] validPlanetNames() {
        return new Object[][] { { "Coruscant" }, { "Champala" }, { "Haruun Kal" } };
    }
    
    @DataProvider(name = "invalid-planet")
    public Object[][] invalidPlanetNames() {
        return new Object[][] { { "Koruscant" }, { "Shampala" }, { "Harun Cal" } };
    }
    
    @Test(enabled = ENABLED, timeOut = TIMEOUT / 3)
    public void checkEndpointTest() {
        assert sw.getStarWarsPlanets("X") != null : "endpoint request failed";
    }

    // external: do not test for exact number, count my change     
    @Test(enabled = ENABLED, dataProvider = "valid-planet", dependsOnMethods = "checkEndpointTest", timeOut = TIMEOUT)
    public void externalSPIValidPlanetTest(final String planet) {
        StarWarsResponse r = sw.getStarWarsPlanets(planet);
        assert r.getResults() != null : "request for " + planet + " missing response";
        assert r.getResults().length > 0 : "request for " + planet + " empty response";
        assert r.getResults()[0].getName().equals(planet) : "request match for valid " + planet + " is incorrect";
    }
    
    @Test(enabled = ENABLED, dataProvider = "invalid-planet", dependsOnMethods = "checkEndpointTest", timeOut = TIMEOUT)
    public void externalSPIInvalidPlanetTest(final String planet) {
        assert sw.getStarWarsPlanets(planet).getResults().length == 0 : "request for invalid " + planet + " should have failed";
     }
}