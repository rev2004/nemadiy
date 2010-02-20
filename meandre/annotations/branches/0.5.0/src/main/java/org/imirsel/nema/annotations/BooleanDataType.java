package org.imirsel.nema.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.imirsel.nema.role.*;

import org.imirsel.nema.renderers.BooleanRenderer;
import org.imirsel.nema.renderers.Renderer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface BooleanDataType{
	Class<? extends Renderer> renderer() default BooleanRenderer.class;
	Class<? extends RolePrincipal> editRole() default RoleUser.class;
	boolean hide() default false;
}
