package ru.javawebinar.topjava.repository.imitation;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.store.MealsStore;

import java.time.LocalDateTime;
import java.util.List;

public class MealMockRepository implements MealRepository {
    private MealsStore mealsStore = MealsStore.getUniqueInstance();

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        mealsStore.add(dateTime, description, calories);
    }

    @Override
    public void delete(int id) {
        mealsStore.delete(id);
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        mealsStore.update(id, dateTime, description, calories);
    }

    @Override
    public Meal getById(int id) {
        return mealsStore.getById(id);
    }

    @Override
    public List<Meal> getAllMeals() {
        return mealsStore.getAllMeals();
    }
}
