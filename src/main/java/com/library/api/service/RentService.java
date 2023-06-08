package com.library.api.service;

import com.library.api.dto.RentDTO;
import com.library.api.entities.Book;
import com.library.api.entities.Client;
import com.library.api.entities.Rent;
import com.library.api.repository.BookRepository;
import com.library.api.repository.ClientRepository;
import com.library.api.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public RentDTO addRent(RentDTO rentDTO) {
        Rent rent = new Rent();
        copyDtoToEntity(rentDTO, rent);
        rentRepository.save(rent);
        return new RentDTO(rent);
    }

    @Transactional
    public RentDTO updateRent(Long id, RentDTO phoneDTO) {
        Rent rent = rentRepository.getReferenceById(id);
        copyDtoToEntity(phoneDTO, rent);
        rent = rentRepository.save(rent);
        return new RentDTO(rent);
    }

    @Transactional(readOnly = true)
    public Page<RentDTO> searchRents(Pageable pageable) {
        Page<Rent> rent = rentRepository.findAll(pageable);
        return rent.map(RentDTO::new);
    }

    @Transactional(readOnly = true)
    public RentDTO searchRentById(Long id) {
        Rent rent = rentRepository.getReferenceById(id);
        return new RentDTO(rent);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteRentById(Long id) {
        rentRepository.deleteById(id);
    }

    public void copyDtoToEntity(RentDTO rentDTO, Rent rent) {
        if (!rentDTO.getRentDate().isEmpty()) {
            rent.setRentDate(LocalDate.parse(rentDTO.getRentDate()));
        }

        if (rentDTO.getDevolutionDate() != null) {
            rent.setDevolutionDate(LocalDate.parse(rentDTO.getDevolutionDate()));
        }

        if (rentDTO.getClientId() != null) {
            Client client = clientRepository.getReferenceById(rentDTO.getClientId());
            rent.setClient(client);
        }

        if (rentDTO.getBookId() != null) {
            Book book = bookRepository.getReferenceById(rentDTO.getBookId());
            rent.setBook(book);
        }
    }

}
