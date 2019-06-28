package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static Map<String, Long> testsStatistic = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

    @Autowired
    private MealService service;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        private void logAndAddToStatisticsMap(long nanos, String testName, String status) {
            long time = TimeUnit.NANOSECONDS.toMillis(nanos);
            log.info("Test {}, status: {}, time: {} ms", testName, status, time);
            testsStatistic.put(testName, time);
        }

        @Override
        protected void succeeded(long nanos, Description description) {
            logAndAddToStatisticsMap(nanos, description.getMethodName(), "succes");
            super.succeeded(nanos, description);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            logAndAddToStatisticsMap(nanos, description.getMethodName(), "failed");
            super.failed(nanos, e, description);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            logAndAddToStatisticsMap(nanos, description.getMethodName(), "skipped");
            super.skipped(nanos, e, description);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logAndAddToStatisticsMap(nanos, description.getMethodName(), "finished");
            super.finished(nanos, description);
        }
    };

    @AfterClass
    public static void showStatistic() {
        Long totalTime = testsStatistic.values().stream().reduce((long) 0, (value, aLong) -> aLong += value);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
        long milliSeconds = totalTime - seconds * 1000;
        log.info("\u001B[35m Meal Service tests [Total time - {} sec {} ms]\u001B[0m", seconds, milliSeconds);
        testsStatistic.forEach((testName, time) -> log.info("\u001B[35mTest '{}' takes {} ms\u001B[0m", testName, time));
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() throws Exception {
        exception.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotOwn() throws Exception {
        exception.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        exception.expect(NotFoundException.class);
        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() throws Exception {
        exception.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        exception.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}