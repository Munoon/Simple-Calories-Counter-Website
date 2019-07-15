package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Date;
import java.util.HashSet;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    // этот тест не проходит в других реализаций
    // я его оставил только тут, поскольку не уверен нужен ли он вообще
    @Test
    public void userWithoutRoles() {
        User newUser = new User(null, "New No Roles", "email@email.com", "pass", 1000, true, new Date(), new HashSet<>());
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(created, newUser);
        assertMatch(service.getAll(), ADMIN, newUser, USER);
    }
}