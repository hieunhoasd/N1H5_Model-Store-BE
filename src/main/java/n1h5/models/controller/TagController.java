package n1h5.models.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.TagRequest;
import n1h5.models.domain.response.TagResponse;
import n1h5.models.service.TagService;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/createTag")
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        TagResponse response = this.tagService.handleCreateTag(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTag/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable("id") Long id) {
        TagResponse tag = this.tagService.handleGetTagById(id);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/getTag")
    public ResponseEntity<PageResponse> getAllTag(Pageable pageable) {
        PageResponse page = this.tagService.handleGetAllTag(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/updateTag/{id}")
    public ResponseEntity<TagResponse> updateTag(
            @PathVariable("id") Long id,
            @Valid @RequestBody TagRequest request) {
        TagResponse updatedTag = this.tagService.handleUpdateTag(id, request);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/deleteTag/{id}")
    public ResponseEntity<String> deleteTagById(@PathVariable("id") Long id) {
        this.tagService.handleDeleteTagById(id);
        return ResponseEntity.ok("delete success");
    }
}