package com.ilyasov.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ilyasov.shared.PersonResp;

import java.util.Date;
import java.util.List;


@RemoteServiceRelativePath("person")
public interface PersonService extends RemoteService {
    List<PersonResp> list();

    PersonResp save(PersonResp person);

    boolean delete(PersonResp person);

    PersonResp update(int id, String newName, Date newBirthday);
}
