package com.b2w.game.planet.dao;

import static com.b2w.game.planet.dao.Dao.QryType.ID;
import static com.b2w.game.planet.dao.Dao.QryType.NOME;
import com.b2w.game.planet.model.Planet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author msa
 */
public class PlanetDaoIT {

    private Dao<Planet> dao;
    private final List<Planet> planets = new ArrayList<>();
    private final Random rnd = new Random();
    private static final String NAME = "n_";
    private static final int SAMPLE_SIZE = PlanetDao.MAX_RESULTS / 10, POOL = 2, RUNS = SAMPLE_SIZE < 10 ? SAMPLE_SIZE : SAMPLE_SIZE / 10;

    private Planet planet(int i) {
        return new Planet(NAME + i, "c__" + i, "t___" + i);
    }

    @BeforeClass
    void setup() {
        dao = new PlanetDao();
        ((PlanetDao)dao).initPersistence();
        IntStream.rangeClosed(0, SAMPLE_SIZE).forEach(p -> planets.add(p, planet(p)));
    }

    @Test(singleThreaded = true, invocationCount = 1)
    void createTest() {
        boolean res = planets.parallelStream().map(p -> dao.create(p)).reduce(Boolean::logicalAnd).orElse(Boolean.FALSE);
        assert res : "create tst nok";
    }

    // without index will fail! log must show exceptions, slow because of exceptions & logging
    @Test(enabled = true, dependsOnMethods = {"createTest"}, threadPoolSize = POOL, invocationCount = RUNS)
    void recreateTest() {
        int p = rnd.nextInt(SAMPLE_SIZE);
        assert !dao.create(planet(p)) : "recreate failed at " + p;
    }

    @Test(dependsOnMethods = {"createTest"})
    void readAllTest() {
        assert dao.list().containsAll(planets) : "read tst todos failed";
    }

    @Test(dependsOnMethods = {"readAllTest"}, threadPoolSize = POOL, invocationCount = RUNS)
    void readByNameTest() {
        int i = rnd.nextInt(SAMPLE_SIZE);
        assert dao.read(NOME, NAME + i).equals(planets.get(i)) : "read tst 'byNome' failed";
    }

    @Test(dependsOnMethods = {"readAllTest"}, threadPoolSize = POOL, invocationCount = RUNS)
    void readByIdTest() {
        int i = rnd.nextInt(SAMPLE_SIZE);
        Planet p = dao.read(NOME, NAME + i);
        assert dao.read(ID, p.getId()).equals(planets.get(i)) : "read tst 'byId' failed";
    }

    @Test(dependsOnMethods = {"readByIdTest", "readByNameTest"})
    void removeTest() {
        boolean res = planets
                .stream()
                .map(p -> dao.<Planet>read(NOME, p.getNome()))
                .map(p -> dao.remove(p.getId()))
                .reduce(Boolean::logicalAnd)
                .orElse(Boolean.FALSE);
        assert res : "remove tst nok";
    }
}
