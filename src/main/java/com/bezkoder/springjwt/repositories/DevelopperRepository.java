package com.bezkoder.springjwt.repositories;

import com.bezkoder.springjwt.models.Developper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevelopperRepository extends JpaRepository<Developper,Long> {
}
