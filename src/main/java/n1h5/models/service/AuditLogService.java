package n1h5.models.service;

import n1h5.models.domain.audit.AuditLog;
import n1h5.models.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Async // Chạy bất đồng bộ để không ảnh hưởng tới tốc độ phản hồi API
    public void saveLog(Integer userId, String action, String entityName, Long entityId, String oldValue, String newValue) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .oldValue(oldValue)
                .newValue(newValue)
                .build();
        auditLogRepository.save(log);
    }
}