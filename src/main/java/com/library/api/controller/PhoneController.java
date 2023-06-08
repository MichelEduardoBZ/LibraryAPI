package com.library.api.controller;

import com.library.api.dto.PhoneDTO;
import com.library.api.service.PhoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/phone")
public class PhoneController {

    @Autowired
    private PhoneService service;

    @PostMapping
    public ResponseEntity<PhoneDTO> insertBook(@RequestBody @Valid PhoneDTO phoneDTO) {
        phoneDTO = service.insertPhone(phoneDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(phoneDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(phoneDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PhoneDTO> updateBook(@PathVariable Long id, @RequestBody @Valid PhoneDTO phoneDTO) {
        phoneDTO = service.updatePhone(id, phoneDTO);
        return ResponseEntity.ok(phoneDTO);
    }

    @GetMapping
    public ResponseEntity<Page<PhoneDTO>> searchBooks(Pageable pageable) {
        Page<PhoneDTO> bookDtos = service.seatchPhones(pageable);
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PhoneDTO> seatchPhoneById(@PathVariable Long id) {
        PhoneDTO phoneDTO = service.searchPhoneById(id);
        return ResponseEntity.ok(phoneDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        service.deletePhoneById(id);
        return ResponseEntity.noContent().build();
    }

}
