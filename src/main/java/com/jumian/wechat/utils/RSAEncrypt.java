package com.jumian.wechat.utils;

import javax.crypto.Cipher;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Base64;

public class RSAEncrypt {
    private static final String ALGORITHOM = "RSA";//固定值，无须修改

    private static final String CIPHER_PROVIDER = "SunJCE";
    private static final String TRANSFORMATION_PKCS1Paddiing = "RSA/ECB/PKCS1Padding";

    private static final String CHAR_ENCODING = "UTF-8";//固定值，无须修改


    //数据加密方法
    private static byte[] encryptPkcs1padding(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(TRANSFORMATION_PKCS1Paddiing, CIPHER_PROVIDER);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }


    //加密后的秘文，使用base64编码方法
    private static String encodeBase64(byte[] bytes) throws Exception {
        return Base64.getEncoder().encodeToString(bytes);
    }


    //对敏感内容（入参Content）加密，其中PUBLIC_KEY_FILENAME为存放平台证书的路径，平台证书文件存放明文平台证书内容，且为pem格式的平台证书（平台证书的获取方式参照平台证书及序列号获取接口，通过此接口得到的参数certificates包含了加密的平台证书内容ciphertext，然后根据接口文档中平台证书解密指引，最终得到明文平台证书内容）
    public static String rsaEncrypt(String Content, String certPath) {
        final byte[] PublicKeyBytes;
        try {
            PublicKeyBytes = Files.readAllBytes(Paths.get(certPath));
            X509Certificate certificate = X509Certificate.getInstance(PublicKeyBytes);
            PublicKey publicKey = certificate.getPublicKey();
            return encodeBase64(encryptPkcs1padding(publicKey, Content.getBytes(CHAR_ENCODING)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            String s = rsaEncrypt("sdafdsafdsa", "/Users/nicholas/Downloads/rootca.pem");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
