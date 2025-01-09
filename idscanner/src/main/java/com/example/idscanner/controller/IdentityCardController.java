package com.example.idscanner.controller;

import com.example.idscanner.model.CnieData;
import com.example.idscanner.service.IdentityCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class IdentityCardController {

  @Autowired
  private IdentityCardService identityCardService;

  @PostMapping("/upload")
  public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
    if (image.isEmpty()) {
      return ResponseEntity.badRequest().body("Please select an image to upload.");
    }

    try {
      // Process the image and extract the French part of the text
      CnieData cnieData = identityCardService.processImage(image);
      return ResponseEntity.ok(cnieData); // Return the extracted data as JSON
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Error processing the image: " + e.getMessage());
    }
  }
}