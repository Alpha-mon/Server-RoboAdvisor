package org.ai.roboadvisor.domain.tendency.repository;


import org.ai.roboadvisor.domain.tendency.entity.StockKr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockKrRepository extends JpaRepository<StockKr, Long> {

    Optional<StockKr> findByStockName(String stockName);

}
