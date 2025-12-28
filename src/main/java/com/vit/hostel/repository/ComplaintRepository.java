package com.vit.hostel.repository;

import com.vit.hostel.entity.Complaint;
import com.vit.hostel.entity.Hostel;
import com.vit.hostel.entity.User;
import com.vit.hostel.entity.enums.ComplaintCategory;
import com.vit.hostel.entity.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Student queries
    List<Complaint> findByUserOrderByCreatedAtDesc(User user);

    Page<Complaint> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // Admin queries - by hostel
    List<Complaint> findByHostelOrderByCreatedAtDesc(Hostel hostel);

    Page<Complaint> findByHostelOrderByCreatedAtDesc(Hostel hostel, Pageable pageable);

    // Filter by status
    List<Complaint> findByHostelAndStatusOrderByCreatedAtDesc(Hostel hostel, ComplaintStatus status);

    // Filter by category
    List<Complaint> findByHostelAndCategoryOrderByCreatedAtDesc(Hostel hostel, ComplaintCategory category);

    // Statistics
    long countByHostel(Hostel hostel);

    long countByHostelAndStatus(Hostel hostel, ComplaintStatus status);

    @Query("SELECT c.category, COUNT(c) FROM Complaint c WHERE c.hostel = :hostel GROUP BY c.category")
    List<Object[]> countByHostelGroupByCategory(@Param("hostel") Hostel hostel);

    @Query("SELECT c.status, COUNT(c) FROM Complaint c WHERE c.hostel = :hostel GROUP BY c.status")
    List<Object[]> countByHostelGroupByStatus(@Param("hostel") Hostel hostel);

    // Recent complaints
    List<Complaint> findTop10ByHostelOrderByCreatedAtDesc(Hostel hostel);

    // Resolved this month
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.hostel = :hostel AND c.status = 'RESOLVED' AND c.resolvedAt >= :startOfMonth")
    long countResolvedThisMonth(@Param("hostel") Hostel hostel, @Param("startOfMonth") LocalDateTime startOfMonth);

    // For ticket number generation
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.hostel = :hostel AND YEAR(c.createdAt) = :year")
    long countByHostelAndYear(@Param("hostel") Hostel hostel, @Param("year") int year);
}
