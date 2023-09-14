package cryptoConversionDtos;

public class ResponseDto<S, T> {
	private S message;
	private T cryptoWalletData;

	public ResponseDto(S message, T cryptoWalletData) {
		this.message = message;
		this.cryptoWalletData = cryptoWalletData;
	}

	public S getMessage() {
		return message;
	}

	public T getCryptoWalletData() {
		return cryptoWalletData;
	}

}
