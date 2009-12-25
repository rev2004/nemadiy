package org.imirsel.nema.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.imirsel.nema.role.*;

import org.imirsel.nema.renderers.Renderer;
import org.imirsel.nema.renderers.StringRenderer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface StringDataType {
	String[] valueList() default {};
	String[] labelList() default {};
	Class<? extends Renderer> renderer() default StringRenderer.class;
	Class<? extends RolePrincipal> editRole() default RoleUser.class;
	boolean hide() default false;
}
