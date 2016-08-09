package com.sot.fenix.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Usuario;

@Repository
public interface UsuarioDAO extends MongoRepository<Usuario, String>{

}
