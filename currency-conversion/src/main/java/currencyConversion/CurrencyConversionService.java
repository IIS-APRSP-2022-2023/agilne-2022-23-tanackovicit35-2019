package currencyConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import currencyConversionDtos.BankAccountDto;
import currencyConversionDtos.CurrencyDto;
import currencyConversionDtos.ResponseDto;

@Service
public class CurrencyConversionService {
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@Autowired
	private BankAccountProxy bankAccountProxy;

	public ResponseDto<String, BankAccountDto> getConversion(String from, String to, BigDecimal quantity, String auth) {
		CurrencyDto exchangeRate = currencyExchangeProxy.getExchange(from, to);

		if (exchangeRate == null) {
			return new ResponseDto<String, BankAccountDto>("There was a problem.",
					null);
		}

		BankAccountDto userAccount = bankAccountProxy.getUserAccount(auth);

		if (!checkBalance(userAccount, from.toUpperCase(), quantity)) {
			return new ResponseDto<String, BankAccountDto>("Not enough", null);
		}

		userAccount = updateBalance(userAccount, exchangeRate, quantity);

		ResponseEntity<?> response = bankAccountProxy.updateUserAccount(userAccount);

		if (response.getStatusCode() == HttpStatus.OK) {
			return new ResponseDto<String, BankAccountDto>(
					"Conversion and transfer successful for the amount of " + quantity + " " + from + " to "
							+ quantity.multiply(exchangeRate.getConversionMultiple()) + " " + to,
					userAccount);
		} else {
			return new ResponseDto<String, BankAccountDto>("Failed to update bank account.", null);
		}

	}

	private boolean isBalanceGreaterThan(BigDecimal balance, BigDecimal quantity) {
		return balance.compareTo(quantity) >= 0;
	}

	private boolean checkBalance(BankAccountDto userAccount, String from, BigDecimal quantity) {
		switch (from) {
		case "RSD":
			return isBalanceGreaterThan(userAccount.getRsd(), quantity);
		case "EUR":
			return isBalanceGreaterThan(userAccount.getEur(), quantity);
		case "USD":
			return isBalanceGreaterThan(userAccount.getUsd(), quantity);
		case "GBP":
			return isBalanceGreaterThan(userAccount.getGbp(), quantity);
		case "CHF":
			return isBalanceGreaterThan(userAccount.getChf(), quantity);
		default:
			return false;
		}
	}

	private BankAccountDto updateBalance(BankAccountDto userAccount, CurrencyDto exchangeRate,
			BigDecimal quantity) {
		BigDecimal amount = quantity.multiply(exchangeRate.getConversionMultiple());
		;

		switch (exchangeRate.getFrom()) {
		case "RSD":

			switch (exchangeRate.getTo()) {
			case "EUR":
				userAccount.setRsd(userAccount.getRsd().subtract(quantity));
				userAccount.setEur(userAccount.getEur().add(amount));

				break;
			case "USD":
				userAccount.setRsd(userAccount.getRsd().subtract(quantity));
				userAccount.setUsd(userAccount.getUsd().add(amount));

				break;
			case "GBP":
				userAccount.setRsd(userAccount.getRsd().subtract(quantity));
				userAccount.setGbp(userAccount.getGbp().add(amount));

				break;
			case "CHF":
				userAccount.setRsd(userAccount.getRsd().subtract(quantity));
				userAccount.setChf(userAccount.getChf().add(amount));

				break;
			}

			break;
		case "EUR":

			switch (exchangeRate.getTo()) {
			case "RSD":
				userAccount.setEur(userAccount.getEur().subtract(quantity));
				userAccount.setRsd(userAccount.getRsd().add(amount));

				break;
			case "USD":
				userAccount.setEur(userAccount.getEur().subtract(quantity));
				userAccount.setUsd(userAccount.getUsd().add(amount));

				break;
			case "GBP":
				userAccount.setEur(userAccount.getEur().subtract(quantity));
				userAccount.setGbp(userAccount.getGbp().add(amount));

				break;
			case "CHF":
				userAccount.setEur(userAccount.getEur().subtract(quantity));
				userAccount.setChf(userAccount.getChf().add(amount));

				break;
			}

			break;
		case "USD":

			switch (exchangeRate.getTo()) {
			case "EUR":
				userAccount.setUsd(userAccount.getUsd().subtract(quantity));
				userAccount.setEur(userAccount.getEur().add(amount));

				break;
			case "RSD":
				userAccount.setUsd(userAccount.getUsd().subtract(quantity));
				userAccount.setRsd(userAccount.getRsd().add(amount));

				break;
			case "GBP":
				userAccount.setUsd(userAccount.getUsd().subtract(quantity));
				userAccount.setGbp(userAccount.getGbp().add(amount));

				break;
			case "CHF":
				userAccount.setUsd(userAccount.getUsd().subtract(quantity));
				userAccount.setChf(userAccount.getChf().add(amount));

				break;
			}

			break;
		case "GBP":
			switch (exchangeRate.getTo()) {
			case "EUR":
				userAccount.setGbp(userAccount.getGbp().subtract(quantity));
				userAccount.setEur(userAccount.getEur().add(amount));

				break;
			case "USD":
				userAccount.setGbp(userAccount.getGbp().subtract(quantity));
				userAccount.setUsd(userAccount.getUsd().add(amount));

				break;
			case "RSD":
				userAccount.setGbp(userAccount.getGbp().subtract(quantity));
				userAccount.setRsd(userAccount.getRsd().add(amount));

				break;
			case "CHF":
				userAccount.setGbp(userAccount.getGbp().subtract(quantity));
				userAccount.setChf(userAccount.getChf().add(amount));

				break;
			}

			break;
		case "CHF":
			switch (exchangeRate.getTo()) {
			case "EUR":
				userAccount.setChf(userAccount.getChf().subtract(quantity));
				userAccount.setEur(userAccount.getEur().add(amount));

				break;
			case "USD":
				userAccount.setChf(userAccount.getChf().subtract(quantity));
				userAccount.setUsd(userAccount.getUsd().add(amount));

				break;
			case "GBP":
				userAccount.setChf(userAccount.getChf().subtract(quantity));
				userAccount.setGbp(userAccount.getGbp().add(amount));

				break;
			case "RSD":
				userAccount.setChf(userAccount.getChf().subtract(quantity));
				userAccount.setRsd(userAccount.getRsd().add(amount));

				break;
			}

			break;
		}

		return userAccount;
	}
}
