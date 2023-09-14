package currencyConversionDtos;

public class ResponseDto<S, T> {
	private S message;
	private T bankAccountData;
	
	public ResponseDto(S message, T bankAccountData) {
		this.message = message;
		this.bankAccountData = bankAccountData;
	}

	public S getMessage() {
		return message;
	}

	public T getBankAccountData() {
		return bankAccountData;
	}
	
}
