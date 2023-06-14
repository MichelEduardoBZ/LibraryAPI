package com.library.api.service;

import com.library.api.constants.PaymentStatus;
import com.library.api.dto.RentDTO;
import com.library.api.dto.RentPaymentDTO;
import com.library.api.dto.RentPenaltyDTO;
import com.library.api.entities.Book;
import com.library.api.entities.Client;
import com.library.api.entities.Rent;
import com.library.api.repository.BookRepository;
import com.library.api.repository.ClientRepository;
import com.library.api.repository.RentRepository;
import com.library.api.service.exceptions.DatabaseException;
import com.library.api.service.exceptions.InsufficientFundsException;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
    public Page<RentPenaltyDTO> validationPenaltyRent(Long rentId) {
        LocalDate now = LocalDate.now();

        List<Rent> rents = rentRepository.findRentsByClientId(rentId);
        List<Rent> rentsValidatedDay = new ArrayList<>();

        if (rents.isEmpty()) {
            return Page.empty();
        }

        List<RentPenaltyDTO> penaltyDTOs = new ArrayList<>();

        for (Rent x : rents) {
            LocalDate validationDay = x.getDevolutionDate().plus(30, ChronoUnit.DAYS);

            if (now.isAfter(validationDay)) {
                rentsValidatedDay.add(x);
            }
        }

        for (Rent x : rentsValidatedDay) {
            RentPenaltyDTO rentPenaltyDTO = new RentPenaltyDTO(x.getClient().getName(), x.getBook().getTitle(), penaltyCalculation(x, now), now);
            penaltyDTOs.add(rentPenaltyDTO);
        }

        return new PageImpl<>(penaltyDTOs);
    }


    @Transactional
    public RentPaymentDTO paymentRent(RentPaymentDTO rentPaymentDTO) {

        Rent rent = rentRepository.findById(rentPaymentDTO.getRentId()).orElseThrow(() -> new ResourceNotFoundException("Rent does not exist"));

        if (rent.getPaymentStatus() == PaymentStatus.PAID) {
            throw new ResourceNotFoundException("Rent does not exist");
        }

        LocalDate validatedBookReturnDay = LocalDate.parse(rentPaymentDTO.getBookReturnDay());

        double cash = Double.valueOf(rentPaymentDTO.getCash().replace(",", "."));
        double value = penaltyCalculation(rent, validatedBookReturnDay);

        if (cash >= value) {
            double cashBack = value - cash;
            rent.setPaymentStatus(PaymentStatus.PAID);
            return new RentPaymentDTO(rent.getId(), rent.getClient().getId(), rent.getBook().getId(), validatedBookReturnDay, Math.abs(cashBack), rent.getPaymentStatus());
        } else {
            throw new InsufficientFundsException("Insufficient funds. Required $: " + value);
        }
    }

    public Double penaltyCalculation(Rent rent, LocalDate validatedBookReturnDay) {

        Book book = bookRepository.getReferenceById(rent.getBook().getId());
        double valueBook = Double.parseDouble(String.valueOf(book.getPriceDayRent()));

        LocalDate validationDay = rent.getDevolutionDate().plus(30, ChronoUnit.DAYS);

        if (validatedBookReturnDay.isAfter(validationDay)) {
            long daysDifference = ChronoUnit.DAYS.between(validationDay, validatedBookReturnDay) - 30;
            double valueBookPenalty = valueBook * 0.1;

            for (int i = 1; i < Math.abs(daysDifference); i++) {
                valueBookPenalty += valueBook * 0.1 + valueBookPenalty;
            }

            return valueBookPenalty;

        } else {
            LocalDate rentDate = rent.getRentDate();
            LocalDate devolutionDate = rent.getDevolutionDate();
            long daysDifference = ChronoUnit.DAYS.between(rentDate, devolutionDate);

            return book.getPriceDayRent() * Double.parseDouble(String.valueOf(daysDifference));
        }
    }


    public void copyDtoToEntity(RentDTO rentDTO, Rent rent) {
        if (!rentDTO.getRentDate().isEmpty()) {
            rent.setRentDate(LocalDate.parse(rentDTO.getRentDate()));
        }

        if (rentDTO.getDevolutionDate() != null) {
            rent.setDevolutionDate(LocalDate.parse(rentDTO.getDevolutionDate()));
        }

        if (rentDTO.getPaymentStatus() != null) {
            rent.setPaymentStatus(PaymentStatus.valueOf(rentDTO.getPaymentStatus()));
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
