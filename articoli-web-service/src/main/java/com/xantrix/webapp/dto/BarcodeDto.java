package com.xantrix.webapp.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BarcodeDto implements Serializable{

	
	private static final long serialVersionUID = -1312650237252526296L;
	
	
	private String barcode;
	
	
	private String idTipoArt;

	
	
}
