package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsStore {
    private ConcurrentMap<Integer, Meal> meals;
    private static AtomicInteger counterId = new AtomicInteger(0);

    public MealsStore() {
        meals = new ConcurrentHashMap<>();
        meals.put(1, new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.put(2, new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.put(3, new Meal(3, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.put(4, new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.put(5, new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.put(6, new Meal(6, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));

        counterId = new AtomicInteger(6);
    }

    public static MealsStore getUniqueInstance() {
        return uniqueInstance;
    }

    private static MealsStore uniqueInstance = new MealsStore();

    public int generateNewId() {
        return counterId.addAndGet(1);
    }

    public void add(LocalDateTime dateTime, String description, int calories) {
        int id = generateNewId();
        meals.put(id, new Meal(id, dateTime, description, calories));
    }

    public List<Meal> getAllMeals() {
        return new ArrayList<Meal>(meals.values());
    }

    public void delete(int id) {
        meals.remove(id);
    }

    public Meal getById(int id) {
        return meals.get(id);
    }

    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        meals.put(id, new Meal(id, dateTime, description, calories));
    }
}
