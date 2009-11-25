package org.imirsel.nema.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.imirsel.nema.renderers.DoubleRenderer;
import org.imirsel.nema.renderers.Renderer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DoubleDataType {
	
	double min();
	double max();
	double[] valueList() default {};
	Class<? extends Renderer> renderer() default DoubleRenderer.class;

}
