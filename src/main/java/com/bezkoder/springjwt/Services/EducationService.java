package com.bezkoder.springjwt.Services;
import com.bezkoder.springjwt.DTO.FormationsDTO;
import com.bezkoder.springjwt.mappers.FormationsMapper;
import com.bezkoder.springjwt.models.Formations;
import com.bezkoder.springjwt.repositories.FormationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EducationService {
    @Autowired
    private FormationsRepository formationsRepository;

    public void saveFormations(FormationsDTO formationsDTO) {
        Formations formations= FormationsMapper.INSTANCE.toEntity(formationsDTO);
        formationsRepository.save(formations);
    }

    public ResponseEntity<?> deleteFormation(Long formationId) {
        if(formationsRepository.existsById(formationId)){
            formationsRepository.deleteById(formationId);
        }
        else{
            return ResponseEntity.ok("This Id: "+formationId+" doesn't exist");
        }
        return ResponseEntity.ok("Education deleted succecfully!");
    }

    public ResponseEntity<?> updateFormations(FormationsDTO formationsDTO) {
        Formations formations=FormationsMapper.INSTANCE.toEntity(formationsDTO);
        if((formations.getId()!=null)&&(formationsRepository.existsById(formationsDTO.getId()))){
            Formations savedFormations=formationsRepository.findById(formations.getId()).orElseThrow();
            savedFormations.setAnnee(formations.getAnnee());
            savedFormations.setDomain_formation(formations.getDomain_formation());
            savedFormations.setDegree(formations.getDegree());
            savedFormations.setEtablissement(formations.getEtablissement());
            savedFormations.setVille(formations.getVille());
            formationsRepository.save(savedFormations);
            return ResponseEntity.ok("Formation updated successfully!");
        }
        else{
            return ResponseEntity.ok("Id not found!");
        }
    }
}
