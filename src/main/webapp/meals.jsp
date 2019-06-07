<%--
  Created by IntelliJ IDEA.
  User: Munoo
  Date: 07.06.2019
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals List</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table>
        <thead>
            <tr>
                <td>Дата</td>
                <td>Время</td>
                <td>Описание</td>
                <td>Калории</td>
                <td>Превышение каллорий в день</td>
                <td>Удалить</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="meal" items="${meals}">
                <tr>
                    <td>${meal.dateTime.toLocalDate()}</td>
                    <td>${meal.dateTime.toLocalTime()}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td>${meal.excess}</td>
                    <td>
                        <form method="post">
                            <input type="hidden" name="id" value="${meal.id}">
                            <input type="submit" value="Удалить">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
