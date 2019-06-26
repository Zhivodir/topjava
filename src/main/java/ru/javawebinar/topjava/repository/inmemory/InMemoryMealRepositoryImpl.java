package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int user_id) {
        Meal mealForDel = repository.getOrDefault(id, null);
        if(mealForDel != null && mealForDel.getUser_id() == user_id) {
            return repository.remove(id) != null;
        } else {
            throw new NotFoundException("Meal is absent or the user isn't owner of this meal");
        }
    }

    @Override
    public Meal get(int id, int user_id) {
        Meal meal = repository.get(id);
        if(meal.getUser_id() != user_id){
            throw new NotFoundException("Meal is absent or the user isn't owner of this meal");
        }
        return meal.getUser_id() == user_id ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int user_id) {
        Comparator<Meal> comparator = Comparator.comparing(Meal::getDateTime);
        return repository.values().stream()
                .filter(u -> u.getUser_id() == user_id)
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> filter(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int user_id) {
        return repository.values().stream()
                .filter(u -> u.getUser_id() == user_id)
                .filter(dateTimePredicate(fromDate, toDate, fromTime, toTime))
                .collect(Collectors.toList());
    }

    private Predicate<Meal> dateTimePredicate(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime)    {
        return p -> DateTimeUtil.isBetween(p.getDate(), fromDate, toDate) && DateTimeUtil.isBetween(p.getTime(), fromTime, toTime);
    }
}

