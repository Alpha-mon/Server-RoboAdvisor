package org.ai.roboadvisor.domain.predict.repository;

import org.ai.roboadvisor.domain.predict.entity.PortFolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortFolioRepository extends MongoRepository<PortFolio, String> {


}
