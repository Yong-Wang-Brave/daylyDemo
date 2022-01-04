package com.example.demo.MD5;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apache.commons.codec.binary.Base64;
 
 
/**
 * RSA加解密工具类
 *
 * 
 */
public class RSAUtil {
 
 public static String publicKey; // 公钥
 public static String privateKey; // 私钥
 
 /**
  * 生成公钥和私钥
  */
 public static void generateKey() {
  // 1.初始化秘钥
  KeyPairGenerator keyPairGenerator;
  try {
   keyPairGenerator = KeyPairGenerator.getInstance("RSA");
   SecureRandom sr = new SecureRandom(); // 随机数生成器
   keyPairGenerator.initialize(512, sr); // 设置512位长的秘钥
   KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 开始创建
   RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
   RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
   // 进行转码
   publicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());
   // 进行转码
   privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
  } catch (NoSuchAlgorithmException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
 
 /**
  * 私钥匙加密或解密
  * 
  * @param content
  * @param privateKeyStr
  * @return
  */
 public static String encryptByprivateKey(String content, String privateKeyStr, int opmode) {
  // 私钥要用PKCS8进行处理
  PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));
  KeyFactory keyFactory;
  PrivateKey privateKey;
  Cipher cipher;
  byte[] result;
  String text = null;
  try {
   keyFactory = KeyFactory.getInstance("RSA");
   // 还原Key对象
   privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
   cipher = Cipher.getInstance("RSA");
   cipher.init(opmode, privateKey);
   if (opmode == Cipher.ENCRYPT_MODE) { // 加密
    result = cipher.doFinal(content.getBytes());
    text = Base64.encodeBase64String(result);
   } else if (opmode == Cipher.DECRYPT_MODE) { // 解密
    result = cipher.doFinal(Base64.decodeBase64(content));
    text = new String(result, "UTF-8");
   }
 
  } catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  return text;
 }
 
 /**
  * 公钥匙加密或解密
  * 
  * @param content
  * @param privateKeyStr
  * @return
  */
 public static String encryptByPublicKey(String content, String publicKeyStr, int opmode) {
  // 公钥要用X509进行处理
  X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
  KeyFactory keyFactory;
  PublicKey publicKey;
  Cipher cipher;
  byte[] result;
  String text = null;
  try {
   keyFactory = KeyFactory.getInstance("RSA");
   // 还原Key对象
   publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
   cipher = Cipher.getInstance("RSA");
   cipher.init(opmode, publicKey);
   if (opmode == Cipher.ENCRYPT_MODE) { // 加密
    result = cipher.doFinal(content.getBytes());
    text = Base64.encodeBase64String(result);
   } else if (opmode == Cipher.DECRYPT_MODE) { // 解密
    result = cipher.doFinal(Base64.decodeBase64(content));
    text = new String(result, "UTF-8");
   }
  } catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  return text;
 }
 
 // 测试方法
 public static void main(String[] args) {
  /**
   * 注意： 私钥加密必须公钥解密 公钥加密必须私钥解密
   *  // 正常在开发中的时候,后端开发人员生成好密钥对，服务器端保存私钥 客户端保存公钥
   */
  System.out.println("-------------生成两对秘钥，分别发送方和接收方保管-------------");
  RSAUtil.generateKey();
  System.out.println("公钥:" + RSAUtil.publicKey);
  System.out.println("私钥:" + RSAUtil.privateKey);
 
  System.out.println("-------------私钥加密公钥解密-------------");
   String textsr = "11111111";
   // 私钥加密
   String cipherText = RSAUtil.encryptByprivateKey(textsr,
   RSAUtil.privateKey, Cipher.ENCRYPT_MODE);
   System.out.println("私钥加密后：" + cipherText);
   // 公钥解密
   String text = RSAUtil.encryptByPublicKey(cipherText,
   RSAUtil.publicKey, Cipher.DECRYPT_MODE);
   System.out.println("公钥解密后：" + text);
 
  System.out.println("-------------公钥加密私钥解密-------------");
  // 公钥加密
  String textsr2 = "222222";
 
  String cipherText2 = RSAUtil.encryptByPublicKey(textsr2, RSAUtil.publicKey, Cipher.ENCRYPT_MODE);
  System.out.println("公钥加密后：" + cipherText2);
  // 私钥解密
  String text2 = RSAUtil.encryptByprivateKey(cipherText2, RSAUtil.privateKey, Cipher.DECRYPT_MODE);
  System.out.print("私钥解密后：" + text2 );
 }
 
}