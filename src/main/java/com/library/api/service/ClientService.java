package com.library.api.service;

import com.library.api.dto.ClientDTO;
import com.library.api.entities.Client;
import com.library.api.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional
    public ClientDTO insertClient(ClientDTO clientDto) {
        Client client = new Client();
        copyDtoToEntity(clientDto, client);
        repository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO updateClientById(Long id, ClientDTO clientDto) {
        Client client = repository.getReferenceById(id);
        copyDtoToEntity(clientDto, client);
        repository.save(client);
        return new ClientDTO(client);
    }

    @Transactional(readOnly = true)
    public ClientDTO searchClientById(Long id) {
        Client client = repository.getReferenceById(id);
        return new ClientDTO(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> searchClients(Pageable pageable) {
         Page<Client> clients = repository.findAll(pageable);
         return clients.map(ClientDTO::new);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void copyDtoToEntity(ClientDTO clientDto, Client client) {

        if (clientDto.getName() != null) {
            client.setName(clientDto.getName());
        }

        if (clientDto.getCpf() != null) {
            client.setCpf(clientDto.getCpf());
        }

        if (clientDto.getName() != null) {
            client.setEmail(clientDto.getEmail());
        }

        if (clientDto.getBirthDate() != null) {
            client.setBirthDate(LocalDate.parse(clientDto.getBirthDate()));
        }
    }
}
