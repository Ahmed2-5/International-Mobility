package tn.esprit.spring.controllers;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.entities.WaitingList;
import tn.esprit.spring.interfaces.IAppointementService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/qrcode")
public class QRCodeGenerator {
    @Autowired
    // @Qualifier("AppointementServiceImpl")
    IAppointementService appointementService;

    public static BufferedImage generateQRCodeImageForWaitingList(WaitingList waitingList) throws WriterException, IOException {
        String codeText = String.format("idWaiting: %d, Paiment: %.2f, PhoneNumber: %d, email: %s, dateDemande: %s",
                waitingList.getIdWaiting(),
                waitingList.getPaiment(),
                waitingList.getPhoneNumber(),
                waitingList.getEmail(),
                waitingList.getDateDemande().toString()
        );
        byte[] qrCodeBytes = generateQRCode(codeText, 350, 350);
        return ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
    }


    public static byte[] generateQRCode(String codeText, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


   /* @GetMapping(value = "/displayQRCode/{idWaiting}")
    public ResponseEntity<byte[]> displayQRCode(@PathVariable("idWaiting") Integer idWaiting)
            throws Exception {
        WaitingList waitingList = appointementService.getWaitingListById(idWaiting);
        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImageForWaitingList(waitingList);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageBytes, headers, HttpStatus.OK);
    }*/
}
