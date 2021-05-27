package com.dfsma.salvo;

import com.dfsma.salvo.models.Game;
import com.dfsma.salvo.models.GamePlayer;
import com.dfsma.salvo.models.Player;
import com.dfsma.salvo.repositories.GamePlayerRepository;
import com.dfsma.salvo.repositories.GameRepository;
import com.dfsma.salvo.repositories.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
		System.out.println("<--------------Server Up-------------->");
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repoPlayer, GamePlayerRepository repoGamePlayer, GameRepository repoGame){
		return (args) -> {


			Date date = new Date(System.currentTimeMillis());

			/*

			Player player1 = new Player("Diego", "diegof.df58@gmail.com");
			Player player2 = new Player("Juan", "juan@gmail.com");
			Player player3 = new Player("Erika", "erika@gmail.com" );
			Player player4 = new Player("Emilia","emilia@gmail.com");
			repoPlayer.save(player1);
			repoPlayer.save(player2);
			repoPlayer.save(player3);
			repoPlayer.save(player4);


			Game game1 = new Game(date);
			repoGame.save(game1);

			GamePlayer gmp1 = new GamePlayer(game1, player1);
			GamePlayer gmp2 = new GamePlayer(game1, player2);
			repoGamePlayer.save(gmp1);
			repoGamePlayer.save(gmp2);
			*/

			Player P1 = new Player("Angela", "angie@proyecto.acc");
			Player P2 = new Player("Brian", "brian@proyecto.acc");
			Player P3 = new Player("Carlos", "charles@proyecto.acc");
			Player P4 = new Player("Daniela", "dani@proyecto.acc");

			Date current = new Date();

			Game G1 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));
			Game G2 = new Game(current); current = Date.from(current.toInstant().plusSeconds(3600));


			GamePlayer GP1 = new GamePlayer(G1, P1);
			GamePlayer GP2 = new GamePlayer(G1, P2);

			repoPlayer.saveAll(Arrays.asList(P1,P2,P3,P4));
			repoGame.saveAll(Arrays.asList(G1,G2));
			repoGamePlayer.saveAll(Arrays.asList(GP1,GP2));



		};
	}

}
