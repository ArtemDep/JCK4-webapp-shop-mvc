<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Заказы</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/products">Товары</a>
    <a href="${pageContext.request.contextPath}/profile">Профиль</a>
    <form class="nav-form" action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="link-button">Выйти</button>
    </form>
</nav>
<main>
    <h1>Мои заказы</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <c:choose>
        <c:when test="${empty orders}">
            <p>Заказов пока нет.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="o" items="${orders}">
                <article class="card">
                    <h2>Заказ #<c:out value="${o.id}"/></h2>
                    <p>Товар: <c:out value="${o.productName}"/></p>
                    <p>Количество: <c:out value="${o.quantity}"/></p>
                    <p>Сумма: <c:out value="${o.totalPrice}"/> ₽</p>
                    <p>Статус: <c:out value="${o.status}"/></p>
                    <c:if test="${o.status == 'CREATED'}">
                        <form action="${pageContext.request.contextPath}/orders" method="post">
                            <input type="hidden" name="action" value="cancel">
                            <input type="hidden" name="id" value="${o.id}">
                            <button type="submit" class="danger">Отменить</button>
                        </form>
                    </c:if>
                </article>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
