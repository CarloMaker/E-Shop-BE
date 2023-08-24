package com.xantrix.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.dto.CategoriaDto;
import com.xantrix.webapp.entity.FamAssort;
import com.xantrix.webapp.repository.CategoriaRepository;


@Service
@Transactional
public class CategorieServiceImpl implements CategorieService{

	@Autowired 
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<CategoriaDto> SelTutti() {
		 List<FamAssort> categorie = categoriaRepository.findAll();
		 List<CategoriaDto> lista = categorie.stream().map( t ->  modelMapper.map(t, CategoriaDto.class)).collect(Collectors.toList());
		 return lista;
	}

}
