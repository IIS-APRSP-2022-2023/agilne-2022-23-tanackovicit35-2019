package currencyConversionDtos;

import java.math.BigDecimal;

public class CurrencyDto {
	private String from;

	private String to;

	private BigDecimal conversionMultiple;

	private BigDecimal conversionTotal;

	private BigDecimal quantity;

	private String enviroment;

	public CurrencyDto() {

	}

	public CurrencyDto(String from, String to, BigDecimal conversionMultiple, BigDecimal conversionTotal,
			BigDecimal quantity, String enviroment) {
		this.from = from;
		this.to = to;
		this.conversionMultiple = conversionMultiple;
		this.conversionTotal = conversionTotal;
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

	public BigDecimal getConversionMultiple() {
		return conversionMultiple;
	}

	public void setConversionMultiple(BigDecimal conversionMultiple) {
		this.conversionMultiple = conversionMultiple;
	}

	public BigDecimal getConversionTotal() {
		return conversionTotal;
	}

	public void setConversionTotal(BigDecimal conversionTotal) {
		this.conversionTotal = conversionTotal;
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
