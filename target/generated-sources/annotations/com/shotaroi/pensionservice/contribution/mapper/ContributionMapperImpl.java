package com.shotaroi.pensionservice.contribution.mapper;

import com.shotaroi.pensionservice.contribution.dto.ContributionRequest;
import com.shotaroi.pensionservice.contribution.dto.ContributionResponse;
import com.shotaroi.pensionservice.domain.Contribution;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-07T00:44:51+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Homebrew)"
)
@Component
public class ContributionMapperImpl implements ContributionMapper {

    @Override
    public Contribution toEntity(ContributionRequest request) {
        if ( request == null ) {
            return null;
        }

        Contribution.ContributionBuilder contribution = Contribution.builder();

        contribution.citizenId( request.getCitizenId() );
        contribution.year( request.getYear() );
        contribution.salary( request.getSalary() );

        return contribution.build();
    }

    @Override
    public ContributionResponse toResponse(Contribution contribution) {
        if ( contribution == null ) {
            return null;
        }

        ContributionResponse.ContributionResponseBuilder contributionResponse = ContributionResponse.builder();

        contributionResponse.id( contribution.getId() );
        contributionResponse.citizenId( contribution.getCitizenId() );
        contributionResponse.year( contribution.getYear() );
        contributionResponse.salary( contribution.getSalary() );
        contributionResponse.contributionAmount( contribution.getContributionAmount() );
        contributionResponse.createdAt( contribution.getCreatedAt() );

        return contributionResponse.build();
    }
}
