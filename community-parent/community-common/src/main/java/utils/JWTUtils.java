package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

/****
 * JWT 工具类
 */
public class JWTUtils {
    // 过期时间
    public static final Long JWT_TTL = 3600000L ;

    // JWT 秘钥
    public static final String JWT_KEY = "shuige"; //秘钥

    /****
     * 生成令牌
     * @param id  唯一标识,这里我们直接放用户的ID
     * @param subject 主题
     * @param ttlMillis  过期时间，如果不设置，默认1小时
     * @return
     */
    public static String createJWT(String id,String subject,Long ttlMillis,String roles){
        // 签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 编码算法
        // 当前系统时间
        long nowMillis = System.currentTimeMillis();
        // 令牌签发时间
        Date date = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis=JWT_TTL;
        }
        Long expMillis=nowMillis+ttlMillis;
        // 生成加密后的秘钥
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)//设置主题(jwt所面向的用户)
                .setIssuer("user")  // 设置颁发者
                .setExpiration(new Date(expMillis))  // 设置过期时间
                .setIssuedAt(new Date())  // 设置签发时间
                .signWith(signatureAlgorithm, secretKey) // 设置签名秘钥
                .claim("roles", roles); // 自定义角色信息

        return builder.compact();
    }
    /**
     * 将秘钥进行加密 secretKey
     * @return
     */
    public static SecretKey generalKey(){
        byte[] encodedKey= Base64.getDecoder().decode(JWTUtils.JWT_KEY);
        SecretKey key=new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }

    public static Claims parseJWT(String jwt) throws Exception{
        Claims cla = Jwts.parser().setSigningKey(generalKey()) // 秘钥
                .parseClaimsJws(jwt)  // 令牌
                .getBody();
        return cla;
    }



}
