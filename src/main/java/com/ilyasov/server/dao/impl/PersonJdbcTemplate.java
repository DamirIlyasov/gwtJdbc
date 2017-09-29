package com.ilyasov.server.dao.impl;

import com.ilyasov.server.dao.PersonDao;
import com.ilyasov.server.dao.mapper.PersonMapper;
import com.ilyasov.shared.PersonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Component
public class PersonJdbcTemplate implements PersonDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int create(String name, Date birthday) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                java.sql.Date sqlBirthday = new java.sql.Date(birthday.getTime());
                String insertRequest = "INSERT INTO person(name, birthday) VALUES " +
                        " (?, ?)";
                PreparedStatement ps = connection.prepareStatement(insertRequest, new String[]{"id"});
                ps.setString(1, name);
                ps.setDate(2, sqlBirthday);
                return ps;
            }, keyHolder);
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<PersonResp> listSPersons() {
        String sqlRequest = "SELECT * FROM person";
        return jdbcTemplate.query(sqlRequest, new PersonMapper());
    }

    @Override
    public void delete(int id) {
        String sqlRequest = "DELETE FROM person WHERE id = ?";
        jdbcTemplate.update(sqlRequest, id);
    }

    @Override
    public PersonResp update(int id, String name, Date birthday) {
        String sqlRequest = "UPDATE person SET (name, birthday) = (?, ?) WHERE id = ?";
        jdbcTemplate.update(sqlRequest, name, birthday, id);
        PersonResp personResp = new PersonResp();
        personResp.setId(id);
        personResp.setName(name);
        personResp.setBirthday(birthday);
        return personResp;
    }
}
