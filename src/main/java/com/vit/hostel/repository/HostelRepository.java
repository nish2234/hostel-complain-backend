package com.vit.hostel.repository;

import com.vit.hostel.entity.Hostel;
import com.vit.hostel.entity.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long> {
    List<Hostel> findByGender(Gender gender);

    Optional<Hostel> findByCode(String code);

    Optional<Hostel> findByAdminEmail(String adminEmail);

    boolean existsByAdminEmail(String adminEmail);
}
