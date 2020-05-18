package com.b2w.game.external.swapi.client;

import com.b2w.game.external.swapi.model.StarWarsResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;

/**
 * (slow/unreliable) Should not proxy, OK if planet list is not large. "number
 * of planets" should be part of the db and synced elsewhere. Returns zero if
 * cache size is exceeded or swapi fails (with cold cache).
 *
 * @author msa
 */
@ApplicationScoped
public class PlanetFilmCountSvc {

    private static class CountCache {

        public final int filmCount;
        public final long last;
        public static final int STALE_MILISECONS = 5 * 60 * 1000, MAX_SIZE = 10000;

        public CountCache(int count) {
            this.filmCount = count;
            this.last = System.currentTimeMillis();
        }

        public boolean isStale() {
            return System.currentTimeMillis() - last > STALE_MILISECONS;
        }
    }

    // External access may be expensive: cache known planets
    private final ConcurrentMap<String, CountCache> planetCount = new ConcurrentHashMap<>();
    private final ExternalSPI spi = new ExternalSPI();
    private final AtomicBoolean isActive = new AtomicBoolean(true);

    public void warmCache(List<String> planets) {
        // todo: make real (first read upto STALE_MILISECONS is zero)
        planets.stream().parallel().forEach(p -> planetCount.put(p, new CountCache(0)));
    }
    
    public void activate() {
        isActive.set(true);
    }
    
    public void deactivate() {
        isActive.set(false);
    }

    // subject to "cold" cache delay
    public Optional<Integer> getFilmCount(String planet) {
        if (!isActive.get()) {
            return Optional.empty();
        }
        if (planetCount.get(planet).isStale() || !planetCount.containsKey(planet)) {
            refresh(planet);
        }
        return planetCount.containsKey(planet) ? Optional.of(planetCount.get(planet).filmCount) : Optional.empty();
    }

    private void refresh(String planet) {
        // improve: rate-limit, async
        StarWarsResponse res = spi.getStarWarsPlanets(planet);
        if (res != null && res.getResults() != null && res.getResults().length > 0) {
            if (planetCount.size() < CountCache.MAX_SIZE) {
                planetCount.put(planet, new CountCache(res.getResults()[0].getFilms().length));
            } else {
                Logger.getAnonymousLogger().severe("swapi cache full");
            }
        }
    }
}
