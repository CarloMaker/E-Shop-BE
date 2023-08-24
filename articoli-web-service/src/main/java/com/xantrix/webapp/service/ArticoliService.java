package com.xantrix.webapp.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.xantrix.webapp.dto.ArticoliDto;
import com.xantrix.webapp.entity.Articoli;

public interface ArticoliService {

	
	public Iterable<Articoli> SelTutti();
	
	public List<ArticoliDto> SelByDescrizione(String descrizione);
		
	public List<Articoli> SelByDescrizione(String descrizione, Pageable pageable);
	
	public ArticoliDto SelByCodArt(String codArt);
	
	public ArticoliDto SelByBarcode(String barcode);
	
	public void DelArticolo(ArticoliDto articolo);
	
	public void InsArticolo(Articoli articolo);

}
