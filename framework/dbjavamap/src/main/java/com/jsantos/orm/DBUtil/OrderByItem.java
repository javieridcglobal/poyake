package com.jsantos.orm.DBUtil;

public class OrderByItem{
	
	protected String name = null;
	protected String stringAsc=null;
	protected String stringDesc=null;
	protected boolean asc = true;
	
	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public OrderByItem(String name, Boolean asc){
		this.name = name;
		this.asc = asc;
	}

	public OrderByItem(){
		}
	
	
	public OrderByItem(String name, String stringAsc,String stringDesc,Boolean asc){
		this.name = name;
		this.stringAsc=stringAsc;
		this.stringDesc=stringDesc;
		this.asc = asc;
	}

	public String getOrder(){
		
		if(asc && null!=stringAsc) return stringAsc;
		if(!asc && null!=stringDesc) return stringDesc;
		return name + " " + (asc ? " asc ": " desc ");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}




