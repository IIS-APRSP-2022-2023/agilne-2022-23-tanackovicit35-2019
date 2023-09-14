package bankAccount;

import org.springframework.data.jpa.repository.JpaRepository;

import bankAccount.model.CustomBankAccount;

public interface BankAccountRepo extends JpaRepository<CustomBankAccount, Long> {
	CustomBankAccount findByEmail(String email);
}
