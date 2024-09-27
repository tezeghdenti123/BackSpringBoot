package com.bezkoder.springjwt.repositories;

import com.bezkoder.springjwt.models.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant,Long> {

}
