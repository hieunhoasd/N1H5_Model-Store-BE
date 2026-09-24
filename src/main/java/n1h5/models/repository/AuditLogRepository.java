package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.audit.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
}