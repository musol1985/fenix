package com.sot.fenix.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.templates.dao.IBasicIdDAO;

@Repository
public interface UsuarioDAO extends IBasicIdDAO<Usuario>{
	public Usuario findByCorreo(String correo);
	

	public Page<Usuario> findByCentro(Centro centro, Pageable pageable);
	public List<Usuario> findByCentro(Centro centro);
	public List<Usuario> findByCentro_id(ObjectId centro);
	
	@Query("{ horario.id : { $ne : ?0 }} ")
	public List<Prestacion> getSinHorarioGenerico(ObjectId horarioGenId);
}
