package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Distributor;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.DistributorRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/distributor")
public class DistributorController {
    @Autowired
    DistributorRepository distributorRepository;

    @GetMapping
    public HttpEntity<?> getAllDistributors(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(distributorRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneDistributor(@PathVariable Long id) {
        Optional<Distributor> optionalDistributor = distributorRepository.findById(id);
        Distributor distributor = optionalDistributor.orElse(null);
        return ResponseEntity.status(distributor != null ? 200 : 404).body(distributor);
    }

    @PostMapping
    public HttpEntity<?> saveDistributor(@RequestBody Distributor distributor){
        Distributor savedDistributor = distributorRepository.save(distributor);
        return ResponseEntity.status(savedDistributor != null ? 201 : 409).body(savedDistributor);
    }

    @PutMapping("/id")
    public HttpEntity<?> editDistributor(@RequestBody Distributor distributor, @PathVariable Long id){
        Optional<Distributor> optionalDistributor = distributorRepository.findById(id);
        Distributor editingDistributor = optionalDistributor.orElse(null);
        if (editingDistributor == null)
            return ResponseEntity.status(404).body("Distributor not found");

        editingDistributor.setName(distributor.getName());
        editingDistributor.setDescription(distributor.getDescription());

        Distributor savedDistributor = distributorRepository.save(editingDistributor);
        return ResponseEntity.status(savedDistributor != null ? 202 : 409).body(savedDistributor);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDistributor(@PathVariable Long id){
        try {
            distributorRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }


}
