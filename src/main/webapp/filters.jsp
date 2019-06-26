<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
  .filter_block {
    padding: 5px;
    width: 100%;
  }

  .block {
    padding: 10px;
    float: left;
  }
</style>

<form action="meals" method="post" class="filter_block">
  <div class="block">
    <div>
      <span>От даты:</span>
      <input type="date" name="fromDate" value="">
    </div>
    <div>
      <span>До даты:</span>
      <input type="date" name="toDate" value="">
    </div>
  </div>

  <div class="block">
    <div>
      <span>От времени:</span>
      <input type="time" name="fromTime" value="">
    </div>
    <div>
      <span>До времени:</span>
      <input type="time" name="toTime"value="">
    </div>
  </div>

  <div>
    <button type="submit" name="action" value="filter">Фильтровать</button>
  </div>
</form>
<br>
<hr/>