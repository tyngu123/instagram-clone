package br.com.yago.socialmedia_api;

import br.com.yago.socialmedia_api.model.Role;
import br.com.yago.socialmedia_api.model.RoleName;
import br.com.yago.socialmedia_api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialmediaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialmediaApiApplication.class, args);
	}


	@Bean
	CommandLineRunner run(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
				roleRepository.save(new Role(RoleName.ROLE_USER));
			}
			if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
				roleRepository.save(new Role(RoleName.ROLE_ADMIN));
			}
		};
	}
}