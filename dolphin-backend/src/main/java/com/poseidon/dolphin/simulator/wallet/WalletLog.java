package com.poseidon.dolphin.simulator.wallet;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.poseidon.dolphin.simulator.wallet.converter.TransferTypeConverter;

@Embeddable
public class WalletLog {
	private long balance;
	private LocalDateTime transferTime;
	@Convert(converter=TransferTypeConverter.class)
	private TransferType transferType;
	private long referenceId;
	
	public WalletLog() {}
	
	public WalletLog(long balance, LocalDateTime transferTime, TransferType transferType, long referenceId) {
		this.balance = balance;
		this.transferTime = transferTime;
		this.transferType = transferType;
		this.referenceId = referenceId;
	}
	
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public LocalDateTime getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(LocalDateTime transferTime) {
		this.transferTime = transferTime;
	}
	public TransferType getTransferType() {
		return transferType;
	}
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

}
