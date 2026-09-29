<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">
    <h1>Вход</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <input name="username" placeholder="Логин" required minlength="3" maxlength="50" autocomplete="username">
        <input type="password" name="password" placeholder="Пароль" required minlength="6" maxlength="100" autocomplete="current-password">
        <button type="submit">Войти</button>
    </form>
    <a href="${pageContext.request.contextPath}/register">Регистрация</a>
</main>
</body>
</html>
