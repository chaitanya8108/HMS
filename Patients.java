import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
    
    private Connection connection;
    private Scanner scanner;
    
    public Patients(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public boolean getPatientById(int patient_id) {
        String getPatientQuery = "SELECT * FROM patients WHERE patient_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getPatientQuery);
            preparedStatement.setInt(1, patient_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void viewPatient() {
        String viewPatientQuery = "SELECT * FROM patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(viewPatientQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients : ");
            System.out.println("+--------------+----------------------------+--------------------+---------------------+");
            System.out.println("|  Patient ID  |         Patient Name        |     Patient Age    |    Patient Gender   |");
            System.out.println("+--------------+----------------------------+--------------------+---------------------+");
            
            while(resultSet.next()) {
                int patient_id = resultSet.getInt("patient_id");
                String patient_name = resultSet.getString("patient_name");
                int patient_age = resultSet.getInt("patient_age");
                String patient_gender = resultSet.getString("patient_gender");
                
                System.out.printf("| %-12d | %-26s | %-18d | %-19s |\n", patient_id, patient_name, patient_age, patient_gender);
                System.out.println("+--------------+----------------------------+--------------------+---------------------+");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addPatient() {
        // Read patient name
        System.out.print("Enter Patient Name: ");
        String patient_name = scanner.next();  // Read the name directly as a string

        // Read patient age
        System.out.print("Enter Patient Age: ");
        int patient_age = scanner.nextInt();  // Read the age as an integer
//        scanner.nextLine();  // Consume the newline left over by nextInt()

        // Read patient gender
        System.out.print("Enter Patient Gender: ");
        String patient_gender = scanner.next();  // Read the gender as a string

        // Insert into the database
        try {
            String addPatientQuery = "INSERT INTO patients (patient_name, patient_age, patient_gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(addPatientQuery);
            preparedStatement.setString(1, patient_name);
            preparedStatement.setInt(2, patient_age);
            preparedStatement.setString(3, patient_gender);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed to Add Patient!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
