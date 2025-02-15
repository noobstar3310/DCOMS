package dcomsassignment;

import java.util.Scanner;

public class HRManagement {
    private static Scanner scanner = new Scanner(System.in);
    private HRMService service;

    public HRManagement(HRMService service) {
        this.service = service;
    }

    public void displayMenu() {
        System.out.println("1. Register New Employee");
        System.out.println("2. Manage Leave Applications");
        System.out.println("3. Generate Yearly Leave Report");
        System.out.println("4. Remove Employee");
        System.out.println("5. Exit");
    }

    public void handleChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                registerNewEmployee();
                break;
            case 2:
                manageLeaveApplications();
                break;
            case 3:
                generateYearlyReport();
                break;
            case 4:
                removeEmployee();
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void registerNewEmployee() throws Exception {
        System.out.println("\n=== Register New Employee ===");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter IC Number: ");
        String icNumber = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Initial Leave Balance (Default is 10): ");
        double leaveBalance = scanner.nextDouble();
        scanner.nextLine();

        Thread registrationThread = new Thread(() -> {
            try {
                System.out.println("Processing registration...");
                boolean registered = service.RegisterEmployee(firstName, lastName, icNumber, leaveBalance, phone);
                System.out.println(registered ? "Employee registered successfully!" : "Registration failed.");
            } catch (Exception e) {
                System.out.println("Registration error: " + e.getMessage());
            }
        });
        registrationThread.start();
        registrationThread.join();
    }

    private void manageLeaveApplications() throws Exception {
        System.out.println("\n=== Manage Leave Applications ===");
        System.out.print("Enter Staff ID to view their leave applications: ");
        int staffId = scanner.nextInt();
        scanner.nextLine();

        service.displayLeaveStatus(staffId);

        System.out.print("Enter Leave Record ID to approve/reject (0 to cancel): ");
        int leaveId = scanner.nextInt();
        scanner.nextLine();

        if (leaveId > 0) {
            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.print("Enter choice: ");
            int decision = scanner.nextInt();
            scanner.nextLine();

            String status = (decision == 1) ? "APPROVED" : "REJECTED";
            System.out.println("Leave application " + status);
        }
    }

    private void generateYearlyReport() throws Exception {
        System.out.println("\n=== Generate Yearly Leave Report ===");
        System.out.print("Enter Staff ID: ");
        int staffId = scanner.nextInt();
        System.out.print("Enter Year (YYYY): ");
        int year = scanner.nextInt();
        scanner.nextLine();

        service.displayLeaveStatus(staffId);
    }

    private void removeEmployee() {
        System.out.println("This feature is under development.");
    }
}