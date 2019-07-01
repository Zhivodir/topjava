package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-repository.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL1_ID ,USER_ID);
        assertMatch(meal, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getOtherUserMeal() throws Exception {
        service.get(ADMIN_MEAL_ID ,USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {
        service.delete(MEAL1_ID ,USER_ID);
        service.get(MEAL1_ID ,USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOtherUserMeal() throws Exception {
        service.delete(ADMIN_MEAL_ID ,USER_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        LocalDate from = LocalDate.of(2019, Month.JUNE, 21);
        LocalDate to = LocalDate.of(2019, Month.JUNE, 22);
        List<Meal> meals = service.getBetweenDates(from, to, USER_ID);
        assertMatch(meals, MEAL1, MEAL2, MEAL3);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        LocalDateTime from = LocalDateTime.of(2019, Month.JUNE, 22, 10, 30);
        LocalDateTime to = LocalDateTime.of(2019, Month.JUNE, 22, 15, 20);
        List<Meal> meals = service.getBetweenDateTimes(from, to, USER_ID);
        assertMatch(meals, MEAL2);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Updated desc");
        updated.setCalories(330);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateOtherUserMeal() throws Exception {
        Meal updated = new Meal(ADMIN_MEAL1);
        updated.setDescription("Updated desc");
        updated.setCalories(330);
        service.update(updated, USER_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.of(2020, Month.JUNE, 22, 9, 0), "Завтрак", 5000);;
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());

        LocalDateTime from = LocalDateTime.of(2020, Month.JUNE, 22, 9, 0);
        LocalDateTime to = LocalDateTime.of(2020, Month.JUNE, 22, 15, 20);
        List<Meal> meals = service.getBetweenDateTimes(from, to, USER_ID);
        assertMatch(meals, newMeal);
    }

}