package cz.upce.fei.inptp.databasedependency.business;

import cz.upce.fei.inptp.databasedependency.dao.PersonDAO;
import cz.upce.fei.inptp.databasedependency.dao.PersonRolesDAO;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import cz.upce.fei.inptp.databasedependency.entity.PersonRole;
import cz.upce.fei.inptp.databasedependency.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthorizationServiceTest {

    private AuthorizationService authorizationService;
    private PersonDAO mockedPersonDAO;
    private PersonRolesDAO mockedPersonRolesDAO;

    @BeforeEach
    void setUp() {
        mockedPersonDAO = Mockito.mock(PersonDAO.class);
        mockedPersonRolesDAO = Mockito.mock(PersonRolesDAO.class);
        authorizationService = new AuthorizationService(mockedPersonDAO, mockedPersonRolesDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void authorize_1() {
        //TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section/subsection", rw)]) - pass
        Person person = new Person(1, "TestUser", "password");
        String section = "/section/subsection";
        AccessOperationType operationType = AccessOperationType.Write;
        String roleWhere = "id = " + person.getId();

        when(mockedPersonDAO.getRoleWhereStringFor(person)).thenReturn(roleWhere);
        PersonRole personRole = new PersonRole(person, List.of(new Role(section, "rw", "")));
        when(mockedPersonRolesDAO.load(roleWhere)).thenReturn(personRole);
        boolean result = authorizationService.Authorize(person, section, operationType);
        assertTrue(result);
    }

    @Test
    void authorize_2() {
        //TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section/subsection", ro)]) - fail
        Person person = new Person(1, "TestUser", "password");
        String section = "/section/subsection";
        AccessOperationType operationType = AccessOperationType.Write;
        String roleWhere = "id = " + person.getId();

        when(mockedPersonDAO.getRoleWhereStringFor(person)).thenReturn(roleWhere);
        PersonRole personRole = new PersonRole(person, List.of(new Role(section, "ro", "")));
        when(mockedPersonRolesDAO.load(roleWhere)).thenReturn(personRole);
        boolean result = authorizationService.Authorize(person, section, operationType);
        assertFalse(result);
    }

}