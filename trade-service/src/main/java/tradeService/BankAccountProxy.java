package tradeService;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import tradeServiceDtos.BankAccountDto;

@FeignClient(name="bank-account")
public interface BankAccountProxy {
	@GetMapping("/bank-account/user/{email}")
	BankAccountDto getBankAccount(@PathVariable String email);

	@PutMapping("/bank-account/{email}/update/{update}/quantity/{quantity}")
	BankAccountDto updateOne(@PathVariable String email, @PathVariable String update, @PathVariable BigDecimal quantity);

}
