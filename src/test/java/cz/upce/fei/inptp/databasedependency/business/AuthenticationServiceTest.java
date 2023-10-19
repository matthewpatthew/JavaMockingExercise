package cz.upce.fei.inptp.databasedependency.business;

import cz.upce.fei.inptp.databasedependency.dao.PersonDAO;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private PersonDAO mockedPerson;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockedPerson = Mockito.mock(PersonDAO.class);
        authenticationService = new AuthenticationService(mockedPerson);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test

    void authenticate_1() {
        String login = "login";
        String password = "pass";
        Person testPerson =  new Person(1,"user",AuthenticationService.encryptPassword(password));
        when(mockedPerson.load("name = '" + login + "'")).thenReturn(testPerson);
        assertTrue(authenticationService.Authenticate(login,password));
    }
    @org.junit.jupiter.api.Test
    void authenticate_2() {
        String login = "login";
        String invPassword = "invalid";
        String password = "pass";
        Person testPerson =  new Person(1,"user",AuthenticationService.encryptPassword(invPassword));
        when(mockedPerson.load("name = '" + login + "'")).thenReturn(testPerson);
        assertFalse(authenticationService.Authenticate(login,password));
    }
    @org.junit.jupiter.api.Test
    void authenticate_3() {
        String login = "login";
        String password = "pass";
        when(mockedPerson.load("name = '" + login + "'")).thenReturn(null);
        assertFalse(authenticationService.Authenticate(login,password));
    }
}