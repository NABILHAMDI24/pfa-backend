package com.example.idscanner.service;

import com.example.idscanner.model.CnieData;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

@Service
public class IdentityCardService {

  public CnieData processImage(MultipartFile image) throws IOException {
    // Save the image to a temporary file
    Path path = Paths.get("src/main/resources/static/uploads/" + image.getOriginalFilename());
    Files.createDirectories(path.getParent()); // Create the directory if it doesn't exist
    Files.write(path, image.getBytes());

    // Load the image
    BufferedImage fullImage = ImageIO.read(new File(path.toUri()));

    // Perform OCR on the entire image
    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("C:\\Users\\Dell\\pfa\\idscanner\\src\\main\\resources\\tessdata"); // Path to tessdata directory
    tesseract.setLanguage("fra"); // Set language to French

    CnieData cnieData = new CnieData();
    try {
      String ocrResult = tesseract.doOCR(fullImage).trim(); // Perform OCR on the entire image

      // Debug: Print the OCR result
      System.out.println("OCR Result:\n" + ocrResult);

      // Extract information from the OCR result using regular expressions
      cnieData.setName(extractField(ocrResult, "Nom:\\s*([A-Z]+)")); // Extract name
      cnieData.setSurname(extractField(ocrResult, "Prénom:\\s*([A-Z]+)")); // Extract surname
      cnieData.setBirthdate(extractField(ocrResult, "Né le\\s*(\\d{2}\\.\\d{2}\\.\\d{4})")); // Extract birthdate
      cnieData.setCardNumber(extractField(ocrResult, "CIN:\\s*([A-Z0-9]+)")); // Extract card number

      // Debug: Print extracted data
      System.out.println("Name: " + cnieData.getName());
      System.out.println("Surname: " + cnieData.getSurname());
      System.out.println("Birthdate: " + cnieData.getBirthdate());
      System.out.println("Card Number: " + cnieData.getCardNumber());
    } catch (TesseractException e) {
      throw new IOException("Error during OCR processing.", e);
    }

    return cnieData;
  }

  private String extractField(String text, String regex) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      // Check if the group exists before accessing it
      if (matcher.groupCount() >= 1) {
        return matcher.group(1).trim(); // Return the matched group
      }
    }
    return null; // Return null if no match is found
  }
}