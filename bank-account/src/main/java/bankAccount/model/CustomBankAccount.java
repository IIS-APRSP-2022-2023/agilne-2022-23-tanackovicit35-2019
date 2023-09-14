package bankAccount.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CustomBankAccount {
	@Id
	private long id;
	@Column
	private BigDecimal usd = new BigDecimal(0);
	@Column
	private BigDecimal eur = new BigDecimal(0);
	@Column
	private BigDecimal gbp = new BigDecimal(0);
	@Column
	private BigDecimal rsd = new BigDecimal(0);
	@Column
	private BigDecimal chf = new BigDecimal(0);
	@Column(unique=true, nullable=false)
	private String email;
	
	public CustomBankAccount() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getUsd() {
		return usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	public BigDecimal getEur() {
		return eur;
	}

	public void setEur(BigDecimal eur) {
		this.eur = eur;
	}

	public BigDecimal getGbp() {
		return gbp;
	}

	public void setGbp(BigDecimal gbp) {
		this.gbp = gbp;
	}

	public BigDecimal getRsd() {
		return rsd;
	}

	public void setRsd(BigDecimal rsd) {
		this.rsd = rsd;
	}

	public BigDecimal getChf() {
		return chf;
	}

	public void setChf(BigDecimal chf) {
		this.chf = chf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
