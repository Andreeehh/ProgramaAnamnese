package br.com.programa.anamnese;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class AnamneseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnamneseApplication.class, args);
	}

}
