package org.example.back_end.controller;

import org.example.back_end.entity.Pet;
import org.example.back_end.repo.PetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "http://localhost:63342") // allow frontend
public class PetController {

    private final PetRepository petRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    // ---------------- CREATE pet ----------------
    @PostMapping("/post")
    public ResponseEntity<Pet> postPet(
            @RequestParam("petName") String petName,
            @RequestParam("petType") String petType,
            @RequestParam("petBreed") String petBreed,
            @RequestParam("petAge") String petAge,
            @RequestParam("petGender") String petGender,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam("petImage") MultipartFile petImage
    ) throws IOException {

        // Ensure upload folder exists
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
            throw new IOException("Failed to create upload directory: " + uploadDir);
        }

        // Save the uploaded image with unique filename
        String fileName = System.currentTimeMillis() + "_" + petImage.getOriginalFilename();
        File file = new File(uploadFolder, fileName);
        petImage.transferTo(file);

        // Generate public URL for frontend
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        // Create Pet entity
        Pet pet = new Pet();
        pet.setPetName(petName);
        pet.setPetType(petType);
        pet.setPetBreed(petBreed);
        pet.setPetAge(petAge);
        pet.setPetGender(petGender);
        pet.setLocation(location);
        pet.setDescription(description);
        pet.setImageUrl(imageUrl);

        // Save to DB
        Pet savedPet = petRepository.save(pet);
        return ResponseEntity.ok(savedPet);
    }

    // ---------------- READ all pets ----------------
    @GetMapping
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // ---------------- READ one pet ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------- DELETE pet ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id) {
        if (!petRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        petRepository.deleteById(id);
        return ResponseEntity.ok("Pet post deleted successfully.");
    }
}
