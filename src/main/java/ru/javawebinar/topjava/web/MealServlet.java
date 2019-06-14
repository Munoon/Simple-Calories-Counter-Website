package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

//    private MealRepository repository;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        repository = new InMemoryMealRepositoryImpl();

        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (request.getParameter("description") != null) {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    SecurityUtil.authUserId(),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            controller.update(meal);
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        }

//        repository.save(meal, SecurityUtil.authUserId());
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getRequestParam(request, "id");
                log.info("Delete {}", id);
//                repository.delete(id, SecurityUtil.authUserId());
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
//                        repository.get(getRequestParam(request), SecurityUtil.authUserId());
                        controller.get(getRequestParam(request, "id"));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "switchUser":
                int user = getRequestParam(request, "user");
                SecurityUtil.setUserId(user);
                log.info("Switched user to {}", user);
                response.sendRedirect("meals");
                break;
            case "filterByDate":
                String startString = request.getParameter("startDate");
                String endString = request.getParameter("endDate");
                LocalDate startDate, endDate;

                startDate = startString != null && !startString.isEmpty() ? LocalDate.parse(startString) : LocalDate.MIN;
                endDate = endString != null && !endString.isEmpty() ? LocalDate.parse(endString) : LocalDate.MAX;

                log.info("getAll from {} to {}", startDate, endDate);
                request.setAttribute("meals",
//                        MealsUtil.getWithExcess(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                        MealsUtil.getFilteredWithExcess(controller.getAllWithFilterByDate(startDate, endDate),
                                MealsUtil.DEFAULT_CALORIES_PER_DAY,
                                LocalTime.MIN, LocalTime.MAX));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "filterByTime":
                String startTimeString = request.getParameter("startTime");
                String endTimeString = request.getParameter("endTime");
                LocalTime startTime, endTime;

                startTime = startTimeString != null && !startTimeString.isEmpty() ? LocalTime.parse(startTimeString) : LocalTime.MIN;
                endTime = endTimeString != null && !endTimeString.isEmpty() ? LocalTime.parse(endTimeString) : LocalTime.MAX;

                log.info("getAll from {} to {}", startTime, endTime);
                request.setAttribute("meals",
//                        MealsUtil.getWithExcess(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                        MealsUtil.getFilteredWithExcess(controller.getAll(),
                                MealsUtil.DEFAULT_CALORIES_PER_DAY,
                                startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
//                        MealsUtil.getWithExcess(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                        MealsUtil.getWithExcess(controller.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getRequestParam(HttpServletRequest request, String param) {
        String paramId = Objects.requireNonNull(request.getParameter(param));
        return Integer.parseInt(paramId);
    }
}
