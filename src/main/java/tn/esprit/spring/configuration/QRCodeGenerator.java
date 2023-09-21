package tn.esprit.spring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import tn.esprit.spring.entities.Opportunity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeGenerator {
    public static byte[] generateQRCodeImage(Opportunity opportunity) throws WriterException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("id", opportunity.getId_Opportunity());
        jsonNode.put("name", opportunity.getCreatedBy().getUnyName());
        jsonNode.put("description", opportunity.getDescription());
        jsonNode.put("endDate", opportunity.getEndDate().toString());
        // Omit start date from QR code
        // jsonNode.put("startDate", opportunity.getStartDate().toString());

        String jsonString = mapper.writeValueAsString(jsonNode);

        int size = 250;
        BitMatrix bitMatrix = new QRCodeWriter().encode(jsonString, BarcodeFormat.QR_CODE, size, size);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        return baos.toByteArray();
    }


}
