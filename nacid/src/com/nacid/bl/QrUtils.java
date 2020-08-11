package com.nacid.bl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nacid.bl.impl.Utils;
import net.glxn.qrgen.javase.QRCode;
import sun.misc.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Georgi
 * Date: 6.7.2020 г.
 * Time: 13:51
 */
public class QrUtils {



    /*public static InputStream generateQRCodeImage(String barcodeText) {
        try {
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, new Integer(1));
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 145, 145, hintMap);

            BufferedImage img = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (WriterException | IOException e) {
            throw Utils.logException(e);
        }
    }*/
    public static InputStream generateQRCodeImage(String barcodeText) {
        ByteArrayOutputStream stream = QRCode
                .from(barcodeText)
                .withSize(145, 145)
                .withHint(EncodeHintType.MARGIN, 1)
                .stream();
        return new ByteArrayInputStream(stream.toByteArray());
    }
}
