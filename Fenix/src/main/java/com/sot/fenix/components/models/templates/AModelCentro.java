package com.sot.fenix.components.models.templates;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sot.fenix.components.models.Centro;

/**
 * Model que exitende de Model con ObjectId como id y aparte tiene el campo Centro
 * @author eduarmar
 *
 */
public abstract class AModelCentro extends AModelId{

	@DBRef
	@Indexed
	protected Centro centro;


	public Centro getCentro() {
		return centro;
	}
	
	public void setCentro(Centro centro) {
		this.centro = centro;
	}
	@JsonGetter("centro")
	public String getJsonCentro(){
		if(centro==null)
			return "";
		return centro.getId().toHexString();
	}
	@JsonSetter("centro")
	public void setJsonCentro(String id) {
		if(id!=null && !id.isEmpty()){
			centro=new Centro();
			centro.id=new ObjectId(id);
		}
	}
}
