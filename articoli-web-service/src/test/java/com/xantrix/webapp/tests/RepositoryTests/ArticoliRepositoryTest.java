package com.xantrix.webapp.tests.RepositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;


@SpringBootTest()
@ContextConfiguration(classes = Application.class)
public class ArticoliRepositoryTest
{
	
	@Autowired
	private ArticoliRepository articoliRepository;

	@Test
	public void TestfindByDescrizioneLike()
	{
		List<Articoli> items = articoliRepository.selByDescrizioneLike("ACQUA ULIVETO%");
		assertEquals(2, items.size());
	}
	
	@Test
	public void TestfindByDescrizioneLikePage()
	{
		List<Articoli> items = articoliRepository.findByDescrizioneLike("ACQUA%",PageRequest.of(0, 10));
		assertEquals(10, items.size());
	}

	
	@Test
	public void TestfindByCodArt() throws Exception
	{
		String descrizione = articoliRepository.findByCodArt("002000301").getDescrizione();
		assertEquals(descrizione ,"ACQUA ULIVETO 15 LT");
				
	}
	

}
