package com.xantrix.webapp.tests.ControllerTests;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xantrix.webapp.Application;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;


@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class SelectArtTest
{
	private MockMvc mockMvc;
	
	private String tokenJwt = "";
	
	private static final String authServerUrl = "http://localhost:9100/auth";
	private static final String userId = "Nicola";
	private static final String password = "123_Stella";
	
	private final ObjectMapper objectMapper = new ObjectMapper();
		
	@Autowired
	private WebApplicationContext wac;
	
	@BeforeEach
	public void setup() throws JSONException, IOException
	{
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();
		
		if (tokenJwt.length() == 0)
			getTokenFromAuthSrv();
	}
	
	@SneakyThrows
	private void getTokenFromAuthSrv()
	{
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject userJson = new JSONObject();
		userJson.put("username", userId);
		userJson.put("password", password);
		
		HttpEntity<String> request = new HttpEntity<String>(userJson.toString(), headers);
		
		String jsonBody = restTemplate.postForObject(authServerUrl, request, String.class);
		JsonNode root = objectMapper.readTree(jsonBody);
		
		tokenJwt = "Bearer " + root.path("token").asText();
		
	}
	
	String JsonData =  
			"{\r\n"
			+ "    \"codArt\": \"002000301\",\r\n"
			+ "    \"descrizione\": \"ACQUA ULIVETO 15 LT\",\r\n"
			+ "    \"um\": \"PZ\",\r\n"
			+ "    \"codStat\": \"\",\r\n"
			+ "    \"pzCart\": 6,\r\n"
			+ "    \"pesoNetto\": 1.5,\r\n"
			+ "    \"idStatoArt\": \"1\",\r\n"
			+ "    \"dataCreazione\": \"2010-06-14\",\r\n"
			+ "    \"prezzo\": 1.07,\r\n" //<-- Aggiunto Prezzo Listino 1
			+ "    \"barcode\": [\r\n"
			+ "        {\r\n"
			+ "            \"barcode\": \"8008490000021\",\r\n"
			+ "            \"idTipoArt\": \"CP\"\r\n"
			+ "        }\r\n"
			+ "    ],\r\n"
			+ "    \"ingredienti\": null,\r\n"
			+ "    \"famAssort\": {\r\n"
			+ "        \"id\": 1,\r\n"
			+ "        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n"
			+ "    },\r\n"
			+ "    \"iva\": {\r\n"
			+ "        \"idIva\": 22,\r\n"
			+ "        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n"
			+ "        \"aliquota\": 22\r\n"
			+ "    }\r\n"
			+ "}";
	
	@Test
	@Order(1)
	public void listArtByEan() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/8008490000021")
				.header("Authorization", this.tokenJwt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				 //articoli
				.andExpect(jsonPath("$.codArt").exists())
				.andExpect(jsonPath("$.codArt").value("002000301"))
				.andExpect(jsonPath("$.descrizione").exists())
				.andExpect(jsonPath("$.descrizione").value("ACQUA ULIVETO 15 LT"))
				.andExpect(jsonPath("$.um").exists())
				.andExpect(jsonPath("$.um").value("PZ"))
				.andExpect(jsonPath("$.codStat").exists())
				.andExpect(jsonPath("$.codStat").value(""))
				.andExpect(jsonPath("$.pzCart").exists())
				.andExpect(jsonPath("$.pzCart").value("6"))
				.andExpect(jsonPath("$.pesoNetto").exists())
				.andExpect(jsonPath("$.pesoNetto").value("1.5"))
				.andExpect(jsonPath("$.idStatoArt").exists())
				.andExpect(jsonPath("$.idStatoArt").value("1"))
				.andExpect(jsonPath("$.dataCreazione").exists())
				.andExpect(jsonPath("$.dataCreazione").value("2010-06-14"))
				.andExpect(jsonPath("$.prezzo").exists())
				.andExpect(jsonPath("$.prezzo").value("1.07"))
				 //barcode
				.andExpect(jsonPath("$.barcode[0].barcode").exists())
				.andExpect(jsonPath("$.barcode[0].barcode").value("8008490000021")) 
				.andExpect(jsonPath("$.barcode[0].idTipoArt").exists())
				.andExpect(jsonPath("$.barcode[0].idTipoArt").value("CP")) 
				 //famAssort
				.andExpect(jsonPath("$.famAssort.id").exists())
				.andExpect(jsonPath("$.famAssort.id").value("1")) 
				.andExpect(jsonPath("$.famAssort.descrizione").exists())
				.andExpect(jsonPath("$.famAssort.descrizione").value("DROGHERIA ALIMENTARE")) 
				 //ingredienti
				.andExpect(jsonPath("$.ingredienti").isEmpty())
				 //Iva
				.andExpect(jsonPath("$.iva.idIva").exists())
				.andExpect(jsonPath("$.iva.idIva").value("22")) 
				.andExpect(jsonPath("$.iva.descrizione").exists())
				.andExpect(jsonPath("$.iva.descrizione").value("IVA RIVENDITA 22%"))
				.andExpect(jsonPath("$.iva.aliquota").exists())
				.andExpect(jsonPath("$.iva.aliquota").value("22"))	
				
				.andDo(print());
	}
	
	@Test
	@Order(2)
	public void listArtByEanWithIdList() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/8008490000021/2")
				.header("Authorization", this.tokenJwt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.prezzo").exists())
				.andExpect(jsonPath("$.prezzo").value("0.87"))
				.andDo(print());
	}
	
	private String Barcode = "8008490002138";
	
	@Test
	@Order(3)
	public void errlistArtByEan() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/barcode/" + Barcode)
				.header("Authorization", this.tokenJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Il barcode " + Barcode + " non e' stato trovato!"))
				.andDo(print());
	}
	
	@Test
	@Order(3)
	public void listArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/002000301")
				.header("Authorization", this.tokenJwt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
	}
	
	@Test
	@Order(4)
	public void listArtByCodArtWithIdList() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/002000301/2")
				.header("Authorization", this.tokenJwt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.prezzo").exists())
				.andExpect(jsonPath("$.prezzo").value("0.87"))
				.andDo(print());
	}
	
	private String CodArt = "002000301b";
	
	@Test
	@Order(5)
	public void errlistArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/" + CodArt)
				.header("Authorization", this.tokenJwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("L'articolo con codice " + CodArt + " non e' stato trovato!"))
				.andDo(print());
	}
	
	private String JsonData2 = "[" + JsonData + "]";

	@Test
	@Order(6)
	public void listArtByDesc() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/ACQUA ULIVETO 15 LT")
				.header("Authorization", this.tokenJwt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData2)) 
				.andReturn();
	}
	
	@Test
	@Order(7)
	public void listArtByDescWithIdList() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/ACQUA ULIVETO 15 LT/2")
				.header("Authorization", this.tokenJwt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].prezzo").exists())
				.andExpect(jsonPath("$[0].prezzo").value("0.87"))
				.andDo(print());
	}
	
	
}
