
package com.b2w.game.external.swapi.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author msa
 */
@XmlRootElement
public class StarWarsPlanet {
    
    String name; // string -- The name of this planet.
    String diameter; // string -- The diameter of this planet in kilometers.
    String rotation_period; // string -- The number of standard hours it takes for this planet to complete a single rotation on its axis.
    String orbital_period; // string -- The number of standard days it takes for this planet to complete a single orbit of its local star.
    String gravity; // string -- A number denoting the gravity of this planet, where "1" is normal or 1 standard G. "2" is twice or 2 standard Gs. "0.5" is half or 0.5 standard Gs.
    String population; // string -- The average population of sentient beings inhabiting this planet.
    String climate; // string -- The climate of this planet. Comma separated if diverse.
    String terrain; // string -- The terrain of this planet. Comma separated if diverse.
    String surface_water; // string -- The percentage of the planet surface that is naturally occurring water or bodies of water.
    String[] residents; // array -- An array of People URL Resources that live on this planet.
    String[] films; // array -- An array of Film URL Resources that this planet has appeared in.
    String url; // string -- the hypermedia URL of this resource.
    String created; // string -- the ISO 8601 date format of the time that this resource was created.
    String edited; // string -- the ISO 8601 date format of the time that this resource was edited.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getRotation_period() {
        return rotation_period;
    }

    public void setRotation_period(String rotation_period) {
        this.rotation_period = rotation_period;
    }

    public String getOrbital_period() {
        return orbital_period;
    }

    public void setOrbital_period(String orbital_period) {
        this.orbital_period = orbital_period;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getSurface_water() {
        return surface_water;
    }

    public void setSurface_water(String surface_water) {
        this.surface_water = surface_water;
    }

    public String[] getResidents() {
        return residents;
    }

    public void setResidents(String[] residents) {
        this.residents = residents;
    }

    public String[] getFilms() {
        return films;
    }

    public void setFilms(String[] films) {
        this.films = films;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "{" + "name:" + name + ", diameter:" + diameter + ", rotation_period:" + rotation_period + ", orbital_period:" + orbital_period + ", gravity:" + gravity + ", population:" + population + ", climate:" + climate + ", terrain:" + terrain + ", surface_water:" + surface_water + ", residents:" + residents + ", films:" + films + ", url:" + url + ", created:" + created + ", edited:" + edited + '}';
    }

}
