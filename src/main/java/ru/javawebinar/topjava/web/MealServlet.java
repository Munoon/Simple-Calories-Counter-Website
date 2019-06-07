package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.util.MealDataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        if (req.getParameter("delete") != null) {

            int id = Integer.parseInt(req.getParameter("delete"));
            data.deleteByID(id);

        } else if (req.getParameter("edit") != null) {

            int id = Integer.parseInt(req.getParameter("edit"));
            req.setAttribute("edit", "EDIT ID " + id);

        }

        doGet(req, resp);
    }
}
