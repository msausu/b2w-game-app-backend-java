package com.b2w.game.planet.dao;

import com.b2w.game.external.swapi.client.PlanetFilmCountSvc;
import com.b2w.game.planet.model.Planet;
import com.b2w.game.planet.model.PlanetWFilm;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 *
 * @author msa
 */
@RequestScoped
public class PlanetWFilmDao extends PlanetDao {

    @Inject
    @NotNull
    private PlanetFilmCountSvc svc;

    @PostConstruct
    void setup() {
        initPersistence();
        svc.warmCache(list().stream().map(p -> p.getNome()).collect(Collectors.toList()));
    }

    public List<PlanetWFilm> listw() {
        return list()
                .parallelStream()
                .map(p -> new PlanetWFilm(p, svc.getFilmCount(p.getNome()).orElse(0)))
                .collect(Collectors.toList());
    }

    public PlanetWFilm readw(String name) {
        Planet p = read(QryType.NOME, name);
        return p == null ? null : new PlanetWFilm(p, svc.getFilmCount(name).orElse(0));
    }

    public PlanetWFilm idw(String name) {
        Planet p = read(QryType.NOME, name);
        return p == null ? null : new PlanetWFilm(p, svc.getFilmCount(name).orElse(0));
    }
}
