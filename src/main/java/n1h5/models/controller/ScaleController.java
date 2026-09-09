package n1h5.models.controller;

import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import n1h5.models.service.ScaleService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import n1h5.models.domain.catalog.Scale;
import n1h5.models.domain.pagination.PageResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping("/api/v1/Scale")
public class ScaleController {
    
    private final ScaleService scaleService;
    public ScaleController(ScaleService scaleService){
        this.scaleService=scaleService;
    }

    @PostMapping("/createScale")
    public ResponseEntity<Scale> CreateScale(@RequestBody Scale scale) {
        return ResponseEntity.ok().body(this.scaleService.handleCreateScale(scale));
    }
    
    @GetMapping("/getScale/{id}")
    public ResponseEntity<Scale> GetScaleById(@PathVariable ("id") Long id) {
        return ResponseEntity.ok().body(this.scaleService.handleGetScaleById(id));
    }

    @GetMapping("/getScale")
    public ResponseEntity<PageResponse> GetAllScale(Pageable pageable){
        PageResponse page = this.scaleService.handleGetAllScale(pageable);
        return ResponseEntity.ok().body(page);
    }
    
    @PutMapping("/updateScale")
    public ResponseEntity<Scale> UpdateScale(@RequestBody Scale scale) {
        
        return ResponseEntity.ok().body(this.scaleService.handleUpdateScale(scale));
    }

    @DeleteMapping("/deleteScale")
    public ResponseEntity<String> DeleteScaleById(@PathVariable ("id") Long id){
        this.scaleService.handleDeleteScaleById(id);
        return ResponseEntity.ok().body("delete success");
    }
    

}
