package cryptoConversionDtos;

import java.math.BigDecimal;

public class CryptoDto {
	private String from;
	private String to;
	private BigDecimal cryptoMultiple;
	private BigDecimal cryptoTotal;
	private BigDecimal quantity;
	private String enviroment;

	public CryptoDto() {
	}

	public CryptoDto(String from, String to, BigDecimal cryptoMultiple, BigDecimal cryptoTotal, BigDecimal quantity,
			String enviroment) {
		this.from = from;
		this.to = to;
		this.cryptoMultiple = cryptoMultiple;
		this.cryptoTotal = cryptoTotal;
		this.quantity = quantity;
		this.enviroment = enviroment;
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

	public BigDecimal getCryptoTotal() {
		return cryptoTotal;
	}

	public void setCryptoTotal(BigDecimal cryptoTotal) {
		this.cryptoTotal = cryptoTotal;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getEnviroment() {
		return enviroment;
	}

	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}

}
