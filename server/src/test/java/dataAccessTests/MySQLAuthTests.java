package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLDAOs.MySQLAuthDAO;
import model.AuthData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthTests {
  MySQLAuthDAO mySQLAuthDAO = null;

  @BeforeEach
  void setUp() throws DataAccessException {
    try {
      mySQLAuthDAO = new MySQLAuthDAO();
      mySQLAuthDAO.deleteAllAuthTokens();
    }
    catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  @Test
  @DisplayName("Add Auth Fail")
  void addAuthFail() throws DataAccessException {
    AuthData auth1 = new AuthData ("authToken", "username");
    AuthData auth2 = new AuthData ("authToken", "username");

    mySQLAuthDAO.addAuth(auth1);
    assertThrows(DataAccessException.class, () -> mySQLAuthDAO.addAuth(auth2));
  }

  @Test
  @DisplayName("Add Auth Success")
  void addAuthSuccess() throws DataAccessException {
    AuthData auth1 = new AuthData ("authToken1", "username");
    AuthData auth2 = new AuthData ("authToken2", "username");
    AuthData auth3 = new AuthData ("authToken3", "username");

    mySQLAuthDAO.addAuth(auth1);
    mySQLAuthDAO.addAuth(auth2);
    mySQLAuthDAO.addAuth(auth3);

    assertEquals(auth1, mySQLAuthDAO.getAuth(auth1.authToken()));
    assertEquals(auth2, mySQLAuthDAO.getAuth(auth2.authToken()));
    assertEquals(auth3, mySQLAuthDAO.getAuth(auth3.authToken()));
  }

  @Test
  @DisplayName("Get Auth Fail")
  void getAuthFail() throws DataAccessException {
    assertNull(mySQLAuthDAO.getAuth("authToken"));
  }

  @Test
  @DisplayName("Get Auth Success")
  void getAuthSuccess() throws DataAccessException {
    AuthData auth = new AuthData ("this_authToken", "this_username");
    mySQLAuthDAO.addAuth(auth);

    assertEquals(auth, mySQLAuthDAO.getAuth(auth.authToken()));
  }

  @Test
  @DisplayName("List Auth Success")
  void listAuthTokensSuccess() throws DataAccessException {
    ArrayList<AuthData> expected = new ArrayList<>();

    AuthData auth1 = new AuthData ("authToken1", "username");
    AuthData auth2 = new AuthData ("authToken2", "username");
    AuthData auth3 = new AuthData ("authToken3", "username");
    expected.add(mySQLAuthDAO.addAuth(auth1));
    expected.add(mySQLAuthDAO.addAuth(auth2));
    expected.add(mySQLAuthDAO.addAuth(auth3));

    assertIterableEquals(expected, mySQLAuthDAO.listAuthTokens());
  }

  @Test
  @DisplayName("Delete Auth Token Success")
  void deleteAuthTokenSuccess() throws DataAccessException {
    ArrayList<AuthData> expected = new ArrayList<>();

    AuthData auth1 = new AuthData ("authToken1", "username");
    AuthData auth2 = new AuthData ("authToken2", "username");
    AuthData auth3 = new AuthData ("authToken3", "username");

    mySQLAuthDAO.addAuth(auth1);
    expected.add(mySQLAuthDAO.addAuth(auth2));
    expected.add(mySQLAuthDAO.addAuth(auth3));

    mySQLAuthDAO.deleteAuthToken(auth1.authToken());
    Collection<AuthData> actual = mySQLAuthDAO.listAuthTokens();

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Delete Auth Token Fail")
  void deleteAuthTokenFail() throws DataAccessException {
    ArrayList<AuthData> expected = new ArrayList<>();

    AuthData auth1 = new AuthData ("authToken1", "username");
    AuthData auth2 = new AuthData ("authToken2", "username");
    AuthData auth3 = new AuthData ("authToken3", "username");

    expected.add(mySQLAuthDAO.addAuth(auth1));
    expected.add(mySQLAuthDAO.addAuth(auth2));
    expected.add(mySQLAuthDAO.addAuth(auth3));

    mySQLAuthDAO.deleteAuthToken("non-existent token");
    Collection<AuthData> actual = mySQLAuthDAO.listAuthTokens();

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Delete All Auth Tokens")
  void deleteAllAuthTokens() throws DataAccessException {
    AuthData auth1 = new AuthData ("authToken1", "username");
    AuthData auth2 = new AuthData ("authToken2", "username");
    AuthData auth3 = new AuthData ("authToken3", "username");

    mySQLAuthDAO.addAuth(auth1);
    mySQLAuthDAO.addAuth(auth2);
    mySQLAuthDAO.addAuth(auth3);

    mySQLAuthDAO.deleteAllAuthTokens();
    Collection<AuthData> actual = mySQLAuthDAO.listAuthTokens();

    assertEquals(0, actual.size());
  }
}
