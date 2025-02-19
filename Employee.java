/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcomsassignment;

/**
 *
 * @author james
 */
import java.io.Serializable;
import java.sql.Date;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int staffid;
    private String firstName;
    private String lastName;
    private String icNumber;
    private double leaveAvailable;
    private String phoneNumber;
    
    // Default constructor
    public Employee() {}
    
    // Full constructor
    public Employee(int staffid, String firstName, String lastName, String icNumber, 
                   double leaveAvailable, String phoneNumber) {
        this.staffid = staffid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.icNumber = icNumber;
        this.leaveAvailable = leaveAvailable;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and setters
    public int getStaffid() {
        return staffid;
    }
    
    public void setStaffid(int staffid) {
        this.staffid = staffid;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getIcNumber() {
        return icNumber;
    }
    
    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }
    
    public double getLeaveAvailable() {
        return leaveAvailable;
    }
    
    public void setLeaveAvailable(double leaveAvailable) {
        this.leaveAvailable = leaveAvailable;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}