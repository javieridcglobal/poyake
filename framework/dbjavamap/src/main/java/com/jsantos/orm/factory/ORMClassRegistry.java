package com.jsantos.orm.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.mt.MTTrigger;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class ORMClassRegistry {
	private static String GeneratedDTOPackage = "com.jsantos.metadata";
	private static String ExtendedDTOPackage = "com.jsantos.custom.extendeddto";
	private static String TriggersPackage = "com.jsantos.custom.trigger";
	private static ArrayList<String> paths= new ArrayList<String>();
	
	private static final ArrayList<MTTrigger> triggers = new ArrayList<MTTrigger>();
	
	public static void loadClasses() {
		ClassGraph classGraph = new ClassGraph();
		addPath(GeneratedDTOPackage);
		addPath(ExtendedDTOPackage);
		addPath(TriggersPackage);
		
		for (String path : paths) {
			classGraph.whitelistPackages(path);
		}
		
//Javier it need to be fixed correctly
		
		try (ScanResult scanResult = classGraph.scan()) {
		    for (ClassInfo info:scanResult.getSubclasses("com.jsantos.orm.dbstatement.DetachedRecord")) {
	    		Class<?> clazz = Class.forName(info.getName());
	    		
	    		if (hasParameterlessPublicConstructor(clazz)) {
					DetachedRecord dto = (DetachedRecord) clazz.newInstance();
					
					if(null==DTOFactory.getDtoregistry().get(dto.getTable())) {
						DTOFactory.getDtoregistry().put(dto.getTable(),info.loadClass());
//						System.out.println(clazz.getName());
					}
				}
		    }

		    for (ClassInfo info:scanResult.getSubclasses("com.jsantos.orm.mt.MTTrigger")) {
	    		Class<?> clazz = Class.forName(info.getName());
	    		if (hasParameterlessPublicConstructor(clazz)) {
	    			triggers.add((MTTrigger)clazz.newInstance());
	    		}
		    }
		}			
		catch (Throwable e) {
			e.printStackTrace();
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

	public static String getGeneratedDTOPackage() {
		return GeneratedDTOPackage;
	}

	public static void setGeneratedDTOPackage(String generatedDTOPackage) {
		GeneratedDTOPackage = generatedDTOPackage;
	}

	public static String getExtendedDTOPackage() {
		return ExtendedDTOPackage;
	}

	public static void setExtendedDTOPackage(String extendedDTOPackage) {
		ExtendedDTOPackage = extendedDTOPackage;
	}

	public static String getTriggersPackage() {
		return TriggersPackage;
	}

	public static void setTriggersPackage(String triggersPackage) {
		TriggersPackage = triggersPackage;
	}

	public static void logTriggers() {
		System.out.println("ORM Triggers: ====================================================");
		for (MTTrigger trigger:triggers)
			System.out.println("\t" + trigger.getClass().getName());
		System.out.println("------------------------------------------------------------------");
	}

	public static ArrayList<MTTrigger> getTriggers() {
		return triggers;
	}
	
	public static void addPath(String path) {
		paths.add(path);
	}
	
}
