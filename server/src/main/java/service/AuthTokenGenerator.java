package service;
import java.util.UUID;

public class AuthTokenGenerator {
  public static String generateAuthToken() {
    // Generate a random UUID as the authToken
    return UUID.randomUUID().toString();
  }
}