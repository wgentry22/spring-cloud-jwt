package com.revature.security.annotations.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.commons.util.SpringFactoryImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.type.AnnotationMetadata;

import com.revature.security.annotations.EnableSpringCloudJwtClient;

public class EnableSpringCloudJwtClientImportSelector extends SpringFactoryImportSelector<EnableSpringCloudJwtClient> {

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		String[] imports = super.selectImports(metadata);
		
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(getAnnotationClass().getName(), true));
		
		boolean autoRegister = attributes.getBoolean("autoRegister");
		
		if (autoRegister) {
			List<String> importList = new ArrayList<>();
			importList.add("com.revature.security.annotations.config.SpringCloudJwtRegistrationProperties.Client");
			importList.add("com.revature.security.annotations.config.SpringCloudJwtRegistrationProperties.Server");
			importList.add("com.revature.security.annotations.config.SpringCloudJwtRegistrationConfiguration");
			importList.add("com.revature.security.JwtWebSecurityConfig");
			imports = importList.toArray(new String[0]);
		} else {
			Environment env = getEnvironment();
			if(ConfigurableEnvironment.class.isInstance(env)) {
				ConfigurableEnvironment configEnv = (ConfigurableEnvironment)env;
				LinkedHashMap<String, Object> map = new LinkedHashMap<>();
				map.put("spring.cloud.jwt.client.enabled", false);
				MapPropertySource propertySource = new MapPropertySource(
						"configClient", map);
				configEnv.getPropertySources().addLast(propertySource);
			}
		}
		
		return imports;
	}
	
	
	@Override
	protected boolean isEnabled() {
		return new RelaxedPropertyResolver(getEnvironment()).getProperty("spring.cloud.jwt.client.enabled", Boolean.class, Boolean.TRUE).booleanValue();
	}
	
	@Override
	protected boolean hasDefaultFactory() {
		return true;
	}

}
