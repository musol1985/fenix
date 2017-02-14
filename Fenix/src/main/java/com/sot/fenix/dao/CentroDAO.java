package com.sot.fenix.dao;

import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.templates.dao.IBasicIdDAO;

@Repository
public interface CentroDAO extends IBasicIdDAO<Centro>{
	public Centro findByCorreoAdmin(String correo);
}
