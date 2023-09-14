package tradeService;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tradeServiceDtos.BankAccountDto;
import tradeServiceDtos.CryptoWalletDto;
import tradeServiceDtos.CurrencyExchangeDto;

@Service
public class TradeServiceService {
	@Autowired
	TradeServiceRepository repo;
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	public Object trade(String from, String to, BigDecimal quantity, String email) {
		double recieverTotal = 0;
		double senderTotal = 0;
	
		if (from.toLowerCase().equals("btc") || from.toLowerCase().equals("eth") || from.toLowerCase().equals("bnb")) {
		CryptoWalletDto wallet = cryptoWalletProxy.getWallet(email);
		if (wallet == null) {
			return "No crypto wallet linked to that email.";
		}

		try {
			switch (from.toLowerCase()) {
			case "btc":
				if (wallet.getBtc().compareTo(quantity) < 0) {
					throw new RuntimeException("BTC");
				}
				break;
			case "eth":
				if (wallet.getEth().compareTo(quantity) < 0) {
					throw new RuntimeException("ETH");
				}
				break;
			case "bnb":
				if (wallet.getBnb().compareTo(quantity) < 0) {
					throw new RuntimeException("BNB");
				}
				break;
			default:
				return "Not supported currency.";

			}
		} catch (Exception e) {
			return "Not enough " + e.getMessage();
		}

		senderTotal = senderTotal - quantity.doubleValue();

		if (to.toLowerCase().equals("eur") || to.toLowerCase().equals("usd")) {
			TradeService temp = repo.findByExchangeFromAndExchangeTo(from, to);

			recieverTotal = recieverTotal + quantity.multiply(temp.getMultiplier()).doubleValue();
		} else if (to.toLowerCase().equals("chf") || to.toLowerCase().equals("gbp")
				|| to.toLowerCase().equals("rsd")) {

			TradeService temp = repo.findByExchangeFromAndExchangeTo(from, "EUR");

			CurrencyExchangeDto ceTemp = currencyExchangeProxy.getExchange("EUR", to.toUpperCase());

			recieverTotal = recieverTotal + quantity.multiply(temp.getMultiplier())
					.multiply(ceTemp.getConversionMultiple()).doubleValue();
		}

		cryptoWalletProxy.updateOne(email, from, new BigDecimal(senderTotal).setScale(5, RoundingMode.HALF_UP));

		BankAccountDto bankFinal = bankAccountProxy.updateOne(email, to,
				new BigDecimal(recieverTotal).setScale(5, RoundingMode.HALF_UP));

		return bankFinal;
	} else if (from.toLowerCase().equals("eur") || from.toLowerCase().equals("usd")) {
		BankAccountDto temp = bankAccountProxy.getBankAccount(email);
		if (temp == null) {
			return "Bank account not found.";
		}

		try {
			switch (from.toLowerCase()) {
			case "eur":
				if (temp.getEur().compareTo(quantity) < 0) {
					throw new RuntimeException("EUR");
				}
				break;
			case "usd":
				if (temp.getUsd().compareTo(quantity) < 0) {
					throw new RuntimeException("USD");
				}
				break;
			}
		} catch (Exception e) {
			return "Not enough " + e.getMessage();
		}

		senderTotal = senderTotal - quantity.doubleValue();

		TradeService cryptoExchange = repo.findByExchangeFromAndExchangeTo(from, to);

		recieverTotal = recieverTotal + quantity.multiply(cryptoExchange.getMultiplier()).doubleValue();

		CryptoWalletDto walletFinal = cryptoWalletProxy.updateOne(email, to, new BigDecimal(recieverTotal));

		bankAccountProxy.updateOne(email, from, new BigDecimal(senderTotal));

		return walletFinal;
	} else if (from.toLowerCase().equals("chf") || from.toLowerCase().equals("gbp")
			|| from.toLowerCase().equals("rsd")) {
		BankAccountDto temp = bankAccountProxy.getBankAccount(email);
		if (temp == null) {
			return "Bank account not found.";
		}

		try {
			switch (from.toLowerCase()) {
			case "rsd":
				if (temp.getEur().compareTo(quantity) < 0) {
					throw new RuntimeException("RSD");
				}
				break;
			case "gbp":
				if (temp.getUsd().compareTo(quantity) < 0) {
					throw new RuntimeException("GBP");
				}
				break;
			case "chf":
				if (temp.getUsd().compareTo(quantity) < 0) {
					throw new RuntimeException("CHF");
				}
				break;
			}
		} catch (Exception e) {
			return "Not enough " + e.getMessage();
		}

		senderTotal = senderTotal - quantity.doubleValue();

		TradeService cryptoExchange = repo.findByExchangeFromAndExchangeTo("EUR", to.toUpperCase());

		CurrencyExchangeDto ceTemp = currencyExchangeProxy.getExchange(from.toUpperCase(), "EUR");

		recieverTotal = recieverTotal + quantity.multiply(cryptoExchange.getMultiplier())
				.multiply(ceTemp.getConversionMultiple()).doubleValue();

		CryptoWalletDto walletFinal = cryptoWalletProxy.updateOne(email, to, new BigDecimal(recieverTotal));

		bankAccountProxy.updateOne(email, from, new BigDecimal(senderTotal));

		return walletFinal;
	}
	return null;
	}
}

