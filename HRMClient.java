package dcomsassignment;

import java.net.InetAddress;
import java.rmi.Naming;
import java.util.Scanner;

public class HRMClient {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            //String serverIP = "172.20.10.2";
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            HRMService service = (HRMService) Naming.lookup("rmi://" + serverIP + "/HRMService");

            System.out.println("=== Welcome to HR & Employee Management System ===");
            System.out.println("Login as:");
            System.out.println("1. Employee");
            System.out.println("2. HR/Admin");
            System.out.print("Enter choice: ");
            int userType = scanner.nextInt();
            scanner.nextLine();

            if (userType == 1) {
                handleEmployeeLogin(service);
            } else if (userType == 2) {
                handleHRLogin(service);
            } else {
                System.out.println("Invalid choice. Exiting...");
            }
            
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void handleEmployeeLogin(HRMService service) throws Exception {
        System.out.print("Enter your Staff ID: ");
        int staffId = scanner.nextInt();
        scanner.nextLine();

        if (!service.isValidStaffId(staffId)) {
            System.out.println("Invalid Staff ID. Exiting...");
            return;
        }

        EmployeeManagement empManagement = new EmployeeManagement(service, staffId);
        
        while (true) {
            System.out.println("\n=== Employee Menu ===");
            empManagement.displayMenu();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) {
                System.out.println("Thank you for using the system. Goodbye!");
                break;
            }
            
            empManagement.handleChoice(choice);
        }
    }

    private static void handleHRLogin(HRMService service) throws Exception {
        System.out.print("Enter HR/Admin Password: ");
        String password = scanner.nextLine();
        
        if (!password.equals("admin123")) {
            System.out.println("Incorrect password. Exiting...");
            return;
        }

        HRManagement hrManagement = new HRManagement(service);
        
        while (true) {
            System.out.println("\n=== HR Menu ===");
            hrManagement.displayMenu();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) {
                System.out.println("Thank you for using the system. Goodbye!");
                break;
            }
            
            hrManagement.handleChoice(choice);
        }
    }
}