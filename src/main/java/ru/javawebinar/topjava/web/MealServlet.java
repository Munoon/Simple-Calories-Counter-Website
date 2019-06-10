package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.CrudInteface;
import ru.javawebinar.topjava.repository.MealDao;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);
    private static CrudInteface crud;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        showMealsList(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getParameter("type") == null) {
            resp.sendRedirect("meals");
            return;
        }

        int id = 0;
        if (req.getParameter("id") != null && !req.getParameter("id").equals(""))
            id = Integer.parseInt(req.getParameter("id"));

        switch (req.getParameter("type")) {
            case "delete":
                crud.delete(id);
                logger.debug("Deleted meal with id {}", id);
                break;
            case "edit":
                req.setAttribute("edit", crud.get(id));
                break;
            case "update":
                crud.update(createMeal(req));
                logger.debug("Updated meal with id {}", id);
                break;
            case "add":
                crud.add(createMeal(req));
                logger.debug("Added meal with id {}", id);
                break;
        }

        showMealsList(req, resp);
    }

    private void showMealsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Meal> meals = crud.findAll();
        List<MealTo> filteredMeals = MealsUtil.getFilteredWithExcess(meals, LocalTime.MIN, LocalTime.MAX, 1500);
        req.setAttribute("meals", filteredMeals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private Meal createMeal(HttpServletRequest req) throws UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");

        int id;
        if (req.getParameter("id") != null && !req.getParameter("id").equals(""))
            id = Integer.parseInt(req.getParameter("id"));
        else id = -1;

        String formDate = req.getParameter("date");
        String formTime = req.getParameter("time");

        LocalDate date = LocalDate.parse(formDate);
        LocalTime time = LocalTime.parse(formTime);

        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        if (id != -1)
            return new Meal(id, localDateTime, req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
        else
            return new Meal(localDateTime, req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
    }

    @Override
    public void init() throws ServletException {
        crud = new MealDao();
        crud.add(new Meal(LocalDateTime.of(2019, Month.JUNE, 7, 10, 0), "Завтрак", 500));
        crud.add(new Meal(LocalDateTime.of(2019, Month.JUNE, 7, 14, 0), "Обед", 400));
        crud.add(new Meal(LocalDateTime.of(2019, Month.JUNE, 7, 18, 0), "Ужин", 500));
        crud.add(new Meal(LocalDateTime.of(2019, Month.JUNE, 8, 10, 0), "Завтрак", 500));
        crud.add(new Meal(LocalDateTime.of(2019, Month.JUNE, 8, 14, 0), "Обед", 500));
        crud.add(new Meal(LocalDateTime.of(2019, Month.JUNE, 8, 18, 0), "Ужин", 600));
    }
}
