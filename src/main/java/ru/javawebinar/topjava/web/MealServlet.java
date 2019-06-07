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
import java.time.LocalDate;
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

        int id = Integer.parseInt(req.getParameter("id"));

        switch (req.getParameter("type")) {
            case "delete":
                data.deleteByID(id);
                break;
            case "edit":
                req.setAttribute("edit", data.getMealByID(id));
                break;
            case "update":
                String formDate = req.getParameter("date");
                String formTime = req.getParameter("time");

                int year = Integer.parseInt(formDate.substring(0, formDate.indexOf("-")));
                int month = Integer.parseInt(formDate.substring(formDate.indexOf("-") + 1, formDate.lastIndexOf("-")));
                int date = Integer.parseInt(formDate.substring(formDate.lastIndexOf("-") + 1));

                int hours = Integer.parseInt(formTime.substring(0, formTime.indexOf(":")));
                int minute = Integer.parseInt(formTime.substring(formTime.indexOf(":") + 1));

                LocalDateTime localDateTime = LocalDateTime.of(year, month, date, hours, minute);

                MealTo newMeal = new MealTo(id, localDateTime, req.getParameter("description"), Integer.parseInt(req.getParameter("calories")), !(req.getParameter("excess") == null));
                data.updateMeal(newMeal);
        }

        doGet(req, resp);
    }
}
