package n1h5.models.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import n1h5.models.domain.catalog.CarSeries;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.CarSeriesRequest;
import n1h5.models.domain.response.CarSeriesResponse;
import n1h5.models.service.CarSeriesService;

@RestController 
@RequestMapping("/api/v1/CarSeries")
public class CarSeriesController {
    private final CarSeriesService carSeriesService;
    public CarSeriesController(CarSeriesService carSeriesService){
        this.carSeriesService=carSeriesService;
    }

    @PostMapping("/creatCarSeries")
    public ResponseEntity<CarSeriesResponse> CreateCarSeries(@RequestBody CarSeriesRequest carSeries){
        return ResponseEntity.ok().body(this.carSeriesService.handleCreateCarSeriesService(carSeries));
    }
    // Create

    // Read All (có Phân trang)
    @GetMapping("/")
    public ResponseEntity<PageResponse> getAll(Pageable pageable) {
        return ResponseEntity.ok(carSeriesService.getAll(pageable));
    }

    // Read Detail
    @GetMapping("/{id}")
    public ResponseEntity<CarSeriesResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carSeriesService.getById(id));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<CarSeriesResponse> update(
            @PathVariable Long id,
            @RequestBody CarSeriesRequest request) {
        return ResponseEntity.ok(carSeriesService.update(id, request));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.carSeriesService.delete(id);
        return ResponseEntity.ok().body("delete success");
    }


}
