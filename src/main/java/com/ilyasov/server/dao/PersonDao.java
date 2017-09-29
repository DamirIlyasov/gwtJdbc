package com.ilyasov.server.dao;

import com.ilyasov.shared.PersonResp;

import java.util.Date;
import java.util.List;

public interface PersonDao {
    int create(String name, Date birthday);

    List<PersonResp> listSPersons();

    void delete(int id);

    PersonResp update(int id, String newName, Date newBirthday);
}
