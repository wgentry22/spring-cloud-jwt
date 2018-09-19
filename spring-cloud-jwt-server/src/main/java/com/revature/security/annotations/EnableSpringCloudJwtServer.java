package com.revature.security.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.revature.security.annotations.config.EnableSpringCloudJwtImportSelector;

/**
 * @author William Gentry
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
@Import(EnableSpringCloudJwtImportSelector.class)
public @interface EnableSpringCloudJwtServer {

	boolean autoRegister() default true;
}
