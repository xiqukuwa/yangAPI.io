package com.yang.yangapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {
    public static String getSign(String body, String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body.toString() + "." + "secretKey";
// 5393554e94bf0eb6436f240a4fd71282
        return md5.digestHex(content);
//
    }

}
