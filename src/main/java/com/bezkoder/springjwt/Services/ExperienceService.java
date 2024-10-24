package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.DTO.ExperienceDTO;
import com.bezkoder.springjwt.DTO.ProjetDTO;
import com.bezkoder.springjwt.mappers.ProjetMapper;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.repositories.AffectationRepository;
import com.bezkoder.springjwt.repositories.ClientRepository;
import com.bezkoder.springjwt.repositories.ConsultantRepository;
import com.bezkoder.springjwt.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExperienceService {
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private AffectationRepository affectationRepository;
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private ClientRepository clientRepository;


    public List<ExperienceDTO> getExperienceDTO(Long consId){
        Consultant consultant=consultantRepository.findById(consId).orElseThrow();
        List<Affectation> affectationList=consultant.getAffectationList();
        List<ExperienceDTO>experienceDTOList=new ArrayList<ExperienceDTO>();
        for (int i=0;i<affectationList.size();i++){
            Affectation affectation=affectationList.get(i);
            Projet projet=projetRepository.findById(affectation.getProjet().getId()).orElseThrow();
            Client client=clientRepository.findById(projet.getClient().getId()).orElseThrow();
            ExperienceDTO experienceDTO=setExperienceDTO(affectation,projet,client);
            experienceDTOList.add(experienceDTO);
        }

        return experienceDTOList;
    }


    public ExperienceDTO setExperienceDTO(Affectation affectation,Projet projet,Client client){
        ExperienceDTO experienceDTO=new ExperienceDTO();
        experienceDTO.setAffectationId(affectation.getId());
        experienceDTO.setTjm(affectation.getTjm());
        experienceDTO.setTitre(projet.getTitre());
        experienceDTO.setDate_debut(affectation.getDate_deb());
        experienceDTO.setDate_fin(affectation.getDate_fin());
        experienceDTO.setClientName(client.getName());
        experienceDTO.setClientId(client.getId());
        experienceDTO.setProjectId(projet.getId());
        return  experienceDTO;
    }
    @Transactional
    public ResponseEntity<?> saveProject(ProjetDTO projetDTO) {
        Projet projet= ProjetMapper.INSTANCE.toEntity(projetDTO);
        List<Affectation> affectationList = projet.getAffectationList();
        if (affectationList != null) {
            for (Affectation affectation : affectationList) {
                affectation.setProjet(projet); // Set the back reference
            }
        }
        projetRepository.save(projet);
        return ResponseEntity.ok("Experience saved!");
    }

    public ResponseEntity<?> deleteExperience(Long experienceId) {
        if(projetRepository.existsById(experienceId)){
            projetRepository.deleteById(experienceId);
            return ResponseEntity.ok("Experience is deleted seccesfully!");
        }
        else{
            return ResponseEntity.ok("this Id: "+experienceId+" doesn't exist!");
        }
    }

    public ResponseEntity<?> updateExperienceDTO(ProjetDTO projetDTO) {
        Projet projet=ProjetMapper.INSTANCE.toEntity(projetDTO);
        if((projet.getId()!=null)&&(projetRepository.existsById(projetDTO.getId()))){
            Projet savedProjet=projetRepository.findById(projetDTO.getId()).orElseThrow();
            savedProjet=projetRepository.findById(projetDTO.getId()).orElseThrow();
            savedProjet.setDate_fin(projet.getDate_fin());
            savedProjet.setDate_debut(projet.getDate_debut());
            savedProjet.setClient(projet.getClient());
            savedProjet.setTitre(projet.getTitre());
            Affectation affectation=projet.getAffectationList().get(0);
            updateAffectation(affectation);
            projetRepository.save(savedProjet);
            return ResponseEntity.ok("Updated successfully!");
        }else{
            return ResponseEntity.ok("Id not found!");
        }
    }

    void updateAffectation(Affectation affectation){
        if((affectation.getId()!=null)&&(affectationRepository.existsById(affectation.getId()))){
            Affectation savedAffectation=affectationRepository.findById(affectation.getId()).orElseThrow();
            if(affectation.getTjm()!=null){
                savedAffectation.setTjm(affectation.getTjm());
            }
            if(affectation.getDate_deb()!=null){
                savedAffectation.setDate_deb(affectation.getDate_deb());
            }
            if(affectation.getDate_fin()!=null){
                savedAffectation.setDate_fin(affectation.getDate_fin());
            }
            if(affectation.getProjet()!=null){
                savedAffectation.setProjet(affectation.getProjet());
            }
            if(affectation.getConsultant()!=null){
                savedAffectation.setConsultant(affectation.getConsultant());
            }

            affectationRepository.save(savedAffectation);
        }
    }
}
