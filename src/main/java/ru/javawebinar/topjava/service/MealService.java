package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {

    Meal create(Meal meal);

    void delete(int id, int user_id) throws NotFoundException;

    Meal get(int id, int user_id) throws NotFoundException;

    void update(Meal meal);

    List<Meal> getAll(int user_id);

    List<Meal> filter(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int user_id);
}