package com.library.api.controller;

import com.library.api.dto.ClientDTO;
import com.library.api.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> insertClient(@RequestBody @Valid ClientDTO clientDto) {
        clientService.insertClient(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(clientDto.getId()).toUri();
        return ResponseEntity.created(uri).body(clientDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDTO clientDto) {
        ClientDTO dto = clientService.updateClientById(id, clientDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> searchById(@PathVariable Long id) {
        ClientDTO dto = clientService.searchClientById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> searchClients(Pageable pageable) {
        Page<ClientDTO> clients = clientService.searchClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> deleteClientById(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
