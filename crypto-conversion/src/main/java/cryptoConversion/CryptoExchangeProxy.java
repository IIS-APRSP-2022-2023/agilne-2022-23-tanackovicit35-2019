package cryptoConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cryptoConversionDtos.CryptoDto;

@FeignClient(name = "crypto-exchange")
public interface CryptoExchangeProxy {
	@GetMapping("/crypto-exchange/from/{from}/to/{to}")
	public CryptoDto getExchange(@PathVariable String from, @PathVariable String to);
}
