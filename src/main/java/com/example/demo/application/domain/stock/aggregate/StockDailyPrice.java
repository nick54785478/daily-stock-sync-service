package com.example.demo.application.domain.stock.aggregate;

import java.time.LocalDate;

import com.example.demo.application.domain.stock.aggregate.vo.Price;
import com.example.demo.application.domain.stock.command.CreateStockDailyPriceCommand;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stock_daily_prices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StockDailyPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String stockCode; // 證券代號

	@Column(nullable = false)
	private String stockName; // 證券名稱

	@Column(nullable = false)
	private LocalDate tradeDate; // 交易日期

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "opening_price")) })
	private Price openingPrice;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "highest_price")) })
	private Price highestPrice;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "lowest_price")) })
	private Price lowestPrice;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "closing_price")) })
	private Price closingPrice;

	private Long tradeVolume; // 成交股數

	// 靜態工廠方法：將外部 DTO 轉換為領域實體
	public static StockDailyPrice create(CreateStockDailyPriceCommand command) {
		// 處理日期轉換 (例如民國 1150508 轉 LocalDate)
		LocalDate date = parseMinguoDate(command.getDate());
		StockDailyPrice stockDailyPrice = new StockDailyPrice();
		stockDailyPrice.stockName = command.getName();
		stockDailyPrice.tradeDate = date;
		stockDailyPrice.stockCode = command.getCode();
		stockDailyPrice.openingPrice = Price.of(command.getOpeningPrice());
		stockDailyPrice.highestPrice = Price.of(command.getHighestPrice());
		stockDailyPrice.lowestPrice = Price.of(command.getLowestPrice());
		stockDailyPrice.closingPrice = Price.of(command.getClosingPrice());
		stockDailyPrice.tradeVolume = Long.parseLong(command.getTradeVolume().replace(",", ""));

		return stockDailyPrice;
	}

	private static LocalDate parseMinguoDate(String minguoDate) {
		// 邏輯範例：1150508 -> 2026-05-08
		int year = Integer.parseInt(minguoDate.substring(0, 3)) + 1911;
		int month = Integer.parseInt(minguoDate.substring(3, 5));
		int day = Integer.parseInt(minguoDate.substring(5, 7));
		return LocalDate.of(year, month, day);
	}
}