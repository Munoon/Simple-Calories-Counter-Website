package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
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

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
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
            controller.update(meal, SecurityUtil.authUserId());
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getRequestParam(request, "id");
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getRequestParam(request, "id"));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                String startString = request.getParameter("startDate");
                String endString = request.getParameter("endDate");
                LocalDate startDate = startString.isEmpty() ? null : LocalDate.parse(startString);
                LocalDate endDate = endString.isEmpty() ? null : LocalDate.parse(endString);

                String startTimeString = request.getParameter("startTime");
                String endTimeString = request.getParameter("endTime");
                LocalTime startTime = startTimeString.isEmpty() ? null : LocalTime.parse(startTimeString);
                LocalTime endTime = endTimeString.isEmpty() ? null : LocalTime.parse(endTimeString);

                log.info("getAll from {} {} to {} {}", startDate, startTime, endDate, endTime);
                request.setAttribute("meals", controller.getAllWithFilter(
                        startDate, startTime, endDate, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getRequestParam(HttpServletRequest request, String param) {
        String paramId = Objects.requireNonNull(request.getParameter(param));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }
}
