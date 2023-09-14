package cryptoExchange;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class CryptoExchange {
	@Id
	private long id;
	@Column(name="crypto_from")
	private String from;
	@Column(name="crypto_to")
	private String to;
	private BigDecimal cryptoMultiple;
	@Transient
	private String environment;
	
	public CryptoExchange() {
	}

	public CryptoExchange(long id, String from, String to, BigDecimal cryptoMultiple, String environment) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.cryptoMultiple = cryptoMultiple;
		this.environment = environment;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getCryptoMultiple() {
		return cryptoMultiple;
	}

	public void setCryptoMultiple(BigDecimal cryptoMultiple) {
		this.cryptoMultiple = cryptoMultiple;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
}
