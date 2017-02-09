package com.sot.fenix.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.config.ConfigCentro;
import com.sot.fenix.components.models.config.IdCentroPK;

@Repository
public interface ConfigCentroDAO extends MongoRepository<ConfigCentro, IdCentroPK>{

}
