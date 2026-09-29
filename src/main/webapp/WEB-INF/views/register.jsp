<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">
    <h1>Регистрация</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <input name="username" placeholder="Логин" required minlength="3" maxlength="50" autocomplete="username">
        <input type="email" name="email" placeholder="Email" required maxlength="100" autocomplete="email">
        <input type="password" name="password" placeholder="Пароль" required minlength="6" maxlength="100" autocomplete="new-password">
        <button type="submit">Зарегистрироваться</button>
    </form>
    <a href="${pageContext.request.contextPath}/login">Войти</a>
</main>
</body>
</html>
