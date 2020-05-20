package com.b2w.game.external.swapi.queue;

/**
 *
 * @author msa
 */
public class CacheEntry {

    public final int filmCount;
    public final long last;
    public static final int STALE_MILISECONS = 5 * 60 * 1000, MAX_SIZE = 10000;

    public CacheEntry(int count) {
        this.filmCount = count;
        this.last = System.currentTimeMillis();
    }

    public boolean isStale() {
        return System.currentTimeMillis() - last > STALE_MILISECONS;
    }
}
