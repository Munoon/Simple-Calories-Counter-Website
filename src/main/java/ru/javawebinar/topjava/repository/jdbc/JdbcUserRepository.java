package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;
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

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

//            пусть это будет моим напоминанием)
//            user.getRoles().forEach(role -> {
//                Map<String, Object> map = new HashMap<>();
//                map.put("user_id", user.getId());
//                map.put("role", role);
//                insertRoles.execute(map);
//            });
        } else {
            int update = namedParameterJdbcTemplate.update("UPDATE users SET name=:name, email=:email, password=:password, " +
                    "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
            if (update == 0) return null;
            else
                jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        batchUpdateRoles(user.getRoles(), user.getId());
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT OUTER JOIN user_roles r ON r.user_id = u.id WHERE id=?", new UserExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT OUTER JOIN user_roles r ON r.user_id = u.id WHERE email=?", new UserExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
//        return jdbcTemplate.query("SELECT * FROM users u INNER JOIN user_roles r ON r.user_id = u.id ORDER BY name, email", ROW_MAPPER);
        return jdbcTemplate.query("SELECT * FROM users u LEFT OUTER JOIN user_roles r ON r.user_id = u.id ORDER BY name, email", new UserExtractor());
    }

    private void batchUpdateRoles(Set<Role> roles, int id) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setString(2, roles.toArray()[i].toString());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    class UserExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            // код сложный, поэтому я напишу коментарии что бы вам было легче его понять
            Map<Integer, User> users = new LinkedHashMap<>(); // создаём мапу юзеров по ID (Map<ID, User>)
            Map<Integer, Set<Role>> roles = new HashMap<>(); // создаю отдельную мапу с ролями. В катчестве Integer у нас ID юзера (Map<User.ID, Roles>)

            while (rs.next()) {
                int id = rs.getInt("id");
                if (!users.containsKey(id)) // если такой юзер уже есть - ничего не делаем
                    users.put(id, new User( // тут всё просто, кладу в мапу юзеров нового юзера, в качестве ключа его ID
                            id,
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("calories_per_day"),
                            rs.getBoolean("enabled"),
                            rs.getDate("registered"),
                            EnumSet.noneOf(Role.class)
                    ));

                roles.computeIfAbsent(id, k -> EnumSet.noneOf(Role.class)); // если список ролей по id нашего юзера ещё не проинициализирована - инициализируем

                String role = rs.getString("role");
                if (role != null) // если у пользователя нет ролей - ничего не делаем
                    roles.get(id).add(Role.valueOf(role)); // добавляем в список ролей полученую роль
            }

            roles.forEach((id, rolesSet) -> users.get(id).setRoles(rolesSet)); // каждому юзеру добавляем роль из мапы roles

            /*
            Пример:
                users = {1=User{email=... roles=null}, 2=User{roles=null}}}
                roles = {1=[ROLE_USER, ROLE_ADMIN], 2=[ROLE_USER]}

            Итог:
                user = {1=User{email=... roles=[ROLE_USER, ROLE_ADMIN]}, 2=User{roles=[ROLE_USER]}}}
             */

            return new ArrayList<>(users.values()); // возвращаем значения
        }
    }
}
