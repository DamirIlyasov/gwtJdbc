package com.ilyasov.server.dao.mapper;

import com.ilyasov.shared.PersonResp;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<PersonResp> {
    @Override
    public PersonResp mapRow(ResultSet resultSet, int i) throws SQLException {
        PersonResp personResp = new PersonResp();
        personResp.setId(resultSet.getInt("id"));
        personResp.setName(resultSet.getString("name"));
        personResp.setBirthday(resultSet.getDate("birthday"));
        return personResp;
    }
}
