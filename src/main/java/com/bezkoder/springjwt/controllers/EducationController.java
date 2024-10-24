package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.DTO.FormationsDTO;
import com.bezkoder.springjwt.Services.EducationService;
import com.bezkoder.springjwt.models.Formations;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class EducationController {
    @Autowired
    private EducationService educationService;


    @PostMapping("/education")
    public ResponseEntity<?> saveFormation(@RequestBody FormationsDTO formationsDTO){
        educationService.saveFormations(formationsDTO);
        return ResponseEntity.ok(new MessageResponse("Education added successfully!"));
    }
    @PutMapping("/education")
    public ResponseEntity<?> updateFormation(@RequestBody FormationsDTO formationsDTO){
        return educationService.updateFormations(formationsDTO);
    }
    @DeleteMapping("/education/{id}")
    public ResponseEntity<?> deleteFormation(@PathVariable("id")Long formationId){
        return educationService.deleteFormation(formationId);
    }
}
