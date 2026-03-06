package com.shotaroi.pensionservice.contribution.mapper;

import com.shotaroi.pensionservice.contribution.dto.ContributionRequest;
import com.shotaroi.pensionservice.contribution.dto.ContributionResponse;
import com.shotaroi.pensionservice.domain.Contribution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContributionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contributionAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "citizen", ignore = true)
    Contribution toEntity(ContributionRequest request);

    ContributionResponse toResponse(Contribution contribution);
}
