package com.xantrix.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.dto.IvaDto;
import com.xantrix.webapp.entity.Iva;
import com.xantrix.webapp.repository.IvaRepository;


@Service
@Transactional
public class IvaServiceImpl implements IvaService{

	@Autowired  
	IvaRepository ivaRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<IvaDto> selTutti() {
		List<Iva> findAll = ivaRepository.findAll();
		List<IvaDto> listaOut = findAll.stream().map( t -> modelMapper.map(t, IvaDto.class) )
				.collect(Collectors.toList());
		return  listaOut;
		
	}

}
