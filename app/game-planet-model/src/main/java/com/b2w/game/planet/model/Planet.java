package com.b2w.game.planet.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import static com.b2w.game.planet.dao.PlanetDao.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

/**
 * there should be an index for name
 * uniqueness must be explicit: set though an index using a db command
 * db.planetas.createIndex({nome: 1}, {unique: true})
 * 
 * @author  msa
 */
@Entity
@NoSql(dataType = "planetas", dataFormat = DataFormatType.MAPPED)
@NamedQueries({  
    @NamedQuery(name = QRY_BY_NOME, query = "SELECT p FROM Planet p WHERE p.nome = :nome"),
    @NamedQuery(name = QRY_BY_ID, query = "SELECT p FROM Planet p WHERE p.id = :id"),
    @NamedQuery(name = QRY_TODOS, query = "SELECT p FROM Planet p")
})
@XmlRootElement
public class Planet implements Serializable {

    public static final String ID_PAT = "\\w+", 
            NM_PAT = "\\w+( \\w+)*", 
            ST_PAT = "\\w+(( |, ?)\\w)*";
    
    @Id
    @GeneratedValue
    @Field(name = "_id") // needed for mongo, nosql uses Field
    String id;
    
    @Field(name = "nome")
    @Column(unique = true) // doesn't enforce index on nosql (Field should have this attr)
    @Size(min = 1, max = 60)
    @Pattern(regexp = NM_PAT)
    String nome;
    
    @Field(name = "clima")
    @Size(min = 3, max = 20)
    @Pattern(regexp = ST_PAT)
    String clima;
    
    @Field(name = "terreno")
    @Size(min = 4, max = 120)
    @Pattern(regexp = ST_PAT)
    String terreno;
    
    public Planet() {
    }
    
    public Planet(String nome, String clima, String terreno) {
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }
    
    public Planet(String id, String nome, String clima, String terreno) {
        this(nome, clima, terreno);
        this.id = id;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nome);
        hash = 41 * hash + Objects.hashCode(this.clima);
        hash = 41 * hash + Objects.hashCode(this.terreno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Planet other = (Planet) obj;
        return Objects.equals(this.terreno, other.terreno) 
                && Objects.equals(this.clima, other.clima)
                && Objects.equals(this.nome, other.nome);
    }

    // debug: json 
    @Override
    public String toString() {
        return "{" + kv("id", id) + kv("nome", nome) + kv("clima", clima) + kv_("terreno:", terreno) + '}';
    }
    
    private String kv_(String k, String v) {
        return "\"" + k + "\":\"" + v + "\"";
    }
    
    private String kv(String k, String v) {
        return kv_(k, v) + ",";
    }
}
