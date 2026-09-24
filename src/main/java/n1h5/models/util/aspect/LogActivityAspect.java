package n1h5.models.util.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import n1h5.models.domain.response.BrandResponse;
import n1h5.models.domain.response.CarGenerationResponse;
import n1h5.models.domain.response.CarSeriesResponse;
import n1h5.models.domain.response.CarVariantResponse;
import n1h5.models.domain.response.CategoryResponse;
import n1h5.models.domain.response.ColorResponse;
import n1h5.models.domain.response.ManufacturerResponse;
import n1h5.models.domain.response.ScaleResponse;
import n1h5.models.service.AuditLogService;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.SecurityUtil.SecurityUtil;

@Aspect
@Component
public class LogActivityAspect {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public LogActivityAspect(AuditLogService auditLogService, ObjectMapper objectMapper) {
        this.auditLogService = auditLogService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(logActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {
        // Thực thi phương thức chính trước
        Object result = joinPoint.proceed();

        try {
            // 1. Lấy userId (Integer) từ Security Context và ép kiểu sang Long
            Integer userId = SecurityUtil.getCurrentUserId(); 
            String action = logActivity.action();
            String entityName = logActivity.entityName();
            Long entityId = null;
            String oldValue = null;
            String newValue = null;

            // 2. Trích xuất thông tin Entity ID và New Value dựa trên kết quả trả về
           if (result != null) {
                newValue = objectMapper.writeValueAsString(result);

                if (result instanceof ManufacturerResponse res) {
                    entityId = res.getManufacturerId();
                } else if (result instanceof BrandResponse b) {
                    entityId = b.getBrandId();
                } else if (result instanceof CategoryResponse c) {
                    entityId = c.getCategoryId();
                } else if (result instanceof ScaleResponse s) {
                    entityId = s.getScaleId();
                } else if (result instanceof CarSeriesResponse cs) {
                    entityId = cs.getSeriesId();
                } else if (result instanceof CarGenerationResponse cg) {
                    entityId = cg.getGenerationId();
                } else if (result instanceof CarVariantResponse cv) {
                    entityId = cv.getVariantId();
                } else if (result instanceof ColorResponse col) {
                    entityId = col.getColorId();
                }
            }

            // Nếu là hành động DELETE, lấy ID từ tham số đầu vào của phương thức
            if ("DELETE".equalsIgnoreCase(action)) {
                Object[] args = joinPoint.getArgs();
                if (args.length > 0 && args[0] instanceof Long id) {
                    entityId = id;
                }
            }

            // 3. Gọi hàm saveLog bất đồng bộ
            auditLogService.saveLog(userId, action, entityName, entityId, oldValue, newValue);
        } catch (Exception e) {
            // Log lỗi hệ thống nhưng không làm dừng luồng nghiệp vụ chính
            System.err.println("Lỗi ghi AuditLog: " + e.getMessage());
        }
        return result;
    }
}