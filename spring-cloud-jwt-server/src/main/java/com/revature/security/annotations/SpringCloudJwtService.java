package com.revature.security.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;

import com.revature.security.JwtWebSecurityConfig;

/**
 * @author William Gentry
 */
@Inherited
@ConditionalOnMissingClass(value="springCloudJwtRegistrationProperties")
@ConditionalOnClass(value=JwtWebSecurityConfig.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface SpringCloudJwtService {

}
