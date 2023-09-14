package walletDtos;

import java.math.BigDecimal;

public class BankAccountDto {
	private Long id;
	private BigDecimal usd;
	private BigDecimal gbp;
	private BigDecimal chf;
	private BigDecimal eur;
	private BigDecimal rsd;
	
	public Long getId() {
		return id;
	}
	public BigDecimal getUsd() {
		return usd;
	}
	public BigDecimal getGbp() {
		return gbp;
	}
	public BigDecimal getChf() {
		return chf;
	}
	public BigDecimal getEur() {
		return eur;
	}
	public BigDecimal getRsd() {
		return rsd;
	}
	
}
