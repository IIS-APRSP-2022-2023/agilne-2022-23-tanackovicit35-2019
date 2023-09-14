package tradeService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeServiceController {
	@Autowired
	TradeServiceService service;
	
	@GetMapping("/trade-service/from/{from}/to/{to}/user/{email}/quantity/{quantity}")
	private ResponseEntity<Object> trade(@PathVariable String from, @PathVariable String to, @PathVariable String email, @PathVariable BigDecimal quantity) {
		Object temp = service.trade(from, to, quantity, email);
		
		return ResponseEntity.ok(temp);
	}
}
