package com.xantrix.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.dto.ArticoliDto;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;

@Service
@Transactional
public class ArticoliServiceImpl implements ArticoliService {
	
	@Autowired
	ArticoliRepository articoliRepository;
	
	@Autowired
	ModelMapper modelMapper;
	

	@Override
	public Iterable<Articoli> SelTutti()
	{
		return articoliRepository.findAll();
	}

	@Override
	public List<Articoli> SelByDescrizione(String descrizione, Pageable pageable)
	{
		return articoliRepository.findByDescrizioneLike(descrizione, pageable);
	}

	@Override
	public List<ArticoliDto> SelByDescrizione(String descrizione)
	{
		String fiter ="%" + descrizione.toUpperCase() + "%";
		 List<Articoli> articoli = articoliRepository.selByDescrizioneLike(fiter);
		 List<ArticoliDto> listaOutput = convertToDto(articoli);
		 
		 return listaOutput;
		 
		 
	}

	
	
	@Override
	public ArticoliDto SelByCodArt(String codArt)
	{
		
		Articoli articoli = articoliRepository.findByCodArt(codArt);
		ArticoliDto articoliDto = convertToDto(articoli);
		return articoliDto;		
		
	}

	@Override
	@Transactional
	public void DelArticolo(ArticoliDto articolo)
	{
		Articoli art = modelMapper.map(articolo, Articoli.class);
		articoliRepository.delete(art);
	}

	@Override
	@Transactional
	public void InsArticolo(Articoli articolo)
	{
		articolo.setDescrizione(articolo.getDescrizione().toUpperCase());
		articoliRepository.save(articolo);
	}

	@Override
	public ArticoliDto SelByBarcode(String barcode) {
		
		Articoli articoli = articoliRepository.selByEan(barcode);
		ArticoliDto articoliDto = convertToDto(articoli);
		return articoliDto; 
	}

	
	
	private List<ArticoliDto> convertToDto(List<Articoli> articoli) {
		/*articoli.forEach( e -> {
			 e.setIdStatoArt(e.getIdStatoArt().trim());
			 e.setUm(e.getUm().trim());
			 e.setDescrizione(e.getDescrizione().trim());
		 });*/
		 
		 List<ArticoliDto> listaOutput = articoli.stream().map( t ->  modelMapper.map(t, ArticoliDto.class))
				
				 .collect(Collectors.toList());
		return listaOutput;
	}
	
	private ArticoliDto convertToDto(Articoli articoli) {
		ArticoliDto articoliDto = null;
		if(articoli != null) {
			articoliDto = modelMapper.map(articoli, ArticoliDto.class);
		
		}
		return articoliDto;
	}
	
}
