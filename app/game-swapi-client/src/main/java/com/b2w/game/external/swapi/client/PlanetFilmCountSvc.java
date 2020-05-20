package com.b2w.game.external.swapi.client;

import com.b2w.game.external.swapi.queue.CacheEntry;
import com.b2w.game.external.swapi.queue.ReqQueue;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author msa
 */
@ApplicationScoped
public class PlanetFilmCountSvc {

    // External access may be expensive: cache known planets
    private final ConcurrentMap<String, CacheEntry> planetCount = new ConcurrentHashMap<>();
    private final ReqQueue req = new ReqQueue(planetCount);
    private final AtomicBoolean isActive = new AtomicBoolean(true);

    public void warmCache(List<String> planets) {
        planets.stream().parallel().forEach(p -> req.refresh(p));
    }

    /**
    * @return empty == "not in cache", -1 == "not a Star Wars Planet", 0 <= "Star Wars Film count"
    */
    public Optional<Integer> getFilmCount(String planet) {
        if (!isActive.get()) {
            return Optional.empty();
        }
        if ((planetCount.containsKey(planet) && planetCount.get(planet).isStale()) || !planetCount.containsKey(planet)) {
            req.refresh(planet);
        }
        return planetCount.containsKey(planet) ? Optional.of(planetCount.get(planet).filmCount) : Optional.empty();
    }

    public void activate() {
        isActive.set(true);
    }

    public void deactivate() {
        isActive.set(false);
    }

    @PreDestroy
    public void terminate() {
        req.stop();
    }
}
