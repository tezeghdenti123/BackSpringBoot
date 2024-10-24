package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.DTO.ExperienceDTO;
import com.bezkoder.springjwt.DTO.ProjetDTO;
import com.bezkoder.springjwt.Services.ExperienceService;
import com.bezkoder.springjwt.models.Projet;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @GetMapping("/consultant/experienceDTOs")
    public List<ExperienceDTO> getAffectations(@RequestParam Long consId){
        return experienceService.getExperienceDTO(consId);
    }

    @PostMapping("/projet")
    public ResponseEntity<?> saveProjet(@RequestBody ProjetDTO projetDTO){
        return experienceService.saveProject(projetDTO);
        //return ResponseEntity.ok(new MessageResponse("Experience added successfully!"));
    }
    @DeleteMapping("/experience/{id}")
    public ResponseEntity<?>deleteExperience(@PathVariable("id")Long experienceId){
        return experienceService.deleteExperience(experienceId);
    }

    @PutMapping("/experience")
    public ResponseEntity<?> updateExperience(@RequestBody ProjetDTO projetDTO){
        return experienceService.updateExperienceDTO(projetDTO);
    }
}
