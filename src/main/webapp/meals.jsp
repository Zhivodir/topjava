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
<form action="meals" method="post">
  <input type="datetime-local" name="datetime" value="" placeholder="Введiть дату та час">
  <input type="text" name="description" value="" placeholder="Введiть опис">
  <input type="number" name="calories" value="" placeholder="Введiть калорii">
  <button type="submit" name="operation" value="add">Добавить еду</button>

  <table>
    <thead>
    <tr>
      <th hidden></th>
      <th>Дата/Время</th>
      <th>Описание</th>
      <th>Калории</th>
      <th></th>
      <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsList}" var="meal">
      <tr class="${meal.excess == true ? "excess" : ""}">
        <th hidden>${meal.id}</th>
        <td>
          <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" type="both" var="parsedDateTime"/>
          <fmt:formatDate value="${parsedDateTime}" pattern="dd.MM.yyyy HH:mm"/>
        </td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td>  <button type="submit" name="operation" value="edit-${meal.id}">Edit</button></td>
        <td><a href="meals?operation=delete&id=${meal.id}">Delete</a></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</form>

</body>
</html>
