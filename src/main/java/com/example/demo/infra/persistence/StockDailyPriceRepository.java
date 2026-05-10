package com.example.demo.infra.persistence;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.application.domain.stock.aggregate.StockDailyPrice;

public interface StockDailyPriceRepository extends JpaRepository<StockDailyPrice, Long>{

	Optional<StockDailyPrice> findByStockCodeAndTradeDate(String stockCode, LocalDate tradeDate);
}
