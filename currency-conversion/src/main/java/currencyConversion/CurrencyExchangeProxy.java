package currencyConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import currencyConversionDtos.CurrencyDto;

@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyDto getExchange(@PathVariable String from, @PathVariable String to);
}

