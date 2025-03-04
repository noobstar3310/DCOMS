package dcomsassignment;

import java.util.ArrayList;
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
        System.out.println("4. Exit");
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

        // 1. Show which staff members have at least one pending leave
        List<Integer> pendingStaffIds = service.getStaffIdsWithPendingLeaves();
        if (pendingStaffIds.isEmpty()) {
            System.out.println("No staff members have pending leaves at the moment.");
            return;
        }

        System.out.println("Staff members with pending leaves:");
        for (Integer id : pendingStaffIds) {
            System.out.println(" - " + id);
        }

        // 2. Now ask the user which staff ID they want to manage
        System.out.print("\nEnter Staff ID to view or manage leave applications: ");
        int staffId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Display leave status for the chosen staff ID
        List<LeaveRecord> leaveRecords = service.getLeaveStatus(staffId);
        if (leaveRecords.isEmpty()) {
            System.out.println("No leave applications found for Staff ID: " + staffId);
            return;
        }

        // 3. Show the leave records for the chosen staff
        System.out.println("\nLeave Applications for Staff ID: " + staffId);
        for (LeaveRecord record : leaveRecords) {
            System.out.println("Leave ID: " + record.getLeaveRecordID());
            System.out.println("Reason: " + record.getReasonOfLeave());
            System.out.println("Duration: " + record.getDurationOfLeave() + " days");
            System.out.println("Status: " + record.getStatus());
            System.out.println("Date: " + record.getLeaveDate());
            System.out.println("----------------------------");
        }

        // 4. Ask which specific leave record to approve or reject
        System.out.print("Enter Leave Record ID to approve/reject (0 to cancel): ");
        int leaveId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (leaveId <= 0) {
            System.out.println("Operation canceled.");
            return;
        }

        // Check if the chosen leave ID is valid for this staff
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

        // 5. Approve or reject
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter choice: ");
        int decision = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String status = (decision == 1) ? "APPROVED" : "REJECTED";
        boolean success = service.approveLeave(leaveId, status);
        System.out.println(success
                ? "Leave application " + status + " successfully."
                : "Failed to update leave application status.");
    }

    private void generateYearlyReport() throws Exception {
        System.out.println("\n=== Generate Yearly Leave Report ===");
        System.out.print("Enter Staff ID: ");
        int staffId = scanner.nextInt();
        System.out.print("Enter Year (YYYY): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline

        // Retrieve all leave records for the staff
        List<LeaveRecord> leaveRecords = service.getLeaveStatus(staffId);

        // Filter records to only include those matching the specified year
        List<LeaveRecord> filteredRecords = new ArrayList<>();
        for (LeaveRecord record : leaveRecords) {
            // Using Java 8+ approach: convert java.sql.Date to LocalDate and compare year
            int recordYear = record.getLeaveDate().toLocalDate().getYear();
            if (recordYear == year) {
                filteredRecords.add(record);
            }
        }

        // Check if we found any matching records
        if (filteredRecords.isEmpty()) {
            System.out.printf("No leave applications found for Staff ID %d in year %d.\n", staffId, year);
            return;
        }

        // Display the filtered records
        System.out.printf("\nLeave Applications for Staff ID %d in year %d:\n", staffId, year);
        for (LeaveRecord record : filteredRecords) {
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
