package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.DTO.ConsultantPrivateDTO;
import com.bezkoder.springjwt.DTO.ConsultantPublicDTO;
import com.bezkoder.springjwt.Services.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ConsultantController {
    @Autowired
    private ConsultantService consultantService;



    @GetMapping("/gestionnaire/consultants")
    public List<ConsultantPublicDTO> getConsultant(@RequestParam Long gestId){
        return consultantService.getConsultantByGestionnaireId(gestId);
    }

    @PutMapping("/gestionnaire/consultant")
    public ResponseEntity<?> updateFullConsultant(@RequestBody ConsultantPrivateDTO consultantPrivateDT){
        return consultantService.updateFullConsultant(consultantPrivateDT);
    }

    @DeleteMapping("/consultant/{id}")
    public ResponseEntity<?> deleteConsultant(@PathVariable("id")Long consId){
        return consultantService.deleteConsultant(consId);
    }

    @GetMapping("/consultants")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ConsultantPublicDTO> getOffreList(){
        return consultantService.getConsultants();
    }


    @GetMapping("/consultantDTOs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ConsultantPublicDTO> getConsultantsDto(){
        return consultantService.getConsultantDTOs();
    }


    @GetMapping("/consultant/{id}")
    public ConsultantPublicDTO getConsultantById(@PathVariable("id") Long consId){
        return consultantService.getConsultantById(consId);
    }
    @GetMapping("/commerciale/consultants")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMMERCIALE')")
    public List<ConsultantPublicDTO> getConsultantByComId(@RequestParam Long commercialeId){
        return consultantService.getConsultantDTOByComId(commercialeId);
    }

    @GetMapping("/commerciale/consultants/available/count")
    @PreAuthorize("hasRole('COMMERCIALE')")
    public int getNbAvailableConsultantByComId(@RequestParam Long commercialeId){
        return consultantService.getNbAvailableConsultantByComId(commercialeId);
    }
    @GetMapping("/consultants/available/count")
    @PreAuthorize("hasRole('ADMIN')")
    public int getNbAvailableConsultant(){
        return consultantService.getNbAvailableConsultants();
    }
    @PutMapping("/consultant")
    public ConsultantPublicDTO updateConsultant(@RequestBody ConsultantPublicDTO consultantPublicDTO){
        return consultantService.updateConsultant(consultantPublicDTO);
    }
    @GetMapping("/consultants/chiffreaffaire")
    public Double getChiffreAffaire(){
        return  consultantService.getChiffreAffaire();
    }


}
