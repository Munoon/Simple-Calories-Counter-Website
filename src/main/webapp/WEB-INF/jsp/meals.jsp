<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>

        <%-- Форму скопировал из демо приложения, сам бы я такое не сделал ) --%>
        <form method="get" action="meals/filter" id="filter">
            <div class="card border-dark">
                <div class="row">
                    <div class="offset-1 col-2">
                        <label><spring:message code="meal.startDate"/></label>
                        <input type="date" name="startDate" value="${param.startDate}" class="form-control">
                    </div>
                    <div class="col-2">
                        <label><spring:message code="meal.endDate"/></label>
                        <input type="date" name="endDate" value="${param.endDate}" class="form-control">
                    </div>
                    <div class="offset-2 col-2">
                        <label><spring:message code="meal.startTime"/></label>
                        <input type="time" name="startTime" value="${param.startTime}" class="form-control">
                    </div>
                    <div class="col-2">
                        <label><spring:message code="meal.endTime"/></label>
                        <input type="time" name="endTime" value="${param.endTime}" class="form-control">
                    </div>
                </div>

                <div class="card-footer text-right">
                    <button type="submit" class="btn btn-primary">
                        <span class="fa fa-filter"></span>
                        <spring:message code="meal.filter"/>
                    </button>
                </div>
            </div>
        </form>

        <button class="btn btn-primary" onclick="meals.showModal()">
            <span class="fa fa-plus"></span>
            <spring:message code="meal.add"/>
        </button>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th><spring:message code="meal.dateTime"/></th>
                    <th><spring:message code="meal.description"/></th>
                    <th><spring:message code="meal.calories"/></th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody id="datatable">
                <c:forEach items="${meals}" var="meal">
                    <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>
                    <tr data-mealExcess="${meal.excess}" data-id="${meal.id}">
                        <td>${fn:formatDateTime(meal.dateTime)}</td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a href="meals/update?id=${meal.id}"><span class="fa fa-pencil"></span></a></td>
                        <td><a onclick="meals.delete(${meal.id})"><span class="fa fa-remove"></span></a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade" tabindex="-1" id="createRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime" required>
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description" placeholder="<spring:message code="meal.description"/>" required>
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories" placeholder="<spring:message code="meal.calories"/>" required>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="meals.create()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>

<%--
    Решил использовать класс, так удобнее как по мне.
    Однако это влечёт проблему: переменная meals инициализируется до инициализации класса
    Поэтому делаю так
--%>
<script src="resources/js/topjava.meals.js"></script>
<script>
    let meals = new Meals({
        ajaxUrl: 'ajax/meals/',
        createModal: document.getElementById('createRow'),
        table: document.getElementById('datatable'),
        filter: document.getElementById('filter')
    });
</script>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>