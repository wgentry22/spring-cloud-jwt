package com.revature.security.annotations.config;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.revature.security.annotations.EnableSpringCloudJwtServer;

public class SpringCloudJwtCondition implements ConfigurationCondition {
	
	/**
	 * @author William Gentry
	 */

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return context.getBeanFactory().getBeanNamesForAnnotation(EnableSpringCloudJwtServer.class).length > 0;
	}

	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.REGISTER_BEAN;
	}

}
