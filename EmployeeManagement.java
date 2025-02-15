package dcomsassignment;

import java.sql.Date;
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
        System.out.println("5. Exit");
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
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void editProfile() throws Exception {
        System.out.println(service.displayEmployeeProfile(staffId));
        System.out.print("Enter new first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter new last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter new IC number: ");
        String icNumber = scanner.nextLine();

        boolean updated = service.updateEmployeeProfile(staffId, firstName, lastName, phoneNumber, Integer.parseInt(icNumber));
        System.out.println(updated ? "Profile updated successfully!" : "Update failed.");
    }

    private void updateFamilyDetails() throws Exception {
        System.out.print("Enter family member's name: ");
        String guardianName = scanner.nextLine();
        
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
        
        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine();
        
        boolean updated = service.addOrUpdateFamilyDetails(staffId, guardianName, relationship, contact);
        System.out.println(updated ? "Family details updated successfully!" : "Update failed.");
    }

    private String getRelationshipType(int choice) {
        switch (choice) {
            case 1: return "Father";
            case 2: return "Mother";
            case 3: return "Sibling";
            case 4: return "Spouse";
            case 5: return "Relative";
            case 6: return "Guardian";
            case 7: return "Other";
            default: return "Other";
        }
    }

    private void viewLeaveBalance() throws Exception {
        double balance = service.getLeaveBalance(staffId);
        System.out.printf("\nCurrent Leave Balance: %.1f days\n", balance);
    }

    private void applyForLeave() throws Exception {
        System.out.print("Enter reason for leave: ");
        String reason = scanner.nextLine();
        
        System.out.print("Enter duration (in days): ");
        double duration = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Enter leave date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        
        try {
            Date leaveDate = Date.valueOf(dateStr);
            boolean applied = service.applyLeave(staffId, reason, duration, leaveDate);
            System.out.println(applied ? "Leave application submitted successfully!" : "Leave application failed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
        }
    }
}
