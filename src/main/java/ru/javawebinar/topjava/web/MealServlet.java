package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.CRUDInteface;
import ru.javawebinar.topjava.util.MealDAO;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);
    private static CRUDInteface crud;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Meals page");
        List<Meal> meals = crud.findAll();
        List<MealTo> filteredMeals = MealsUtil.getFilteredWithExcess(meals, meals.get(0).getDateTime().toLocalTime(), meals.get(meals.size() - 1).getDateTime().toLocalTime(), 1500);
        req.setAttribute("meals", filteredMeals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        super.init();
        crud = new MealDAO();
        crud.addAll(Arrays.asList(
                new Meal(LocalDateTime.of(2019, Month.JUNE, 7, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2019, Month.JUNE, 7, 14, 0), "Обед", 400),
                new Meal(LocalDateTime.of(2019, Month.JUNE, 7, 18, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2019, Month.JUNE, 8, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2019, Month.JUNE, 8, 14, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2019, Month.JUNE, 8, 18, 0), "Ужин", 600)
        ));
    }
}
