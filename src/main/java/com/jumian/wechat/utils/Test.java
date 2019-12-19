package com.jumian.wechat.utils;

import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.cert.X509Certificate;
import java.util.Base64;
public class Test {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int NONCE_LENGTH_BYTE = 12;
    private static final String AES_KEY = "666879a1f5d0e6735cf74c04214d92c3"; // APIv3密钥
    private static final String TRANSFORMATION_PKCS1Padding = "RSA/ECB/PKCS1Padding";
    private static String aesgcmDecrypt(String aad, String iv, String cipherText) throws Exception {
        final Cipher cipher = Cipher.getInstance(ALGORITHM, "SunJCE");
        SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        cipher.updateAAD(aad.getBytes());
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }
    public static void main(String[] args) {
        final String associatedData = "certificate"; // encrypt_certificate.associated_data
        final String nonce = "d33b283eec5d"; // encrypt_certificate.nonce
        final String cipherText = "N2WgKZpRbODi/P9YA1UwNTf0uJgEeSibtav5RWQHgotyIMfEBNWp7JNXzMQA5smKZ4XO+XUgDYqqio904/Z2dWP8XQ+Rk36z7yMPlJxup6fP6hktRiIurSJ6nWhgbhLcZNNo6SnnNV5AP0EKpcBjIZKJWG/KN/vHYUeVXRmos1z1xdfMWL0xydM7rBE0zXkJg328kojGG+97TmxyJJHecVJqRnBQV62R4MlhGhb+0dEb8HIGaK09r5Hmg46bEKcJb6UMnCLBkmlv3ohY4h8DNpWAUyh33bgfWENpGkmsXddniOhuA6/nKUxaXcfMkMeMeh3fGz2MZioWQAM0vEfAU8cpjK5uQTaaVvgA+/OEPWnJnl8971SRaAW8BVBZFdeaiQ8iXN5SObGeJWfSe3Jd+kH+Q0ibfwcQH6EqlKyDyw26kzb0x9kl2JYKEAp5uvFnF/gBCu609L9Hbs/FBm2ziJmswHt57yv8JzbrUq6xQxG8SWxtghJRmfPV+Dq6UaIKLgepN3jQDYr81TMe5WZVKLBXpZs29kuRvrCbgEGEr1shNM1C5Df/2ox5XUaabFxcErUK/KUHqK9+YTP9Wjcb6rE7uFpuPD9SVdfT156vZp0XL4EJambO1CheQCh/C6dpT22S+S8q/mgtkXfPRLfg0PqrxuS/fo27N6pgcBdqHRVIczkvAYGIw6zUG3kPUoUgSrP2zu1tjfYRU4EA3E5Bmd0aVZCUg1EbDzaZCr3vt7MH4BZkJenY6fot7h9iwSsfJJhNG8XivsWPNZ5h4R719tFtN8vkSXLGFWk3MOuGyBF4pRgPEpPtLjz05OCMgILaNkmW8zPtteUNdf3B3slduREy/nIaJTVHHmV9xBEwzwWbxz3xyjki1aw86Jmh6hkgePwqjAkjguaNf8SdgzPGPhkuKl0eLpiZOcEPc0UeDOjMwWUnRLyo6xIfvJImhGBg6R/H6iPPY+1AlDHw78Z1ElOdexsbLCUoWxgCt2J4dHN9xgdZNrvpJHDtaIikJDTv4ya/i2O+HBCBrXbUoeY9bwRwbk8otMKieOYBUGGYjZb598HyC1GAtIvMPQw5fvskyJJaAABCn4hmBJueDZfbPt5BVTh2Pu7G0PIdzOHlCCktMr5CVhhcIj70bRwtMBzaxB6vC+bVqOTHD6TBxvYrK2S/AJjMsOn9nN9GFg6aT30dN3G9c20Ps5PVa/Cj+BARSk5xxcx5SQw25rt/99bXCYFXos2LILkoya9M0ByhreVvDravpiI05EwP8poa3lWQQT/irs5vr/RhQOILoXC2NvDg5kzvBAqxPVLWJ1U8g1tDF60NYvdCcGE/cAzmk6gzoiWryDGPNbuwc1xrTwxkGu2KaGB+1OxLiAykRcbBaAPWAe2+m3gVS8dUj9bfLWBV4EWm/t7Pdu6BJ5t97pJoEgsLmuSWMmmBMaFdpXRE3IxbopmjHgld8dGCz3F6ZGQxvgN4X3t3ZEvcFwpWdZgZsD0VxUKtdY0nNMRj8BY4uXQOrkgcQURFPBtye1ZSulU0Hf76xqFw/9Q05HBUADlHlCfrZC52RHIkzNsQv76E+Wv9KkLiiFyuGEEnLaM08rxCz+JadJ5cpWMUUEBbBR/Lwg+h+sIdu8jf4qjgQj4DkViXpsTwR3lbSJQAf657iEQLFuD+XCxa7gNqKIDqF7nbeG1+FXbDxYHdh4BkUnOJIRguc5awCbPKu2luOBITHfIHhlyoL6yFQy6AHkqW5N2QvGpOiYpsAW7R9j4KV3g2X6lIcPO3djwSo+Fq8PZzQuXEKIS9vUyv3C5e2fHX03O+TuzF8Uubh/5ijXQHbHYAWHHhhq1EddALetqDfdtvHl/GOyc3ISEkb+yNVfxrsdf/pKdJRqQU9w=="; // encrypt_certificate.ciphertext
        try {
            String wechatpayCert = aesgcmDecrypt(associatedData, nonce, cipherText);
            System.out.println(wechatpayCert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
