package com.poseidon.dolphin.simulator.account;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author gang-yeongho
 * 납입주기
 */
public enum PaymentFrequency {
	DAY {
		@Override
		public long calculateTurns(LocalDate contractDate, LocalDate expiryDate) {
			return ChronoUnit.DAYS.between(contractDate, expiryDate);
		}

		@Override
		public LocalDate nextTurn(LocalDate depositDate) {
			return depositDate.plusDays(1);
		}
	}, 
	WEEK {
		@Override
		public long calculateTurns(LocalDate contractDate, LocalDate expiryDate) {
			return ChronoUnit.WEEKS.between(contractDate, expiryDate);
		}

		@Override
		public LocalDate nextTurn(LocalDate depositDate) {
			return depositDate.plusWeeks(1);
		}
	}, 
	MONTH {
		@Override
		public long calculateTurns(LocalDate contractDate, LocalDate expiryDate) {
			return ChronoUnit.MONTHS.between(contractDate, expiryDate);
		}

		@Override
		public LocalDate nextTurn(LocalDate depositDate) {
			return depositDate.plusMonths(1);
		}
	}, 
	YEAR {
		@Override
		public long calculateTurns(LocalDate contractDate, LocalDate expiryDate) {
			return ChronoUnit.YEARS.between(contractDate, expiryDate);
		}

		@Override
		public LocalDate nextTurn(LocalDate depositDate) {
			return depositDate.plusYears(1);
		}
	};
	
	public abstract long calculateTurns(LocalDate contractDate, LocalDate expiryDate);

	public abstract LocalDate nextTurn(LocalDate depositDate);
}
