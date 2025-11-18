package com.projeto.max.generic;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

public class GeradorQrCode {

    public static String gerarQrCodeBase64(String texto, int largura, int altura) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(
                texto,
                BarcodeFormat.QR_CODE,
                largura,
                altura
        );

        BufferedImage imagem = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagem, "png", baos);

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
