package com.revature.security.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.revature.security.annotations.config.EnableSpringCloudJwtClientImportSelector;

@Retention(RUNTIME)
@Target(TYPE)
@Inherited
@Import(EnableSpringCloudJwtClientImportSelector.class)
public @interface EnableSpringCloudJwtClient {

	boolean autoRegister() default true;
}
