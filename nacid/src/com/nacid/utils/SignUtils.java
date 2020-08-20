package com.nacid.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nacid.bl.signature.RequestJson;
import com.nacid.bl.signature.SignatureParams;
import com.nacid.bl.signature.SuccessSign;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by denislav.veizov on 8.1.2018 г..
 */
public class SignUtils {


    public static SuccessSign readSuccessSign(String successSign, String secretKey){
        try {
            ObjectMapper mapper = new ObjectMapper();
            RequestJson requestJson = mapper.readValue(successSign, RequestJson.class);
            String encodedParams = requestJson.getEncodedParams();
            String hmac = requestJson.getHmac();

            String calculatedHmac = createHmac(encodedParams, secretKey);
            if (!calculatedHmac.equalsIgnoreCase(requestJson.getHmac()))
                throw new RuntimeException("Calculated Hmac don't match with response Hmac!\n Calculated Hmac: " + calculatedHmac + "\n Response Hmac: " + hmac);

            String decodedParams = new String(Base64.decodeBase64(encodedParams.getBytes()));
            return  mapper.readValue(decodedParams, SuccessSign.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String createRequestJson(SignatureParams signatureParams, String secretKey){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonParams = mapper.writeValueAsString(signatureParams);
            String jsonParamsEncoded = new String(Base64.encodeBase64(jsonParams.getBytes("UTF-8")));
            String hmac = createHmac(jsonParamsEncoded, secretKey);
            return mapper.writeValueAsString(new RequestJson(hmac, jsonParamsEncoded));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String createHmac(String base64EncodedString, String secretKey) {
        String hash;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            hash = new String(Base64.encodeBase64(sha256_HMAC.doFinal(base64EncodedString.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return hash;
    }

    public static String generateContextUrl(HttpServletRequest request) {
        String serverName = request.getServerName().toLowerCase();
        String scheme = request.getScheme();
        int port = request.getServerPort();

        String val = scheme + "://" + serverName;
        if (("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
            //do nothing, else add port
        } else {
            val += ":" + port;
        }
        val += request.getContextPath();
        return val;
    }
}
