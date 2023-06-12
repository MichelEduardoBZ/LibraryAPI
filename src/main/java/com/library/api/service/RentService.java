package com.library.api.service;

import com.library.api.dto.RentDTO;
import com.library.api.dto.RentPenaltyDTO;
import com.library.api.entities.Book;
import com.library.api.entities.Client;
import com.library.api.entities.Rent;
import com.library.api.repository.BookRepository;
import com.library.api.repository.ClientRepository;
import com.library.api.repository.RentRepository;
import com.library.api.service.exceptions.DatabaseException;
import com.library.api.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        try {
            Rent rent = rentRepository.getReferenceById(id);
            copyDtoToEntity(phoneDTO, rent);
            rent = rentRepository.save(rent);
            return new RentDTO(rent);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Rent does not exist");
        }
    }

    @Transactional(readOnly = true)
    public Page<RentDTO> searchRents(Pageable pageable) {
        Page<Rent> rent = rentRepository.findAll(pageable);
        return rent.map(RentDTO::new);
    }

    @Transactional(readOnly = true)
    public RentDTO searchRentById(Long id) {
        Rent rent = rentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rent does not exist"));
        return new RentDTO(rent);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteRentById(Long id) {
        if (!rentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rent does not exist");
        }

        try {
            rentRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");
        }
    }

    @Transactional
    public Page<RentPenaltyDTO> validationPenaltyRent(Long id) {
        DecimalFormat df = new DecimalFormat("###,##0.00");
        LocalDate now = LocalDate.now();

        List<Rent> rents = rentRepository.findRentsByClientId(id);
        List<Rent> rentsValidatedDay = new ArrayList<>();

        if (!rents.isEmpty()) {
            List<RentPenaltyDTO> penaltyDTOs = new ArrayList<>();

            for (Rent x : rents) {
                LocalDate validationDay = x.getDevolutionDate().plus(30, ChronoUnit.DAYS);

                if (now.isAfter(validationDay)) {
                    rentsValidatedDay.add(x);
                }
            }

            for (Rent x : rentsValidatedDay) {
                Book book = bookRepository.getReferenceById(x.getBook().getId());

                double valueBook = Double.parseDouble(String.valueOf(book.getPriceDayRent()));
                double valueBookPenalty = valueBook * 0.1;

                long daysDifference = ChronoUnit.DAYS.between(x.getDevolutionDate(), now) - 30;

                for (int i = 1; i < daysDifference; i++) {
                    valueBookPenalty += valueBook * 0.1 + valueBookPenalty;
                }

                RentPenaltyDTO rentPenaltyDTO = new RentPenaltyDTO(x.getClient().getName(), book.getTitle(), df.format(valueBookPenalty), String.valueOf(now));
                penaltyDTOs.add(rentPenaltyDTO);
            }
            return new PageImpl<>(penaltyDTOs);
        }
        return Page.empty();
    }

    public void copyDtoToEntity(RentDTO rentDTO, Rent rent) {
        if (!rentDTO.getRentDate().isEmpty()) {
            rent.setRentDate(LocalDate.parse(rentDTO.getRentDate()));
        }

        if (rentDTO.getDevolutionDate() != null) {
            rent.setDevolutionDate(LocalDate.parse(rentDTO.getDevolutionDate()));
        }

        if (rentDTO.getClientId() != 0) {
            Client client = clientRepository.getReferenceById(rentDTO.getClientId());
            rent.setClient(client);
        }

        if (rentDTO.getBookId() != 0) {
            Book book = bookRepository.getReferenceById(rentDTO.getBookId());
            rent.setBook(book);
        }
    }
}
