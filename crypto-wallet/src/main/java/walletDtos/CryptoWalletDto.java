package walletDtos;

import java.math.BigDecimal;

public class CryptoWalletDto {
	private BigDecimal BTC;
	private BigDecimal ETH;
	private BigDecimal BNB;
	private String email;
	
	public CryptoWalletDto() {
	}

	public CryptoWalletDto(String email, BigDecimal bTC, BigDecimal eTH, BigDecimal bNB) {
		this.email = email;
		BTC = bTC;
		ETH = eTH;
		BNB = bNB;
	}

	public BigDecimal getBTC() {
		return BTC;
	}

	public void setBTC(BigDecimal bTC) {
		BTC = bTC;
	}

	public BigDecimal getETH() {
		return ETH;
	}

	public void setETH(BigDecimal eTH) {
		ETH = eTH;
	}

	public BigDecimal getBNB() {
		return BNB;
	}

	public void setBNB(BigDecimal bNB) {
		BNB = bNB;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
