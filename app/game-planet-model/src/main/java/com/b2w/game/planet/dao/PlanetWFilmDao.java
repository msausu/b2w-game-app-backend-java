package com.b2w.game.planet.dao;

import com.b2w.game.external.swapi.client.PlanetFilmCountSvc;
import com.b2w.game.planet.model.Planet;
import com.b2w.game.planet.model.PlanetWFilm;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import static com.b2w.game.external.swapi.client.PlanetFilmCountSvc.UNKNOWN;

/**
 *
 * @author msa
 */
public class PlanetWFilmDao extends PlanetDao {

    @Inject
    @NotNull
    private PlanetFilmCountSvc svc;

    @PostConstruct
    void setup() {
        super.initPersistence();
        svc.warmCache(list().stream().map(p -> p.getNome()).collect(Collectors.toList()));
    }

    public List<PlanetWFilm> listw() {
        return super.list()
                .parallelStream()
                .map(p -> new PlanetWFilm(p, svc.getFilmCount(p.getNome()).orElse(UNKNOWN)))
                .collect(Collectors.toList());
    }

    public PlanetWFilm read(@NotNull QryType type, @NotNull String name) {
        Planet p = super.read(type, name);
        return p == null ? null : new PlanetWFilm(p, svc.getFilmCount(name).orElse(UNKNOWN));
    }
}
