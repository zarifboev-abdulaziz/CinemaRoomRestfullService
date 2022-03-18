package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.PriceCategory;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.HallDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.PriceCategoryRepository;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/price-category")
public class PriceCategoryController {
    @Autowired
    PriceCategoryRepository priceCategoryRepository;

    @GetMapping
    public HttpEntity<?> getAllPriceCategories(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(priceCategoryRepository.findAll(pageable));
    }

    @GetMapping("/{priceCategoryId}")
    public HttpEntity<?> getOneHall(@PathVariable Long priceCategoryId) throws IOException {
        Optional<PriceCategory> optionalPriceCategory = priceCategoryRepository.findById(priceCategoryId);
        PriceCategory priceCategory = optionalPriceCategory.orElse(null);
        return ResponseEntity.status(priceCategory != null ? 200 : 404).body(priceCategory);
    }

    @DeleteMapping("/{priceCategoryId}")
    public HttpEntity<?> deletePriceCategory(@PathVariable Long priceCategoryId){
        try {
            priceCategoryRepository.deleteById(priceCategoryId);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> savePriceCategory(@RequestBody PriceCategory priceCategory){
        PriceCategory savedPriceCategory = priceCategoryRepository.save(priceCategory);
        return ResponseEntity.status(201).body(new ApiResponse("Successfully Saved", true, savedPriceCategory));
    }

    @PutMapping("/{priceCategoryId}")
    public HttpEntity<?> editPriceCategory(@PathVariable Long priceCategoryId, @RequestBody PriceCategory priceCategory){
        Optional<PriceCategory> optionalPriceCategory = priceCategoryRepository.findById(priceCategoryId);
        if (!optionalPriceCategory.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Price Category not found", false));

        PriceCategory editingPriceCategory = optionalPriceCategory.get();
        editingPriceCategory.setName(priceCategory.getName());
        editingPriceCategory.setColor(priceCategory.getColor());
        editingPriceCategory.setAdditionalFeePercentage(priceCategory.getAdditionalFeePercentage());

        PriceCategory editedPriceCategory = priceCategoryRepository.save(editingPriceCategory);
        return ResponseEntity.status(201).body(new ApiResponse("Successfully Edited", true, editedPriceCategory));
    }



}
