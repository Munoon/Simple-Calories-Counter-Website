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
                    <c:if test="${meal.excess}">
                        <td style="color: blue">${meal.excess}</td> <!-- Сделал голубое что бы мог различать. Я дальтоник и для меня зелённый и красный - одинаковые цвета( -->
                    </c:if>
                    <c:if test="${!meal.excess}">
                        <td style="color: red">${meal.excess}</td>
                    </c:if>
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

    <hr>

    <c:if test="${edit != null}">
        <h2>Редактироваить продукт</h2>
    </c:if>
    <c:if test="${edit == null}">
        <h2>Добавить продукт</h2>
    </c:if>

    <form method="post">
        <c:if test="${edit != null}">
            <input type="hidden" name="type" value="update">
        </c:if>
        <c:if test="${edit == null}">
            <input type="hidden" name="type" value="add">
        </c:if>
        <input type="hidden" name="id" value="${edit.id}">
        <input type="date" name="date" value="${edit.dateTime.toLocalDate()}" placeholder="Дата"><br>
        <input type="time" name="time" value="${edit.dateTime.toLocalTime()}" placeholder="Время"><br>
        <input type="number" name="calories" value="${edit.calories}" placeholder="Калории"><br>
        <input type="text" name="description" value="${edit.description}" placeholder="Описание"><br>
        <c:if test="${edit != null}">
            <input type="submit" value="Обновить">
        </c:if>
        <c:if test="${edit == null}">
            <input type="submit" value="Добавить">
        </c:if>
    </form>
</body>
</html>
