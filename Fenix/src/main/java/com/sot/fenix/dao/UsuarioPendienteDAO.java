package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;

@Repository
public interface UsuarioPendienteDAO extends MongoRepository<UsuarioPendiente, String>{
	public UsuarioPendiente findByCorreo(String correo);
}
