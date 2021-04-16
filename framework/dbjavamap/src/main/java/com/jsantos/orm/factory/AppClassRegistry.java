package com.jsantos.orm.factory;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.springframework.core.io.Resource;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class AppClassRegistry {
	

	public static Class<?> getClasses(String GUI_PACKAGE,String className) {
		ClassGraph classGraph = new ClassGraph();
		
		classGraph.whitelistPackages(GUI_PACKAGE);

		try (ScanResult scanResult = classGraph.scan()) {

			System.out.println("---------------------------------APPCLASSREGISTRY BUILDERS--------------------------------");
			System.out.println(GUI_PACKAGE+" "+className);

			for (ClassInfo info:scanResult.getClassesImplementing(className)){
	    		Class<?> clazz = Class.forName(info.getName());
	    		if (!info.isAbstract() && hasParameterlessPublicConstructor(clazz)) {
	    			System.out.println("Registered class : " + clazz.getName());
	    			return clazz;
	    		}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("no class found for " +GUI_PACKAGE+className );
		return null;

	}

	public static Object loadClass(Resource resource)
			throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		String className = resource.getURL().toString();
		// String classNameOriginal = resource.getURL().toString();
		try {
			className = className.substring(className.indexOf("!/") + 2, className.length());
			className = className.replace("/", ".");
			className = className.substring(0, className.lastIndexOf('.'));
			Class<?> clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (InstantiationException | ClassNotFoundException e) {
			System.err.println("Couldn't instantiate class: " + className);
			throw e;
		}
	}

	static boolean hasParameterlessPublicConstructor(Class<?> clazz) {
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (constructor.getParameterCount() == 0) {
				return true;
			}
		}
		return false;
	}

}
