package com.sot.fenix.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.templates.dao.IBasicIdDAO;

@Repository
public interface UsuarioPendienteDAO extends IBasicIdDAO<UsuarioPendiente>{
	public UsuarioPendiente findByCorreo(String correo);
	public Page<UsuarioPendiente> findByCentro(Centro centro, Pageable pageable);
	public List<UsuarioPendiente> findByCentro(Centro centro);
}
