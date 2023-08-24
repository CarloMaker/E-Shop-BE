package com.xantrix.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="barcode")
@Data
public class Barcode implements Serializable{

	
	private static final long serialVersionUID = -1312650237252526296L;
	
	@Id
	@Size(min = 8, max  = 13 , message="{Size.Barcode.barcode.Validation}")
	@NotNull(message="{NotNull.Barcode.barcode.Validation}")
	@Column(name = "BARCODE")
	private String barcode;
	
	@Column(name = "IDTIPOART")
	@NotNull(message="{NotNull.Barcode.idTipoArt.Validation}")
	private String idTipoArt;

	/*
	 *
	 * L'annotazione @JsonBackReference è una specifica dell'ObjectMapper di Jackson, una libreria Java per la serializzazione e deserializzazione di oggetti Java in formato JSON. Questa annotazione serve a gestire la serializzazione ciclica degli oggetti, evitando un loop infinito nella conversione degli oggetti da e verso il formato JSON.
	   Quando hai una relazione bidirezionale tra due classi (ad esempio, una relazione "uno-a-molti" o "molti-a-molti"), potresti avere un problema di riferimento ciclico durante la serializzazione. Questo problema si verifica quando un oggetto fa riferimento all'altro e viceversa, creando un loop infinito quando si tenta di serializzare l'oggetto in formato JSON.
		L'annotazione @JsonBackReference risolve questo problema. 
		Puoi utilizzare @JsonBackReference sull'oggetto che rappresenta il lato "molti" della relazione, 
		mentre sull'oggetto che rappresenta il lato "uno", puoi usare @JsonManagedReference. 
		In questo modo, l'oggetto con @JsonManagedReference verrà serializzato normalmente, 
		mentre l'oggetto con @JsonBackReference verrà ignorato durante la serializzazione, evitando il loop infinito.
	 */
	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "CODART" , referencedColumnName = "codArt")
	@JsonBackReference
	private Articoli articolo;
	
}
