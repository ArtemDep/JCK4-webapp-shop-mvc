<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Товары</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<nav>
    <b>Shop MVC</b>
    <a href="${pageContext.request.contextPath}/products">Товары</a>
    <a href="${pageContext.request.contextPath}/orders">Мои заказы</a>
    <a href="${pageContext.request.contextPath}/profile">Профиль</a>
    <form class="nav-form" action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="link-button">Выйти</button>
    </form>
</nav>
<main>
    <h1>Товары</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <c:if test="${sessionScope.user.role == 'ADMIN'}">
        <section class="card">
            <h2>Добавить товар</h2>
            <form action="${pageContext.request.contextPath}/products" method="post">
                <input type="hidden" name="action" value="add">
                <input name="name" placeholder="Название" required maxlength="150">
                <input name="description" placeholder="Описание" maxlength="2000">
                <input type="number" step="0.01" min="0" name="price" placeholder="Цена" required>
                <input type="number" min="0" name="quantity" placeholder="Количество" required>
                <button type="submit">Добавить</button>
            </form>
        </section>
    </c:if>

    <section class="grid">
        <c:forEach var="p" items="${products}">
            <article class="card">
                <h2><c:out value="${p.name}"/></h2>
                <p><c:out value="${p.description}"/></p>
                <p><b><c:out value="${p.price}"/></b> ₽</p>
                <p>На складе: <c:out value="${p.quantity}"/></p>
                <c:if test="${p.quantity > 0}">
                    <form action="${pageContext.request.contextPath}/orders" method="post">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" name="productId" value="${p.id}">
                        <input type="number" name="quantity" min="1" max="${p.quantity}" value="1" required>
                        <button type="submit">Заказать</button>
                    </form>
                </c:if>
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <form action="${pageContext.request.contextPath}/products" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit" class="danger">Удалить</button>
                    </form>
                </c:if>
            </article>
        </c:forEach>
    </section>
</main>
</body>
</html>
