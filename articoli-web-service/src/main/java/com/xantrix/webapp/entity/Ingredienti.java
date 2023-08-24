package com.xantrix.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="ingredienti")
@Data
public class Ingredienti implements Serializable {

	
	private static final long serialVersionUID = 5847787496903001925L;

	@Id 
	@Column(name = "CODART")
	private String codArt;
	
	@Column(name = "INFO")
	private String info;

	@OneToOne
	@PrimaryKeyJoinColumn
	@JsonIgnore //UGUALE A JsonBackReference 
	private Articoli articolo;
}
