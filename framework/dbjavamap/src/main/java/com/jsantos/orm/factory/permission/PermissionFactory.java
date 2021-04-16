package com.jsantos.orm.factory.permission;

import com.jsantos.orm.factory.AppClassRegistry;
import com.jsantos.orm.factory.internationalization.ILabelProvider;
import com.jsantos.orm.factory.internationalization.LabelProviderDefault;

public class PermissionFactory {

	private static String GUI_PACKAGE = "com.jsantos.service";
	private static String className = "com.jsantos.orm.factory.permission.IPermissionProvider";
	private static  IPermissionProvider permissionProvider = null;
	
	
	public static void init()  {
		if(null==permissionProvider){
			Class<?> iclass=AppClassRegistry.getClasses(GUI_PACKAGE, className);
			if(null!=iclass)
				try {
					permissionProvider=(IPermissionProvider) iclass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			else permissionProvider=new PermissionProviderDefault();
			}
	}
	public static IPermissionProvider getProvider() {
		if(null==permissionProvider) init();
		return permissionProvider;
	}
	
		
	
}
