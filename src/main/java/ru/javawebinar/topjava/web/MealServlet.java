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
        List<Meal> meals = crud.findAll();
        List<MealTo> filteredMeals = MealsUtil.getFilteredWithExcess(meals, LocalTime.MIN, LocalTime.MAX, 1500);
        req.setAttribute("meals", filteredMeals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("type") == null) {
            resp.sendRedirect("meals");
            return;
        }

        req.setCharacterEncoding("UTF-8");
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
                crud.update(id, createMeal(req));
                logger.debug("Updated meal with id {}", id);
                break;
            case "add":
                crud.add(createMeal(req));
                logger.debug("Added meal with id {}", id);
                break;
        }

        doGet(req, resp); // при редиректе теряеться атрибуты request
        // Чесно говоря не понимаю чем это плохо? Так адресс остаёться нормальным (нет .jsp в конце) и данные не теряються
        // На мой взгял это удобно и практично
    }

    private Meal createMeal(HttpServletRequest req) throws UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");

        String formDate = req.getParameter("date");
        String formTime = req.getParameter("time");

        int year = Integer.parseInt(formDate.substring(0, formDate.indexOf("-")));
        int month = Integer.parseInt(formDate.substring(formDate.indexOf("-") + 1, formDate.lastIndexOf("-")));
        int date = Integer.parseInt(formDate.substring(formDate.lastIndexOf("-") + 1));

        int hours = Integer.parseInt(formTime.substring(0, formTime.indexOf(":")));
        int minute = Integer.parseInt(formTime.substring(formTime.indexOf(":") + 1));

        LocalDateTime localDateTime = LocalDateTime.of(year, month, date, hours, minute);

        return new Meal(localDateTime, req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
    }

    @Override
    public void init() throws ServletException {
        super.init();
        crud = new MealDao();
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
