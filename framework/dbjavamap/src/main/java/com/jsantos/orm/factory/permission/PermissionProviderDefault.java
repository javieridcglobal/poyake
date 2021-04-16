package com.jsantos.orm.factory.permission;

import java.sql.SQLException;

import com.jsantos.common.util.MapValues;

public class PermissionProviderDefault implements IPermissionProvider {

	@Override
	public boolean isImplemented() {
		
		return false;
	}

	@Override
	public MapValues<Integer> getPermissions(Object Id) throws SQLException {
		
		return new MapValues<Integer>();
	}

	
	
	@Override
	public Integer getPermissionType(String permissionType) {
		return 0;
	}

	@Override
	public boolean canRead(Object permission) {
		return true;
	}

	@Override
	public boolean canWrite(Object permission) {
		return true;
	}

	@Override
	public boolean hasAnyPermission(Object permission) {
		return true;
	}

	@Override
	public boolean hasAllPermission(Object permission) {
		return true;
	}
}
