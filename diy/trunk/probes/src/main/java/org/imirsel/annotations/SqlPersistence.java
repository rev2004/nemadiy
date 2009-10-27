package org.imirsel.annotations;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlPersistence {
	String create();
	String store() default "[unassigned]";
	String start() default "[unassigned]";
	String queryById() default "[unassigned]";
	String queryByName() default "[unassigned]";
	String select() default "[unassigned]"; 
	String finish() default "[unassigned]";
	String updateHostAndPort() default "[unassigned]";
}
