package com.dfsma.salvo;

import com.dfsma.salvo.models.*;
import com.dfsma.salvo.repositories.*;
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
	public CommandLineRunner initData(PlayerRepository repoPlayer, GamePlayerRepository repoGamePlayer, GameRepository repoGame, ShipRepository repoShip, SalvoRepository repoSalvo){
		return (args) -> {

			Player player1 = new Player("Diego", "diego@gmail.com");
			Player player2 = new Player("Juan", "juan@gmail.com");
			Player player3 = new Player("Erika", "erika@gmail.com" );
			Player player4 = new Player("Emilia","emilia@gmail.com");


			LocalDateTime date = LocalDateTime.now();
			Game game1 = new Game(date);
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

			/*Game 1 -> Player1 Ships*/
			Ship ship1 = new Ship("destroyer", gamePlayer1, Arrays.asList("H1","H2","H3"));
			Ship ship2 = new Ship("Submarine", gamePlayer1, Arrays.asList("E1","F1","G1"));
			Ship ship3 = new Ship("Patrol Boat", gamePlayer1, Arrays.asList("B4","B5"));
			/*Game 2 -> Player2 Ships*/
			Ship ship4 = new Ship("destroyer", gamePlayer2, Arrays.asList("H3","H4","H5"));
			Ship ship5 = new Ship("Submarine", gamePlayer2, Arrays.asList("E4","F4","G4"));
			Ship ship6 = new Ship("Patrol Boat", gamePlayer2, Arrays.asList("B6","B7"));

			/*Game 1 -> Player1 & Player2 Salvos*/
			Salvo salvo1 = new Salvo(1, gamePlayer1, Arrays.asList("H4","H5","H6"));
			Salvo salvo2 = new Salvo(2, gamePlayer1, Arrays.asList("H5","A2"));

			Salvo salvo3 = new Salvo(1, gamePlayer2, Arrays.asList("B6","B7","B8"));
			Salvo salvo4 = new Salvo(2, gamePlayer2, Arrays.asList("H1","H2"));

			repoPlayer.saveAll(Arrays.asList(player1,player2,player3,player4));
			repoGame.saveAll(Arrays.asList(game1,game2, game3));
			repoGamePlayer.saveAll(Arrays.asList(gamePlayer1,gamePlayer2, gamePlayer3, gamePlayer4, gamePlayer5, gamePlayer6));
			repoShip.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5,ship6));
			repoSalvo.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4));


		};
	}

}
