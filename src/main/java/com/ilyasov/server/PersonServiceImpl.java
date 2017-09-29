package com.ilyasov.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ilyasov.client.PersonService;
import com.ilyasov.server.dao.impl.PersonJdbcTemplate;
import com.ilyasov.server.service.AppContext;
import com.ilyasov.shared.PersonResp;

import java.util.Date;
import java.util.List;


@SuppressWarnings("serial")
public class PersonServiceImpl extends RemoteServiceServlet
        implements PersonService {
    private final PersonJdbcTemplate personJdbcTemplate = AppContext.getBean(PersonJdbcTemplate.class);

    @Override
    public List<PersonResp> list() {
        return personJdbcTemplate.listSPersons();
    }

    @Override
    public PersonResp save(PersonResp personResp) {
        personResp.setId(personJdbcTemplate.create(personResp.getName(), personResp.getBirthday()));
        return personResp;
    }

    @Override
    public boolean delete(PersonResp person) {
        personJdbcTemplate.delete(person.getId());
        return true;
    }

    @Override
    public PersonResp update(int id, String newName, Date newBirthday) {
        return personJdbcTemplate.update(id, newName, newBirthday);
    }
}
