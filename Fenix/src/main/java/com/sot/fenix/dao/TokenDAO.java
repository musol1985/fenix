package com.sot.fenix.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Token;

@Repository
public interface TokenDAO extends MongoRepository<Token, String>{
	public Token findBySeries(String series);
}
