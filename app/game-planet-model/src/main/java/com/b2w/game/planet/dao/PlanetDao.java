package com.b2w.game.planet.dao;

import com.b2w.game.planet.model.Planet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author msa
 */
@RequestScoped
public class PlanetDao {

    public static final String QRY_TODOS = "TODOS", QRY_BY_ID = "ID", QRY_BY_NOME = "NOME";
    private static final Logger LOG = Logger.getLogger(PlanetDao.class.getName());
    private EntityManagerFactory emf;
    public static final int MAX_RESULTS = 1000; // truncate limit, hw/sec dependant
    
    public enum QryType {
        ID, NOME
    };

    @PostConstruct
    void initPersistence() {
        emf = Persistence.createEntityManagerFactory("SW_PLANETAS_PU");
    }
    
    void exec(final String cmd) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query qry = em.createNativeQuery(cmd, Planet.class);
            LOG.log(Level.INFO, "índice para nome: {0}", qry.getSingleResult().toString());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "exceção ao criar índice para nome: {0}", ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Planet> list() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query qry = em.createNamedQuery(QRY_TODOS);
            return (List<Planet>) qry.setMaxResults(MAX_RESULTS).getResultList();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "exceção ao listar todos planetas: {0}", ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Planet read(QryType type, Object param) {
        EntityManager em = null;
        Query qry = null;
        try {
            em = emf.createEntityManager();
            switch (type) {
                case ID:
                    qry = em.createNamedQuery(QRY_BY_ID);
                    qry.setParameter("id", param);
                    break;
                case NOME:
                    qry = em.createNamedQuery(QRY_BY_NOME);
                    qry.setParameter("nome", param);
                    break;
            }
            return (Planet) qry.getSingleResult();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "exceção ao obter planeta: {0}", ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public boolean create(@NotNull Object object) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            em.persist(object);
            t.commit();
            return true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "exceção ao criar planeta {0}", ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return false;
    }

    public boolean remove(@NotNull Object id) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            EntityTransaction t = em.getTransaction();
            t.begin();
            Object obj = em.getReference(Planet.class, id);
            em.remove(obj);
            t.commit();
            return true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "exceção ao excluir planeta {0}", ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return false;
    }
}
