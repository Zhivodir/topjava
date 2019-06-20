package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.imitation.MealMockRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository = new MealMockRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(("delete").equals(req.getParameter("operation"))) {
            mealRepository.delete(Integer.valueOf(req.getParameter("id")));
            log.debug("delete meal with id=" + req.getParameter("id"));
        }

        log.debug("redirect to meals list");

        req.setAttribute("mealsList", MealsUtil.getMealsWithExcess(mealRepository.getAllMeals(), MealsUtil.caloriesPerDay));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String operation = req.getParameter("operation");

        int calories = Integer.valueOf(req.getParameter("calories"));
        String description = req.getParameter("description");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("datetime"));

        if(("add").equals(operation)) {
            mealRepository.add(dateTime, description, calories);
            log.debug("add new meal with id=" + req.getParameter("id"));
        }

        if(operation.startsWith("edit")) {
            int charIndex = operation.indexOf('-');
            int id = Integer.valueOf(operation.substring(charIndex + 1));
            mealRepository.update(id, dateTime, description, calories);
            log.debug("get edit's meal with id=" + req.getParameter("id"));
        }

        log.debug("redirect to meals list");

        req.setAttribute("mealsList", MealsUtil.getMealsWithExcess(mealRepository.getAllMeals(), MealsUtil.caloriesPerDay));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
