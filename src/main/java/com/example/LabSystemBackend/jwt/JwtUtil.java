package com.example.LabSystemBackend.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;

public class JwtUtil {
    /**
     * 密钥
     */
    private static final String SECRET = "my_secret";
    private static final int EXPIRATION = 120;

    /**
     * 过期时间
     **/
    public static String createToken(User user) {
        try {
            Map<String, Object> header = new HashMap<>();

            header.put("alg", "HS256");//alg为header和payload的加密方式

            header.put("typ","JWT");

            Date nowDate = new Date();
            Date expireDate =   DateUtil.getNextMinute(nowDate, EXPIRATION);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            String token = JWT.create()

                    .withHeader(header)
                    .withClaim("email",user.getEmail())
                    .withClaim("password",user.getUserPassword())
                    .withClaim("role",user.getUserRole().getRoleValue())
                    .withClaim("status", user.getUserAccountStatus().getStatusValue())
                    .withClaim("firstName",user.getFirstName())
                    .withIssuedAt(nowDate)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
            return token;

        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }

    }


    /**
     * 检验token是否正确
     * @param token 需要校验的token
     * @return 校验是否成功
     */
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static String getUserInfo(String token,String info){

        try {

            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(info).asString();
        } catch (Exception e){

            e.printStackTrace();
            return null;
        }
    }



////  刷新令牌中的当前时间与到期时间
//
//    public static <Claims> String reFreshToken(String token){
////        首先获取到token中的userName信息，再生成新的token
//
//        try{
//            Claims claims = (Claims) Jwts.parser()
//
//                    .setSigningKey(SECRET)
//
//                    .parseClaimsJws(token).getBody();
//
//            return createToken(claims.get("userName").toString());
//
//        }catch (Exception e){
//            throw new RuntimeException("解密失败");
//
//        }
//
//    }

}

