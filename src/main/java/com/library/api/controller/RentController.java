package com.library.api.controller;

import com.library.api.dto.RentDTO;
import com.library.api.service.RentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/rent")
public class RentController {

    @Autowired
    private RentService service;

    @PostMapping
    public ResponseEntity<RentDTO> addRent(@RequestBody @Valid RentDTO rentDto) {
        rentDto = service.addRent(rentDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(rentDto.getId()).toUri();
        return ResponseEntity.created(uri).body(rentDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<RentDTO> updateRent(@PathVariable Long id, @RequestBody @Valid RentDTO rentDTO) {
        rentDTO = service.updateRent(id, rentDTO);
        return ResponseEntity.ok(rentDTO);
    }

    @GetMapping
    public ResponseEntity<Page<RentDTO>> searchBooks(Pageable pageable) {
        Page<RentDTO> rentDtos = service.searchRents(pageable);
        return ResponseEntity.ok(rentDtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RentDTO> searchBookById(@PathVariable Long id) {
        RentDTO rentDTO = service.searchRentById(id);
        return ResponseEntity.ok(rentDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        service.deleteRentById(id);
        return ResponseEntity.noContent().build();
    }

}
