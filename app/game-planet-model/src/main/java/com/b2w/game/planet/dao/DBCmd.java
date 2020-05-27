package com.b2w.game.planet.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.validation.constraints.NotNull;

/**
 * helper to decrease boilerplate
 * @author msa
 * @param <T>
 */
public class DBCmd<T> implements AutoCloseable {

    private final EntityManager em;
    private final String errorMessage;
    private T result;
    private List<T> results;

    public DBCmd(EntityManagerFactory emf) {
        this(emf, "");
    }
    
    public DBCmd(EntityManagerFactory emf, String errorMessage) {
        this.em = emf.createEntityManager();
        this.errorMessage = errorMessage;
    }

    public EntityManager getManager() {
        return em;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
    
    public boolean exec(@NotNull Runnable r) {
        try {
            EntityTransaction t = em.getTransaction();
            t.begin();
            r.run();
            t.commit();
            return true;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "exception {0}: {1}", new Object[] { errorMessage, e.getMessage() });
        } 
        return false;
    }

    @Override
    public void close() {
        if (em != null) {
            em.close();
        }
    }
}
