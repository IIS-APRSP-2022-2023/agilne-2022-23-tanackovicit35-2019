package cryptoConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cryptoConversionDtos.CryptoDto;
import cryptoConversionDtos.CryptoWalletDto;
import cryptoConversionDtos.ResponseDto;

@Service
public class CryptoConversionService {
	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	public ResponseDto<String, CryptoWalletDto> getConversion(String from, String to, BigDecimal quantity,
			String auth) {
		CryptoDto excRate = cryptoExchangeProxy.getExchange(from, to);
		if (excRate == null) {
			return new ResponseDto<String, CryptoWalletDto>("A problem occurred!", null);
		}
		CryptoWalletDto account = cryptoWalletProxy.getUserAccount(auth);

		if (!checkBalance(account, from.toUpperCase(), quantity)) {
			return new ResponseDto<String, CryptoWalletDto>("There is not enough assets!", null);
		}
		account = updateBalance(account, excRate, quantity);
		ResponseEntity<?> response = cryptoWalletProxy.updateUserAccount(account);

		if (response.getStatusCode() == HttpStatus.OK) {
			return new ResponseDto<String, CryptoWalletDto>("Successfull conversion and transfer for the amount "
					+ quantity + " " + from + " to " + quantity.multiply(excRate.getCryptoMultiple()) + " " + to,
					account);
		} else {
			return new ResponseDto<String, CryptoWalletDto>("Failed to update crypto wallet!", null);
		}
	}

	private boolean isBalanceGreaterThan(BigDecimal balance, BigDecimal quantity) {
		return balance.compareTo(quantity) >= 0;
	}

	private boolean checkBalance(CryptoWalletDto account, String from, BigDecimal quantity) {
		switch (from) {
		case "ETH":
			return isBalanceGreaterThan(account.getETH(), quantity);
		case "BTC":
			return isBalanceGreaterThan(account.getBTC(), quantity);
		case "BNB":
			return isBalanceGreaterThan(account.getBNB(), quantity);
		default:
			return false;
		}
	}

	private CryptoWalletDto updateBalance(CryptoWalletDto account, CryptoDto excRate, BigDecimal quantity) {
		BigDecimal amount = quantity.multiply(excRate.getCryptoMultiple());

		switch (excRate.getFrom()) {
		case "ETH":
			switch (excRate.getTo()) {
			case "BTC":
				account.setETH(account.getETH().subtract(quantity));
				account.setBTC(account.getBTC().add(amount));
				break;
			case "BNB":
				account.setETH(account.getETH().subtract(quantity));
				account.setBNB(account.getBNB().add(amount));
				break;
			}
			break;
		case "BTC":
			switch (excRate.getTo()) {
			case "ETH":
				account.setBTC(account.getBTC().subtract(quantity));
				account.setETH(account.getETH().add(amount));
				break;
			case "BNB":
				account.setBTC(account.getBTC().subtract(quantity));
				account.setBNB(account.getBNB().add(amount));
				break;
			}
			break;
		case "BNB":
			switch (excRate.getTo()) {
			case "ETH":
				account.setBNB(account.getBNB().subtract(quantity));
				account.setETH(account.getETH().add(amount));
				break;
			case "BTC":
				account.setBNB(account.getBNB().subtract(quantity));
				account.setBTC(account.getBTC().add(amount));
				break;
			}
			break;
		}
		return account;
	}
}
