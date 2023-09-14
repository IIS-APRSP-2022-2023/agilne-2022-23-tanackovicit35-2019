package cryptoWallet;

import org.springframework.data.jpa.repository.JpaRepository;

import cryptoWallet.CustomCryptoWallet;

public interface CryptoWalletRepo extends JpaRepository<CustomCryptoWallet, Long> {
	CustomCryptoWallet findByEmail(String email);
}
