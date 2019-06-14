<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<input>
    <h3><a href="index.html">Home</a></h3>
    <form method="get">
        <input type="hidden" name="action" value="switchUser">
        <input type="hidden" name="user" value="1">
        <input type="submit" value="User 1">
    </form>
    <form method="get">
        <input type="hidden" name="action" value="switchUser">
        <input type="hidden" name="user" value="2">
        <input type="submit" value="User 2">
    </form>
    <form method="get">
        <input type="hidden" name="action" value="switchUser">
        <input type="hidden" name="user" value="3">
        <input type="submit" value="User 3">
    </form>
    <hr/>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <form method="get">
        <input type="hidden" name="action" value="filterByDate">
        <input type="date" name="startDate">
        <input type="date" name="endDate">
        <input type="submit" value="Filter">
    </form>
    <form method="get">
        <input type="hidden" name="action" value="filterByTime">
        <input type="time" name="startTime">
        <input type="time" name="endTime">
        <input type="submit" value="Filter">
    </form>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>