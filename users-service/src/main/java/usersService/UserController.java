package usersService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usersDtos.AdminUserCreateDto;
import usersDtos.CreateUserDto;
import usersDtos.UpdateUserDto;
import usersDtos.UserDto;
import usersService.model.CustomUser;

@RestController
@RequestMapping("users-service/users")
public class UserController {

	@Autowired
	private CustomUserRepository repo;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	
	@GetMapping
	public List<CustomUser> getAllUsers() {
		List<CustomUser> users = repo.findAll();

		return users;

	}
	
	@PostMapping("/addUser")
	public ResponseEntity<?> createUser(@RequestBody CreateUserDto newUserDto) {
		CustomUser user = repo.findByEmail(newUserDto.getEmail());

		if (user != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists in the database");
		} else {

			if (newUserDto.getEmail() == null || newUserDto.getPassword() == null) {
				return ResponseEntity.status(400).body("Email and password are required.");
			}

			Long userId = ThreadLocalRandom.current().nextLong(4, 101);

			CustomUser newUser = new CustomUser();

			newUser.setId(userId);
			newUser.setEmail(newUserDto.getEmail());
			newUser.setPassword(newUserDto.getPassword());
			newUser.setRole("USER");

			repo.save(newUser);
			ResponseEntity<?> bankAccountResponse = bankAccountProxy.addUserBankAccount(newUserDto.getEmail());
			ResponseEntity<?> cryptoWalletResponse = cryptoWalletProxy.addUserCryptoWallet(newUserDto.getEmail());

			if (bankAccountResponse.getStatusCode() == HttpStatus.OK && cryptoWalletResponse.getStatusCode() == HttpStatus.OK) {
				Map<String, Object> responseBody = new HashMap<>();
				responseBody.put("message", "User, his new bank account and crypto wallet have been created.");
				responseBody.put("user", mapperUserDto(newUser));

				return ResponseEntity.status(201).body(responseBody);
			} else {
				return ResponseEntity.status(500).body("Something went wrong.");
			}
		}
	}

	@PostMapping("/addAdminUser")
	public ResponseEntity<?> createUserOrAdmin(@RequestBody AdminUserCreateDto newUserAdminDto) {
		CustomUser user = repo.findByEmail(newUserAdminDto.getEmail());

		if (user != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
		} else {

			if (newUserAdminDto.getEmail() == null || newUserAdminDto.getPassword() == null
					|| newUserAdminDto.getRole() == null) {
				return ResponseEntity.status(400).body("Email, password and role are required.");
			}

			if (newUserAdminDto.getRole().toString().equals("OWNER")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is already a OWNER in the database.");
			} else if (newUserAdminDto.getRole().toString().equals("USER")) {
				Long userId = ThreadLocalRandom.current().nextLong(5, 101);

				CustomUser newUser = new CustomUser();

				newUser.setId(userId);
				newUser.setEmail(newUserAdminDto.getEmail());
				newUser.setPassword(newUserAdminDto.getPassword());
				newUser.setRole(newUserAdminDto.getRole());

				repo.save(newUser);

				ResponseEntity<?> bankAccountResponse = bankAccountProxy.addUserBankAccount(newUserAdminDto.getEmail());
				ResponseEntity<?> cryptoWalletResponse = cryptoWalletProxy.addUserCryptoWallet(newUserAdminDto.getEmail());

				if (bankAccountResponse.getStatusCode() == HttpStatus.OK && cryptoWalletResponse.getStatusCode() == HttpStatus.OK) {
					Map<String, Object> responseBody = new HashMap<>();
					responseBody.put("message", "User, his new bank account and crypto wallet have been created.");
					responseBody.put("user", mapperUserDto(newUser));

					return ResponseEntity.status(201).body(responseBody);
				} else {
					return ResponseEntity.status(500).body("Something went wrong.");
				}
			} else if (newUserAdminDto.getRole().toString().equals("ADMIN")) {
				Long userId = ThreadLocalRandom.current().nextLong(7, 101);

				CustomUser newUser = new CustomUser();

				newUser.setId(userId);
				newUser.setEmail(newUserAdminDto.getEmail());
				newUser.setPassword(newUserAdminDto.getPassword());
				newUser.setRole(newUserAdminDto.getRole());

				repo.save(newUser);

				Map<String, Object> responseBody = new HashMap<>();
				responseBody.put("message", "Admin has been created.");
				responseBody.put("user", mapperUserDto(newUser));

				return ResponseEntity.status(201).body(responseBody);
			} else {
				return ResponseEntity.status(400).body("Role must be ADMIN or USER.");
			}
		}
	}

	@PutMapping("/updateUser/{email}")
	public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto updateUserDto,
			@PathVariable("email") String emailToChange) {
		CustomUser user = repo.findByEmail(emailToChange);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with such email found in the database");
		} else {

			if (updateUserDto.getEmail() == null || updateUserDto.getPassword() == null) {
				return ResponseEntity.status(400).body("Email and password are required.");
			}

			if (!user.getRole().toString().equals("USER")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin can only update data about USER.");
			} else {
				user.setEmail(updateUserDto.getEmail());
				user.setPassword(updateUserDto.getPassword());

				jdbcTemplate.update("UPDATE custom_user SET email = ?, password = ? WHERE id = ?", user.getEmail(),
						user.getPassword(), user.getId());

				ResponseEntity<?> bankAccountResponse = bankAccountProxy.updateUserEmail(updateUserDto.getEmail(), emailToChange);
				ResponseEntity<?> cryptoWalletResponse = cryptoWalletProxy.updateUserEmail(updateUserDto.getEmail(), emailToChange);

				if (bankAccountResponse.getStatusCode() == HttpStatus.OK && cryptoWalletResponse.getStatusCode() == HttpStatus.OK) {
					Map<String, Object> responseBody = new HashMap<>();
					responseBody.put("message", "User data, bank data and crypto wallet data updated.");
					responseBody.put("user", mapperUserUpdateDto(user));

					return ResponseEntity.status(200).body(responseBody);

				} else {
					return ResponseEntity.status(500)
							.body("Something went wrong.");
				}
			}
		}
	}

	@PutMapping("/update/{email}")
	public ResponseEntity<?> updateAnyone(@RequestBody UpdateUserDto updateDto,
			@PathVariable("email") String emailToChange) {
		CustomUser user = repo.findByEmail(emailToChange);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with inserted email.");
		} else {

			if (updateDto.getEmail() == null || updateDto.getPassword() == null) {
				return ResponseEntity.status(400).body("Email and password are required.");
			}

			if (user.getRole().toString().equals("USER")) {
				user.setEmail(updateDto.getEmail());
				user.setPassword(updateDto.getPassword());

				jdbcTemplate.update("UPDATE custom_user SET email = ?, password = ? WHERE id = ?", user.getEmail(),
						user.getPassword(), user.getId());

				ResponseEntity<?> bankAccountResponse = bankAccountProxy.updateUserEmail(updateDto.getEmail(), emailToChange);
				ResponseEntity<?> cryptoWalletResponse = cryptoWalletProxy.updateUserEmail(updateDto.getEmail(), emailToChange);

				if (bankAccountResponse.getStatusCode() == HttpStatus.OK && cryptoWalletResponse.getStatusCode() == HttpStatus.OK) {
					Map<String, Object> responseBody = new HashMap<>();
					responseBody.put("message", "User data is updated!");
					responseBody.put("user", mapperUserUpdateDto(user));

					return ResponseEntity.status(201).body(responseBody);

				} else {
					return ResponseEntity.status(500).body("Something went wrong.");
				}

			} else {
				user.setEmail(updateDto.getEmail());
				user.setPassword(updateDto.getPassword());

				jdbcTemplate.update("UPDATE custom_user SET email = ?, password = ? WHERE id = ?", user.getEmail(),
						user.getPassword(), user.getId());

				Map<String, Object> responseBody = new HashMap<>();
				responseBody.put("message", "Data updated.");
				responseBody.put("user", mapperUserUpdateDto(user));

				return ResponseEntity.status(200).body(responseBody);

			}
		}
	}

	@DeleteMapping("/removeUser/{email}")
	public ResponseEntity<?> removeUser(@PathVariable("email") String email) {
		CustomUser user = repo.findByEmail(email);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with inserted email.");
		} else {
			String role = user.getRole();

			Long userId = user.getId();

			if (role.toString().equals("USER")) {
				repo.deleteById(userId);

				ResponseEntity<?> bankAccountResponse = bankAccountProxy.removeUserBankAccount(email);
				ResponseEntity<?> cryptoWalletResponse = cryptoWalletProxy.removeUserCryptoWallet(email);

				if (bankAccountResponse.getStatusCode() == HttpStatus.OK && cryptoWalletResponse.getStatusCode() == HttpStatus.OK) {
					return ResponseEntity.status(200).body("User, his bank account and crypto wallet are deleted.");
				} else {
					return ResponseEntity.status(500).body("Something went wrong.");
				}
			} else {
				repo.deleteById(userId);

				return ResponseEntity.status(200).body(role + " is deleted.");
			}
		}
	}

	private UserDto mapperUserDto(CustomUser entity) {
		return new UserDto(entity.getEmail(), entity.getPassword(), entity.getRole());
	}

	private UpdateUserDto mapperUserUpdateDto(CustomUser entity) {
		return new UpdateUserDto(entity.getEmail(), entity.getPassword());
	}

 }
