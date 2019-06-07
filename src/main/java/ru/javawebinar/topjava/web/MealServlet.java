package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);
    private static List<MealTo> mealsList;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Meals page");
        req.setAttribute("meals", mealsList);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        mealsList = Arrays.asList(
                new MealTo(LocalDateTime.of(2019, Month.JUNE, 7, 10, 0), "Завтрак", 500, false),
                new MealTo(LocalDateTime.of(2019, Month.JUNE, 7, 14, 0), "Обед", 400, false),
                new MealTo(LocalDateTime.of(2019, Month.JUNE, 7, 19, 0), "Ужин", 400, false),
                new MealTo(LocalDateTime.of(2019, Month.JUNE, 8, 10, 0), "Завтрак", 500, true),
                new MealTo(LocalDateTime.of(2019, Month.JUNE, 8, 14, 0), "Обед", 500, true),
                new MealTo(LocalDateTime.of(2019, Month.JUNE, 8, 20, 0), "Ужин", 700, true)
        );
    }
}
