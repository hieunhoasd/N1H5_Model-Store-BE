package n1h5.models.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import n1h5.models.domain.DTO.ManufacturerCreateDTO;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.response.ManufacturerResponse;
import n1h5.models.service.ManufacturerService;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ManufacturerResponse> create(@ModelAttribute ManufacturerCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerService.create(dto));
    }

    @GetMapping
    public ResponseEntity<PageResponse> getAll(Pageable pageable) {
        return ResponseEntity.ok(manufacturerService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(manufacturerService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ManufacturerResponse> update(
            @PathVariable Long id,
            @ModelAttribute ManufacturerCreateDTO dto) {
        return ResponseEntity.ok(manufacturerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        manufacturerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}