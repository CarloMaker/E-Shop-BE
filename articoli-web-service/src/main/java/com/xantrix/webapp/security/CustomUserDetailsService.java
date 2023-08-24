package com.xantrix.webapp.security;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.java.Log;

@Service("customUserDetailsService")
@Log
public class CustomUserDetailsService implements UserDetailsService {
	

	@Autowired
	UserConfig userConfig;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String errMsg = "";
		if (username == null || username.length() < 2) {
			errMsg = "Nome utente non valido";
			log.warning(errMsg);
			throw new UsernameNotFoundException(errMsg);
		}
		Optional<Utenti> utente = GetHttpValue(username);
		if(utente.isEmpty()) {
			errMsg = String.format("utente %s non trovato",username);
			log.warning(errMsg);
			throw new UsernameNotFoundException(errMsg);
		}
		//aggiungiamo l'utente restuito dal servizio come utente 
		UserBuilder builder = null;
		Utenti utenti = utente.get();
		builder = User.withUsername(utenti.getUserId());
		builder.disabled(utenti.getAttivo().equals("Si") ? false : true);
		builder.password(utenti.getPassword());
		
		String[] profili = utenti.getRuoli().stream().map(  t -> "ROLE_"+ t) .toArray(String[]::new); // toArray(size -> new String[size])
		builder.authorities(profili);
		return builder.build();

	}
	
	
	private Optional<Utenti> GetHttpValue(String userId) {
		URI url = null;
		Utenti utente = null;
		try {
			String srvUlr = userConfig.getSrvUrl();
			url = new URI(srvUlr+userId);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userConfig.getUserId(), userConfig.getPassword()));
			utente = restTemplate.getForObject(url, Utenti.class);
		} catch (Exception e) {
			log.warning(e.getMessage());
		}
		return Optional.of(utente);
	}

	public static void main(String[] args) {
		List<String> lista = Arrays.asList("USER","ADMIN");
		
		String[] out= lista.stream().map(t -> "_" + t).toArray(   String[]::new);
		System.out.println(out);
	}
}
