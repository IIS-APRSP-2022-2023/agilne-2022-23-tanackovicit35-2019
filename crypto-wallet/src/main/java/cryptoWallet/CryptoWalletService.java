package cryptoWallet;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cryptoWallet.CustomCryptoWallet;

@Service
public class CryptoWalletService {
	@Autowired
	CryptoWalletRepo repo;
	
	public CustomCryptoWallet findByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	public CustomCryptoWallet update(CustomCryptoWallet wallet) {
		return repo.save(wallet);
	}
	
	public CustomCryptoWallet updateOne(String email, String update, BigDecimal quantity) {
		CustomCryptoWallet wallet = findByEmail(email);
		
		switch(update.toLowerCase()) {
		case "btc":
			wallet.setBtc(wallet.getBtc().add(quantity));
			break;
		case "ada":
			wallet.setBnb(wallet.getBnb().add(quantity));
			break;
		case "eth":
			wallet.setEth(wallet.getEth().add(quantity));
			break;
		default:
			break;
		}
		return repo.save(wallet);
	}
}
