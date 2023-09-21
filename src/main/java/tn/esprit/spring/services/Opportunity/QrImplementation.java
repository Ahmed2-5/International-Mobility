package tn.esprit.spring.services.Opportunity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.interfaces.QrCodeInterface;
import tn.esprit.spring.repositories.OpportunityRepository;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
public class QrImplementation implements QrCodeInterface {
    @Autowired
    OpportunityRepository opportunityRepository;
    @Override
    public byte[] generateQRCodeForOpportunity(Integer idOpportunity) throws Exception {
        Opportunity opportunity = opportunityRepository.findById(idOpportunity).orElse(null);
        if (opportunity == null) {
            throw new Exception("Opportunity not found");
        }

        // Création du contenu du QR code
        String qrCodeContent =
                "\nName Opportunity: " + opportunity.getTitle()
                        + "\nType: " + opportunity.getType()
                        + "\nDescription: " + opportunity.getDescription()
                        + "\nStart date: " + opportunity.getStarDate()
                        + "\nEnd date: " + opportunity.getEndDate()
                        + "\nSpeciality: " + opportunity.getSpecialite()
                        + "\nNote Elimonatoire: " + opportunity.getNeeds();


        // Génération du QR code en utilisant la bibliothèque QRGen
        ByteArrayOutputStream
                baos = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 250, 250);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
        return baos.toByteArray();
    }
}
