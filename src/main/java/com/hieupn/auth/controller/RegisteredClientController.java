package com.hieupn.auth.controller;

import com.hieupn.auth.mapper.JPARegisteredClientMapper;
import com.hieupn.auth.model.dto.JPARegisteredClientDTO;
import com.hieupn.auth.repository.JPARegisteredClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegisteredClientController {

    private final JPARegisteredClientRepository registeredClientRepository;
    private final JPARegisteredClientMapper registeredClientMapper;

    public RegisteredClientController(JPARegisteredClientRepository registeredClientRepository,
                                      JPARegisteredClientMapper registeredClientMapper) {
        this.registeredClientRepository = registeredClientRepository;
        this.registeredClientMapper = registeredClientMapper;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<JPARegisteredClientDTO>> getAllRegisteredClients() {
        List<JPARegisteredClientDTO> clientDTOs = registeredClientRepository.findAll().stream()
                .map(registeredClientMapper::toRegisteredClientDTO)
                .toList();

        return ResponseEntity.ok(clientDTOs);
    }
}
