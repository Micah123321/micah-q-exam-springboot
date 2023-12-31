package com.micah.exam_springboot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 *
 * @author Micah
 */

@Slf4j
public class JwtUtil {

    /**
     * 过期时间 3 days
     */
    public static final Long JWT_TTL = 3 * 24 * 60 * 60 * 1000L;

    /**
     * 密钥,用于base64编码,注意自行修改
     */
    public static final String JWT_KEY = "12Micah@toksens@gmail.com";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 生成jwt
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("Micah")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     *
     * @param id        指定id
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间 可以设置为null 存在默认值
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    @SuppressWarnings("RedundantThrows")
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 判断jwt是否合法
     *
     * @param jwt
     * @return
     */
    public static boolean isExpiredJWT(String jwt) {
        try {
            Claims claims = parseJWT(jwt);
            Date expiration = claims.getExpiration();
            Date date = new Date();
            log.info("-------------------所减时间" + (date.getTime() - expiration.getTime()));
            if (expiration.before(date)) {
                log.info("JWT令牌已过期");
                return false;
            } else {
                // Token未过期
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("JWT令牌验证非法");
            return false;
        }
        log.info("JWT令牌验证合法");
        return true;
    }

    /**
     * 获取封装的信息
     *
     * @param jwt
     * @return
     */
    public static String getSub(String jwt) {
        String sub = null;
        try {
            sub = parseJWT(jwt).getSubject();
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("JWT令牌验证非法");
            return sub;
        }
        log.info("JWT令牌验证合法");
        return sub;
    }

    /**
     * 获取过期时间
     *
     * @param jwt
     * @return
     */
    public static String getExpiration(String jwt) {
        String Expiration = null;
        try {
            Claims claims = parseJWT(jwt);
            Date expiration = claims.getExpiration();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Expiration = simpleDateFormat.format(expiration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Expiration;
    }

    public static void main(String[] args) throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MmE3MjBiZTgxNjA0ZWI0YmMyOTAyNTNjODc1Y2Y5YSIsInN1YiI6IntcInVpZFwiOjU1LFwidXNlcm5hbWVcIjpcIjEyMzQ1Njc4OVwifSIsImlzcyI6Imh1aSIsImlhdCI6MTY2MjcxNTc4MCwiZXhwIjoxNjYyNzIyOTgwfQ.Fe8tndEM3s3jOV-dqL7mBRfgR5-3Dk8OcItdqjvDQLY";
        String jwt1 = "";
        Claims claims = parseJWT(jwt);
        String subject = claims.getSubject();
//        claims.get;
//        Date date = exp.asDate();
//        Long exp = Long.valueOf(claims.get("exp").toString());
//        Date date2 = new Date();
//        date2.setTime(exp);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        System.out.println(simpleDateFormat.format(date2));
        Date expiration = claims.getExpiration();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(expiration));
    }
}
