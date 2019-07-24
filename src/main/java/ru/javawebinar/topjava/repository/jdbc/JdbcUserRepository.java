package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final DataSourceTransactionManager txnManager = new DataSourceTransactionManager();

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        String insertRoles = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            batchInsertRoles(insertRoles, user);
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    private void batchInsertRoles(String sql, User user) {
        for (Role role : user.getRoles()) {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.getId());
                    ps.setString(2, role.name());
                }

                @Override
                public int getBatchSize() {
                    return user.getRoles().size();
                }
            });
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON id = user_id " +
                "WHERE id=?", getResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON id = user_id " +
                "WHERE email=?", getResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users " +
                "INNER JOIN user_roles ON id = user_id ORDER BY name, email", getResultSetExtractor()
        );
    }

    private ResultSetExtractor<List<User>> getResultSetExtractor() {
        return new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, User> users = new LinkedHashMap<>();
                while (rs.next()) {
                    int userId = rs.getInt("id");
                    if (users.containsKey(userId)) {
                        users.get(userId).getRoles().add(Role.valueOf(rs.getString("role")));
                    } else {
                        User user = new User();
                        Set<Role> roles = EnumSet.noneOf(Role.class);
                        roles.add(Role.valueOf(rs.getString("role")));

                        user.setRoles(roles);
                        user.setId(rs.getInt("id"));
                        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                        user.setEmail(rs.getString("email"));
                        user.setEnabled(rs.getBoolean("enabled"));
                        user.setPassword(rs.getString("password"));
                        user.setName(rs.getString("name"));
                        user.setRegistered(rs.getTimestamp("registered"));
                        users.put(userId, user);
                    }
                }
                return users.values().stream().collect(Collectors.toList());
            }
        };
    }
}
