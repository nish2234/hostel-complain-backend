package com.vit.hostel.repository;

import com.vit.hostel.entity.Attachment;
import com.vit.hostel.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByComplaint(Complaint complaint);
}
