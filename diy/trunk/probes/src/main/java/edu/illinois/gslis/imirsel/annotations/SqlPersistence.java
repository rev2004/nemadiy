package edu.illinois.gslis.imirsel.annotations;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlPersistence {
	String create();
	String store() default "[unassigned]";
	String queryById() default "[unassigned]";
	String queryByName() default "[unassigned]"; 
}
