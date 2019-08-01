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
        <form method="get" action="meals/filter">
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
                        <input type="date" name="startTime" value="${param.startTime}" class="form-control">
                    </div>
                    <div class="col-2">
                        <label><spring:message code="meal.endTime"/></label>
                        <input type="date" name="endTime" value="${param.endTime}" class="form-control">
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

        <a class="btn btn-primary" href="meals/create">
            <span class="fa fa-plus"></span>
            <spring:message code="meal.add"/>
        </a>

        <table class="table table-striped" id="datatable">
            <thead>
                <tr>
                    <th><spring:message code="meal.dateTime"/></th>
                    <th><spring:message code="meal.description"/></th>
                    <th><spring:message code="meal.calories"/></th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>
                <tr data-mealExcess="${meal.excess}">
                    <td>${fn:formatDateTime(meal.dateTime)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals/update?id=${meal.id}"><span class="fa fa-pencil"></span></a></td>
                    <td><a href="meals/delete?id=${meal.id}"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>