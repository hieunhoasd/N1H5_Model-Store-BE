package n1h5.models.service;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import n1h5.models.domain.catalog.Scale;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.repository.ScaleRepository;
import n1h5.models.util.Exception.BusinessException;

@Service 
public class ScaleService {
    private final ScaleRepository scaleRepository;
    public ScaleService(ScaleRepository scaleRepository){
        this.scaleRepository=scaleRepository;
    }

    public Scale handleCreateScale(Scale scale) {
        Scale newScale = new Scale();
        newScale.setScaleId(scale.getScaleId());
        newScale.setScaleName(scale.getScaleName());
        return newScale;
    }

    public Scale handleGetScaleById(Long id) {
        Optional<Scale>  optionalSc=this.scaleRepository.findById(id);
        if(optionalSc.isEmpty()){
            throw new BusinessException("khong tim thay Scale voi id "+id);
        }
        return optionalSc.get();
    }

    public PageResponse handleGetAllScale(Pageable pageable) {
        Page<Scale> pageScale=this.scaleRepository.findAll(pageable);
        Meta meta=new Meta();
         meta.setPage(pageable.getPageNumber() + 1); 
         meta.setPageSize(pageable.getPageSize());
         meta.setPages(pageScale.getTotalPages());
         meta.setTotal(pageScale.getTotalElements());
         
        PageResponse res=new PageResponse<>();
        res.setMeta(meta);
        res.setResult(pageScale.getContent());
        
        return res;
    }

    public Scale handleUpdateScale(Scale scale) {
        Scale newScale= this.handleGetScaleById(scale.getScaleId());
        if(newScale==null){
            return null;
        }
        newScale.setScaleName(scale.getScaleName());
        this.scaleRepository.save(newScale);
        return newScale;
    }

    public void handleDeleteScaleById(Long id) {
        this.scaleRepository.deleteById(id);
    }
    









}
