package com.library.api.service;

import com.library.api.dto.PhoneDTO;
import com.library.api.entities.Client;
import com.library.api.entities.Phone;
import com.library.api.repository.ClientRepository;
import com.library.api.repository.PhoneRepository;
import com.library.api.service.exceptions.DatabaseException;
import com.library.api.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public PhoneDTO insertPhone(PhoneDTO phoneDTO) {
        Phone phone = new Phone();
        copyDtoToEntity(phoneDTO, phone);
        repository.save(phone);
        return new PhoneDTO(phone);
    }

    @Transactional
    public PhoneDTO updatePhone(Long id, PhoneDTO phoneDTO) {
        try{
            Phone phone = repository.getReferenceById(id);
            copyDtoToEntity(phoneDTO, phone);
            phone = repository.save(phone);
            return new PhoneDTO(phone);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Phone does not exist");
        }
    }

    @Transactional(readOnly = true)
    public Page<PhoneDTO> searchPhones(Pageable pageable) {
        Page<Phone> phones = repository.findAll(pageable);
        return phones.map(PhoneDTO::new);
    }

    @Transactional(readOnly = true)
    public PhoneDTO searchPhoneById(Long id) {
        Phone phone = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Phone does not exist"));
        return new PhoneDTO(phone);
    }

    @Transactional
    public void deletePhoneById(Long id) {

        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Phone does not exist");
        }

        try{
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");
        }
    }

    public void copyDtoToEntity(PhoneDTO phoneDTO, Phone phone) {
        if (!phoneDTO.getPhone().isEmpty()) {
            phone.setPhone(phoneDTO.getPhone());
        }

        if (phoneDTO.getClientId() != 0) {
            Client client = clientRepository.getReferenceById(phoneDTO.getClientId());
            phone.setClient(client);
        }
    }
}
