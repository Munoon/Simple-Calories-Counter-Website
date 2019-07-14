package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class JspMealController {
    private static final Logger logger = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping("/meals")
    public String get(HttpServletRequest request) throws UnsupportedEncodingException {
        String action = request.getParameter("action");

        switch (action == null ? "default" : action) {
            case "delete":
                return delete(request);
            case "create":
            case "update":
                return updatePage(request, action);
            case "filter":
                return filter(request);
            case "default":
            default:
                return getAll(request);
        }
    }

    @PostMapping("/meals")
    public String post(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            create(meal);
        } else {
            update(request, meal);
        }
        return "redirect:meals";
    }

    private String delete(HttpServletRequest request) {
        int id = getId(request);
        service.delete(id, authUserId());
        logger.info("Deleted meal with id {}", id);
        return "redirect:meals";
    }

    private String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", convert(getBetween(startDate, endDate, startTime, endTime)));
        logger.info("Get all with filter {} {} - {} {}", startDate, startTime, endDate, endTime);
        return "meals";
    }

    private Meal get(int id) {
        return service.get(id, authUserId());
    }

    private String getAll(HttpServletRequest request) {
        List<Meal> all = service.getAll(authUserId());
        request.setAttribute("meals", convert(all));
        logger.info("Get all");
        return "meals";
    }

    private String updatePage(HttpServletRequest request, String action) {
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                get(getId(request));
        logger.info("create".equals(action) ? "Show create page of {}" : "Show updated page of {}", meal);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    private void create(Meal meal) {
        service.create(meal, authUserId());
        logger.info("Created {}", meal);
    }

    private void update(HttpServletRequest request, Meal meal) {
        meal.setId(getId(request));
        service.update(meal, authUserId());
        logger.info("Updated {}", meal);
    }

    private List<Meal> getBetween(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return service.getBetweenDates(startDate, endDate, authUserId())
                .stream()
                .filter(meal -> Util.isBetween(meal.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private List<MealTo> convert(List<Meal> list) {
        return MealsUtil.getWithExcess(list, SecurityUtil.authUserCaloriesPerDay());
    }
}
