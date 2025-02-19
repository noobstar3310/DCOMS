
package dcomsassignment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

public interface HRMService extends Remote {
    // Authentication methods
    boolean isValidStaffId(int staffId) throws RemoteException;
    
    // Employee profile management
    boolean RegisterEmployee(Employee employee) throws RemoteException;
    boolean updateEmployeeProfile(Employee employee) throws RemoteException;
    Employee getEmployeeProfile(int staffId) throws RemoteException;  // Changed from String to Employee
    boolean removeEmployee(int staffId) throws RemoteException;
    
    // Family details management
    boolean addOrUpdateFamilyDetails(Family family, int staffID) throws RemoteException;
    
    // Leave management
    boolean applyLeave(LeaveRecord leaveRecord, int staffID) throws RemoteException;
    boolean approveLeave(int leaveRecordId, String status) throws RemoteException;
    List<LeaveRecord> getLeaveStatus(int staffId) throws RemoteException;  // Changed from void to List<LeaveRecord>
    double getLeaveBalance(int staffId) throws RemoteException;
    
    // Reporting
    String generateYearlyReport(int staffId, int year) throws RemoteException;
}
