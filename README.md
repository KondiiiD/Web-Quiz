
# Web Quiz

Simple web quiz API to create, solve and manage database, based on user's authority


## API Reference

**Project uses Liquibase to create database schema and to import test data when the application runs.**

#### Available endpoints for API



| Function | Method     | Api endpoint               |
| :-------- | :------- | :------------------------- |
| `Get all quizzes` | `GET` | /api/quizzes |
| `Get specific quiz by ID` | `GET` | /api/quizzes/{id} |
| `Create quiz` | `POST` | /api/quizzes |
| `Solve quiz` | `POST` | /api/quizzes/{id}/solve |
| `Get all completed quizzes by authenticated user` | `GET` | /api/quizzes/completed |
| `Delete quiz` | `DELETE` | /api/quizzes/{id}|
| `Register new user` | `POST` | /api/user/register |
| `Authenticate user` | `POST` | /api/auth |


- Except registration, all of these endpoints require user to be authenticated.
- Delete operation can be performed only by ADMIN, or author of the quiz.


## Tech Stack

- Java 14
- Spring Boot
- Spring Security
- H2 Database
- Liquibase
- Maven

