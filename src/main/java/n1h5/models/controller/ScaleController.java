package n1h5.models.controller;

import jakarta.validation.Valid;
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

import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.ScaleRequest;
import n1h5.models.domain.response.ScaleResponse;
import n1h5.models.service.ScaleService;

@RestController
@RequestMapping("/api/v1/scale")
public class ScaleController {

    private final ScaleService scaleService;

    public ScaleController(ScaleService scaleService) {
        this.scaleService = scaleService;
    }

    @PostMapping("/createScale")
    public ResponseEntity<ScaleResponse> createScale(@Valid @RequestBody ScaleRequest request) {
        ScaleResponse response = this.scaleService.handleCreateScale(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getScale/{id}")
    public ResponseEntity<ScaleResponse> getScaleById(@PathVariable("id") Long id) {
        ScaleResponse scale = this.scaleService.handleGetScaleById(id);
        return ResponseEntity.ok(scale);
    }

    @GetMapping("/getScale")
    public ResponseEntity<PageResponse> getAllScale(Pageable pageable) {
        PageResponse page = this.scaleService.handleGetAllScale(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/updateScale/{id}")
    public ResponseEntity<ScaleResponse> updateScale(
            @PathVariable("id") Long id,
            @Valid @RequestBody ScaleRequest request) {
        ScaleResponse updatedScale = this.scaleService.handleUpdateScale(id, request);
        return ResponseEntity.ok(updatedScale);
    }

    @DeleteMapping("/deleteScale/{id}")
    public ResponseEntity<String> deleteScaleById(@PathVariable("id") Long id) {
        this.scaleService.handleDeleteScaleById(id);
        return ResponseEntity.ok("delete success");
    }
}