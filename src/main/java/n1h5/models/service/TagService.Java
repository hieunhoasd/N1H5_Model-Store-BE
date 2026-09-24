package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import n1h5.models.domain.catalog.Tag;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageRespons;
import n1h5.models.domain.request.TagRequest;
import n1h5.models.domain.response.TagResponse;
import n1h5.models.repository.TagRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private TagResponse mapToResponse(Tag entity) {
        return TagResponse.builder()
                .tagId(entity.getTagId())
                .tagName(entity.getTagName())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "Tag")
    public TagResponse handleCreateTag(TagRequest request) {
        if (this.tagRepository.existsByTagName(request.getTagName())) {
            throw new BusinessException("Tag name đã tồn tại");
        }

        Tag tag = Tag.builder()
                .tagName(request.getTagName())
                .build();

        return mapToResponse(this.tagRepository.save(tag));
    }

    public TagResponse handleGetTagById(Long id) {
        Tag tag = this.tagRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Tag với ID: " + id));
        return mapToResponse(tag);
    }

    public PageResponse handleGetAllTag(Pageable pageable) {
        Page<Tag> pageTag = this.tagRepository.findAll(pageable);

        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageTag.getTotalPages());
        meta.setTotal(pageTag.getTotalElements());

        List<TagResponse> listResponses = pageTag.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(meta);
        res.setResult(listResponses);

        return res;
    }

    @LogActivity(action = "UPDATE", entityName = "Tag")
    public TagResponse handleUpdateTag(Long id, TagRequest request) {
        Tag existingTag = this.tagRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Tag với ID: " + id));

        existingTag.setTagName(request.getTagName());

        return mapToResponse(this.tagRepository.save(existingTag));
    }

    @LogActivity(action = "DELETE", entityName = "Tag")
    public void handleDeleteTagById(Long id) {
        if (!this.tagRepository.existsById(id)) {
            throw new BusinessException("Không tìm thấy Tag với ID: " + id);
        }
        this.tagRepository.deleteById(id);
    }
}