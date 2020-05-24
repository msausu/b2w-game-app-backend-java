
package com.b2w.game.planet.model;

import com.b2w.game.external.swapi.client.PlanetFilmCountSvc;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author msa
 */
@XmlRootElement
@XmlSeeAlso(Planet.class)
public class PlanetWFilm extends Planet {
    
    @Min(PlanetFilmCountSvc.UNKNOWN)
    @Max(15) // ?
    int numFilm;

    public PlanetWFilm() {
    }

    public PlanetWFilm(Planet planet, int numFilm) {
        super(planet.id, planet.nome, planet.clima, planet.terreno);
        this.numFilm = numFilm;
    }
    
    public PlanetWFilm(String nome, String clima, String terreno) {
        super(nome, clima, terreno);
    }
    
    public int getNumFilmes() {
        return numFilm;
    }

    public void setNumFilmes(int numFilm) {
        this.numFilm = numFilm;
    }
    
}
