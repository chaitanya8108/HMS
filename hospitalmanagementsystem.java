import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class hospitalmanagementsystem {
	
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "demogorgon@69";

	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patients patients = new Patients(connection, scanner);
			Doctors doctors = new Doctors(connection);
			
			while(true) {
			System.out.println();
			System.out.println("HOSPITAL MANAGEMENT SYSTEM");
			System.out.println("1. View Doctor :\t");
			System.out.println("2. Add Patient :\t");
			System.out.println("3. View Patient :\t");
			System.out.println("4. Book Appointment :\t");
			System.out.println("5. Exit :\t");
			System.out.println("Enter your choice (1-5) : ");
			int choice = scanner.nextInt();
            System.out.println();
			switch(choice) {
				case 1:
//					view doctor
					doctors.viewDoctor();
					break;
					
				case 2:
//					add patient
					patients.addPatient();
					break;
				case 3:
//					view patient
					patients.viewPatient();
					break;
				case 4:
//					book appointment
					bookAppointment(patients, doctors, connection, scanner);
					break;
				case 5:
//					exit
                    System.out.println("Thanks for using Hospital Management System.");
                    System.out.println();
					return;
				default :
					System.out.println("Please enter a valid choice from the above (1-5) !");
			}
			
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void bookAppointment(Patients patients, Doctors doctors, Connection connection, Scanner scanner) {
		System.out.println("Enter Patient ID : ");
		int patient_id = scanner.nextInt();
		System.out.println("Enter Doctor ID : ");
		int doctor_id = scanner.nextInt();
		System.out.println("Enter Appointment Date (YYYY-MM-DD) : ");
		String appointment_date = scanner.next();
		if(patients.getPatientById(patient_id) && doctors.getDoctorById(doctor_id)) {
			if(checkDoctorAvailable(doctor_id, connection, appointment_date)) {
				String appointmentQuery = "insert into appointments (patient_id, doctor_id, appointment_date) values (?, ?, ?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1,  patient_id);
					preparedStatement.setInt(2,  doctor_id);
					preparedStatement.setString(3, appointment_date);
					
					int affectedRows = preparedStatement.executeUpdate();
					if(affectedRows > 0) {
						System.out.println("Appointment Booked Successfully.");
					}else {
						System.out.println("Failed to Book Appointment !");
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Doctor is not Available on this Date !");
			}
			 
		}else {
			System.out.println("Either Patient or Doctor not Found !");
		}
			
	}
	
	public static boolean checkDoctorAvailable(int doctor_id, Connection connection, String appointment_date) {
		
		String checkQuery = "select count(*) from appointments where doctor_id = ? and appointment_date = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
			preparedStatement.setInt(1, doctor_id);
			preparedStatement.setString(2, appointment_date);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				int count = resultSet.getInt(1);
				if(count == 0) {
					return true;
				}else {
					return false;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
