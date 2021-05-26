package com.dfsma.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
		System.out.println("<--------------Server Up-------------->");
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository){
		return (args) -> {
			repository.save(new Player("Diego", "diegof.df58@gmail.com"));
			repository.save(new Player("Juan", "juan@gmail.com"));
		};
	}

}
