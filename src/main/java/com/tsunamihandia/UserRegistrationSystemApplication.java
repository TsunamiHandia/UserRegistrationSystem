package com.tsunamihandia;

import com.tsunamihandia.dto.UsersDTO;
import com.tsunamihandia.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserRegistrationSystemApplication {
	private static UserJpaRepository userJpaRepository;

	@Autowired
	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationSystemApplication.class, args);

		loadData();
	}

	private static void loadData() {
		for (int i = 1; i <= 20; i++) {
			userJpaRepository.save(new UsersDTO(
					String.format("Name#%1$s", i),
					String.format("Adress#%1$s", i),
					String.format("Email#%1$s@abc.tk", i)
			));
		}
	}

}
