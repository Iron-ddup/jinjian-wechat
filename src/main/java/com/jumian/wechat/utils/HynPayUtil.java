package com.jumian.wechat.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 请求签名工具
 */
public class HynPayUtil {

    private static final String KEY_ALGORTHM="RSA";//
    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final int MAX_ENCRYPT_BLOCK = 117;


    /**
     * 业务参数加密，公共参数加签
     */
    public static String sign(Map<String, String> map, String appKey, String privateKey) throws Exception {
        String paramStr = getParamStr(map);
        String sign = RsaUtils.sign((appKey + paramStr + appKey).getBytes(), privateKey);
        return sign;
    }

    /**
     * 用公钥解密
     * @param data	加密数据
     * @param publicKey	密钥
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(byte[] data,String publicKey)throws Exception{
        byte[] keyBytes =  Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * 用私钥解密
     * @param data 	加密数据
     * @param privateKey	密钥
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(byte[] data,String privateKey)throws Exception{
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * map参数转key1=value1&key2=value2格式
     */
    private static String getParamStr(Map<String, String> map) {
        Set<String> nameList = map.keySet();
        List<String> listParam = new ArrayList<String>();
        for (String param : nameList) {
            if (!param.equals("sign")) {
                listParam.add(param);
            }
        }
        /*
         * 将接收到的参数进行字典排序,根据排序后的参数与参数值进行加密
         */
        Collections.sort(listParam);
        StringBuffer strValue = new StringBuffer();
        for (int i = 0; i < listParam.size(); i++) {
            if (i != listParam.size() - 1) {
                strValue.append(listParam.get(i)).append("=").append(map.get(listParam.get(i))).append("&");
            } else {
                strValue.append(listParam.get(i)).append("=").append(map.get(listParam.get(i)));
            }
        }
        return strValue.toString();
    }
}
