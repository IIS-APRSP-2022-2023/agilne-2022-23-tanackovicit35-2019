package usersService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="bank-account")
public interface BankAccountProxy {
	@DeleteMapping("/bank-account/removeUser/{email}")
	public ResponseEntity<?> removeUserBankAccount(@PathVariable String email);
	
	@PostMapping("/bank-account/addAccount")
	public ResponseEntity<?> addUserBankAccount(@RequestBody String email);
	
	@PutMapping("/bank-account/changeUserEmail/{email}")
	public ResponseEntity<?> updateUserEmail(@RequestBody String newEmail, @PathVariable("email") String oldEmail);
}
