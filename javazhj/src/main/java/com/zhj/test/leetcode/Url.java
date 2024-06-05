package com.zhj.test.leetcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Url {
    public static void main(String[] args) {
        //String url = "https://www.zxhtest.com";
        String url = "http://8.130.87.167:8899/#/id-1";
        String filePath = "C:\\Users\\张洪俊\\Desktop\\张小寒\\test.png";
        int width = 300;
        int height = 300;

        try {
            generateQRCode(url, filePath, width, height);
            System.out.println("QR Code generated successfully!");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateQRCode(String url, String filePath, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}