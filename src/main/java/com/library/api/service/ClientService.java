package com.library.api.service;

import com.library.api.dto.ClientDto;
import com.library.api.entities.Client;
import com.library.api.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional
    public ClientDto insertClient(ClientDto clientDto) {
        Client client = new Client();
        copyDtoToEntity(clientDto, client);
        repository.save(client);
        return new ClientDto(client);
    }

    @Transactional
    public ClientDto updateClientById(Long id, ClientDto clientDto) {
        Client client = repository.getReferenceById(id);
        copyDtoToEntity(clientDto, client);
        repository.save(client);
        return new ClientDto(client);
    }

    @Transactional(readOnly = true)
    public ClientDto searchClientById(Long id) {
        Client client = repository.getReferenceById(id);
        return new ClientDto(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> searchClients(Pageable pageable) {
        Page<Client> client = repository.findAll(pageable);
        return client.map(ClientDto::new);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void copyDtoToEntity(ClientDto clientDto, Client client) {

        if (clientDto.getName() != null) {
            client.setName(clientDto.getName());
        }

        if (clientDto.getName() != null) {
            client.setEmail(clientDto.getEmail());
        }

        if (clientDto.getBirthDate() != null) {
            client.setBirthDate(clientDto.getBirthDate());
        }
    }
}
