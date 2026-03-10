package com.shotaroi.pensionservice.citizen.mapper;

import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.dto.CitizenResponse;
import com.shotaroi.pensionservice.domain.Citizen;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-07T00:44:51+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Homebrew)"
)
@Component
public class CitizenMapperImpl implements CitizenMapper {

    @Override
    public Citizen toEntity(CitizenRequest request) {
        if ( request == null ) {
            return null;
        }

        Citizen.CitizenBuilder citizen = Citizen.builder();

        citizen.personalNumber( request.getPersonalNumber() );
        citizen.firstName( request.getFirstName() );
        citizen.lastName( request.getLastName() );
        citizen.birthDate( request.getBirthDate() );

        return citizen.build();
    }

    @Override
    public CitizenResponse toResponse(Citizen citizen) {
        if ( citizen == null ) {
            return null;
        }

        CitizenResponse.CitizenResponseBuilder citizenResponse = CitizenResponse.builder();

        citizenResponse.id( citizen.getId() );
        citizenResponse.personalNumber( citizen.getPersonalNumber() );
        citizenResponse.firstName( citizen.getFirstName() );
        citizenResponse.lastName( citizen.getLastName() );
        citizenResponse.birthDate( citizen.getBirthDate() );
        citizenResponse.createdAt( citizen.getCreatedAt() );

        return citizenResponse.build();
    }
}
