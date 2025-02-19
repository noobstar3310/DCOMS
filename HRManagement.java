package dcomsassignment;

import java.util.List;
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
        Employee np = new Employee();

        System.out.println("\n=== Register New Employee ===");
        System.out.print("Enter First Name: ");
        np.setFirstName(scanner.nextLine());
        System.out.print("Enter Last Name: ");
        np.setLastName(scanner.nextLine());
        System.out.print("Enter IC Number: ");
        np.setIcNumber(scanner.nextLine());
        System.out.print("Enter Phone Number: ");
        np.setPhoneNumber(scanner.nextLine());
        System.out.print("Enter Initial Leave Balance (Default is 10): ");
        np.setLeaveAvailable(scanner.nextDouble());
        scanner.nextLine();

        Thread registrationThread = new Thread(() -> {
            try {
                System.out.println("Processing registration...");
                boolean registered = service.RegisterEmployee(np);
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
        scanner.nextLine(); // Consume the newline character

        // Display leave status for the given staff ID
        List<LeaveRecord> leaveRecords = service.getLeaveStatus(staffId);

        if (leaveRecords.isEmpty()) {
            System.out.println("No leave applications found for Staff ID: " + staffId);
            return;
        }

        // Display leave records
        System.out.println("\nLeave Applications for Staff ID: " + staffId);
        for (LeaveRecord record : leaveRecords) {
            System.out.println("Leave ID: " + record.getLeaveRecordID());
            System.out.println("Reason: " + record.getReasonOfLeave());
            System.out.println("Duration: " + record.getDurationOfLeave() + " days");
            System.out.println("Status: " + record.getStatus());
            System.out.println("Date: " + record.getLeaveDate());
            System.out.println("----------------------------");
        }

        System.out.print("Enter Leave Record ID to approve/reject (0 to cancel): ");
        int leaveId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (leaveId > 0) {
            // Check if the leave ID is valid for the given staff ID
            boolean validLeaveId = false;
            for (LeaveRecord record : leaveRecords) {
                if (record.getLeaveRecordID() == leaveId) {
                    validLeaveId = true;
                    break;
                }
            }

            if (!validLeaveId) {
                System.out.println("Invalid Leave Record ID for Staff ID: " + staffId);
                return;
            }

            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.print("Enter choice: ");
            int decision = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String status = (decision == 1) ? "APPROVED" : "REJECTED";

            // Call the approveLeave method to update the leave status
            boolean success = service.approveLeave(leaveId, status);

            if (success) {
                System.out.println("Leave application " + status + " successfully.");
            } else {
                System.out.println("Failed to update leave application status.");
            }
        } else {
            System.out.println("Operation canceled.");
        }
    }

    private void generateYearlyReport() throws Exception {
        System.out.println("\n=== Generate Yearly Leave Report ===");
        System.out.print("Enter Staff ID: ");
        int staffId = scanner.nextInt();
        System.out.print("Enter Year (YYYY): ");
        int year = scanner.nextInt();
        scanner.nextLine();

        List<LeaveRecord> leaveRecords = service.getLeaveStatus(staffId);

        if (leaveRecords.isEmpty()) {
            System.out.println("No leave applications found for Staff ID: " + staffId);
            return;
        }

        // Display leave records
        System.out.println("\nLeave Applications for Staff ID: " + staffId);
        for (LeaveRecord record : leaveRecords) {
            System.out.println("Leave ID: " + record.getLeaveRecordID());
            System.out.println("Reason: " + record.getReasonOfLeave());
            System.out.println("Duration: " + record.getDurationOfLeave() + " days");
            System.out.println("Status: " + record.getStatus());
            System.out.println("Date: " + record.getLeaveDate());
            System.out.println("----------------------------");
        }
    }

    private void removeEmployee() {
        System.out.println("This feature is under development.");
    }
}
