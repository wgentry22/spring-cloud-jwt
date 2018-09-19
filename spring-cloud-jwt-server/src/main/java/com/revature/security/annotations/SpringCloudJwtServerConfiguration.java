package com.revature.security.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.revature.security.annotations.config.SpringCloudJwtCondition;
import com.revature.security.annotations.config.SpringCloudJwtServerConfigImportSelector;

/**
 * @author William Gentry
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Inherited
@Conditional(SpringCloudJwtCondition.class)
@Import(SpringCloudJwtServerConfigImportSelector.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface SpringCloudJwtServerConfiguration {
	
	boolean autoScan() default true;
}
