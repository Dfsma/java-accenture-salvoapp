package com.dfsma.salvo;

import com.dfsma.salvo.models.*;
import com.dfsma.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
		System.out.println("<--------------Server Up-------------->");
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repoPlayer,
									  GamePlayerRepository repoGamePlayer,
									  GameRepository repoGame,
									  ShipRepository repoShip,
									  SalvoRepository repoSalvo,
									  ScoreRepository repoScore ){
		return (args) -> {

			Player player1 = new Player("Diego", "diego@gmail.com", passwordEncoder().encode("diego123"));
			Player player2 = new Player("Juan", "juan@gmail.com", passwordEncoder().encode("juan123"));
			Player player3 = new Player("Erika", "erika@gmail.com", passwordEncoder().encode("erika123"));
			Player player4 = new Player("Emilia","emilia@gmail.com", passwordEncoder().encode("emilia123"));


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

			Score score1 = gamePlayer1.getScore(1);
			Score score2 = gamePlayer2.getScore(1);

			Score score3 = gamePlayer3.getScore(1);
			Score score4 = gamePlayer4.getScore(0);

			Score score5 = gamePlayer5.getScore(0.5);


			repoPlayer.saveAll(Arrays.asList(player1,player2,player3,player4));
			repoGame.saveAll(Arrays.asList(game1,game2, game3));
			repoGamePlayer.saveAll(Arrays.asList(gamePlayer1,gamePlayer2, gamePlayer3, gamePlayer4, gamePlayer5, gamePlayer6));
			repoShip.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5,ship6));
			repoSalvo.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4));
			repoScore.saveAll(Arrays.asList(score1, score2, score3, score4, score5));


		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByEmail(inputName);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/web/**").permitAll();

		http.formLogin()
				.usernameParameter("email")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");



		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

	}
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}




