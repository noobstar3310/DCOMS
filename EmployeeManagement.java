package dcomsassignment;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagement {

    private static Scanner scanner = new Scanner(System.in);
    private HRMService service;
    private int staffId;

    public EmployeeManagement(HRMService service, int staffId) {
        this.service = service;
        this.staffId = staffId;
    }

    public void displayMenu() {
        System.out.println("1. Edit Profile");
        System.out.println("2. Add/Update Family Details");
        System.out.println("3. View Leave Balance");
        System.out.println("4. Apply for Leave");
        System.out.println("5. View leave application status");
        System.out.println("6. Exit");
    }

    public void handleChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                editProfile();
                break;
            case 2:
                updateFamilyDetails();
                break;
            case 3:
                viewLeaveBalance();
                break;
            case 4:
                applyForLeave();
                break;
            case 5:
                viewLeaveStatus();
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void editProfile() throws Exception {
        Employee emp = service.getEmployeeProfile(staffId);
        if (emp != null) {
            System.out.println("=== Employee Profile ===");
            System.out.println("Staff ID: " + emp.getStaffid());
            System.out.println("First Name: " + emp.getFirstName());
            System.out.println("Last Name: " + emp.getLastName());
            System.out.println("IC Number: " + emp.getIcNumber());
            System.out.println("Leave Available: " + emp.getLeaveAvailable());
            System.out.println("Phone Number: " + emp.getPhoneNumber());
        } else {
            System.out.println("No profile found for Staff ID: " + staffId);
        }
        System.out.print("Enter First Name: ");
        emp.setFirstName(scanner.nextLine());
        System.out.print("Enter Last Name: ");
        emp.setLastName(scanner.nextLine());
        System.out.print("Enter IC Number: ");
        emp.setIcNumber(scanner.nextLine());
        System.out.print("Enter Phone Number: ");
        emp.setPhoneNumber(scanner.nextLine());

        boolean updated = service.updateEmployeeProfile(emp);
        System.out.println(updated ? "Profile updated successfully!" : "Update failed.");
    }

    private void updateFamilyDetails() throws Exception {
        Family fam = new Family();
        System.out.print("Enter family member's name: ");
        fam.setGuardianName(scanner.nextLine());

        System.out.println("Select relationship:");
        System.out.println("1. Father");
        System.out.println("2. Mother");
        System.out.println("3. Sibling");
        System.out.println("4. Spouse");
        System.out.println("5. Relative");
        System.out.println("6. Guardian");
        System.out.println("7. Other");

        System.out.print("Choose relationship (1-7): ");
        int relationChoice = scanner.nextInt();
        scanner.nextLine();

        String relationship = getRelationshipType(relationChoice);
        fam.setRelationship(relationship);

        System.out.print("Enter contact number: ");
        fam.setGuardianContact(scanner.nextLine());

        boolean updated = service.addOrUpdateFamilyDetails(fam, staffId);
        System.out.println(updated ? "Family details updated successfully!" : "Update failed.");
    }

    private String getRelationshipType(int choice) {
        switch (choice) {
            case 1:
                return "Father";
            case 2:
                return "Mother";
            case 3:
                return "Sibling";
            case 4:
                return "Spouse";
            case 5:
                return "Relative";
            case 6:
                return "Guardian";
            case 7:
                return "Other";
            default:
                return "Other";
        }
    }

    private void viewLeaveBalance() throws Exception {
        double balance = service.getLeaveBalance(staffId);
        System.out.printf("\nCurrent Leave Balance: %.1f days\n", balance);
    }

    private void applyForLeave() throws Exception {
        LeaveRecord leave = new LeaveRecord();

        System.out.print("Enter reason for leave: ");
        leave.setReasonOfLeave(scanner.nextLine());

        System.out.print("Enter duration (in days): ");
        leave.setDurationOfLeave(scanner.nextDouble());

        // Consume the leftover newline so the next input is read correctly
        scanner.nextLine();

        System.out.print("Enter leave date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();

        try {
            leave.setLeaveDate(Date.valueOf(dateStr));
            boolean applied = service.applyLeave(leave, staffId);
            System.out.println(applied ? "Leave application submitted successfully!" : "Leave application failed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
        }
    }
    
    private void viewLeaveStatus() throws Exception {

        List<LeaveRecord> leaveRecords = service.getLeaveStatus(staffId);

        if (leaveRecords.isEmpty()) {
            System.out.println("You have no leave application!");
            return;
        }

        // Display leave records
        System.out.println("\nLeave Applications");
        for (LeaveRecord record : leaveRecords) {
            System.out.println("Leave ID: " + record.getLeaveRecordID());
            System.out.println("Reason: " + record.getReasonOfLeave());
            System.out.println("Duration: " + record.getDurationOfLeave() + " days");
            System.out.println("Status: " + record.getStatus());
            System.out.println("Date: " + record.getLeaveDate());
            System.out.println("----------------------------");
        }
    }
}
