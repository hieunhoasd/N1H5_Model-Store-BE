package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import n1h5.models.domain.catalog.Color;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.ColorRequest;
import n1h5.models.domain.response.ColorResponse;
import n1h5.models.repository.ColorRepository;
import n1h5.models.util.Exception.BusinessException;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    private ColorResponse mapToResponse(Color entity) {
        return ColorResponse.builder()
                .colorId(entity.getColorId())
                .colorName(entity.getColorName())
                .hex(entity.getHex())
                .build();
    }

    // @LogActivity(action = "CREATE", entityName = "Color")
    public ColorResponse create(ColorRequest request) {
        Color color = Color.builder()
                .colorName(request.getColorName())
                .hex(request.getHex())
                .build();

        return mapToResponse(colorRepository.save(color));
    }

    public PageResponse getAll(Pageable pageable) {
        Page<Color> pageColors = colorRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageColors.getTotalPages());
        mt.setTotal(pageColors.getTotalElements());

        List<ColorResponse> listResponses = pageColors.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);
        return res;
    }

    public ColorResponse getById(Long id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Color not found with id: " + id));
        return mapToResponse(color);
    }

    // @LogActivity(action = "UPDATE", entityName = "Color")
    public ColorResponse update(Long id, ColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Color not found with id: " + id));

        color.setColorName(request.getColorName());
        color.setHex(request.getHex());

        return mapToResponse(colorRepository.save(color));
    }

    // @LogActivity(action = "DELETE", entityName = "Color")
    public void delete(Long id) {
        if (!colorRepository.existsById(id)) {
            throw new BusinessException("Color not found with id: " + id);
        }
        colorRepository.deleteById(id);
    }
}