package com.sot.fenix.components.models.templates;

import org.bson.types.ObjectId;

public interface IUsuario{
	public ObjectId getId();
	public String getCorreo();
	public String getNombre();
	public boolean isAdmin();
	public boolean isPendiente();
}
