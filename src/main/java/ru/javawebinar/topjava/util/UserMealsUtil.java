package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        getFilteredWithExceeded_Optional1(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> list = getFilteredWithExceeded_Optional2_Stream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        for (UserMealWithExceed m : list) {
            System.out.println(m);
        }
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        Map<LocalDate, Integer> caloriesForDate = new HashMap<>();
        for (UserMeal meal : mealList) {
            caloriesForDate.merge(meal.getDate(), meal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
        }

        List<UserMealWithExceed> mealsWithExceed = new ArrayList<>();
        for (UserMeal meal : mealList) {
            boolean isBetween = TimeUtil.isBetween(meal.getTime(), startTime, endTime);
            boolean isExceed = caloriesForDate.get(meal.getDate()) > caloriesPerDay;
            if (isBetween) {
                mealsWithExceed.add(UserMealWithExceed.of(meal, isExceed));
            }
        }
        return mealsWithExceed;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded_Optional1(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesForDate = mealList.stream()
                .collect(Collectors.toMap(
                        meal -> meal.getDate(),
                        meal -> meal.getCalories(),
                        (oldVal, newVal) -> oldVal + newVal)
                );

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> UserMealWithExceed.of(meal, caloriesForDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded_Optional2_Cycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesForDate = new HashMap<>();
        List<UserMealWithExceed> mealsWithExceed = new ArrayList<>();

        for (UserMeal meal : mealList) {
            caloriesForDate.merge(meal.getDate(), meal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
            boolean isBetween = TimeUtil.isBetween(meal.getTime(), startTime, endTime);

            if (isBetween) {
                mealsWithExceed.add(UserMealWithExceed.of(meal, false));
            }
        }

        for (UserMealWithExceed meal : mealsWithExceed) {
            boolean isExceed = caloriesForDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
            meal.setExceed(isExceed);
        }
        return mealsWithExceed;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded_Optional2_Stream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Collection<List<UserMeal>> list = mealList.stream().collect(Collectors.groupingBy(UserMeal::getDate)).values();

        return list.stream().flatMap(dayMeals -> {
            boolean excess = dayMeals.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
            return dayMeals.stream().filter(meal ->
                    TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                    .map(meal -> UserMealWithExceed.of(meal, excess));
        }).collect(Collectors.toList());
    }
}
