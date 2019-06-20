package ru.javawebinar.topjava.repository;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    public void add(LocalDateTime dateTime, String description, int calories);
    public void delete(int id);
    public void update(int id, LocalDateTime dateTime, String description, int calories);
    public Meal getById(int id);
    public List<Meal> getAllMeals();
}
