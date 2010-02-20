package org.imirsel.nema.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.imirsel.nema.role.*;

import org.imirsel.nema.renderers.IntegerRenderer;
import org.imirsel.nema.renderers.Renderer;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IntegerDataType {
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
	int[] valueList() default {};
	String[] labelList() default {};
	Class<? extends Renderer> renderer() default IntegerRenderer.class;
	Class<? extends RolePrincipal> editRole() default RoleUser.class;
	boolean hide() default false;
}
