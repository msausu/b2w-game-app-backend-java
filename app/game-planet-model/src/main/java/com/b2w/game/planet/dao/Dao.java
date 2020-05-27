
package com.b2w.game.planet.dao;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author msa
 * @param <T>
 */
public interface Dao<T> {
    
    enum QryType {
        ID, NOME
    };
    
    enum Type {
        PLANET, PLANETWFILM;
    }
    
    List<? extends T> list();
    
    <V extends T> V  read(@NotNull QryType type, @NotNull Object object); 
    
    boolean create(@NotNull Object obj);
    
    boolean remove(@NotNull Object id);
}
