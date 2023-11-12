package org.ai.roboadvisor.domain.predict.repository;

import org.ai.roboadvisor.domain.predict.entity.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends MongoRepository<Price, String> {


}
