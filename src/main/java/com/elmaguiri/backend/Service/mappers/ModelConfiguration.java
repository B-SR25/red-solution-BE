package com.elmaguiri.backend.Service.mappers;

import com.elmaguiri.backend.Service.dtos.PartnerDTO;
import com.elmaguiri.backend.dao.entities.Client;
import com.elmaguiri.backend.dao.entities.Partner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
    public class ModelConfiguration {

  //      @Bean
        public ModelMapper modelMapper() {
            ModelMapper modelMapper = new ModelMapper();
            configureMappings(modelMapper);
            return modelMapper;
        }

        private void configureMappings(ModelMapper modelMapper) {
            // Create TypeMap for PartnerDTO to Partner
            TypeMap<PartnerDTO, Partner> typeMap = modelMapper.createTypeMap(PartnerDTO.class, Partner.class);

            // Map nested properties explicitly
            typeMap.addMappings(mapper -> {
                mapper.map(PartnerDTO::getClientId, (dest, v) -> {
                    if (v != null) {
                        Client client = new Client();
                        client.setId((Long) v);
                        dest.setClient(client);
                    }
                });
            });

            // Additional custom mappings can be added here if needed
            // Example: Mapping other complex fields or handling null cases
        }
    }