package cz.upce.fei.inptp.databasedependency.business;

import cz.upce.fei.inptp.databasedependency.dao.PersonRolesDAO;
import cz.upce.fei.inptp.databasedependency.dao.PersonDAO;
import cz.upce.fei.inptp.databasedependency.entity.PersonRole;
import cz.upce.fei.inptp.databasedependency.entity.Person;
import cz.upce.fei.inptp.databasedependency.entity.Role;

/**
 * Authorization service. User is authorized to access specified part of system,
 * if he has required access directly to specified part, or to upper level part.
 */
public class AuthorizationService {

    private PersonDAO persondao;
    private PersonRolesDAO personRolesDao;

    public AuthorizationService(PersonDAO persondao, PersonRolesDAO personRolesDao) {
        this.persondao = persondao;
        this.personRolesDao = personRolesDao;

    }

    public AuthorizationService() {
        this.persondao = new PersonDAO();
        this.personRolesDao = new PersonRolesDAO();
    }

    // TODO: add tests
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section/subsection", rw)]) - pass
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section/subsection", ro)]) - fail
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section", rw)]) - pass
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section", ro)]) - fail
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/", rw)]) - pass
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/", ro)]) - fail
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section/subsection", admin)]) - pass
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/section", admin)]) - pass
    // TODO: Authorize(person, "/section/subsection", rw) - PersonRole([Role("/", admin)]) - pass
    public boolean Authorize(Person person, String section, AccessOperationType operationType) {
        String roleWhere = persondao.getRoleWhereStringFor(person);

        PersonRole roles = personRolesDao.load(roleWhere);
        if (roles == null) {
            return false;
        }

        do {
            for (Role role : roles.getRoles()) {
                if (role.getSection().equals(section)) {
                    if (role.getAccess().equals(operationType.getOp())) {
                        return true;
                    }

                    if (role.getAccess().equals("admin")) {
                        return true;
                    }

                    return false;
                }
            }

            section = getUpperLever(section);
            //System.out.println("newsection " + section);
        } while (!section.equals(""));

        return false;
    }

    // TODO: add tests
    // TODO: "/section/subsection/subsubsection" -> "/section/subsection"
    // TODO: "/section/subsection" -> "/section"
    // TODO: "/section" -> "/"
    // TODO: "/" -> ""
    private String getUpperLever(String section) {
        if (section.equals("/")) {
            return "";
        }

        String ret = section.substring(0, section.lastIndexOf("/") + 1);
        return ret.substring(0, ret.length() - 1);
    }

}
