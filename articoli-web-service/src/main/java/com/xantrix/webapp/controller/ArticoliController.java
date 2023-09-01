package com.xantrix.webapp.controller;

import java.lang.ProcessHandle.Info;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dto.ArticoliDto;
import com.xantrix.webapp.dto.InfoMsg;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.exception.BindingException;
import com.xantrix.webapp.exception.DuplicateException;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.ArticoliService;

import lombok.extern.java.Log;

@RestController
@RequestMapping("api/articoli")
//@CrossOrigin("http://localhost:4200")
@Log
public class ArticoliController {
	
	@Autowired
	private ArticoliService articoliService;
	
	@Autowired
	private ResourceBundleMessageSource errMessage;

	@Autowired
	private PriceClient priceClient;
	
	private double getPriceArt(String CodArt, String IdList, String Header)
	{
		
		double Prezzo = (IdList.length() > 0) ? 
				priceClient.getPriceArt(Header, CodArt, IdList) : 
				priceClient.getDefPriceArt(Header, CodArt);
		 
		log.info("Prezzo Articolo " + CodArt + ": " + Prezzo);

		return Prezzo;
	}
	

	
	@GetMapping(value="/test", produces = "application/json")
	public ResponseEntity<InfoMsg> testAuth(){
		return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), "Autenticazione effettuata."),HttpStatus.OK);
	}
	
	@GetMapping(value={"/cerca/barcode/{ean}","/cerca/barcode/{ean}/{idlist}"}, produces = "application/json")
	public ResponseEntity<ArticoliDto> listArtByEan(@PathVariable ("ean") String ean,
			@PathVariable("idlist") Optional<String> optIdList ,HttpServletRequest httpRequest)
	 throws NotFoundException{
		
		log.info("Calling ListByEAN");
		
		String IdList = (optIdList.isPresent()) ? optIdList.get() : "";
		String AuthHeader = httpRequest.getHeader("Authorization");

		
		ArticoliDto articolo =  articoliService.SelByBarcode(ean);
		if(articolo == null) {
			String errMsg=String.format("Il barcode %s non è stato trovato!", ean);
			log.warning(errMsg);
			throw new NotFoundException(errMsg);
			
		}else
		{
			articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), IdList, AuthHeader));
		}

		return new ResponseEntity<ArticoliDto>(articolo,HttpStatus.OK);
	}
	
	
	@GetMapping(value = {"/cerca/codice/{codart}","/cerca/codice/{codart}/{idlist}"}, produces = "application/json")
	public ResponseEntity<ArticoliDto> listArtByCodArt(@PathVariable("codart") String codArt,
			@PathVariable("idlist") Optional<String> optIdList,
			HttpServletRequest httpRequest) throws NotFoundException{
		
		log.info("Calling List Articoli");
		String IdList = (optIdList.isPresent()) ? optIdList.get() : "";
		String AuthHeader = httpRequest.getHeader("Authorization");
		

		ArticoliDto articolo = articoliService.SelByCodArt(codArt);
		
		if(articolo == null) {
			String errMsg=String.format("L'articolo con codice %s non è stato trovato!", codArt);
			log.warning(errMsg);
			throw new NotFoundException(errMsg);
		}else {
			articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), IdList, AuthHeader));

		}
		return new ResponseEntity<ArticoliDto>(articolo,HttpStatus.OK);
	}
	
	@GetMapping(value = {"/cerca/descrizione/{filter}","/cerca/descrizione/{filter}/{idlist}"}, produces = "application/json")
	public ResponseEntity<List<ArticoliDto>> listArtByDesc(@PathVariable("filter") String filter,
			@PathVariable("idlist") Optional<String> optIdList,
			HttpServletRequest httpRequest)	throws NotFoundException{
		
		log.info("Calling  listArtByDesc");
		String authHeader = httpRequest.getHeader("Authorization");
		String idList = (optIdList.isPresent()) ? optIdList.get() : "";

		
		List<ArticoliDto> articoli = articoliService.SelByDescrizione(filter.toUpperCase() + "%");
		
		if (articoli.isEmpty()) {
			String errMsg = String.format("Non trovato articoli con descrizione %s", filter);
			log.warning(errMsg);
			throw new NotFoundException(errMsg);
		}else {
			articoli.forEach(t -> t.setPrezzo(getPriceArt(t.getCodArt(), idList, authHeader)));
		}
		return new ResponseEntity<>(articoli, HttpStatus.OK);
	}
	
	
	@PostMapping(value="/inserisci")
	public ResponseEntity<InfoMsg> createArt(@Valid  @RequestBody Articoli articolo,BindingResult bindingResult) throws BindingException, DuplicateException{
		log.info("Calling  inserisci articolo " + articolo.getCodArt());
		
		if(bindingResult.hasErrors()) {
			String error = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warning(error);
			throw new BindingException(error);
		}
		
		if(articolo.getIva().getIdIva() == -1) {
			String msgErr = String.format("Seleziona IVA Valida");
			log.warning(msgErr);
			throw new BindingException(msgErr);
		}
		
		ArticoliDto art = articoliService.SelByCodArt(articolo.getCodArt());
		if(art != null) {
			String msgErr = String.format("Articolo %s presente in anagrafica!" + "Impossibile utilizzare il metodo POST", art.getCodArt());
			log.warning(msgErr);
			throw new DuplicateException(msgErr);
		}
		
		articoliService.InsArticolo(articolo);
		
		return new ResponseEntity<InfoMsg>(createInfoMsg("Inserimento effettuato con successo"), new HttpHeaders(),HttpStatus.CREATED);		
	}
	
	private InfoMsg createInfoMsg(String msg) {
		
		InfoMsg infoMsg = new InfoMsg(LocalDate.now(), msg);
		return infoMsg;
		
	}


	@RequestMapping(value="/modifica",method=RequestMethod.PUT)
	public ResponseEntity<InfoMsg> updateArt(@Valid  @RequestBody Articoli articolo,BindingResult bindingResult)
			throws BindingException,NotFoundException{
		log.info("Calling  modifica articolo " + articolo.getCodArt());
		
		if(bindingResult.hasErrors()) {
			String error = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warning(error);
			throw new BindingException(error);
		}
		
		ArticoliDto art = articoliService.SelByCodArt(articolo.getCodArt());
		if(art == null) {
			String msgErr = String.format("Articolo %s non presente in anagrafica!" + "Impossibile utilizzare il metodo PUT", art.getCodArt());
			log.warning(msgErr);
			throw new NotFoundException(msgErr);
		}
		
		articoliService.InsArticolo(articolo);
		
		
		return new ResponseEntity<InfoMsg>(createInfoMsg("Articolo Modificato con successo"), new HttpHeaders(),HttpStatus.CREATED);		
	}
	
	@RequestMapping(value="/elimina/{codart}",method=RequestMethod.DELETE)
	public ResponseEntity<InfoMsg> updateArt(@PathVariable ("codart") String codArt)
			throws BindingException,NotFoundException{
		log.info("Calling  elimina articolo " + codArt);
		
		
		ArticoliDto art = articoliService.SelByCodArt(codArt);
		if(art == null) {
			String msgErr = String.format("Articolo %s non presente in anagrafica!" + "Impossibile utilizzare il metodo PUT", art.getCodArt());
			log.warning(msgErr);
			throw new NotFoundException(msgErr);
		}
		
		articoliService.DelArticolo(art);
		
		return new ResponseEntity<>(createInfoMsg("Eliminazione avvenuta con successo"), new HttpHeaders(),HttpStatus.CREATED);		
	}
	

}
