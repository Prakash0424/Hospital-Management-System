package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner scn;

    public Patients(Connection connection, Scanner scn) {
        this.connection = connection;
        this.scn = scn;
    }

    public void addPatients() {
        System.out.print("Enter Patient Name: ");
        String name = scn.next();
        System.out.print("Enter Patient Age: ");
        int age = scn.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scn.next();

        String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added successfully.");
            } else {
                System.out.println("Failed to add patient.");
            }
        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
    }

    public void viewPatient() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Patient Details:");
            System.out.println("| ID | Name       | Age | Gender |");
            System.out.println("-----------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                System.out.printf("| %-2d | %-10s | %-3d | %-6s |\n", id, name, age, gender);
            }
        } catch (Exception e) {
            System.out.println("Error fetching patients: " + e.getMessage());
        }
    }

    public boolean getPatientId(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error fetching patient ID: " + e.getMessage());
        }
        return false;
    }
}
