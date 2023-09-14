package currencyConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import currencyConversionDtos.BankAccountDto;

@FeignClient(name="bank-account")
public interface BankAccountProxy {
	@GetMapping("/bank-account")
	public BankAccountDto getUserAccount(@RequestHeader("Authorization") String auth);
	
	@PutMapping("/bank-account")
	ResponseEntity<BankAccountDto> updateUserAccount(@RequestBody BankAccountDto userAccountDto);
}
