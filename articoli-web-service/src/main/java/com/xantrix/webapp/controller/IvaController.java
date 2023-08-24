package com.xantrix.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.dto.IvaDto;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.IvaService;

import lombok.extern.java.Log;

@RestController
@RequestMapping("api/iva")
@Log
@CrossOrigin("http://localhost:4200")
public class IvaController {
	
	@Autowired IvaService ivaService;
	
	
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<IvaDto>> GetIva() throws NotFoundException {
		
		log.info("Chiamata a GetIVa");
		
	   List<IvaDto> selTutti = ivaService.selTutti();
	  
	   if(selTutti.isEmpty()) {
		   
		   String errorMsg = String.format("Nessun record presente");
		   
		   log.warning(errorMsg);
		   
		   throw new NotFoundException(errorMsg);
	   
	   }
	   return new ResponseEntity<List<IvaDto>>(selTutti,HttpStatus.OK); 
	}
		

}
