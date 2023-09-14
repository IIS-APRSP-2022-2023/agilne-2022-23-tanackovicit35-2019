package cryptoWallet;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CustomCryptoWallet {
	@Id
	private long id;
	@Column(precision = 20, scale = 5)
	private BigDecimal btc;
	@Column(precision = 20, scale = 5)
	private BigDecimal eth;
	@Column(precision = 20, scale = 5)
	private BigDecimal bnb;
	@Column(name = "email")
	private String email;
	
	public CustomCryptoWallet() {
	}

	public CustomCryptoWallet(long id, BigDecimal btc, BigDecimal eth, BigDecimal bnb, String email) {
		this.id = id;
		this.btc = btc;
		this.eth = eth;
		this.bnb = bnb;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getBtc() {
		return btc;
	}

	public void setBtc(BigDecimal btc) {
		this.btc = btc;
	}

	public BigDecimal getEth() {
		return eth;
	}

	public void setEth(BigDecimal eth) {
		this.eth = eth;
	}

	public BigDecimal getBnb() {
		return bnb;
	}

	public void setBnb(BigDecimal bnb) {
		this.bnb = bnb;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}

