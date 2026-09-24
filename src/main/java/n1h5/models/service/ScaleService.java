package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import n1h5.models.domain.catalog.Scale;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.ScaleRequest;
import n1h5.models.domain.response.ScaleResponse;
import n1h5.models.repository.ScaleRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service 
public class ScaleService {

    private final ScaleRepository scaleRepository;

    public ScaleService(ScaleRepository scaleRepository) {
        this.scaleRepository = scaleRepository;
    }

    private ScaleResponse mapToResponse(Scale entity) {
        return ScaleResponse.builder()
                .scaleId(entity.getScaleId())
                .scaleName(entity.getScaleName())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "Scale")
    public ScaleResponse handleCreateScale(ScaleRequest request) {
        Scale scale = Scale.builder()
                .scaleName(request.getScaleName())
                .build();

        Scale savedScale = this.scaleRepository.save(scale);
        return mapToResponse(savedScale);
    }

    public ScaleResponse handleGetScaleById(Long id) {
        Scale scale = this.scaleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Scale với ID: " + id));
        return mapToResponse(scale);
    }

    public PageResponse handleGetAllScale(Pageable pageable) {
        Page<Scale> pageScale = this.scaleRepository.findAll(pageable);

        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1); 
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageScale.getTotalPages());
        meta.setTotal(pageScale.getTotalElements());

        List<ScaleResponse> listResponses = pageScale.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(meta);
        res.setResult(listResponses);

        return res;
    }

    @LogActivity(action = "UPDATE", entityName = "Scale")
    public ScaleResponse handleUpdateScale(Long id, ScaleRequest request) {
        Scale existingScale = this.scaleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Scale với ID: " + id));

        existingScale.setScaleName(request.getScaleName());

        Scale updatedScale = this.scaleRepository.save(existingScale);
        return mapToResponse(updatedScale);
    }

    @LogActivity(action = "DELETE", entityName = "Scale")
    public void handleDeleteScaleById(Long id) {
        if (!this.scaleRepository.existsById(id)) {
            throw new BusinessException("Không tìm thấy Scale với ID: " + id);
        }
        this.scaleRepository.deleteById(id);
    }
}