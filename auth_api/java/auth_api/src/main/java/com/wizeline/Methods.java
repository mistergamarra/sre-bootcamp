package com.wizeline;

import com.wizeline.commons.Jwt;
import com.wizeline.domain.User;
import com.wizeline.commons.DataSource;

import java.sql.SQLException;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.wizeline.exception.BusinessException;
import com.wizeline.exception.NotFoundException;
import com.wizeline.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Hex;

public class Methods {

  public static String generateToken(String username, String password) {
    User user;
    try{
      user = DataSource.getUser(username);
    } catch (SQLException ex){
      throw new BusinessException("Something went wrong !");
    }

    if(user == null) throw new NotFoundException("User not found");
    if (validateUser(password,user)){
      return Jwt.createJWT(username, user.getRole());
    }else{
      throw new UnauthorizedException("Invalid Credentials!");
    }
  }

  public static String accessData(String authorization){
    Claims claim = Jwt.decodeJWT(authorization);
    if(claim.isEmpty()) throw new UnauthorizedException("Your token is not valid!");
    return "You are under protected data";
  }

  public static boolean validateUser(String password, User user){
    String encodedPassword = user.getPassword();
    String hashedPassword = getHashSHA512(password,user.getSalt());
    return encodedPassword.equals(hashedPassword);
  }

  public static String getHashSHA512(String StringToHash, String salt){
    String generatedPassword = null;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      byte[] bytes = md.digest(StringToHash.concat(salt).getBytes(StandardCharsets.UTF_8));
      generatedPassword = Hex.encodeHexString(bytes);
    }
    catch (NoSuchAlgorithmException e){
      throw new BusinessException("Something went wrong !");
    }
    return generatedPassword;
  }

}

