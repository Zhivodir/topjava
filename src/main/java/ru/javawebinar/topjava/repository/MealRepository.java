package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealRepository {
    Meal save(Meal meal);

    // false if not found
    boolean delete(int id, int user_id);

    // null if not found
    Meal get(int id, int user_id);

    // Collection<Meal> getAll(User user);
    Collection<Meal> getAll(int user_id);

    Collection<Meal> filter(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int user_id);
}
