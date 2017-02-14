package com.sot.fenix.templates.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sot.fenix.components.models.templates.AModelId;

public interface IBasicIdDAO<I extends AModelId> extends MongoRepository<I, ObjectId>{

}
