package bankAccount;


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

import bankAccount.model.CustomBankAccount;
import bankAccountDtos.BankAccountDto;
import bankAccountDtos.UpdateBankAccountDto;

@RestController
public class BankAccountController {
	@Autowired
	private BankAccountRepo repo;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("/bank-account")
	public ResponseEntity<?> getUserAccount(@RequestHeader("Authorization") String auth){
		String pair = new String(Base64.decodeBase64(auth.substring(6)));
		String email = pair.split(":")[0];
		CustomBankAccount userAccount = repo.findByEmail(email);
		
		if(userAccount == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no bank account linked to that email.");
		} else {
			BankAccountDto userAccountDto = new BankAccountDto();
			userAccountDto.setEmail(userAccount.getEmail());
			userAccountDto.setEur(userAccount.getEur());
			userAccountDto.setRsd(userAccount.getRsd());
			userAccountDto.setUsd(userAccount.getUsd());
			userAccountDto.setGbp(userAccount.getGbp());
			userAccountDto.setChf(userAccount.getChf());
			return ResponseEntity.ok(userAccountDto);
		}
		
	}
	
	@GetMapping("/bank-account/user/{email}")
	public CustomBankAccount getBankAccount(@PathVariable String email) {
		return repo.findByEmail(email);
	}
	
	@PutMapping("/bank-account/{email}/update/{update}/quantity/{quantity}")
	public CustomBankAccount updateOne(@PathVariable String email, @PathVariable String update, @PathVariable BigDecimal quantity) {
		CustomBankAccount account = repo.findByEmail(email);
		
		switch(update.toUpperCase()) {
		case "USD":
			account.setUsd(account.getUsd().add(quantity));
			break;
		case "EUR":
			account.setEur(account.getEur().add(quantity));
			break;
		case "RSD":
			account.setRsd(account.getRsd().add(quantity));
			break;
		case "GBP":
			account.setGbp(account.getGbp().add(quantity));
			break;
		case "CHF":
			account.setChf(account.getChf().add(quantity));
			break;
		}
		return repo.save(account);
	}
	
	@PutMapping("/bank-account/changeUserEmail/{email}")
	public ResponseEntity<?> updateUserEmail(@RequestBody String newEmail, @PathVariable("email") String oldEmail){
		CustomBankAccount account = repo.findByEmail(oldEmail);
		
		if(account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no bank account linked to that email.");
		} else {
			Long userId = account.getId();
			account.setEmail(newEmail);
			jdbcTemplate.update("UPDATE custom_bank_account SET email = ? WHERE id = ?", account.getEmail(), userId);
			return ResponseEntity.ok().build();
		}
	}
	@PutMapping("/bank-account/editAccount/{email}")
	public ResponseEntity<?> updateUserBankAccount(@RequestBody UpdateBankAccountDto accountDto, @PathVariable("email") String email){
		CustomBankAccount account = repo.findByEmail(email);
		if(account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no bank account linked to that email.");
		} else {
			account.setRsd(accountDto.getRsd());
			account.setUsd(accountDto.getUsd());
			account.setEur(accountDto.getEur());
			account.setGbp(accountDto.getGbp());
			account.setChf(accountDto.getChf());
			
			jdbcTemplate.update(
					"UPDATE custom_bank_account SET usd = ?, gbp = ?, chf = ?, eur = ?, rsd = ? WHERE email = ?",
					account.getUsd(),
					account.getGbp(),
					account.getChf(),
					account.getEur(),
					account.getRsd(),
					email
				);
			
			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("message", "Assets updated successfully!");
			responseBody.put("bank account", mapperBankAccountUpdateDto(account));
			return ResponseEntity.ok().body(responseBody);
		}
	}
	
	@PutMapping("/bank-account")
	public ResponseEntity<?> updateUserAccount(@RequestBody BankAccountDto userAccountDto) {
		CustomBankAccount account = repo.findByEmail(userAccountDto.getEmail());
		
		if(account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("There is no bank account linked to that email.");
		} else {
			account.setRsd(userAccountDto.getRsd());
			account.setEur(userAccountDto.getEur());
			account.setUsd(userAccountDto.getUsd()); 
			account.setGbp(userAccountDto.getGbp());
			account.setChf(userAccountDto.getChf());
			
			jdbcTemplate.update(
					"UPDATE custom_bank_account SET usd = ?, gbp = ?, chf = ?, eur = ?, rsd = ? WHERE email = ?",
					account.getUsd(),
					account.getGbp(),
					account.getChf(),
					account.getEur(),
					account.getRsd(),
					account.getEmail()
				);
			
			return ResponseEntity.ok(mapperBankAccountDto(account));
		}
	}
	
	@PostMapping("/bank-account/addAccount")
	public ResponseEntity<?> addUserBankAccount(@RequestBody String email) {
		CustomBankAccount account = repo.findByEmail(email);
		
		if(account != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("This user already has a bank account.");
		} else {
			Long bankAccountId = ThreadLocalRandom.current().nextLong(3, 101);
			
			CustomBankAccount newUserAccount = new CustomBankAccount();
			
			newUserAccount.setId(bankAccountId);
			newUserAccount.setChf(new BigDecimal(0));
			newUserAccount.setRsd(new BigDecimal(0));
			newUserAccount.setEur(new BigDecimal(0));
			newUserAccount.setUsd(new BigDecimal(0));
			newUserAccount.setGbp(new BigDecimal(0));
			newUserAccount.setEmail(email);
			
			repo.save(newUserAccount);
			
			return ResponseEntity.ok().build();
		}
		
	}
	
	@DeleteMapping("/bank-account/removeUser/{email}")
	public ResponseEntity<?> removeUserBankAccount(@PathVariable("email") String email) {
		CustomBankAccount account = repo.findByEmail(email);
		
		if(account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("There is no bank account linked to that email.");
		} else {
			Long userId = account.getId();
			
			repo.deleteById(userId);
			
			return ResponseEntity.ok().build();
		}
	}
	
	private BankAccountDto mapperBankAccountDto(CustomBankAccount entity) {
		return new BankAccountDto(
			entity.getEmail(),
			entity.getRsd(),
			entity.getEur(),
			entity.getUsd(),
			entity.getGbp(),
			entity.getChf()
		);
	}
	
	private UpdateBankAccountDto mapperBankAccountUpdateDto(CustomBankAccount entity) {
		return new UpdateBankAccountDto(
			entity.getRsd(),
			entity.getEur(),
			entity.getUsd(),
			entity.getGbp(),
			entity.getChf()
		);
	}
}
