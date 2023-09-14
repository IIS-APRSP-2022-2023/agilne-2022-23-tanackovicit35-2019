package cryptoConversion;

import java.math.BigDecimal;

public class CryptoConversion {
	private String from;
	private String to;
	private BigDecimal cryptoMultiple;
	private String environment;
	private BigDecimal cryptoTotal;
	private Double quantity;

	public CryptoConversion() {
	}

	public CryptoConversion(String from, String to, BigDecimal cryptoMultiple, String environment, Double quantity,
			BigDecimal cryptoTotal) {
		this.from = from;
		this.to = to;
		this.cryptoMultiple = cryptoMultiple;
		this.environment = environment;
		this.quantity = quantity;
		this.cryptoTotal = cryptoTotal;
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

	public BigDecimal getCryptoTotal() {
		return cryptoTotal;
	}

	public void setCryptoTotal(BigDecimal cryptoTotal) {
		this.cryptoTotal = cryptoTotal;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
