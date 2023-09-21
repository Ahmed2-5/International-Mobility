package tn.esprit.spring.services.User;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService  {

    public void uploadfile(MultipartFile file) throws IllegalStateException, IOException {
        file.transferTo(new File("C:\\PiDevFiles\\"+file.getOriginalFilename()));
    }


}
