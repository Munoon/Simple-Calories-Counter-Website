package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealDataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);
    public static final MealDataUtil data = new MealDataUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Meals page");
        req.setAttribute("meals", data.getMeals());
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("type") == null) {
            doGet(req, resp);
            return;
        }

        int id;

        switch (req.getParameter("type")) {
            case "delete":
                id = Integer.parseInt(req.getParameter("id"));
                data.deleteByID(id);
                logger.debug("Deleted meal with id " + id);
                break;
            case "edit":
                id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("edit", data.getMealByID(id));
                break;
            case "update":
                id = Integer.parseInt(req.getParameter("id"));
                data.updateMeal(createMealTo(req, id));
                logger.debug("Edited meal with id " + id);
                break;
            case "add":
                id  = data.getLastID() + 1;
                data.addMeal(createMealTo(req, id));
                logger.debug("Added meal with id " + id);
                break;
        }

        doGet(req, resp);
    }

    private MealTo createMealTo(HttpServletRequest req, int id) {
        String formDate = req.getParameter("date");
        String formTime = req.getParameter("time");

        int year = Integer.parseInt(formDate.substring(0, formDate.indexOf("-")));
        int month = Integer.parseInt(formDate.substring(formDate.indexOf("-") + 1, formDate.lastIndexOf("-")));
        int date = Integer.parseInt(formDate.substring(formDate.lastIndexOf("-") + 1));

        int hours = Integer.parseInt(formTime.substring(0, formTime.indexOf(":")));
        int minute = Integer.parseInt(formTime.substring(formTime.indexOf(":") + 1));

        LocalDateTime localDateTime = LocalDateTime.of(year, month, date, hours, minute);

        return new MealTo(id, localDateTime, req.getParameter("description"), Integer.parseInt(req.getParameter("calories")), !(req.getParameter("excess") == null));
    }
}
