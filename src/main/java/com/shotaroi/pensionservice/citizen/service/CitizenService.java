package com.shotaroi.pensionservice.citizen.service;

import com.shotaroi.pensionservice.citizen.dto.CitizenRequest;
import com.shotaroi.pensionservice.citizen.dto.CitizenResponse;
import com.shotaroi.pensionservice.citizen.mapper.CitizenMapper;
import com.shotaroi.pensionservice.citizen.repository.CitizenRepository;
import com.shotaroi.pensionservice.domain.Citizen;
import com.shotaroi.pensionservice.exception.DuplicateResourceException;
import com.shotaroi.pensionservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final CitizenMapper citizenMapper;

    @Transactional
    public CitizenResponse create(CitizenRequest request) {
        if (citizenRepository.existsByPersonalNumber(request.getPersonalNumber())) {
            throw new DuplicateResourceException(
                    "Citizen with personal number " + request.getPersonalNumber() + " already exists");
        }

        Citizen citizen = citizenMapper.toEntity(request);
        citizen.setCreatedAt(LocalDateTime.now());
        citizen = citizenRepository.save(citizen);
        return citizenMapper.toResponse(citizen);
    }

    @Transactional(readOnly = true)
    public CitizenResponse getById(Long id) {
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Citizen", id));
        return citizenMapper.toResponse(citizen);
    }

    @Transactional(readOnly = true)
    public Page<CitizenResponse> getAll(Pageable pageable) {
        return citizenRepository.findAll(pageable)
                .map(citizenMapper::toResponse);
    }
}
