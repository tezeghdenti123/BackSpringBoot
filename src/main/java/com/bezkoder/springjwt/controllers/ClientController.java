package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.DTO.ClientDTO;
import com.bezkoder.springjwt.Services.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ClientController {
    @Autowired
    private ClientService clientService;



    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClients();
    }

    @GetMapping("/clients/count")
    public int getNbClients(){
        return clientService.getNbClients();
    }

    @PostMapping("/client")
    public ResponseEntity<?> saveClient(@RequestBody ClientDTO clientDTO){
        return clientService.saveClient(clientDTO);
    }
    @PutMapping("/client")
    public ResponseEntity<?> updateClient(@RequestBody ClientDTO clientDTO){
        return clientService.updateClient(clientDTO);
    }
    @DeleteMapping("/client/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id")Long clientId){
        return clientService.deleteClient(clientId);
    }
}
