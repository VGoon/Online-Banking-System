# The Revature Bank

Revature Bank is a banking application for anyone to use that also has management views for customers and employees. Login to view transactions, profile information, and account information, as well as creating new transactions, user accounts, and banking accounts. Employees need to approve new accounts and they also have the ability to freeze and unfreeze any account. Customers can deposit, withdraw and manage all accounts they have requested. Manager can view list of customers and search any customer based on transaction id, customer name and account number and review a log of past events. 

## Technologies Used:
- HTML, CSS, and Bootstrap to display to the web page
- AJAX and JavaScript to send out server calls
- Java with Javalin to receive and respond to server calls
- JDBC and PostgreSQL to persist information
- JUnit, Mockito, and Log4j to test and log the process
- DAO design pattern to make sure the database stays intact

## Features
List of features and TODOs for future development
- Customers and Employees can create a user account
- Customers can sign up for a bank account
- Customers can deposit and withdraw money
- Customers can transfer money to their own accounts along with outside accounts
- Employees can approve all accounts
- Employees can freeze and unfreeze accounts
- Employees can review a log of all past transactions
- Employees can delete accounts for customers who no longer wish to have one

To-do list:
- Email a user when an account is approved
- Password reset for customers who have forgotten their password
- Give the customers the choice to change their details
- Add the Front Controller Design Pattern to organize server calls
- Be able to scroll through and delete log messages as an employee
- Be able to filter bank accounts and users
- Hash passwords to keep passwords secure
- Employee logs get a time stamp

## Getting Started
- Be sure to have Postgre SQL Database and configure the environment variables to reflect your personal credentials for the database. 
- Be sure to set up the database with the "DB Table SQL" file
- Make sure you use port 9000
