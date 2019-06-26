package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {
    @Autowired
    private MealRepository repository;

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id, int user_id) throws NotFoundException {
        repository.delete(id, user_id);
    }

    @Override
    public Meal get(int id, int user_id) throws NotFoundException {
        return repository.get(id, user_id);
    }

    @Override
    public void update(Meal meal) {
        repository.save(meal);
    }

    @Override
    public List<Meal> getAll(int user_id) {
        return repository.getAll(user_id).stream().collect(Collectors.toList());
    }

    @Override
    public List<Meal> filter(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int user_id) {
        return repository.filter(fromDate, toDate, fromTime, toTime, user_id).stream().collect(Collectors.toList());
    }
}