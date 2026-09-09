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
import n1h5.models.domain.request.CarGenerationRequest;
import n1h5.models.domain.response.CarGenerationResponse;
import n1h5.models.service.CarGenerationService;

@RestController
@RequestMapping("/api/v1/car-generations")
public class CarGenerationController {

    private final CarGenerationService carGenerationService;

    public CarGenerationController(CarGenerationService carGenerationService) {
        this.carGenerationService = carGenerationService;
    }

    @PostMapping
    public ResponseEntity<CarGenerationResponse> create(@RequestBody CarGenerationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carGenerationService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse> getAll(Pageable pageable) {
        return ResponseEntity.ok(carGenerationService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarGenerationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carGenerationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarGenerationResponse> update(
            @PathVariable Long id,
            @RequestBody CarGenerationRequest request) {
        return ResponseEntity.ok(carGenerationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carGenerationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}