package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctors {
    private Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
    }
    public void viewDoctor() {
        String query = "SELECT * FROM doctors";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Doctor Details:");
            System.out.println("| ID | Name       | Department   |");
            System.out.println("-----------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String dept = rs.getString("dept");
                System.out.printf("| %-2d | %-10s | %-11s |\n", id, name, dept);
            }
        } catch (Exception e) {
            System.out.println("Error fetching doctors: " + e.getMessage());
        }
    }

    public boolean getDoctorId(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error fetching doctor ID: " + e.getMessage());
        }
        return false;
    }
}
