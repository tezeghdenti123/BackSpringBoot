package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.DTO.OffreDTO;
import com.bezkoder.springjwt.mappers.OffreMapper;
import com.bezkoder.springjwt.models.GestionnaireRH;
import com.bezkoder.springjwt.models.Offre;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repositories.GesionnaireRHRepository;
import com.bezkoder.springjwt.repositories.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OffreService {

    @Autowired
    private GesionnaireRHRepository gesionnaireRHRepository;
    @Autowired
    private OffreRepository offreRepository;


    public List<OffreDTO> getOffreByGestionnaireId(Long gestionnaireId) {
        GestionnaireRH gestionnaireRH=gesionnaireRHRepository.findById(gestionnaireId).orElseThrow(()-> new RuntimeException("Gestionnaire id:"+ gestionnaireId +"not found!"));
        List<Offre>offreList= gestionnaireRH.getOffreList();
        List<OffreDTO>offreDTOList=new ArrayList<OffreDTO>();
        for(int i=0;i<offreList.size();i++){
            OffreDTO offreDTO= OffreMapper.INSTANCE.toDTO(offreList.get(i));
            offreDTOList.add(offreDTO);
        }
        return offreDTOList;
    }

    public List<OffreDTO> getActiveOffres() {
        List<Offre>offreList=offreRepository.findAll();
        List<OffreDTO>activeList=new ArrayList<OffreDTO>();
        for(int i=0;i<offreList.size();i++){
            if(offreList.get(i).getStatus()==null){
                continue;
            }
            else if(offreList.get(i).getStatus().equals("Active")){
                OffreDTO offreDTO=OffreMapper.INSTANCE.toDTO(offreList.get(i));
                activeList.add(offreDTO);
            }

        }
        return activeList;
    }

    public ResponseEntity<?> saveOffre(OffreDTO offreDTO) {
        Offre offre=OffreMapper.INSTANCE.toEntity(offreDTO);
        if(offre.getGestionnaireRH().getId()==null){
            offre.setGestionnaireRH(null);
        }
        offreRepository.save(offre);
        return ResponseEntity.ok(new MessageResponse("Offre created successfully!"));
    }

    public List<OffreDTO> getOffres() {
        List<Offre>offreList=offreRepository.findAll();
        List<OffreDTO>offreDTOList=new ArrayList<OffreDTO>();
        for(int i=0;i<offreList.size();i++){
            OffreDTO offreDTO=OffreMapper.INSTANCE.toDTO(offreList.get(i));
            offreDTOList.add(offreDTO);
        }
        return offreDTOList;
    }

    public ResponseEntity<?> deleteOffre(Long offreId) {
        if(offreRepository.existsById(offreId)){
            offreRepository.deleteById(offreId);
            return ResponseEntity.ok("Ok!");
        }
        else{
            return ResponseEntity.ok("this Id: "+offreId+" doesn't exist!");
        }
    }

    public ResponseEntity<?> updateOffre(OffreDTO offreDTO) {
        Offre offre=OffreMapper.INSTANCE.toEntity(offreDTO);
        if(offreRepository.existsById(offre.getId())){
            Offre savedOffre=offreRepository.findById(offre.getId()).orElseThrow();
            savedOffre.setDateDebut(offre.getDateDebut());
            savedOffre.setDescription(offre.getDescription());
            savedOffre.setExperience(offre.getExperience());
            savedOffre.setLocation(offre.getLocation());
            savedOffre.setPosition(offre.getPosition());
            savedOffre.setStatus(offre.getStatus());
            savedOffre.setTitle(offre.getTitle());
            savedOffre.setTjm(offre.getTjm());
            savedOffre.setType(offre.getType());
            offreRepository.save(savedOffre);
            return ResponseEntity.status(HttpStatus.OK).body("updated!");
        }
        else{
            System.out.println(offre.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Id not found!");
        }

    }
}
