package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.DTO.ProjetDTO;
import com.bezkoder.springjwt.mappers.ProjetMapper;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.repositories.AffectationRepository;
import com.bezkoder.springjwt.repositories.ClientRepository;
import com.bezkoder.springjwt.repositories.ConsultantRepository;
import com.bezkoder.springjwt.repositories.ProjetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")  // Use the test profile for H2
@Transactional
class ExperienceServiceTest {
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private AffectationRepository affectationRepository;
    @Autowired
    private ConsultantRepository consultantRepository;

    @Test
    void getExperienceDTO() {
    }

    @Test
    void setExperienceDTO() {
    }

    @Test
    void saveProject() {
        Client client=new Client();
        client.setName("Test");
        client=clientRepository.save(client);
        assertEquals(1,clientRepository.findAll().size());
        Consultant consultant=new Consultant();
        consultant.setTitle("Test");
        consultant.setUsername("Test");
        consultant.setEmail("Test@gmail.com");
        consultant.setPassword("Test123456");
        consultant=consultantRepository.save(consultant);
        assertEquals(1,consultantRepository.findAll().size());
        ProjetDTO projetDTO=new ProjetDTO();
        projetDTO.setDate_debut(LocalDate.now().minusMonths(1));
        projetDTO.setDate_fin(LocalDate.now().plusMonths(1));
        projetDTO.setClientId(client.getId());
        Affectation affectation=new Affectation();
        affectation.setDate_deb(LocalDate.now());
        affectation.setTjm(11.5);
        affectation.setConsultant(consultant);
        List<Affectation>affectationList=new ArrayList<Affectation>();
        affectationList.add(affectation);
        projetDTO.setAffectationList(affectationList);
        experienceService.saveProject(projetDTO);
        assertEquals(1,projetRepository.findAll().size());
        assertEquals(1,affectationRepository.findAll().size());
    }

    @Test
    void deleteExperience() {
    }

    @Test
    void updateExperienceDTO() {
        Client client=new Client();
        client.setName("Test");
        client=clientRepository.save(client);
        assertEquals(1,clientRepository.findAll().size());
        Consultant consultant=new Consultant();
        consultant.setTitle("Test");
        consultant.setUsername("Test");
        consultant.setEmail("Test@gmail.com");
        consultant.setPassword("Test123456");
        consultant=consultantRepository.save(consultant);
        assertEquals(1,consultantRepository.findAll().size());
        Projet projet=new Projet();
        projet.setDate_debut(LocalDate.now().minusMonths(1));
        projet.setDate_fin(LocalDate.now().plusMonths(1));
        projet.setClient(client);
        Affectation affectation=new Affectation();
        affectation.setDate_deb(LocalDate.now());
        affectation.setTjm(11.5);
        affectation.setConsultant(consultant);
        List<Affectation>affectationList=new ArrayList<Affectation>();
        affectationList.add(affectation);
        projet.setAffectationList(affectationList);
        projet=projetRepository.save(projet);
        assertEquals(1,projetRepository.findAll().size());
        assertEquals(1,affectationRepository.findAll().size());
        ProjetDTO projetDTO= ProjetMapper.INSTANCE.toDTO(projet);
        projetDTO.setDate_debut(LocalDate.now());
        Affectation affectation1=projetDTO.getAffectationList().get(0);
        affectation1.setTjm(120.5);
        experienceService.updateExperienceDTO(projetDTO);
        assertEquals(LocalDate.now(),projetRepository.findById(projetDTO.getId()).orElseThrow().getDate_debut());
    }
}