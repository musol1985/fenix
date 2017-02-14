package com.sot.fenix.templates.dao;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.templates.AModelNombre;

public interface INombreCentroIdDAO<I extends AModelNombre> extends ICentroIdDAO<I>{
	public I findByCentroAndNombre(Centro centro, String nombre);
}
