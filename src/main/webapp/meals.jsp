<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
  table {
    border: 1px solid;
    border-collapse: collapse;
  }

  td, th {
    padding: 5px;
    border: 1px solid black;
  }

  th {
    background: #b0e0e6;
  }

  .excess {
    background-color: red;
  }
</style>

<html>
<head>
  <title>List of meals</title>
</head>

<body>
<table>
  <thead>
    <tr>
      <th>Дата/Время</th>
      <th>Описание</th>
      <th>Калории</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach items="${mealsList}" var="meal">
    <tr class="${meal.excess == true ? "excess" : ""}">
      <td>
        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" type="both" var="parsedDateTime" />
        <fmt:formatDate value="${parsedDateTime}" pattern="dd.MM.yyyy HH:mm" />
      </td>
      <td>${meal.description}</td>
      <td>${meal.calories}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</body>
</html>
