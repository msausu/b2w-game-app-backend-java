package com.b2w.game.external.swapi.client;

/**
 *
 * @author msa
 */
public class CacheEntry {

    public final int filmCount;
    public final long last;
    public final int STALE_MILISECONS;
    public static final int MAX_SIZE = 10000, D_EXISTING = 5 * 60 * 1000, D_INEXISTING = 12 * D_EXISTING;

    public CacheEntry(int count) {
        this(count, D_EXISTING);
    }
    
    public CacheEntry(int count, int delay) {
        this.filmCount = count;
        this.STALE_MILISECONS = delay;
        this.last = System.currentTimeMillis();
    }

    public boolean isStale() {
        return System.currentTimeMillis() - last > STALE_MILISECONS;
    }
}
