package com.xantrix.webapp;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xantrix.webapp.dto.ArticoliDto;
import com.xantrix.webapp.dto.BarcodeDto;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.entity.Barcode;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		 ModelMapper modelMapper = new ModelMapper();
		 modelMapper.getConfiguration().setSkipNullEnabled(true); // togli i null
		 modelMapper.addMappings(articoliMapping);
		 modelMapper.addMappings( new PropertyMap<Barcode, BarcodeDto>() {
			 @Override
			protected void configure() {
				 map().setIdTipoArt (source.getIdTipoArt());
				
			}
		});
		 //CONVERTER GENERICO
		modelMapper.addConverter(new Converter<String, String>() {

			@Override
			public String convert(MappingContext<String, String> context) {
				return context.getSource() == null ? "" : context.getSource().trim();
			}
			 
		});
		 
		 return modelMapper;
	}
	

	PropertyMap<Articoli, ArticoliDto> articoliMapping = new PropertyMap<Articoli, ArticoliDto>() {

		@Override
		protected void configure() {
			map().setDataCreazione(source.getDataCreaz());
			
		}
	};
}
