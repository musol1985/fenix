package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.horarios.HorarioEditor;

@Repository
public interface HorarioEditorDAO extends MongoRepository<HorarioEditor, ObjectId>{	

}
