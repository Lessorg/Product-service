# Проєкт Product-service

## Запуск проекту
Потрібно мати встановлений JDK 17+.
### Клонування репозиторію
```bash
git clone <https://github.com/Lessorg/Product-service>
cd <папка проекту>
```
### Налаштування IDE

В IntelliJ IDEA потрібно увімкнути Annotation Processing:

File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors

### Запуск 
Maven:

```bash
./mvnw spring-boot:run
```

## Архітектура проєкту
Проєкт побудований за багатошаровою архітектурою з використанням Spring Boot.
### Основні шари:
1. **Контролер (Controller)**  
   ProductController відповідає за прийом HTTP-запитів, валідацію вхідних даних та відправку відповідей клієнту.
2. **Сервіс (Service)**  
   ProductServiceImpl у ньому відбувається обробка даних, взаємодія з репозиторієм, кешування, валідації та інші операції.
3. **Репозиторій (Repository)**  
   ProductRepository взаємодіє з базою даних через Spring Data JPA.
4. **Модель (Model/Entity)**  
   Product
5. **DTO (Data Transfer Object)**  
   ProductRequestDto зберігає базову логіку валідації.
6. **Кешування (Cache)**  
   Використання Caffeine для кешування часто запитуваних даних з TTL 10 хвилин.

   UML діаграма:
   ![task](https://github.com/user-attachments/assets/a40e1745-e998-4b46-977a-3d470232e1a0)
## API документація

- **GET** `/api/v1/products/{id}`  
  Отримати товар за ідентифікатором

- **PUT** `/api/v1/products/{id}`  
  Оновити інформацію про товар

- **DELETE** `/api/v1/products/{id}`  
  Видалити товар

- **GET** `/api/v1/products`  
  Отримати всі товари (з підтримкою пагінації)

- **POST** `/api/v1/products`  
  Створити новий товар

- **GET** `/api/v1/products/category/{category}`  
  Отримати товари за категорією

Swagger documentation(http://localhost:8080/swagger-ui/index.html):

![image](https://github.com/user-attachments/assets/18761c92-6a66-47fc-8468-d297b9834a76)

## Припущення

- **Пагінація**  
  Якщо ендпоінт не потребує пагінації за умоваою завдання, **Pageable** не використовується.

- **Description**  
  Немає обмежень по довжині для поля `description`.

- **Response DTO**  
  Окремі Response DTO не створювалися, оскільки не бачив необхідності для реалізації завдання.

- **Валідація**  
  Поля Request DTO валідуються за допомогою анотацій (`@NotBlank`, `@NotNull`, тощо).

- **Безпека**  
  Наразі реалізовано Basic Auth без окремих ролей.
  (Краще було б створити власну сутність `User`, а не використовувати In-Memory менеджер)

## Використані технології
- **Spring Boot** — для створення REST сервісу.
- **Spring Data JPA** — робота з базою даних.
- **H2** — для зберігання даних.
- **Caffeine Cache** — кешування з TTL (10 хвилин).
- **Spring Security** — базова автентифікація юзер (user::password).
- **Flyway** — створення схеми бд.
- **JUnit + Spring Test** — для тестів.
- **Swagger та MapStruct** - зменшення кількості boilerplate коду.
