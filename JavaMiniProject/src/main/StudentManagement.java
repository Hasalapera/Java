package main;

import database.DatabaseConnection;
import student.Login;

public class StudentManagement {

    public static void main(String[] args) {
        System.out.println("Welcome to Student Management System!");
        DatabaseConnection.connect(); // Connect to MySQL
        String userName = "";
        String password = "";
        new Login(); // Open login UI
    }
}
