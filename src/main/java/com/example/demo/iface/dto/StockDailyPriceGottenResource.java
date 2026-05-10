package com.example.demo.iface.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDailyPriceGottenResource {
	
	@JsonProperty("Code")
	private String code; // 證券代號
	
	@JsonProperty("Name")
	private String name; // 證券名稱
	
	@JsonProperty("TradeVolume")
	private String tradeVolume; // 成交股數
	
	@JsonProperty("OpeningPrice")
	private String openingPrice; // 開盤價
	
	@JsonProperty("HighestPrice")
	private String highestPrice; // 最高價
	
	@JsonProperty("LowestPrice")
	private String lowestPrice; // 最低價
	
	@JsonProperty("ClosingPrice")
	private String closingPrice; // 收盤價
	
	@JsonProperty("Change")
	private String change; // 漲跌價差
	
	@JsonProperty("Date")
    private String date; // 證券交易日期 (例如 "1150508")
}