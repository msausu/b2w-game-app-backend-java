package com.b2w.game.external.swapi.queue;

import com.b2w.game.external.swapi.client.PlanetFilmCountSvc;
import com.b2w.game.external.swapi.model.StarWarsResponse;
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
public class ReqQueue {

    private static class Req extends TimerTask {

        private final SWAPI spi = new SWAPI();
        private final ConcurrentMap<String, CacheEntry> planetCount;
        private final LinkedBlockingQueue<String> queue;

        Req(LinkedBlockingQueue<String> rq, ConcurrentMap<String, CacheEntry> planetCount) {
            this.queue = rq;
            this.planetCount = planetCount;
        }

        @Override
        public void run() {
            if (queue.isEmpty()) {
                return;
            }
            try {
                String planet = queue.take();
                StarWarsResponse res = spi.getStarWarsPlanets(planet);
                int count = -1; // nonexisting convention
                if (res != null && res.getResults() != null) {
                    if (res.getResults().length > 0) {
                        if (planetCount.size() < CacheEntry.MAX_SIZE) {
                            count = res.getResults()[0].getFilms().length;
                        } else {
                            throw new IllegalStateException("swapi cache full");
                        }
                    }
                }
                planetCount.put(planet, new CacheEntry(count));
            } catch (Exception e) {
                Logger.getLogger(PlanetFilmCountSvc.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    // no need to be fast, must comply with FREQ
    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(MAX_SIZE);
    private final Timer timer = new Timer(true);
    public static final int MAX_SWAPI_REQ_DAY = 10000, MAX_SIZE = MAX_SWAPI_REQ_DAY, SECS_DAY = 24 * 60 * 60,
            FREQ = (SECS_DAY / MAX_SWAPI_REQ_DAY) * 1000;

    public ReqQueue(ConcurrentMap<String, CacheEntry> map) {
        timer.scheduleAtFixedRate(new Req(queue, map), 0, FREQ);
    }

    public void refresh(final String planet) {
        if (!queue.contains(planet) && queue.size() < MAX_SIZE) {
            try {
                queue.put(planet);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReqQueue.class.getName()).log(Level.SEVERE, null, ex);
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
