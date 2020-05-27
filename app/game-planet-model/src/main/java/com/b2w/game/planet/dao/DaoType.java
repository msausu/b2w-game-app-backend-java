
package com.b2w.game.planet.dao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import javax.inject.Qualifier;

/**
 *
 * @author msa
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface DaoType {
    public Dao.Type value() default Dao.Type.PLANET;   
}
