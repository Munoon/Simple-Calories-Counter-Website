package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRoles;

    private final JdbcTemplate rolesJdbcTemplate;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate rolesJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.insertRoles = new SimpleJdbcInsert(rolesJdbcTemplate)
                .withTableName("user_roles");

        this.jdbcTemplate = jdbcTemplate;
        this.rolesJdbcTemplate = rolesJdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            user.getRoles().forEach(role -> {
                Map<String, Object> map = new HashMap<>();
                map.put("user_id", user.getId());
                map.put("role", role);
                insertRoles.execute(map);
            });
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u INNER JOIN user_roles r ON r.user_id = u.id WHERE id=?", new UserExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u INNER JOIN user_roles r ON r.user_id = u.id WHERE email=?", new UserExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
//        return jdbcTemplate.query("SELECT * FROM users u INNER JOIN user_roles r ON r.user_id = u.id ORDER BY name, email", ROW_MAPPER);
        return jdbcTemplate.query("SELECT * FROM users u INNER JOIN user_roles r ON r.user_id = u.id ORDER BY name, email", new UserExtractor());
    }

    class UserExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            // код сложный, поэтому я напишу коментарии что бы вам было легче его понять
            Map<Integer, User> users = new LinkedHashMap<>(); // создаём мапу юзеров по ID (Map<ID, User>)
            Map<Integer, Set<Role>> roles = new HashMap<>(); // создаю отдельную мапу с ролями. В катчестве Integer у нас ID юзера (Map<User.ID, Roles>)

            while (rs.next()) {
                int id = rs.getInt("id");
                users.put(id, new User( // тут всё просто, кладу в мапу юзеров нового юзера, в качестве ключа его ID
                        id,
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("calories_per_day"),
                        rs.getBoolean("enabled"),
                        rs.getDate("registered"),
                        null
                ));

                roles.computeIfAbsent(id, k -> new HashSet<>()); // если список ролей по id нашего юзера ещё не проинициализирована - инициализируем
                roles.get(id).add(Role.valueOf(rs.getString("role"))); // добавляем в список ролей полученую роль
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
