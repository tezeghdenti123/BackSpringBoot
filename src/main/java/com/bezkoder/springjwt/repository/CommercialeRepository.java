package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Commerciale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialeRepository extends JpaRepository<Commerciale,Long> {
}
