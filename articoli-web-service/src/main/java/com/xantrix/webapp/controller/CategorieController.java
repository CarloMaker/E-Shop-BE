package com.xantrix.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.dto.CategoriaDto;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.CategorieService;

import lombok.extern.java.Log;

@RestController
@RequestMapping("api/cat")
@Log
@CrossOrigin("http://localhost:4200")
public class CategorieController {
	
	@Autowired
	private CategorieService categorieService;
	
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<CategoriaDto>> GetCat() throws NotFoundException{
		
		log.info("Get All Categories..");
		
		List<CategoriaDto> list = categorieService.SelTutti();
		
		if(list.isEmpty() ) {
			String errorMessage = String.format("Nessun elemento");
			
			log.warning(errorMessage);
			
			throw new NotFoundException(errorMessage);
			
		}
		
		return new ResponseEntity<List<CategoriaDto>>(list,HttpStatus.OK); 
		
	}
	
	

}
