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
import n1h5.models.domain.request.CarVariantRequest;
import n1h5.models.domain.response.CarVariantResponse;
import n1h5.models.service.CarVariantService;

@RestController
@RequestMapping("/api/v1/car-variants")
public class CarVariantController {

    private final CarVariantService carVariantService;

    public CarVariantController(CarVariantService carVariantService) {
        this.carVariantService = carVariantService;
    }

    @PostMapping
    public ResponseEntity<CarVariantResponse> create(@RequestBody CarVariantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carVariantService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse> getAll(Pageable pageable) {
        return ResponseEntity.ok(carVariantService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarVariantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carVariantService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarVariantResponse> update(
            @PathVariable Long id,
            @RequestBody CarVariantRequest request) {
        return ResponseEntity.ok(carVariantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carVariantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}