package tradeService;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import tradeServiceDtos.CryptoWalletDto;

@FeignClient("crypto-wallet")
public interface CryptoWalletProxy {
	@GetMapping("/crypto-wallet/{email}")
	CryptoWalletDto getWallet(@PathVariable String email);	
	
	@PutMapping("/crypto-wallet/{email}/update/{update}/quantity/{quantity}")
	CryptoWalletDto updateOne(@PathVariable String email, @PathVariable String update, @PathVariable BigDecimal quantity);
}
