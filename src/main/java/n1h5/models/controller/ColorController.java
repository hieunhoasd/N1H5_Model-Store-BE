package n1h5.models.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.ColorRequest;
import n1h5.models.domain.response.ColorResponse;
import n1h5.models.service.ColorService;

@RestController
@RequestMapping("/api/v1/colors")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping
    public ResponseEntity<ColorResponse> create(@RequestBody ColorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colorService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse> getAll(Pageable pageable) {
        return ResponseEntity.ok(colorService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(colorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColorResponse> update(
            @PathVariable Long id,
            @RequestBody ColorRequest request) {
        return ResponseEntity.ok(colorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        colorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}