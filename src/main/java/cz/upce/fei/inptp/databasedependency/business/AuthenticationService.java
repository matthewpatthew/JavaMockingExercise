package cz.upce.fei.inptp.databasedependency.business;

import cz.upce.fei.inptp.databasedependency.dao.PersonDAO;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authentication service - used for authentication of users stored in db.
 * Authentication should success if login and password (hashed) matches.
 */
public class AuthenticationService {

    private PersonDAO persondao;

    public AuthenticationService() {
        this.persondao = new PersonDAO();
    }

    public AuthenticationService(PersonDAO persondao) {
        this.persondao = persondao;
    }

    // TODO: add tests
    // TODO: Authenticate("user", "pass") - Person("user", encryptPwd("pass")) - pass
    // TODO: Authenticate("user", "invalid") - Person("user", encryptPwd("pass")) - fail
    // TODO: Authenticate("user", "pass") - nonexistent person - fail
    public boolean Authenticate(String login, String password) {
        Person person = persondao.load("name = '" + login + "'");
        if (person == null) {
            return false;
        }

        return person.getPassword().equals(encryptPassword(password));
    }

    // TODO: (low priority) - change to safe password hash function (PBKDF2, bcrypt, scrypt)
    public static String encryptPassword(String password) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));

            return new BigInteger(1, crypt.digest()).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
