
package com.b2w.game.planet.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author msa
 */
@XmlRootElement
@XmlSeeAlso(Planet.class)
public class PlanetWFilm extends Planet {
    
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
