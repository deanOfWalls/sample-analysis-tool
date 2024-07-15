package com.yourname.sample_analysis_tool.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yourname.sample_analysis_tool.model.ChemicalSample;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SampleService {

    public ChemicalSample generateSample(String chemicalId, String chemicalName) {
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HHmm"));
        String date = now.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        String sampleId = String.format("%s-%s-%s", chemicalId, time, date.replace("-", ""));

        // Generate QR code as Base64
        String qrCode = generateQRCode(sampleId);

        ChemicalSample sample = new ChemicalSample();
        sample.setChemicalName(chemicalName);
        sample.setTime(now.format(DateTimeFormatter.ofPattern("HH:mm")));
        sample.setDate(date);
        sample.setSampleId(sampleId);
        sample.setQrCode(qrCode);

        return sample;
    }

    private String generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 200;
        int height = 200;
        String imageFormat = "png";

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, imageFormat, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
