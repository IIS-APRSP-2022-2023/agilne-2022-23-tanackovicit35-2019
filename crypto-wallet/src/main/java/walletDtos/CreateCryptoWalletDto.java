package walletDtos;

import java.math.BigDecimal;

public class CreateCryptoWalletDto {
	private Long id;
	private BigDecimal BTC;
	private BigDecimal ETH;
	private BigDecimal BNB;
	private String email;
	
	public Long getId() {
		return id;
	}
	public BigDecimal getBTC() {
		return BTC;
	}
	public BigDecimal getETH() {
		return ETH;
	}
	public BigDecimal getBNB() {
		return BNB;
	}
	public String getEmail() {
		return email;
	}
	
}
