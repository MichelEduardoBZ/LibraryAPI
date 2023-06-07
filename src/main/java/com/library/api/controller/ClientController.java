package com.library.api.controller;

import com.library.api.dto.ClientDto;
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
    public ResponseEntity<ClientDto> insertClient(@RequestBody @Valid ClientDto clientDto){
        clientService.insertClient(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clientDto.getId()).toUri();
        return ResponseEntity.created(uri).body(clientDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDto clientDto){
        ClientDto dto = clientService.updateClientById(id, clientDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDto> searchById(@PathVariable Long id){
        ClientDto dto = clientService.searchClientById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> searchClients(Pageable pageable){
        Page<ClientDto> clients = clientService.searchClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ClientDto> deleteClientById(@PathVariable Long id){
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}