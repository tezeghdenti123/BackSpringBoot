package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.DTO.ClientDTO;
import com.bezkoder.springjwt.mappers.ClientMapper;
import com.bezkoder.springjwt.models.Client;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    //Test implemented
    public List<ClientDTO> getClients() {

        List<Client>clientList= clientRepository.findAll();
        List<ClientDTO>clientDTOList=new ArrayList<ClientDTO>();
        for(int i=0;i<clientList.size();i++){
            ClientDTO clientDTO= ClientMapper.INSTANCE.toDTO(clientList.get(i));
            clientDTOList.add(clientDTO);
        }
        return clientDTOList;
    }

    //Test implemented
    public ResponseEntity<?> saveClient(ClientDTO clientDTO) {
        Client client=ClientMapper.INSTANCE.toEntity(clientDTO);
        clientRepository.save(client);
        return ResponseEntity.ok(new MessageResponse("Client added successfully!"));
    }


    //Test implemented
    public ResponseEntity<?> updateClient(ClientDTO clientDTO) {
        Client client=ClientMapper.INSTANCE.toEntity(clientDTO);
        System.out.println(client.getId());
        System.out.println(client.getName());
        if((client.getId()!=null)&&(clientRepository.existsById(clientDTO.getId()))){
            Client savedClient=clientRepository.findById(clientDTO.getId()).orElseThrow();
            savedClient.setEmail(client.getEmail());
            savedClient.setName(client.getName());
            savedClient.setAdresse(client.getAdresse());
            savedClient.setContact(client.getContact());
            savedClient.setTelephone(client.getTelephone());
            savedClient.setNum_siret(client.getNum_siret());
            savedClient.setNum_tva(client.getNum_tva());
            clientRepository.save(savedClient);
        }
        else{
            return new ResponseEntity<>("Id not found!", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Updated succefully!", HttpStatus.OK);
    }


    //Test implemented
    public ResponseEntity<?> deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
        return ResponseEntity.ok("Client deleted successfully!");
    }

    public int getNbClients() {
        return clientRepository.findAll().size();
    }
}
