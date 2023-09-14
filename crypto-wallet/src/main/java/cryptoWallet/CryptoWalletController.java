package cryptoWallet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cryptoWallet.CustomCryptoWallet;
import walletDtos.CryptoWalletDto;
import walletDtos.UpdateCryptoWalletDto;

@RestController
public class CryptoWalletController {
	@Autowired
	private CryptoWalletRepo repo;
	@Autowired
	private CryptoWalletService service;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("/crypto-wallet")
	public ResponseEntity<?> getUserAccount(@RequestHeader("Authorization") String auth){
		String pair = new String(Base64.decodeBase64(auth.substring(6)));
		String email = pair.split(":")[0];
		CustomCryptoWallet wallet = repo.findByEmail(email);
		if(wallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no crypto wallet linked to this email.");
		} else {
			CryptoWalletDto walletDto = new CryptoWalletDto();
			walletDto.setEmail(wallet.getEmail());
			walletDto.setETH(wallet.getEth());
			walletDto.setBNB(wallet.getBnb());
			walletDto.setBTC(wallet.getBtc());
			return ResponseEntity.ok(walletDto);
		}
	}
	
	@GetMapping("/crypto-wallet/{email}")
	public CustomCryptoWallet getWallet(@PathVariable String email) {
		return service.findByEmail(email);
	}
	
	@PutMapping("/crypto-wallet/{email}/update/{update}/quantity/{quantity}")
	public CustomCryptoWallet updateOne(@PathVariable String email, @PathVariable String update, @PathVariable BigDecimal quantity) {
		return service.updateOne(email, update, quantity);
	}
	
	@PutMapping("/crypto-wallet/changeUserEmail/{email}")
	public ResponseEntity<?> updateUserEmail(@RequestBody String newEmail, @PathVariable("email") String oldEmail){
		CustomCryptoWallet wallet = repo.findByEmail(oldEmail);
		if(wallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no crypto wallet linked to that email.");
		} else {
			Long userId = wallet.getId();
			wallet.setEmail(newEmail);
			jdbcTemplate.update("UPDATE custom_crypto_wallet SET email = ? WHERE id = ?", wallet.getEmail(), userId);

			return ResponseEntity.ok().build();
		}
	}
	
	@PutMapping("/crypto-wallet")
	public ResponseEntity<?> updateUserAccount(@RequestBody CryptoWalletDto userAccountDto){
		CustomCryptoWallet account = repo.findByEmail(userAccountDto.getEmail());
		
		if(account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no crypto wallet linked to that email.");
		} else {
			account.setBnb(userAccountDto.getBNB());
			account.setBtc(userAccountDto.getBTC());
			account.setEth(userAccountDto.getETH());
			
			jdbcTemplate.update("UPDATE custom_crypto_wallet SET bnb = ?, btc = ?, eth = ? WHERE email = ?",
					account.getBnb(), account.getBtc(), account.getEth(), account.getEmail());

			return ResponseEntity.ok(mapperCryptoWalletDto(account));
		}
	}
	
	@PutMapping("/crypto-wallet/editWallet/{email}")
	public ResponseEntity<?> updateUserBankAccount(@RequestBody UpdateCryptoWalletDto cryptoWalletDto,
			@PathVariable("email") String email) {
		CustomCryptoWallet wallet = repo.findByEmail(email);

		if (wallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is not wallet with that email.");
		} else {
			wallet.setBnb(cryptoWalletDto.getBNB());
			wallet.setBtc(cryptoWalletDto.getBTC());
			wallet.setEth(cryptoWalletDto.getETH());

			jdbcTemplate.update("UPDATE custom_crypto_wallet SET bnb = ?, btc = ?, eth = ? WHERE email = ?",
					wallet.getBnb(), wallet.getBtc(), wallet.getEth(), email);

			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("message", "Assets on the crypto wallet have been updated.");
			responseBody.put("bank account", mapperCryptoWalletUpdateDto(wallet));

			return ResponseEntity.ok().body(responseBody);
		}
	}
	
	@PostMapping("/crypto-wallet/addAccount")
	public ResponseEntity<?> addUserCryptoWallet(@RequestBody String email) {
		CustomCryptoWallet wallet = repo.findByEmail(email);

		if (wallet != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already has a wallet.");
		} else {
			Long userAccountId = ThreadLocalRandom.current().nextLong(3, 101);

			CustomCryptoWallet newUserAccount = new CustomCryptoWallet();

			newUserAccount.setId(userAccountId);
			newUserAccount.setBnb(new BigDecimal(0));
			newUserAccount.setEth(new BigDecimal(0));
			newUserAccount.setBtc(new BigDecimal(0));
			newUserAccount.setEmail(email);

			repo.save(newUserAccount);

			return ResponseEntity.ok().build();
		}

	}
	
	@DeleteMapping("/crypto-wallet/removeUser/{email}")
	public ResponseEntity<?> removeUserCryptoWallet(@PathVariable("email") String email) {
		CustomCryptoWallet wallet = repo.findByEmail(email);

		if (wallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no wallet linked to that email.");
		} else {
			Long userId = wallet.getId();

			repo.deleteById(userId);

			return ResponseEntity.ok().build();
		}
	}
	
	private UpdateCryptoWalletDto mapperCryptoWalletUpdateDto(CustomCryptoWallet entity) {
		return new UpdateCryptoWalletDto(entity.getBnb(), entity.getBtc(), entity.getEth());
	}

	private CryptoWalletDto mapperCryptoWalletDto(CustomCryptoWallet entity) {
		return new CryptoWalletDto(entity.getEmail(), entity.getBnb(), entity.getEth(), entity.getBtc());
	}
}
