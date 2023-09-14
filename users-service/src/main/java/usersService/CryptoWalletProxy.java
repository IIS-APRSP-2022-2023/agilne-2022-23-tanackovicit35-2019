package usersService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="crypto-wallet")
public interface CryptoWalletProxy {
	@DeleteMapping("/crypto-wallet/removeUser/{email}")
	public ResponseEntity<?> removeUserCryptoWallet(@PathVariable String email);
	
	@PostMapping("/crypto-wallet/addAccount")
	public ResponseEntity<?> addUserCryptoWallet(@RequestBody String email);
	
	@PutMapping("/crypto-wallet/changeUserEmail/{email}")
	public ResponseEntity<?> updateUserEmail(@RequestBody String newEmail, @PathVariable("email") String oldEmail);
}
