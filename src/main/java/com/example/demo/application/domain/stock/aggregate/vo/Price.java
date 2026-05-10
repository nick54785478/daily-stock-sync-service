package com.example.demo.application.domain.stock.aggregate.vo;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {
	private BigDecimal value;

	public static Price of(String value) {
		if (value == null || value.isBlank() || "--".equals(value)) {
			return new Price(BigDecimal.ZERO);
		}
		return new Price(new BigDecimal(value.replace(",", "")));
	}
}