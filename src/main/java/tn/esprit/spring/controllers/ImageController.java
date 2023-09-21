package tn.esprit.spring.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.entities.Image;
import tn.esprit.spring.services.User.ImageService;

import java.io.IOException;

@RestController
@AllArgsConstructor
//@RequestMapping("/imageUpload")
//@CrossOrigin(origins = "http://localhost:8082") open for specific port
@CrossOrigin() // open for all ports
public class ImageController {

    ImageService iimageService;

    @PutMapping("/image")
    public ResponseEntity<ImageUploadResponse> uplaodImage(@RequestParam("image") MultipartFile file,@RequestParam("UserName") String UserName)
            throws IOException {

        return   iimageService.uplaodImage(file,UserName);
    }

    @GetMapping(path = {"/get/info/{name}"})
    public Image getImageDetails(@PathVariable("name") String name) throws IOException {

        return iimageService.getImageDetails(name);
    }

    @GetMapping(path = {"/get/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        return iimageService.getImage(name);
    }

}
