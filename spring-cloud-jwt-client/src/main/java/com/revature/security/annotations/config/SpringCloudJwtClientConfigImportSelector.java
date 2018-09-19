package com.revature.security.annotations.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.commons.util.SpringFactoryImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.type.AnnotationMetadata;

import com.revature.security.annotations.SpringCloudJwtClientConfiguration;
import com.revature.security.annotations.SpringCloudJwtComponent;

public class SpringCloudJwtClientConfigImportSelector extends SpringFactoryImportSelector<SpringCloudJwtClientConfiguration> {

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		String[] imports = super.selectImports(metadata);
		
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(getAnnotationClass().getName(), true));
		
		boolean autoScan = attributes.getBoolean("autoScan");
		
		if (autoScan) {
			List<String> importList = new ArrayList<>(Arrays.asList(imports));
			try {
				Class<?>[] jwtComponentClasses = getClasses("com.revature.security");
				for (Class<?> clazz : jwtComponentClasses)
					if (Arrays.stream(clazz.getAnnotatedInterfaces()).collect(Collectors.toList()).containsAll(Arrays.asList(SpringCloudJwtComponent.class.getAnnotatedSuperclass())))
						importList.add(clazz.getSimpleName().toString().substring(0, 1).toLowerCase() + clazz.getSimpleName().toString().substring(1));
				imports = importList.toArray(new String[0]);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			imports = importList.toArray(new String[0]);
		} else {
			Environment env = getEnvironment();
			if(ConfigurableEnvironment.class.isInstance(env)) {
				ConfigurableEnvironment configEnv = (ConfigurableEnvironment)env;
				LinkedHashMap<String, Object> map = new LinkedHashMap<>();
				map.put("spring.cloud.jwt.client.enabled", false);
				MapPropertySource propertySource = new MapPropertySource(
						"springCloudJwtClient", map);
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

	private static Class<?>[] getClasses(String packageName)  throws ClassNotFoundException, IOException {
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    for (File directory : dirs) {
	        classes.addAll(findClasses(directory, packageName));
	    }
	    return classes.toArray(new Class[classes.size()]);
	}
	
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    if (!directory.exists()) {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        } else if (file.getName().endsWith(".class")) {
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	        }
	    }
	    return classes;
	}
}
