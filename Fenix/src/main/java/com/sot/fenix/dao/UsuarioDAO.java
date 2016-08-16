package com.sot.fenix.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;

@Repository
public interface UsuarioDAO extends MongoRepository<Usuario, ObjectId>{
	public Usuario findByCorreo(String correo);
	

	public Page<Usuario> findByCentro(Centro centro, Pageable pageable);
	public List<Usuario> findByCentro(Centro centro);
}
