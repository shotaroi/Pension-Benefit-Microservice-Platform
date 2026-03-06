package com.shotaroi.pensionservice.citizen.mapper;

import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.dto.CitizenResponse;
import com.shotaroi.pensionservice.domain.Citizen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitizenMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Citizen toEntity(CitizenRequest request);

    CitizenResponse toResponse(Citizen citizen);
}
