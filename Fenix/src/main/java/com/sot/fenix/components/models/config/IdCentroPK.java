package com.sot.fenix.components.models.config;

import java.io.Serializable;

import org.bson.types.ObjectId;

public class IdCentroPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -181079546600345586L;
	private ObjectId centroId;
	private String paramId;
	

	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
	public IdCentroPK(){
		
	}
	
	public IdCentroPK(ObjectId centro, String paramId){
		this.centroId=centro;
		this.paramId=paramId;
	}
}
