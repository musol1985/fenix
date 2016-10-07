package com.sot.fenix.dao;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Cita;

@Repository
public interface CitaDAO extends MongoRepository<Cita, ObjectId>{	
	//public Cliente findByCentro_idAndDni(ObjectId centro, String dni);
	
	public List<Cita> findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndProfesional_idAndPrestacion_id(Date fechaIni, Date fechaFin, ObjectId centroId, ObjectId profesionalId, ObjectId prestacionId);
	public List<Cita> findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndProfesional_id(Date fechaIni, Date fechaFin, ObjectId centroId, ObjectId profesionalId);
	public List<Cita> findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndPrestacion_id(Date fechaIni, Date fechaFin, ObjectId centroId, ObjectId prestacionId);
	public List<Cita> findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(Date fechaIni, Date fechaFin, ObjectId centroId);

}
