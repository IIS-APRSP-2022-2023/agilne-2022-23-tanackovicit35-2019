package cryptoConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import cryptoConversionDtos.CryptoWalletDto;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
	@GetMapping("/crypto-wallet")
	public CryptoWalletDto getUserAccount(@RequestHeader("Authorization") String auth);

	@PutMapping("/crypto-wallet")
	ResponseEntity<CryptoWalletDto> updateUserAccount(@RequestBody CryptoWalletDto userAccountDto);
}
