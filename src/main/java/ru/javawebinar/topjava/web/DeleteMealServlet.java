package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteMealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteMealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("meals");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        // removeIf почему-то не хочет работать. Решил сделать так
        MealServlet.mealsList.forEach(meal -> {
            if (meal.getId() == id) {
                MealServlet.mealsList.remove(meal);
            }
        });

        logger.debug("Deleted meal with id " + id);
        resp.sendRedirect("meals");
    }
}
