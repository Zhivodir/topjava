package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public void doPostOperation(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("action") != null && request.getParameter("action").equals("filter")) {
            LocalDate fromDate = DateTimeUtil.convertToLocalDate(request.getParameter("fromDate"));
            LocalDate toDate = DateTimeUtil.convertToLocalDate(request.getParameter("toDate"));
            LocalTime fromTime = DateTimeUtil.convertToLocalTime(request.getParameter("fromTime"));
            LocalTime toTime = DateTimeUtil.convertToLocalTime(request.getParameter("toTime"));
            List<Meal> meals = service.filter(fromDate, toDate, fromTime, toTime, SecurityUtil.authUserId());

            request.setAttribute("meals", MealsUtil.getWithExcess(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    SecurityUtil.authUserId()
            );

            if (meal.isNew()) {
                log.info("Create {}", meal);
                service.create(meal);
            } else {
                log.info("Update {}", meal);
                service.update(meal);
            }
        }

        response.sendRedirect("meals");
    }

    public void doGetOperation(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                service.delete(id, SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo
                                (ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
                        service.get(getId(request), SecurityUtil.authUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter
                ("id"));
        return Integer.parseInt(paramId);
    }

    public List<Meal> getAll(int user_id) {
        log.debug("get all meals for user_id:" + user_id);
        return service.getAll(user_id);
    }

    public Meal create(Meal meal) {
        log.debug("create meal:" + meal);
        return service.create(meal);
    }

    public Meal getById(int id, int user_id) {
        log.debug("get meal by id :" + id);
        return service.get(id, user_id);
    }

    public void update(Meal meal) {
        log.debug("update meal:" + meal);
        service.update(meal);
    }

    public void delete(int id, int user_id) {
        log.debug("delete meal:" + id);
        service.delete(id, user_id);
    }
}