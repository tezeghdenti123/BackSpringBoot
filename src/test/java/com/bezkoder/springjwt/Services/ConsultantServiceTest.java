package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.DTO.ConsultantPrivateDTO;
import com.bezkoder.springjwt.DTO.ConsultantPublicDTO;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")  // Use the test profile for H2
@Transactional
class ConsultantServiceTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AffectationRepository affectationRepository;
    @Autowired
    private CommercialeRepository commercialeRepository;
    @Autowired
    private GesionnaireRHRepository gesionnaireRHRepository;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private DevelopperRepository developperRepository;
    @Autowired
    private ConsultantService consultantService;
    @Autowired
    private UserRepository userRepository;


    @Test
    void isAvailable() {
        //Given: affection list
        List<Affectation>affectationList=new ArrayList<Affectation>();
        //When: aucune aafectation est une affectation courant
        Affectation affectation=new Affectation();
        affectation.setDate_deb(LocalDate.now().minusMonths(4));
        affectation.setDate_fin(LocalDate.now().minusMonths(3));
        affectationList.add(affectation);
        //Then: isAvailable should return True
        assertEquals(true,consultantService.isAvailable(affectationList));
        //When: there is an affectation that has today as a end day
        affectation.setDate_deb(LocalDate.now().minusMonths(1));
        affectation.setDate_fin(LocalDate.now());
        affectationList.add(affectation);
        //Then: isAvailable should return True
        assertEquals(true,consultantService.isAvailable(affectationList));
        //When: there is a current affectation
        affectation.setDate_deb(LocalDate.now());
        affectation.setDate_fin(LocalDate.now().plusMonths(1));
        affectationList.add(affectation);
        //Then: isAvailable should return False
        assertEquals(false,consultantService.isAvailable(affectationList));
        //When: there is a current affectation
        affectation.setDate_deb(LocalDate.now().minusMonths(1));
        affectation.setDate_fin(LocalDate.now().plusMonths(1));
        affectationList.add(affectation);
        //Then: isAvailable should return False
        assertEquals(false,consultantService.isAvailable(affectationList));
    }

    @Test
    void updateConsultant() {
        //Given: already saved Consultant
        Consultant consultant=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant","Consultant@gmail.com","Test2019","Consultant");
        consultant.setPhone("94102105");
        consultant.setLinkedIn("LinkedIn");
        consultant.setTitle("Title");
        Set<Role>roles=setRoles(ERole.ROLE_DEVELOPPER);
        consultant.setRoles(roles);
        List<Skill>skillList=new ArrayList<>();
        Skill skill=new Skill();
        skill.setName("S1");
        skillList.add(skill);
        consultant.setSkillList(skillList);
        consultant=consultantRepository.save(consultant);
        assertNotEquals(null,consultant.getId());
        assertEquals(1,consultant.getSkillList().size());

        //When: calling the function updateConsultant with a ConsultantPublicDTO entity
        ConsultantPublicDTO consultantPublicDTO=new ConsultantPublicDTO();
        consultantPublicDTO.setId(consultant.getId());
        consultantPublicDTO.setName("Consultant1");
        consultantPublicDTO.setEmail("Consultant1@gmail.com");
        consultantPublicDTO.setUsername("Consultant1");
        consultantPublicDTO.setPhone("1111");
        consultantPublicDTO.setTitle("Title1");
        consultantPublicDTO.setLinkedIn("LinkedIn1");
        consultantService.updateConsultant(consultantPublicDTO);

        //Then: assert that the consultant is updated based on the ConsultantPublicDTO
        consultant=consultantRepository.findById(consultant.getId()).orElseThrow();
        assertEquals("Consultant1",consultant.getName());
        assertEquals("Consultant1@gmail.com",consultant.getEmail());
        assertEquals("Consultant1",consultant.getUsername());
        assertEquals("1111",consultant.getPhone());
        assertEquals("Title1",consultant.getTitle());
        assertEquals("LinkedIn1",consultant.getLinkedIn());
        assertEquals(1,consultant.getSkillList().size());


    }


    @Test
    void isCurrent() {
        LocalDate currentDay=LocalDate.now();
        LocalDate startDate=currentDay.minusDays(1);
        LocalDate endDate=currentDay.plusDays(1);
        assertEquals(true,consultantService.isCurrent(startDate,endDate));
        startDate=currentDay.plusDays(1);
        assertEquals(false,consultantService.isCurrent(startDate,endDate));
        startDate=currentDay;
        assertEquals(true,consultantService.isCurrent(startDate,endDate));
        endDate=currentDay.minusDays(4);
        assertEquals(false,consultantService.isCurrent(startDate,endDate));
    }
    @Test
    void deleteConsultant() {

        //ensure when deleting a commerciale none of the associated consultant will be deleted
        Consultant consultant1=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant1","consultant@gmail.com","test2019","consultant");
        Commerciale commerciale= setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale1","Commerciale1","Commerciale1@gmail.com","test2019");
        List<Consultant>consultantList=new ArrayList<>();
        consultantList.add(consultant1);
        commerciale.setConsultantList(consultantList);
        commerciale=commercialeRepository.save(commerciale);
        assertEquals(2,consultantRepository.findAll().size());//should return 2 beacuse there is two consultants commerciale and 1 associated consultant

        consultantService.deleteConsultant(commerciale.getId());
        assertEquals(1,consultantRepository.findAll().size());//should return 1 to ensure that the associated consultant persist
        assertEquals(1,userRepository.findAll().size());
        //ensure when deleting a Gestionnaire none of the associated consultant will be deleted
        Consultant consultant2=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant2","consultant2@gmail.com","test2019","consultant2");
        GestionnaireRH gestionnaireRH=setGestionnaire(ERole.ROLE_GESTIONNAIRE,"Gestionnaire1","Gestionnaire1","gest@gmail.com","test2019");
        List<Consultant>consultantList1=new ArrayList<>();
        consultantList1.add(consultant2);
        gestionnaireRH.setConsultantList(consultantList1);
        gestionnaireRH=gesionnaireRHRepository.save(gestionnaireRH);
        assertEquals(3,consultantRepository.findAll().size());//should return 3 beacuse there is 2 consultants (the previuos one and the new one) and 1 associated gestionnaire
        consultantService.deleteConsultant(gestionnaireRH.getId());
        assertEquals(2,consultantRepository.findAll().size());//should return 2 to ensure that the associated consultant persist
        assertEquals(2,userRepository.findAll().size());


    }


    @Test
    void updateFullConsultant() {
        //Given: a consultant already saved in database
        Consultant consultant=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant","Consultant@gmail.com","Test2019","Consultant");
        consultant.setTitle("Test");
        consultant.setPhone("94102105");
        consultant.setLinkedIn("Linkedin");
        Commerciale commerciale= setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale","Commerciale","Commerciale@gmail.com","test2019");
        GestionnaireRH gestionnaireRH= setGestionnaire(ERole.ROLE_GESTIONNAIRE,"Gestionnaire","Gestionnaire","Gestionnaire@gmail.com","test2019");
        commerciale=commercialeRepository.save(commerciale);
        gestionnaireRH=gesionnaireRHRepository.save(gestionnaireRH);
        consultant.setGestionnaireRH(gestionnaireRH);
        consultant.setCommerciale(commerciale);
        List<Skill> skillList=setSkills();
        consultant.setSkillList(skillList);
        consultant=consultantRepository.save(consultant);
        assertNotEquals(null,consultant.getGestionnaireRH().getId());
        assertNotEquals(null,consultant.getCommerciale().getId());
        assertEquals(1,consultant.getSkillList().size());

        // when: new ConsultantPrivateDto
        ConsultantPrivateDTO consultantPrivateDTO=new ConsultantPrivateDTO();
        consultantPrivateDTO.setId(consultant.getId());
        consultantPrivateDTO.setName("Consultant1");
        consultantPrivateDTO.setEmail("Consultant1@gmail.com");
        consultantPrivateDTO.setUsername("Consultant1");
        consultantPrivateDTO.setPassword("Test2018");
        Role role=roleRepository.findByName(ERole.ROLE_GESTIONNAIRE).orElseThrow();
        Set<Role>roles=new HashSet<>();
        roles.add(role);
        consultantPrivateDTO.setRoles(roles);
        Commerciale commerciale1=setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale1","Commerciale1","Commerciale1@gmail.com","test2019");
        commerciale1=commercialeRepository.save(commerciale1);
        consultantPrivateDTO.setCommercialeId(commerciale1.getId());
        consultantPrivateDTO.setGestionnaireId(null);
        consultantService.updateFullConsultant(consultantPrivateDTO);
        consultant=consultantRepository.findById(consultant.getId()).orElseThrow();
        //Then: ensure that the consultant is up to date
        assertEquals("Consultant1",consultant.getName());
        assertEquals("Consultant1@gmail.com",consultant.getEmail());
        assertEquals("Consultant1",consultant.getUsername());
        assertEquals(commerciale1.getId(),consultant.getCommerciale().getId());
        assertEquals(null,consultant.getGestionnaireRH());
        assertEquals(1,consultant.getSkillList().size());

    }

    @Test
    void updateConsultantGestionnaire() {
        /****************** First Scenario *****************/
        // Given: a pervious consultant with his associated commerciale
        Consultant previousConsultant=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant1","Consultant1@gmail.com","Test2019","Consultant1");
        GestionnaireRH gestionnaireRH= setGestionnaire(ERole.ROLE_GESTIONNAIRE,"Gestionnaire1","Gestionnaire1","gest@gmail.com","test2019");
        previousConsultant.setGestionnaireRH(gestionnaireRH);
        //when: creating a DTO with a new commerciale and call the function updateConsultantRoles()
        ConsultantPrivateDTO consultantPrivateDTO=new ConsultantPrivateDTO();
        consultantPrivateDTO.setGestionnaireId(2L);
        assertEquals(2L,consultantPrivateDTO.getGestionnaireId());
        previousConsultant=consultantService.updateConsultantGestionnaire(previousConsultant,consultantPrivateDTO);
        //Then: assert that the Id of the commerciale in the previous consultant is set to the new one
        assertEquals(2L,previousConsultant.getGestionnaireRH().getId());
        /****************** Second Scenario *****************/
        // Given: a pervious consultant with his associated commerciale
        previousConsultant=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant1","Consultant1@gmail.com","Test2019","Consultant1");
        gestionnaireRH= setGestionnaire(ERole.ROLE_GESTIONNAIRE,"Gestionnaire1","Gestionnaire1","gest@gmail.com","test2019");
        previousConsultant.setGestionnaireRH(gestionnaireRH);
        //when: creating a DTO with a new commerciale with null Id and call the function updateConsultantRoles()
        consultantPrivateDTO=new ConsultantPrivateDTO();

        consultantPrivateDTO.setGestionnaireId(null);
        previousConsultant=consultantService.updateConsultantGestionnaire(previousConsultant,consultantPrivateDTO);
        //Then: assert that the Id of the commerciale in the previous consultant is set to the new one
        assertEquals(null,previousConsultant.getGestionnaireRH());

    }

    @Test
    void updateConsultantCommerciale() {
        /****************** First Scenario *****************/
        // Given: a pervious consultant with his associated commerciale
        Consultant previousConsultant=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant1","Consultant1@gmail.com","Test2019","Consultant1");
        Commerciale commerciale= setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale1","Commerciale1","Commerciale1@gmail.com","test2019");
        previousConsultant.setCommerciale(commerciale);
        //when: creating a DTO with a new commerciale and call the function updateConsultantRoles()
        ConsultantPrivateDTO consultantPrivateDTO=new ConsultantPrivateDTO();
        Commerciale commerciale1= setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale2","Commerciale2","Commerciale2@gmail.com","test2019");
        commerciale1.setId(2L);
        consultantPrivateDTO.setCommercialeId(commerciale1.getId());
        previousConsultant=consultantService.updateConsultantCommerciale(previousConsultant,consultantPrivateDTO);
        //Then: assert that the Id of the commerciale in the previous consultant is set to the new one
        assertEquals(2L,previousConsultant.getCommerciale().getId());
        /****************** Second Scenario *****************/
        // Given: a pervious consultant with his associated commerciale
        previousConsultant=setConsultant(ERole.ROLE_DEVELOPPER,"Consultant1","Consultant1@gmail.com","Test2019","Consultant1");
        commerciale= setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale1","Commerciale1","Commerciale1@gmail.com","test2019");
        previousConsultant.setCommerciale(commerciale);
        //when: creating a DTO with a new commerciale with null Id and call the function updateConsultantRoles()
        consultantPrivateDTO=new ConsultantPrivateDTO();
        commerciale1= setCommerciale(ERole.ROLE_COMMERCIALE,"Commerciale2","Commerciale2","Commerciale2@gmail.com","test2019");
        commerciale1.setId(null);
        consultantPrivateDTO.setCommercialeId(commerciale1.getId());
        previousConsultant=consultantService.updateConsultantCommerciale(previousConsultant,consultantPrivateDTO);
        //Then: assert that the Id of the commerciale in the previous consultant is set to the new one
        assertEquals(null,previousConsultant.getCommerciale());
    }



    private List<Skill> setSkills(){
        List<Skill> skillList=new ArrayList<>();
        Skill skill=new Skill();
        skill.setName("S1");
        skillList.add(skill);
        return skillList;
    }
    private Set<Role> setRoles(ERole erole){
        Set<Role> roles=new HashSet<>();
        Role role=new Role(erole);
        role=roleRepository.save(role);
        roles.add(role);
        return roles;

    }
    private Consultant setConsultant(ERole eRole,String username,String email,String password,String name){
        Consultant consultant=new Consultant();
        consultant.setName(name);
        consultant.setEmail(email);
        consultant.setUsername(username);
        Set<Role> roles=setRoles(eRole);
        consultant.setRoles(roles);
        consultant.setPassword(password);
        return consultant;
    }
    private Commerciale setCommerciale(ERole eRole,String name,String username,String email,String password){
        Commerciale commerciale= new Commerciale();
        commerciale.setName(name);
        commerciale.setEmail(email);
        commerciale.setUsername(username);
        commerciale.setPassword(password);
        Set<Role>roles1=setRoles(eRole);
        commerciale.setRoles(roles1);
        return commerciale;
    }

    private GestionnaireRH setGestionnaire(ERole eRole,String name,String username,String email,String password){
        GestionnaireRH gestionnaireRH=new GestionnaireRH();
        gestionnaireRH.setName(name);
        gestionnaireRH.setEmail(email);
        gestionnaireRH.setUsername(username);
        gestionnaireRH.setPassword(password);
        Set<Role>roles=setRoles(eRole);
        gestionnaireRH.setRoles(roles);
        return gestionnaireRH;
    }


    @Test
    void getChiffreAffaire() {
        Consultant consultant=new Consultant();
        consultant.setEmail("Test@gmail.com");
        consultant.setPassword("medbac2019");
        consultant.setUsername("Test123");
        Affectation affectation=new Affectation();
        affectation.setDate_fin(LocalDate.now().plusMonths(1));
        affectation.setDate_deb(LocalDate.now().minusMonths(1));
        affectation.setTjm(120D);
        affectation=affectationRepository.save(affectation);
        Affectation affectation1=new Affectation();
        affectation1.setDate_fin(LocalDate.now().minusMonths(1));
        affectation1.setDate_deb(LocalDate.now().minusMonths(4));
        affectation1.setTjm(500D);
        affectation1=affectationRepository.save(affectation1);
        List<Affectation>affectationList=new ArrayList<Affectation>();
        affectationList.add(affectation);
        affectationList.add(affectation1);
        consultant.setAffectationList(affectationList);
        consultant=consultantRepository.save(consultant);
        Double chiffreAffaire=consultantService.getChiffreAffaire();
        assertEquals(consultantService.getWorkingDaysUntilToday()*120,chiffreAffaire);
    }

    @Test
    void callFranceCalenderApi() {
        List<LocalDate>jourFérierList=consultantService.callFranceCalendarApi("2025");
        assertEquals(11,jourFérierList.size());
    }

    @Test
    void isWeekend() {
        LocalDate localDate=LocalDate.of(2024,10,10);
        assertEquals(false,consultantService.isWeekend(localDate));
        localDate=LocalDate.of(2024,10,12);
        assertEquals(true,consultantService.isWeekend(localDate));
        localDate=LocalDate.of(2024,10,13);
        assertEquals(true,consultantService.isWeekend(localDate));
    }
}