package com.poseidon.dolphin.api.fss;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;
import org.springframework.scheduling.support.CronSequenceGenerator;

import com.poseidon.dolphin.api.fss.company.scheduler.CompanyScheduler;
import com.poseidon.dolphin.api.fss.deposit.scheduler.DepositScheduler;
import com.poseidon.dolphin.api.fss.saving.scheduler.SavingScheduler;

public class SchedulerTests {
	
	@Test
	public void givenCompanySchedulerCronExpressionThenExpectWorkingTime() {
		LocalDateTime currentTime = LocalDateTime.of(LocalDate.of(2019, 1, 1), LocalTime.of(8, 0));
		CronSequenceGenerator generator = new CronSequenceGenerator(CompanyScheduler.SCHEDULER_CRON);
		Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
		date = generator.next(date);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		assertThat(formatter.format(date)).isEqualTo("2019-01-01 09:00");
		
		for(int i=2; i<=4; i++) {
			date = generator.next(date);
			assertThat(formatter.format(date)).isEqualTo("2019-01-0" + i + " 09:00");
		}
		
		date = generator.next(date);
		assertThat(formatter.format(date)).isEqualTo("2019-01-07 09:00");
	}
	
	@Test
	public void givenSavingSchedulerCronExpressionThenExpectWorkingTime() {
		LocalDateTime currentTime = LocalDateTime.of(LocalDate.of(2019, 1, 1), LocalTime.of(8, 0));
		CronSequenceGenerator generator = new CronSequenceGenerator(SavingScheduler.SCHEDULER_CRON);
		Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
		date = generator.next(date);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		assertThat(formatter.format(date)).isEqualTo("2019-01-01 10:00");
		
		for(int i=2; i<=4; i++) {
			date = generator.next(date);
			assertThat(formatter.format(date)).isEqualTo("2019-01-0" + i + " 10:00");
		}
		
		date = generator.next(date);
		assertThat(formatter.format(date)).isEqualTo("2019-01-07 10:00");
	}
	
	@Test
	public void givenDepositSchedulerCronExpressionThenExpectWorkingTime() {
		LocalDateTime currentTime = LocalDateTime.of(LocalDate.of(2019, 1, 1), LocalTime.of(7, 0));
		CronSequenceGenerator generator = new CronSequenceGenerator(DepositScheduler.SCHEDULER_CRON);
		Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
		date = generator.next(date);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		assertThat(formatter.format(date)).isEqualTo("2019-01-01 08:00");
		
		for(int i=2; i<=4; i++) {
			date = generator.next(date);
			assertThat(formatter.format(date)).isEqualTo("2019-01-0" + i + " 08:00");
		}
		
		date = generator.next(date);
		assertThat(formatter.format(date)).isEqualTo("2019-01-07 08:00");
	}
	
}
