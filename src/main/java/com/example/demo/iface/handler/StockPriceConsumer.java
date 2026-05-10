package com.example.demo.iface.handler;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.application.domain.stock.command.CreateStockDailyPriceCommand;
import com.example.demo.application.service.StockPriceApplicationService;
import com.example.demo.iface.dto.StockDailyPriceGottenResource;
import com.example.demo.util.BaseDataTransformer;
import com.example.demo.util.JsonParseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockPriceConsumer {

	private final StockPriceApplicationService applicationService;

	@KafkaListener(topics = "topic.twse-stock-daily", groupId = "stock-consumer-group")
	public void consume(String message) {
		// 1. 通訊協議層級的初步驗證
		if (message == null || message.trim().isEmpty() || "null".equalsIgnoreCase(message.trim())) {
			log.warn("收到空訊息，略過處理。");
			return;
		}

		try {
			// 2. 格式轉換 (JSON -> Resource List)
			List<StockDailyPriceGottenResource> resources = JsonParseUtil.unserializeArrayOfObject(message,
					StockDailyPriceGottenResource.class);

			if (resources != null && !resources.isEmpty()) {
				for (StockDailyPriceGottenResource resource : resources) {

					// 3. 防腐層處理：將外部資源轉換為內部業務命令 (Command)
					CreateStockDailyPriceCommand command = BaseDataTransformer.transformData(resource,
							CreateStockDailyPriceCommand.class);

					// 4. 委託 Application Service 執行核心業務
					applicationService.createStockDailyPrice(command);
				}
			}

		} catch (Exception e) {
			// 這裡主要捕捉 JSON 解析或轉換層級的錯誤
			log.error("Consumer 轉譯層級發生未預期錯誤: {}", e.getMessage(), e);
		}
	}
}