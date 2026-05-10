package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.domain.stock.aggregate.StockDailyPrice;
import com.example.demo.application.domain.stock.command.CreateStockDailyPriceCommand;
import com.example.demo.infra.persistence.StockDailyPriceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // 建議開啟事務，確保資料一致性
public class StockPriceApplicationService {

	private final StockDailyPriceRepository repository;

	/**
	 * 執行建立每日行情的業務邏輯，包含：Aggregate 建立、業務冪等檢查、持久化
	 */
	public void createStockDailyPrice(CreateStockDailyPriceCommand command) {
		// 1. 透過 Command 建立領域聚合 (Aggregate)
		StockDailyPrice aggregate = StockDailyPrice.create(command);

		// 2. 業務冪等檢查 (Idempotency Check)
		repository.findByStockCodeAndTradeDate(aggregate.getStockCode(), aggregate.getTradeDate())
				.ifPresentOrElse(existing -> log.warn("業務冪等攔截：{} ({}) 於日期 {} 的行情已存在，跳過處理。", existing.getStockName(),
						existing.getStockCode(), existing.getTradeDate()), () -> {
							try {
								// 3. 執行持久化
								repository.save(aggregate);
								log.info("業務執行成功：已存入 {} ({}) 的行情資料。", aggregate.getStockName(),
										aggregate.getStockCode());
							} catch (Exception e) {
								// 捕捉資料庫層級的併發衝突 (Unique Constraint)
								log.error("持久化衝突：可能發生重複消費，無法存入資料。錯誤：{}", e.getMessage());
							}
						});
	}
}