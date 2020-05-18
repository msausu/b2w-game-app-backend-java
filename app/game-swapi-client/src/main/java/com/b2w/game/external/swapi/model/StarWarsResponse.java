package com.b2w.game.external.swapi.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author msa
 */
@XmlRootElement
public class StarWarsResponse {

    private Integer count;
    private String next, previous;
    private StarWarsPlanet[] results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public StarWarsPlanet[] getResults() {
        return results;
    }

    public void setResults(StarWarsPlanet[] results) {
        this.results = results;
    }

}
