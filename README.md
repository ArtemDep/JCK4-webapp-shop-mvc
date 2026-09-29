# Shop MVC — Servlet/JSP/Tomcat/PostgreSQL

Учебный Web Project на Java 17 с Servlet, JSP/JSTL, MVC, Layered Architecture, JDBC, DAO и PostgreSQL.

## Требования
- JDK 17+
- Maven 3.9+
- Tomcat 10.1+
- PostgreSQL 14+

## Архитектура

```text
Controller (Servlet)
        ↓
Service
        ↓
DAO
        ↓
JDBC / PostgreSQL
```

JSP/JSTL используется как View. Фильтры отвечают за кодировку UTF-8, авторизацию, права администратора и security headers.

## База данных

Создайте БД `shop_db`, затем выполните `src/main/resources/schema.sql`.

Данные подключения находятся в `src/main/resources/db.properties` и должны соответствовать вашей локальной PostgreSQL.

Схема содержит три основные таблицы:
- `users`
- `products`
- `orders`

Часть начальных данных содержит кириллицу. Приложение принудительно использует UTF-8.

## Запуск в IntelliJ IDEA

1. Откройте папку проекта как Maven project.
2. Выполните Maven `clean package`.
3. Добавьте Tomcat 10.1+.
4. Deployment → `shop-mvc:war exploded`.
5. Application context: `/shop-mvc`.

## Демонстрационный администратор

- login: `admin`
- password: `admin`

Пароль в базе хранится только в виде BCrypt-хеша.

## Реализованные требования

- Servlet + JSP/JSTL без скриплетов
- MVC и Layered Architecture
- JDBC и DAO
- HikariCP connection pool
- PreparedStatement для защиты от SQL Injection
- BCrypt для паролей
- Session для пользовательского состояния
- Authentication и Admin filters
- UTF-8 encoding filter
- Security headers и HTML escaping через JSTL `c:out` для защиты от XSS
- POST/Redirect/GET для успешных изменяющих операций
- Client-side и server-side validation
- SLF4J/Log4J2 logging
- JUnit 5 + Mockito tests
- Javadoc для основных классов и методов
- Maven build
- Sign in / Sign out / Sign up
- Просмотр товаров и заказов
- Создание и отмена заказов
- Добавление и удаление товаров администратором
- Редактирование email профиля

## Структура

```text
src/main/java/com/shop
├── controller
├── dao
├── filter
├── listener
├── model
├── service
└── util

src/main/resources
├── db.properties
├── log4j2.xml
└── schema.sql

src/main/webapp
├── WEB-INF/views
└── style.css

src/test/java/com/shop
├── service
└── util
```
