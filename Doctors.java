import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Scanner;

public class Doctors {
	private Connection connection;
//	private  Scanner scanner;
	
	public Doctors(Connection connection) {
		this.connection = connection;
//		this.scanner = scanner;
	}
	
	public boolean getDoctorById(int doctor_id) {
		String getDoctorQuery = "select * from doctors where doctor_id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(getDoctorQuery);
			preparedStatement.setInt(1, doctor_id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void viewDoctor() {
		String viewDoctorQuery = "select * from doctors";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(viewDoctorQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("Doctors : ");
			System.out.println("+--------------+----------------------+------------------------+");
			System.out.println("|  Doctor ID   |      Doctor Name     |     Specialization     |");
			System.out.println("+--------------+----------------------+------------------------+");
			
			while(resultSet.next()) {
				
				int doctor_id = resultSet.getInt("doctor_id");
				String doctor_name = resultSet.getString("doctor_name");
				String specialization = resultSet.getString("specialization");
				
				System.out.printf("| %-10s   | %-18s   | %-20s   |\n", doctor_id, doctor_name, specialization);
				System.out.println("+--------------+----------------------+------------------------+");
			}
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}