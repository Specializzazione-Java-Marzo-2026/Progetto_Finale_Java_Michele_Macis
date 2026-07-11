package it.aulab.progetto_finale_michele_macis;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.aulab.progetto_finale_michele_macis.config.LegacyPasswordEncoder;

@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement
@SpringBootApplication
public class ProgettoFinaleMicheleMacisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoFinaleMicheleMacisApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new LegacyPasswordEncoder();
	}

	@Bean
	public ModelMapper istanceModelMapper() {
		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
}
