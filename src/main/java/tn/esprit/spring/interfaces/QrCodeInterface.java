package tn.esprit.spring.interfaces;

public interface QrCodeInterface {
    public byte[] generateQRCodeForOpportunity(Integer idOpportunity) throws Exception;
}
