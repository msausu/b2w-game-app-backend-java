package com.b2w.game.external.swapi.queue;

import com.b2w.game.external.swapi.client.CacheEntry;
import com.b2w.game.external.swapi.client.PlanetFilmCountSvc;
import static com.b2w.game.external.swapi.client.PlanetFilmCountSvc.*;
import com.b2w.game.external.swapi.model.StarWarsResponse;
import static com.b2w.game.external.swapi.queue.SWAPIClient.SWAPI_MAX_REQ_DAY;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author msa
 */
public class RequestQueue {

    private static class Request extends TimerTask {

        private final SWAPIClient spi = new SWAPIClient();
        private final ConcurrentMap<String, CacheEntry> planetCount;
        private final LinkedBlockingQueue<String> queue;

        Request(LinkedBlockingQueue<String> queue, ConcurrentMap<String, CacheEntry> planetCount) {
            this.queue = queue;
            this.planetCount = planetCount;
        }

        @Override
        public void run() {
            if (queue.isEmpty()) {
                return;
            }
            int count = INEXISTENT;
            String planet = null;
            try {
                planet = queue.take();
                // behavior could be improved by reducing the frequency
                // of calls to inexistent planets
                StarWarsResponse response = spi.getStarWarsPlanets(planet);
                if (response != null && response.getResults() != null) {
                    if (response.getResults().length > 0) {
                        if (planetCount.size() < CacheEntry.MAX_SIZE) {
                            count = response.getResults()[0].getFilms().length;
                        } else {
                            throw new IllegalStateException("swapi cache full");
                        }
                    }
                }
            } catch (Throwable e) {
                count = UNKNOWN;
                Logger.getLogger(PlanetFilmCountSvc.class.getName()).log(Level.SEVERE, null, e);
            }
            if (!planetCount.containsKey(planet) || (planetCount.get(planet).filmCount >= 0 && count >= 0)) {
                planetCount.put(planet, new CacheEntry(count));
            }
        }
    }

    public static final int MAX_SIZE = SWAPI_MAX_REQ_DAY, SECS_DAY = 24 * 60 * 60, FREQ = (SECS_DAY / SWAPI_MAX_REQ_DAY) * 1_000;
    // no need to be fast, must comply with FREQ
    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(MAX_SIZE);
    private final Timer timer = new Timer(true);

    public RequestQueue(ConcurrentMap<String, CacheEntry> map) {
        timer.scheduleAtFixedRate(new Request(queue, map), 0, FREQ);
    }

    public void refresh(final String planet) {
        if (!queue.contains(planet) && queue.size() < MAX_SIZE) {
            try {
                queue.put(planet);
            } catch (InterruptedException e) { //ok
            }
        }
    }

    public int size() {
        return queue.size();
    }

    public void stop() {
        timer.cancel();
    }
}
