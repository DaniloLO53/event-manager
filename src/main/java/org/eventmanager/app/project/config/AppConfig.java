package org.eventmanager.app.project.config;

import org.eventmanager.app.project.models.Event;
import org.eventmanager.app.project.payload.request.events.CreateEventRequestPayload;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.OffsetDateTime;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(CreateEventRequestPayload.class, Event.class)
                .addMappings(mapper -> {
                    mapper.skip(Event::setId);
                });

        return modelMapper;
    }
}