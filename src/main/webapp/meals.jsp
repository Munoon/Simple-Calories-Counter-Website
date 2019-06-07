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
                <td>Редактироваить</td>
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
                            <input type="hidden" name="type" value="edit">
                            <input type="hidden" name="id" value="${meal.id}">
                            <input type="submit" value="Редактироваить">
                        </form>
                    </td>
                    <td>
                        <form method="post">
                            <input type="hidden" name="type" value="delete">
                            <input type="hidden" name="id" value="${meal.id}">
                            <input type="submit" value="Удалить">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <c:if test="${edit != null}">
        <hr>
        <form method="post">
            <input type="hidden" name="type" value="update">
            <input type="hidden" name="id" value="${edit.id}">
            <input type="date" name="date" value="${edit.dateTime.toLocalDate()}" placeholder="Дата"><br>
            <input type="time" name="time" value="${edit.dateTime.toLocalTime()}" placeholder="Время"><br>
            <input type="number" name="calories" value="${edit.calories}" placeholder="Калории"><br>
            Перевышен ли лимит каллорий в день?
                <c:if test="${edit.excess == true}">
                    <input type="checkbox" name="excess" checked>
                </c:if>
                <c:if test="${edit.excess == false}">
                    <input type="checkbox" name="excess">
                </c:if>
            <br>
            <input type="text" name="description" value="${edit.description}" placeholder="Описание"><br>
            <input type="submit" value="Обновить">
        </form>
    </c:if>
</body>
</html>
