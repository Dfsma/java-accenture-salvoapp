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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

			Player player1 = new Player("Diego", "diego@gmail.com");
			Player player2 = new Player("Juan", "juan@gmail.com");
			Player player3 = new Player("Erika", "erika@gmail.com" );
			Player player4 = new Player("Emilia","emilia@gmail.com");


			LocalDateTime date = LocalDateTime.now();
			Game game1 = new Game(date);

			/*Local Data + 1 hrs (game2)*/
			date = LocalDateTime.from(date.plusSeconds(3600));
			Game game2 = new Game(date);
			date = LocalDateTime.from(date.plusSeconds(3600));
			Game game3 = new Game(date);



			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1);

			GamePlayer gamePlayer3 = new GamePlayer(player3, game2);
			GamePlayer gamePlayer4 = new GamePlayer(player4, game2);

			GamePlayer gamePlayer5 = new GamePlayer(player1, game3);
			GamePlayer gamePlayer6 = new GamePlayer(player4, game3);



			repoPlayer.saveAll(Arrays.asList(player1,player2,player3,player4));
			repoGame.saveAll(Arrays.asList(game1,game2, game3));
			repoGamePlayer.saveAll(Arrays.asList(gamePlayer1,gamePlayer2, gamePlayer3, gamePlayer4, gamePlayer5, gamePlayer6));


		};
	}

}
