# Java Customer Management Tool

## Overview
This is a simple customer management tool created using Java and SQL Server as part of my self-study for software development.

The purpose of this project was to understand basic Java programming, database connectivity, and CRUD operations.

## Project Purpose
This project was created to learn:

- Java basic programming
- Database connectivity using JDBC
- SQL basic operations (SELECT / INSERT / UPDATE / DELETE)
- Input validation
- Exception handling
- Basic application flow

## Functions
This application supports the following features:

- Customer list display
- Customer ID search
- Customer name partial search (LIKE search)
- Customer registration
- Customer update
- Customer deletion

## Technologies Used

Language:
- Java

Database:
- SQL Server

Technologies:
- JDBC
- PreparedStatement
- SQL
- Console Application

## Key Learning Points

Through this project I learned:

- How Java connects to a database
- How to execute SQL from Java
- How to handle user input safely
- How to implement CRUD operations
- Basic exception handling techniques
- Simple validation logic

## Implementation Highlights

- Customer ID numeric validation
- Required input validation
- Email format validation
- Duplicate customer ID prevention
- Error handling with try-catch
- Safe SQL execution using PreparedStatement

## How to Run

### Compile

javac -encoding UTF-8 -cp "lib\mssql-jdbc-13.4.0.jre11.jar" -d . src\JavaCustomerSearch.java


### Run


java -cp ".;lib\mssql-jdbc-13.4.0.jre11.jar" JavaCustomerSearch


## Database Structure

Table: customers

Columns:

customer_id (INT)  
customer_name (VARCHAR)  
email (VARCHAR)

## Future Improvements

Possible improvements include:

- GUI implementation (Swing)
- Better email validation
- Logging functionality
- Code refactoring
- Layer separation (Service / DAO)
