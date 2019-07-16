package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger logger = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealRestController controller;

    @GetMapping
    public String getAll(HttpServletRequest request) {
        request.setAttribute("meals", controller.getAll());
        request.setAttribute("filter", false);
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        controller.delete(getId(request));
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        logger.info("Create page");
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request) {
        final Meal meal = controller.get(getId(request));
        logger.info("Update page");
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", controller.getBetween(startDate, startTime, endDate, endTime));
        request.setAttribute("filter", true);
        return "meals";
    }

    @PostMapping("/post")
    public String postMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );

        if (StringUtils.isEmpty(request.getParameter("id")))
            controller.create(meal);
        else
            controller.update(meal, getId(request));

        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
