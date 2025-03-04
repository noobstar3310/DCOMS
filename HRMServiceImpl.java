package dcomsassignment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HRMServiceImpl extends UnicastRemoteObject implements HRMService {
//    private Connection conn;
//
//    public HRMServiceImpl(Connection conn) throws RemoteException {
//        this.conn = conn;
//    }
//
//    public HRMServiceImpl() throws RemoteException {
//        super();
//        try {
//            // Establish the database connection (Modify URL, username, password as per your DB)
//            String url = "jdbc:derby://localhost:1527/DCOMS";  // Java DB (Derby) Example
//            String user = "DCOMS";
//            String password = "DCOMS";
//
//            conn = DriverManager.getConnection(url, user, password);
//            System.out.println("Database connected successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static final long serialVersionUID = 1L;
    private Connection conn;
    private final ExecutorService executorService;

    public HRMServiceImpl() throws RemoteException {
        super();
        this.executorService = Executors.newFixedThreadPool(5);
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            String url = "jdbc:derby://localhost:1527/DCOMS";
            String user = "DCOMS";
            String password = "DCOMS";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @Override
    public boolean RegisterEmployee(Employee employee) throws RemoteException {
        try {
            String query = "INSERT INTO Employee (first_name, last_name, ic_number, phone_number, leave_available) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getIcNumber());  // Ensure ic_number is unique
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setDouble(5, employee.getLeaveAvailable());

            int rowsInserted = stmt.executeUpdate();

            // Check if insertion was successful
            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedStaffId = rs.getInt(1);
                    System.out.println("Employee registered successfully! Staff ID: " + generatedStaffId);
                }
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // @Override
    public boolean updateEmployeeProfile(Employee employee) throws RemoteException {
        try {
            String query = "UPDATE Employee SET first_name = ?, last_name = ?, phone_number = ?, ic_number = ? WHERE staffid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getPhoneNumber());
            stmt.setString(4, employee.getIcNumber());
            stmt.setInt(5, employee.getStaffid());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // @Override
    public boolean addOrUpdateFamilyDetails(Family family, int staffID) throws RemoteException {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Family WHERE staffid = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, staffID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Update existing record
                String updateQuery = "UPDATE Family SET guardian_name = ?, relationship = ?, guardian_contact = ? WHERE staffid = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setString(1, family.getGuardianName());
                updateStmt.setString(2, family.getRelationship());
                updateStmt.setString(3, family.getGuardianContact());
                updateStmt.setInt(4, staffID);
                return updateStmt.executeUpdate() > 0;
            } else {
                // Insert new record
                String insertQuery = "INSERT INTO Family (staffid, guardian_name, relationship, guardian_contact) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, staffID);
                insertStmt.setString(2, family.getGuardianName());
                insertStmt.setString(3, family.getRelationship());
                insertStmt.setString(4, family.getGuardianContact());
                return insertStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // @Override
    public boolean applyLeave(LeaveRecord leave, int staffID) throws RemoteException {
        try {
            // Check if employee has enough leave balance
            String checkLeaveQuery = "SELECT leave_available FROM Employee WHERE staffid = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkLeaveQuery);
            checkStmt.setInt(1, staffID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double leaveBalance = rs.getDouble("leave_available");

                if (leaveBalance < leave.getDurationOfLeave()) {
                    System.out.println("Insufficient leave balance!");
                    return false;  // Reject leave application due to insufficient balance
                }

                // Insert leave request into LeaveRecord table
                String insertQuery = "INSERT INTO LeaveRecord (reason_of_leave, duration_of_leave, status, leave_date, staffid) VALUES (?, ?, 'PENDING', ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertQuery);

                stmt.setString(1, leave.getReasonOfLeave());
                stmt.setDouble(2, leave.getDurationOfLeave());
                stmt.setDate(3, new java.sql.Date(leave.getLeaveDate().getTime()));
                stmt.setInt(4, staffID);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Leave application submitted successfully.");
                    return true;
                }
            } else {
                System.out.println("Employee not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // @Override
    public void displayLeaveStatus(int staffId) throws RemoteException {
        try {
            String query = "SELECT leaveRecordID, reason_of_leave, duration_of_leave, status, leave_date "
                    + "FROM LeaveRecord WHERE staffid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=== Leave Application Status ===");
            boolean hasRecords = false;

            while (rs.next()) {
                hasRecords = true;
                int leaveRecordID = rs.getInt("leaveRecordID");
                String reason = rs.getString("reason_of_leave");
                double duration = rs.getDouble("duration_of_leave");
                String status = rs.getString("status");
                Date leaveDate = rs.getDate("leave_date");

                System.out.println("Leave ID: " + leaveRecordID);
                System.out.println("Reason: " + reason);
                System.out.println("Duration: " + duration + " day(s)");
                System.out.println("Date: " + leaveDate);
                System.out.println("Status: " + status);
                System.out.println("----------------------------");
            }

            if (!hasRecords) {
                System.out.println("No leave applications found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @Override
    public Employee getEmployeeProfile(int staffId) throws RemoteException {
        try {
            String query = "SELECT staffid, first_name, last_name, ic_number, leave_available, phone_number "
                    + "FROM Employee WHERE staffid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                        rs.getInt("staffid"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("ic_number"),
                        rs.getDouble("leave_available"),
                        rs.getString("phone_number")
                );
            } else {
                return null; // No employee found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving employee profile", e);
        }
    }

    @Override
    public List<LeaveRecord> getLeaveStatus(int staffId) throws RemoteException {
        List<LeaveRecord> leaveRecords = new ArrayList<>();

        try {
            String query = "SELECT leaveRecordID, reason_of_leave, duration_of_leave, status, leave_date "
                    + "FROM LeaveRecord WHERE staffid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int leaveRecordID = rs.getInt("leaveRecordID");
                String reasonOfLeave = rs.getString("reason_of_leave");
                double durationOfLeave = rs.getDouble("duration_of_leave");
                String status = rs.getString("status");
                Date leaveDate = rs.getDate("leave_date");

                // Create a LeaveRecord object and add it to the list
                LeaveRecord leaveRecord = new LeaveRecord(leaveRecordID, reasonOfLeave, durationOfLeave, status, leaveDate, staffId);
                leaveRecords.add(leaveRecord);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaveRecords;
    }

    // @Override
    public double getLeaveBalance(int staffId) throws RemoteException {
        double leaveBalance = -1; // Default value in case of failure
        try {
            String query = "SELECT leave_available FROM Employee WHERE staffid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                leaveBalance = rs.getDouble("leave_available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveBalance;
    }

    // @Override
    public boolean isValidStaffId(int staffId) throws RemoteException {
        try {
            String query = "SELECT COUNT(*) FROM Employee WHERE staffid = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Staff ID exists
            } else {
                return false; // Staff ID does not exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean approveLeave(int leaveRecordId, String status) throws RemoteException {
//        return executorService.submit(() -> {
//            try {
//                conn.setAutoCommit(false); // Start transaction
//                
//                // First get the leave record details
//                String getLeaveQuery = "SELECT lr.staffid, lr.duration_of_leave, lr.status, e.leave_available " +
//                                     "FROM LeaveRecord lr " +
//                                     "JOIN Employee e ON lr.staffid = e.staffid " +
//                                     "WHERE lr.leaveRecordID = ?";
//                PreparedStatement getStmt = conn.prepareStatement(getLeaveQuery);
//                getStmt.setInt(1, leaveRecordId);
//                ResultSet rs = getStmt.executeQuery();
//                
//                if (rs.next()) {
//                    int staffId = rs.getInt("staffid");
//                    double duration = rs.getDouble("duration_of_leave");
//                    String currentStatus = rs.getString("status");
//                    double currentLeaveBalance = rs.getDouble("leave_available");
//                    
//                    // Check if leave is already processed
//                    if (!currentStatus.equals("PENDING")) {
//                        System.out.println("Leave request already processed");
//                        conn.rollback();
//                        return false;
//                    }
//                    
//                    // Validate leave balance if approving
//                    if (status.equals("APPROVED") && currentLeaveBalance < duration) {
//                        System.out.println("Insufficient leave balance");
//                        conn.rollback();
//                        return false;
//                    }
//                    
//                    // Update leave record status
//                    String updateStatusQuery = "UPDATE LeaveRecord SET status = ? WHERE leaveRecordID = ?";
//                    PreparedStatement updateStmt = conn.prepareStatement(updateStatusQuery);
//                    updateStmt.setString(1, status);
//                    updateStmt.setInt(2, leaveRecordId);
//                    updateStmt.executeUpdate();
//                    
//                    // If approved, update employee's leave balance
//                    if (status.equals("APPROVED")) {
//                        String updateBalanceQuery = "UPDATE Employee SET leave_available = leave_available - ? WHERE staffid = ?";
//                        PreparedStatement balanceStmt = conn.prepareStatement(updateBalanceQuery);
//                        balanceStmt.setDouble(1, duration);
//                        balanceStmt.setInt(2, staffId);
//                        balanceStmt.executeUpdate();
//                    }
//                    
//                    conn.commit();
//                    return true;
//                }
//                
//                conn.rollback();
//                return false;
//            } catch (SQLException e) {
//                try {
//                    conn.rollback();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//                e.printStackTrace();
//                return false;
//            } finally {
//                try {
//                    conn.setAutoCommit(true);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).get();
//    }
    public boolean approveLeave(int leaveRecordId, String status) throws RemoteException {
        Future<Boolean> future = executorService.submit(() -> {
            try {
                conn.setAutoCommit(false); // Start transaction

                // Retrieve leave record details
                String query = "SELECT lr.staffid, lr.duration_of_leave, lr.status, e.leave_available "
                        + "FROM LeaveRecord lr "
                        + "JOIN Employee e ON lr.staffid = e.staffid "
                        + "WHERE lr.leaveRecordID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, leaveRecordId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        int staffId = rs.getInt("staffid");
                        double duration = rs.getDouble("duration_of_leave");
                        String currentStatus = rs.getString("status");
                        double currentLeaveBalance = rs.getDouble("leave_available");

                        // If leave is already processed
                        if (!currentStatus.equals("PENDING")) {
                            conn.rollback();
                            return false;
                        }

                        // If approving, ensure sufficient leave balance
                        if (status.equals("APPROVED") && currentLeaveBalance < duration) {
                            conn.rollback();
                            return false;
                        }

                        // Update leave record status
                        String updateStatusQuery = "UPDATE LeaveRecord SET status = ? WHERE leaveRecordID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateStatusQuery)) {
                            updateStmt.setString(1, status);
                            updateStmt.setInt(2, leaveRecordId);
                            updateStmt.executeUpdate();
                        }

                        // If approved, update employee's leave balance
                        if (status.equals("APPROVED")) {
                            String updateBalanceQuery = "UPDATE Employee SET leave_available = leave_available - ? WHERE staffid = ?";
                            try (PreparedStatement balanceStmt = conn.prepareStatement(updateBalanceQuery)) {
                                balanceStmt.setDouble(1, duration);
                                balanceStmt.setInt(2, staffId);
                                balanceStmt.executeUpdate();
                            }
                        }

                        conn.commit();
                        return true;
                    }
                }

                conn.rollback();
                return false;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        });

        try {
            return future.get(); // Get the result from the async execution
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    
    public String generateYearlyReport(int staffId, int year) throws RemoteException {
        Future<String> future = executorService.submit(() -> {
            StringBuilder report = new StringBuilder();

            try {
                // Query to get employee details
                String empQuery = "SELECT first_name, last_name FROM Employee WHERE staffid = ?";
                try (PreparedStatement empStmt = conn.prepareStatement(empQuery)) {
                    empStmt.setInt(1, staffId);
                    try (ResultSet empRs = empStmt.executeQuery()) {
                        if (!empRs.next()) {
                            return "Employee not found";
                        }

                        String firstName = empRs.getString("first_name");
                        String lastName = empRs.getString("last_name");

                        // Query to get leave records for the given year
                        String leaveQuery = "SELECT leaveRecordID, leave_date, duration_of_leave, reason_of_leave, status "
                                + "FROM LeaveRecord WHERE staffid = ? AND YEAR(leave_date) = ? "
                                + "ORDER BY leave_date";
                        try (PreparedStatement leaveStmt = conn.prepareStatement(leaveQuery)) {
                            leaveStmt.setInt(1, staffId);
                            leaveStmt.setInt(2, year);

                            try (ResultSet leaveRs = leaveStmt.executeQuery()) {
                                report.append(String.format("Yearly Leave Report for %s %s (Staff ID: %d)\n",
                                        firstName, lastName, staffId));
                                report.append(String.format("Year: %d\n\n", year));

                                double totalApproved = 0;
                                double totalPending = 0;
                                double totalRejected = 0;

                                while (leaveRs.next()) {
                                    report.append(String.format("Leave ID: %d\n", leaveRs.getInt("leaveRecordID")));
                                    report.append(String.format("Date: %s\n", leaveRs.getDate("leave_date")));
                                    report.append(String.format("Duration: %.1f days\n", leaveRs.getDouble("duration_of_leave")));
                                    report.append(String.format("Reason: %s\n", leaveRs.getString("reason_of_leave")));
                                    report.append(String.format("Status: %s\n\n", leaveRs.getString("status")));

                                    double duration = leaveRs.getDouble("duration_of_leave");
                                    String status = leaveRs.getString("status");
                                    switch (status) {
                                        case "APPROVED":
                                            totalApproved += duration;
                                            break;
                                        case "PENDING":
                                            totalPending += duration;
                                            break;
                                        case "REJECTED":
                                            totalRejected += duration;
                                            break;
                                    }
                                }

                                report.append("Summary:\n");
                                report.append(String.format("Total Approved Leave: %.1f days\n", totalApproved));
                                report.append(String.format("Pending Leave: %.1f days\n", totalPending));
                                report.append(String.format("Rejected Leave: %.1f days\n", totalRejected));
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return "Error generating report: " + e.getMessage();
            }

            return report.toString();
        });

        try {
            return future.get(); // Retrieve result from asynchronous execution
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating report: " + e.getMessage();
        }
    }

//    public boolean removeEmployee(int staffId) throws RemoteException {
//        return executorService.submit(() -> {
//            try {
//                conn.setAutoCommit(false); // Start transaction
//                
//                // First check if employee exists
//                String checkQuery = "SELECT staffid FROM Employee WHERE staffid = ?";
//                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
//                checkStmt.setInt(1, staffId);
//                ResultSet rs = checkStmt.executeQuery();
//                
//                if (!rs.next()) {
//                    conn.rollback();
//                    return false; // Employee not found
//                }
//                
//                // Thanks to ON DELETE CASCADE in our schema, we don't need to explicitly
//                // delete from Family and LeaveRecord tables
//                
//                String deleteQuery = "DELETE FROM Employee WHERE staffid = ?";
//                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
//                deleteStmt.setInt(1, staffId);
//                
//                int rowsDeleted = deleteStmt.executeUpdate();
//                
//                if (rowsDeleted > 0) {
//                    conn.commit();
//                    return true;
//                } else {
//                    conn.rollback();
//                    return false;
//                }
//                
//            } catch (SQLException e) {
//                try {
//                    conn.rollback();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//                e.printStackTrace();
//                return false;
//            } finally {
//                try {
//                    conn.setAutoCommit(true);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).get();
//    }
    @Override
    public boolean removeEmployee(int staffId) throws RemoteException {
        Future<Boolean> future = executorService.submit(() -> {
            try {
                conn.setAutoCommit(false); // Start transaction

                // Check if the employee exists
                String checkQuery = "SELECT staffid FROM Employee WHERE staffid = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setInt(1, staffId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false; // Employee not found
                        }
                    }
                }

                // Delete employee (ON DELETE CASCADE handles dependent tables)
                String deleteQuery = "DELETE FROM Employee WHERE staffid = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, staffId);
                    int rowsDeleted = deleteStmt.executeUpdate();

                    if (rowsDeleted > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
                return false;
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            return future.get(); // Retrieve result from asynchronous execution
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //@Override
    public void finalize() {
        executorService.shutdown();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getStaffIdsWithPendingLeaves() throws RemoteException {
        List<Integer> staffIds = new ArrayList<>();
        String query = "SELECT DISTINCT staffid FROM LeaveRecord WHERE status = 'PENDING'";

        try (PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffIds.add(rs.getInt("staffid"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffIds;
    }
}
