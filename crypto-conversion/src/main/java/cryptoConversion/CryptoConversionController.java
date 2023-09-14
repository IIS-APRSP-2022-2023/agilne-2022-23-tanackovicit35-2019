package cryptoConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cryptoConversionDtos.CryptoWalletDto;
import cryptoConversionDtos.ResponseDto;
import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CryptoConversionController {
	@Autowired
	private CryptoConversionService service;

	@RateLimiter(name = "defaultRL", fallbackMethod = "rateLimiterResponse")
	@GetMapping("/crypto-conversion-feign")
	public ResponseDto<String, CryptoWalletDto> getConversionFeign(@RequestParam String from, @RequestParam String to,
			@RequestParam(defaultValue = "10") BigDecimal quantity, @RequestHeader("Authorization") String auth) {
		try {
			return service.getConversion(from, to, quantity, auth);
		} catch (FeignException e) {
			return new ResponseDto<String, CryptoWalletDto>(e.getMessage(), null);
		}
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException e) {
		return ResponseEntity.status(e.getStatusCode()).body(
				"Value [" + e.getParameterType() + "] of parameter [" + e.getParameterName() + "] has been ommited");
	}

	@ExceptionHandler(RequestNotPermitted.class)
	public ResponseEntity<String> rateLimiterResponse(Exception e) {
		return ResponseEntity.status(503).body("Too many requests! The maximum is 2 requests per 45 seconds.");
	}
}
