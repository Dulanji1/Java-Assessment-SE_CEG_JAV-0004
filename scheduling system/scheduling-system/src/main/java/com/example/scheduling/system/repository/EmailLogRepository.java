package com.example.scheduling.system.repository;

import com.example.scheduling.system.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    List<EmailLog> findByUserId(Long userId);
    List<EmailLog> findByTaskId(Long taskId);
}
