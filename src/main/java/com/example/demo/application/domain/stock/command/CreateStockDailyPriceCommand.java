package com.example.demo.application.domain.stock.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateStockDailyPriceCommand {

	private String code; // 證券代號

	private String name; // 證券名稱

	private String tradeVolume; // 成交股數

	private String openingPrice; // 開盤價

	private String highestPrice; // 最高價

	private String lowestPrice; // 最低價

	private String closingPrice; // 收盤價

	private String change; // 漲跌價差

	private String date; // 證券交易日期 (例如 "1150508")
}