
package dcomsassignment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

public interface HRMService extends Remote{
    // Authentication methods
    //done
    boolean isValidStaffId(int staffId) throws RemoteException;
    
    // Employee profile management
    //done
    boolean RegisterEmployee(String firstName, String lastName, String icNumber, double leaveAvailable, String phoneNumber) throws RemoteException;
    //done
    boolean updateEmployeeProfile(int staffId, String firstName, String lastName, String phoneNumber, int icNumber) throws RemoteException;
    //done
    String displayEmployeeProfile(int staffId) throws RemoteException;
    boolean removeEmployee(int staffId) throws RemoteException;
    
    // Family details management
    //done
    boolean addOrUpdateFamilyDetails(int staffId, String guardianName, String relationship, String contact) throws RemoteException;
    
    // Leave management
    //done
    boolean applyLeave(int staffId, String reasonOfLeave,double durationOfLeave, Date leaveDate) throws RemoteException;
    boolean approveLeave(int leaveRecordId, String status) throws RemoteException;
    //done
    void displayLeaveStatus(int staffId) throws RemoteException;
    //done
    double getLeaveBalance(int staffId) throws RemoteException;
    
    // Reporting
    String generateYearlyReport(int staffId, int year) throws RemoteException;
}
