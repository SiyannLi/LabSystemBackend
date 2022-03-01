package com.example.LabSystemBackend.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
/**
 * @version 1.0
 * @author Cong Liu
 *
 * Token util
 */
public class JwtUtil {
    private static final String SECRET = "my_secret";
    private static int EXPIRATION = 120;
    private static int REFRESH = 90;


    public static String createToken(User user) {
        try {
            Map<String, Object> header = new HashMap<>();

            header.put("alg", "HS256");//alg为header和payload的加密方式

            header.put("typ","JWT");

            Date nowDate = new Date();
            Date expireDate =   DateUtil.getNextMinute(nowDate, EXPIRATION);
            Date refreshDate =   DateUtil.getNextMinute(nowDate, REFRESH);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            String token = JWT.create()

                    .withHeader(header)
                    .withClaim("email",user.getEmail())
                    .withClaim("expireDate", expireDate)
                    .withClaim("refreshDate", refreshDate)
                    .withIssuedAt(nowDate)
                    .sign(algorithm);
            return token;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }



    }

    //for test
    public static String createToken(User user, int exp, int refresh) {
        try {
            Map<String, Object> header = new HashMap<>();

            header.put("alg", "HS256");//alg为header和payload的加密方式

            header.put("typ","JWT");

            Date nowDate = new Date();
            Date expireDate =   DateUtil.getNextSecond(nowDate, exp);
            Date refreshDate =   DateUtil.getNextSecond(nowDate, refresh);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            String token = JWT.create()

                    .withHeader(header)
                    .withClaim("email",user.getEmail())
                    .withClaim("expireDate", expireDate)
                    .withClaim("refreshDate", refreshDate)
                    .withIssuedAt(nowDate)
                    .sign(algorithm);
            return token;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }



    }



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

    public static Date getExpiresTime (String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date date = jwt.getClaim("expireDate").asDate();

            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getRefreshTime (String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date date = jwt.getClaim("refreshDate").asDate();

            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

