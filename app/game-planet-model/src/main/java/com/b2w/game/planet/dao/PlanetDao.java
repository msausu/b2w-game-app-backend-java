
package com.b2w.game.planet.dao;

import com.b2w.game.planet.model.Planet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

/**
 *
 * @author msa
 */
@DaoType
public class PlanetDao implements Dao<Planet> {

    public static final int MAX_RESULTS = 1000, MAX_PLANETS = 5000; // truncate limit, hw/sec dependant
    public static final String QRY_TODOS = "TODOS", QRY_BY_ID = "ID", QRY_BY_NOME = "NOME";
    private EntityManagerFactory emf;

    @PostConstruct
    public void initPersistence() {
        emf = Persistence.createEntityManagerFactory("SW_PLANETAS_PU");
    }

    @Override
    public List<? extends Planet> list() {
        try (DBCmd<Planet> cmd = new DBCmd(emf, "listing planets")) {
            cmd.exec(() -> {
                cmd.setResults(cmd.getManager().createNamedQuery(QRY_TODOS).setMaxResults(MAX_RESULTS).getResultList());
            });
            return cmd.getResults();
        }
    }

    @Override
    public Planet read(@NotNull QryType type, @NotNull Object param) {
        try (DBCmd<Planet> cmd = new DBCmd(emf, "reading planet")) {
            cmd.exec(() -> {
                EntityManager em = cmd.getManager();
                Query qry = null;
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
                cmd.setResult((Planet) qry.getSingleResult());
            });
            return cmd.getResult();
        }
    }

    @Override
    public boolean create(@NotNull Object object) {
        try (DBCmd cmd = new DBCmd(emf, "creating planet")) {
            return cmd.exec(() -> {
                cmd.getManager().persist(object);
            });
        }
    }

    @Override
    public boolean remove(@NotNull Object id) {
        try (DBCmd cmd = new DBCmd(emf, "removing planet")) {
            return cmd.exec(() -> {
                EntityManager em = cmd.getManager();
                em.remove(em.getReference(Planet.class, id));
            });
        }
    }
}
