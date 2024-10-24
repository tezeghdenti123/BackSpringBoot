package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.DTO.ConsultantPrivateDTO;
import com.bezkoder.springjwt.DTO.ConsultantPublicDTO;
import com.bezkoder.springjwt.mappers.ConsultantMapper;
import com.bezkoder.springjwt.mappers.ConsultantPrivateMapper;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class ConsultantService {
    @Autowired
    private GesionnaireRHRepository gesionnaireRHRepository;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private CommercialeRepository commercialeRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private DevelopperRepository developperRepository;
    @Autowired
    private UserRepository userRepository;





    public List<ConsultantPublicDTO> getConsultantByGestionnaireId(Long gestId) {
        GestionnaireRH gestionnaireRH=gesionnaireRHRepository.findById(gestId).orElseThrow(()-> new EntityNotFoundException("Gestionnaire not found!"));
        List<Consultant>consultantList= gestionnaireRH.getConsultantList();
        List<ConsultantPublicDTO> consultantPublicDTOList =new ArrayList<ConsultantPublicDTO>();
        for(int i=0;i<consultantList.size();i++){
            ConsultantPublicDTO consultantPublicDTO = ConsultantMapper.INSTANCE.toDTO(consultantList.get(i));
            consultantPublicDTOList.add(consultantPublicDTO);
        }
        return consultantPublicDTOList;
    }

    public List<ConsultantPublicDTO> getConsultants() {
        List<Consultant>consultantList=consultantRepository.findAll();
        List<ConsultantPublicDTO> consultantPublicDTOList =new ArrayList<ConsultantPublicDTO>();
        for(int i=0;i<consultantList.size();i++){
            ConsultantPublicDTO consultantPublicDTO = ConsultantMapper.INSTANCE.toDTO(consultantList.get(i));
            consultantPublicDTOList.add(consultantPublicDTO);
        }
        return consultantPublicDTOList;
    }

    public List<ConsultantPublicDTO> getConsultantDTOs(){
        List<Consultant>consultantList= consultantRepository.findAll();
        List<ConsultantPublicDTO> consultantPublicDTOList =new ArrayList<ConsultantPublicDTO>();
        for(int i=0;i<consultantList.size();i++ ){
            Consultant consultant=consultantList.get(i);
            ConsultantPublicDTO consultantPublicDTO = ConsultantMapper.INSTANCE.toDTO(consultant);
            List<Affectation>affectationList=consultant.getAffectationList();
            Boolean available=isAvailable(affectationList);
            consultantPublicDTO.setAvailable(available);
            consultantPublicDTOList.add(consultantPublicDTO);
        }
        return consultantPublicDTOList;
    }

    public List<ConsultantPublicDTO> getConsultantDTOByComId(Long commercialeId){
        Commerciale commerciale=commercialeRepository.findById(commercialeId).orElseThrow(() -> new RuntimeException("Commerciale not found"));
        List<Consultant>consultantList= commerciale.getConsultantList();
        List<ConsultantPublicDTO> consultantPublicDTOList =new ArrayList<ConsultantPublicDTO>();
        for(int i=0;i<consultantList.size();i++ ){
            Consultant consultant=consultantList.get(i);
            ConsultantPublicDTO consultantPublicDTO = ConsultantMapper.INSTANCE.toDTO(consultant);
            List<Affectation>affectationList=consultant.getAffectationList();
            Boolean available=isAvailable(affectationList);
            consultantPublicDTO.setAvailable(available);
            consultantPublicDTOList.add(consultantPublicDTO);
        }
        return consultantPublicDTOList;
    }

    //Test implemented
    public Boolean isAvailable(List<Affectation>affectationList){
        for(int i=0;i<affectationList.size();i++){
            Affectation affectation=affectationList.get(i);
            if(isCurrent(affectation.getDate_deb(),affectation.getDate_fin())){
                return false;
            }
        }

        return true;
    }


    //Test implemented
    public Boolean isCurrent(LocalDate dateDebut, LocalDate dateFin){
        LocalDate currentDate=LocalDate.now();
        if(dateDebut!=null&&dateFin!=null){
            return (currentDate.isAfter(dateDebut)&&currentDate.isBefore(dateFin))||(currentDate.isEqual(dateDebut)&&currentDate.isBefore(dateFin));
        }
        return false;
    }


    //Test implemented
    public ConsultantPublicDTO updateConsultant(ConsultantPublicDTO consultantPublicDTO){
        Optional<Consultant> savedConsultantOptional=consultantRepository.findById(consultantPublicDTO.getId());
        if(savedConsultantOptional.isPresent()){

            Consultant savedConsultant=savedConsultantOptional.get();
            savedConsultant.setLinkedIn(consultantPublicDTO.getLinkedIn());
            savedConsultant.setName(consultantPublicDTO.getName());
            savedConsultant.setPhone(consultantPublicDTO.getPhone());
            savedConsultant.setUsername(consultantPublicDTO.getUsername());
            savedConsultant.setEmail(consultantPublicDTO.getEmail());
            savedConsultant.setTitle(consultantPublicDTO.getTitle());

            Consultant consultant=consultantRepository.save(savedConsultant);
            return ConsultantMapper.INSTANCE.toDTO(consultant);
        }
        return null;

    }



    public ConsultantPublicDTO getConsultantById(Long consId) {
        Consultant consultant= consultantRepository.findById(consId).orElseThrow();
        return ConsultantMapper.INSTANCE.toDTO(consultant);
    }

    //Test implemented
    public ResponseEntity<?> updateFullConsultant(ConsultantPrivateDTO consultantPrivateDTO) {
        Consultant consultant= ConsultantPrivateMapper.INSTANCE.toEntity(consultantPrivateDTO);
        if(consultantRepository.existsById(consultant.getId())){
            Consultant existedConsultant=consultantRepository.findById(consultant.getId()).orElseThrow();
            existedConsultant.setName(consultant.getName());
            existedConsultant.setUsername(consultant.getUsername());
            existedConsultant.setEmail(consultant.getEmail());
            existedConsultant.setPhone(consultant.getPhone());
            existedConsultant.setTitle(consultant.getTitle());
            existedConsultant=updateConsultantCommerciale(existedConsultant,consultantPrivateDTO);
            existedConsultant=updateConsultantGestionnaire(existedConsultant,consultantPrivateDTO);
            existedConsultant.setPassword(encoder.encode(consultant.getPassword()));
            consultantRepository.save(existedConsultant);
            return ResponseEntity.status(HttpStatus.OK).body("Updated!");
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body("Id not found!");
        }
    }

    //Test implemented
    Consultant updateConsultantGestionnaire(Consultant savedConsultant,ConsultantPrivateDTO consultantPrivateDTO){
        if(consultantPrivateDTO.getGestionnaireId()!=null){
            Consultant consultant=ConsultantPrivateMapper.INSTANCE.toEntity(consultantPrivateDTO);
            System.out.println(consultant.getGestionnaireRH().getId());
            savedConsultant.setGestionnaireRH(consultant.getGestionnaireRH());
        }
        else{
            savedConsultant.setGestionnaireRH(null);
        }
        return savedConsultant;
    }
    //Test implemented
    Consultant updateConsultantCommerciale(Consultant savedConsultant,ConsultantPrivateDTO consultantPrivateDTO){
        if(consultantPrivateDTO.getCommercialeId()!=null){
            Consultant consultant=ConsultantPrivateMapper.INSTANCE.toEntity(consultantPrivateDTO);
            savedConsultant.setCommerciale(consultant.getCommerciale());
        }
        else{
            savedConsultant.setCommerciale(null);
        }
        return savedConsultant;
    }
    //Test implemented

    Developper toDevelopper(Consultant consultant){
        Developper developper=new Developper();
        developper.setPassword(consultant.getPassword());
        developper.setName(consultant.getName());
        developper.setUsername(consultant.getUsername());
        developper.setEmail(consultant.getEmail());
        developper.setTitle(consultant.getTitle());
        developper.setLinkedIn(consultant.getLinkedIn());
        developper.setPhone(consultant.getPhone());
        developper.setRoles(consultant.getRoles());
        return developper;
    }

    Commerciale toCommerciale(Consultant consultant){
        Commerciale commerciale=new Commerciale();
        commerciale.setPassword(consultant.getPassword());
        commerciale.setName(consultant.getName());
        commerciale.setUsername(consultant.getUsername());
        commerciale.setEmail(consultant.getEmail());
        commerciale.setTitle(consultant.getTitle());
        commerciale.setLinkedIn(consultant.getLinkedIn());
        commerciale.setPhone(consultant.getPhone());
        commerciale.setRoles(consultant.getRoles());
        return commerciale;

    }

    GestionnaireRH toGestionnaire(Consultant consultant){
        GestionnaireRH gestionnaireRH=new GestionnaireRH();
        gestionnaireRH.setPassword(consultant.getPassword());
        gestionnaireRH.setName(consultant.getName());
        gestionnaireRH.setUsername(consultant.getUsername());
        gestionnaireRH.setEmail(consultant.getEmail());
        gestionnaireRH.setTitle(consultant.getTitle());
        gestionnaireRH.setLinkedIn(consultant.getLinkedIn());
        gestionnaireRH.setPhone(consultant.getPhone());
        gestionnaireRH.setRoles(consultant.getRoles());
        return gestionnaireRH;
    }

    //Test implemented
    @Transactional
    public ResponseEntity<?> deleteConsultant(Long consId) {
        if(consultantRepository.existsById(consId)){
            Consultant consultant=consultantRepository.findById(consId).orElseThrow();

            String role= String.valueOf(consultant.getRoles().iterator().next().getName());
            if(role.equals("ROLE_DEVELOPPER")){
                developperRepository.deleteById(consId);
            }
            else if (role.equals("ROLE_GESTIONNAIRE")) {
                System.out.println(consId);
                GestionnaireRH gestionnaireRH=gesionnaireRHRepository.findById(consId).orElseThrow();
                gestionnaireRH.getConsultantList().forEach(consultant1 -> {consultant1.setGestionnaireRH(null);});
                gesionnaireRHRepository.deleteById(consId);
            }
            else {
                Commerciale commerciale=commercialeRepository.findById(consId).orElseThrow();
                commerciale.getConsultantList().forEach(consultant1 -> {consultant1.setCommerciale(null);});
                commercialeRepository.deleteById(consId);
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body("Deleted!");
    }

    public int getNbAvailableConsultantByComId(Long commercialeId) {
        int nbAvailableConsultant=0;
        List<ConsultantPublicDTO>consultantPublicDTOList= getConsultantDTOByComId(commercialeId);
        for(int i=0;i<consultantPublicDTOList.size();i++){
            if (consultantPublicDTOList.get(i).getAvailable()){
                nbAvailableConsultant+=1;
            }
        }
        return nbAvailableConsultant;
    }

    public Double getChiffreAffaire() {
        Double chiffreAffaire=0D;
        List<Consultant> consultantList=consultantRepository.findAll();
        for(int i=0;i<consultantList.size();i++){
            List<Affectation>affectationList=consultantList.get(i).getAffectationList();
            for(int j=0;j<affectationList.size();j++){
                Affectation affectation=affectationList.get(j);
                if(isCurrent(affectation.getDate_deb(),affectation.getDate_fin())){
                    chiffreAffaire+=affectation.getTjm()*getWorkingDaysUntilToday();
                }
            }
        }
        return chiffreAffaire;
    }


    public int getWorkingDaysUntilToday() {

        List<LocalDate>jourFerieList=callFranceCalendarApi(String.valueOf(LocalDate.now().getYear()));
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        int workingDaysCount = 0;
        // Loop through each day of the current month until today
        for (LocalDate date = firstDayOfMonth; !date.isAfter(today); date = date.plusDays(1)) {
            // Check if the day is not a weekend and not a holiday
            if (!isWeekend(date) && !jourFerieList.contains(date)) {
                workingDaysCount++;
            }
        }
        return workingDaysCount;
    }
    //Test implementd
    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
    //Test implemented
    public List<LocalDate> callFranceCalendarApi(String year) {
        List<LocalDate>jourFerieList=new ArrayList<>();
        WebClient webClient = webClientBuilder.build();
        webClient.get()
                .uri("https://calendrier.api.gouv.fr/jours-feries/metropole/" + year + ".json")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .doOnError(WebClientRequestException.class, e -> {
                    System.err.println("Error fetching holiday data: " + e.getMessage());
                    // You can add more error handling logic here, like logging or retries
                }).flatMap(holidayData -> {
                    holidayData.forEach((date, title) -> {
                        LocalDate localDate = LocalDate.parse(date);
                        jourFerieList.add(localDate);
                    });

                    return Mono.empty(); // You can return a different Mono if needed
                }).block();
        return jourFerieList;
    }

    public int getNbAvailableConsultants() {
        int nbAvailableConsultant=0;
        List<Consultant>consultantList=consultantRepository.findAll();
        for(int i=0;i<consultantList.size();i++ ){
            Consultant consultant=consultantList.get(i);
            List<Affectation>affectationList=consultant.getAffectationList();
            Boolean available=isAvailable(affectationList);
            if(available){
                nbAvailableConsultant+=1;
            }
        }
        return nbAvailableConsultant;
    }
}

