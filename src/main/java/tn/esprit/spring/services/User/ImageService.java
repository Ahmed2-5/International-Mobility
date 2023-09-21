package tn.esprit.spring.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.controllers.ImageUploadResponse;
import tn.esprit.spring.entities.Image;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repositories.ImageRepository;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.util.ImageUtility;

import java.io.IOException;
import java.util.Optional;
@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userDao;


    public ResponseEntity<ImageUploadResponse> uplaodImage(MultipartFile file, String UserName)
            throws IOException {
        Image image1=new Image();
        image1.setImage(ImageUtility.compressImage(file.getBytes()));
        image1.setType(file.getContentType());
        image1.setName(file.getOriginalFilename());
        User user= userDao.findByUserName(UserName);
       /* imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes())).build());*/
        imageRepository.save(image1);
        user.setPhoto(image1);
        userDao.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));


    }


    public Image getImageDetails(String name) throws IOException {


        final Optional<Image> dbImage = imageRepository.findByName(name);

        return Image.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .image(ImageUtility.decompressImage(dbImage.get().getImage())).build();
    }

    public ResponseEntity<byte[]> getImage(String name) throws IOException {


        final Optional<Image> dbImage = imageRepository.findByName(name);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getImage()));    }
}
