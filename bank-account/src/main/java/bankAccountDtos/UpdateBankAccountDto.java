package bankAccountDtos;

import java.math.BigDecimal;

public class UpdateBankAccountDto {
	private BigDecimal rsd;
	private BigDecimal eur;
	private BigDecimal usd;
	private BigDecimal gbp;
	private BigDecimal chf;
	
	public UpdateBankAccountDto() {
	}

	public UpdateBankAccountDto(BigDecimal rsd, BigDecimal eur, BigDecimal usd, BigDecimal gbp, BigDecimal chf) {
		this.rsd = rsd;
		this.eur = eur;
		this.usd = usd;
		this.gbp = gbp;
		this.chf = chf;
	}

	public BigDecimal getRsd() {
		return rsd;
	}

	public void setRsd(BigDecimal rsd) {
		this.rsd = rsd;
	}

	public BigDecimal getEur() {
		return eur;
	}

	public void setEur(BigDecimal eur) {
		this.eur = eur;
	}

	public BigDecimal getUsd() {
		return usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	public BigDecimal getGbp() {
		return gbp;
	}

	public void setGbp(BigDecimal gbp) {
		this.gbp = gbp;
	}

	public BigDecimal getChf() {
		return chf;
	}

	public void setChf(BigDecimal chf) {
		this.chf = chf;
	}
	
}
