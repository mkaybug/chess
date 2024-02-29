package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;
import service.AuthService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {
  static final AuthService authService = new AuthService(new MemoryAuthDAO());

  // Setup
  @BeforeEach
  void clear() throws DataAccessException {
    authService.deleteAllAuthTokens();
  }

  @Test
  void authenticateUser() throws DataAccessException {
    AuthData auth = new AuthData("token", "username");

    // Negative test case -> authentication fails
    assertThrows(DataAccessException.class, () -> authService.authenticateUser(auth));

    // Positive test case -> authentication succeeds
    authService.addAuth(auth);

    assertEquals(auth, authService.authenticateUser(auth));
  }

  @Test
  void logout() throws DataAccessException {
    List<AuthData> expected = new ArrayList<>();

    AuthData logoutAuth1 = new AuthData("token3", "username3");
    AuthData logoutAuth2 = new AuthData("token3", "username2");

    // Add tokens to the database
    expected.add(authService.addAuth(new AuthData("token1", "username1")));
    expected.add(authService.addAuth(new AuthData("token2", "username2")));

    // Negative test case -> trying to delete an authToken that doesn't exist, fails
    assertThrows(DataAccessException.class, () -> authService.logout(logoutAuth1));
    assertThrows(DataAccessException.class, () -> authService.logout(logoutAuth2));

    // Add the token we just tried to delete
    authService.addAuth(new AuthData("token3", "username3"));

    // Positive test case -> trying to delete authToken again, succeeds
    authService.logout(logoutAuth1);

    var actual = authService.listAuthTokens();
    assertIterableEquals(expected, actual);
  }

  @Test
  void addAuth() throws DataAccessException {
    var auth = new AuthData("token", "username");
    auth = authService.addAuth(auth);

    var authTokens = authService.listAuthTokens();
    assertEquals(1, authTokens.size());
    assertTrue(authTokens.contains(auth));
  }

  @Test
  void listAuthTokens() throws DataAccessException {
    List<AuthData> expected = new ArrayList<>();
    expected.add(authService.addAuth(new AuthData("token1", "user1")));
    expected.add(authService.addAuth(new AuthData("token2", "user2")));
    expected.add(authService.addAuth(new AuthData("token3", "user3")));

    var actual = authService.listAuthTokens();
    assertIterableEquals(expected, actual);
  }

  @Test
  void deleteAuthToken() throws DataAccessException {
    List<AuthData> expected = new ArrayList<>();
    var auth = authService.addAuth(new AuthData("token1", "user1"));
    expected.add(authService.addAuth(new AuthData("token2", "user2")));
    expected.add(authService.addAuth(new AuthData("token3", "user3")));

    authService.deleteAuthToken(auth.authToken());
    var actual = authService.listAuthTokens();
    assertIterableEquals(expected, actual);
  }

  @Test
  void deleteAllAuthTokens() throws DataAccessException {
    authService.addAuth(new AuthData("token1", "user1"));
    authService.addAuth(new AuthData("token2", "user2"));
    authService.addAuth(new AuthData("token3", "user3"));

    authService.deleteAllAuthTokens();
    assertEquals(0, authService.listAuthTokens().size());
  }
}
