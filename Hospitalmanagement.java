package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Hospitalmanagement {

    private static final String url = "jdbc:mysql://localhost:3306/hospitalm";
    private static final String username = "root";
    private static final String password = "Suriya1@";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Driver loading error: " + e.getMessage());
        }

        Scanner scn = new Scanner(System.in);
        try (
        	Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database.");
            Patients patient = new Patients(connection, scn);
            Doctors doctor = new Doctors(connection);

            while (true) {
                System.out.println("\nWelcome To ABC Hospital Management");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter Your Choice: ");
                int choice = scn.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatients();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, scn);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid request. Please try again.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patients patient, Doctors doctor, Connection connection, Scanner scn) {
        System.out.print("Enter The Patient ID: ");
        int patientId = scn.nextInt();

        System.out.print("Enter the Patient Name: ");
        String patientName = scn.next();

        System.out.print("Enter The Doctor ID: ");
        int doctorId = scn.nextInt();

        System.out.print("Enter The Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scn.next();

        if (patient.getPatientId(patientId) && doctor.getDoctorId(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointment(patient_id, patient_name, doctor_id, appointment_date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(appointmentQuery)) {
                    ps.setInt(1, patientId);
                    ps.setString(2, patientName);
                    ps.setInt(3, doctorId);
                    ps.setString(4, appointmentDate);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked successfully!");
                    } else {
                        System.out.println("Failed to book appointment.");
                    }
                } catch (Exception e) {
                    System.out.println("Error while booking appointment: " + e.getMessage());
                }
            } else {
                System.out.println("The doctor is not available on the given date.");
            }
        } else {
            System.out.println("Invalid patient ID or doctor ID.");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, doctorId);
            ps.setString(2, appointmentDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; 
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking doctor availability: " + e.getMessage());
        }
        return false;
    }
}
