<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/products">Товары</a>
    <a href="${pageContext.request.contextPath}/orders">Заказы</a>
    <form class="nav-form" action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="link-button">Выйти</button>
    </form>
</nav>
<main>
    <section class="card">
        <h1>Профиль</h1>
        <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
        <p>Логин: <c:out value="${sessionScope.user.username}"/></p>
        <form action="${pageContext.request.contextPath}/profile" method="post">
            <input type="email" name="email" value="<c:out value='${sessionScope.user.email}'/>" required maxlength="100" autocomplete="email">
            <button type="submit">Сохранить</button>
        </form>
    </section>
</main>
</body>
</html>
