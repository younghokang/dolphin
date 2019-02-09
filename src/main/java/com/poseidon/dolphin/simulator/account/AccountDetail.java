package com.poseidon.dolphin.simulator.account;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AccountDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**
	 * 회차
	 */
	private int turn;
	/**
	 * 금액
	 */
	private long balance;
	/**
	 * 입금일
	 */
	private LocalDate depositDate;
	@CreatedDate
	private LocalDateTime createdDate;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	public AccountDetail() {}
	
	public AccountDetail(int turn, long balance, LocalDate depositDate) {
		this.turn = turn;
		this.balance = balance;
		this.depositDate = depositDate;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public LocalDate getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(LocalDate depositDate) {
		this.depositDate = depositDate;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
