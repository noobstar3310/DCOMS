/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcomsassignment;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author james
 */
public class LeaveRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int leaveRecordID;
    private String reasonOfLeave;
    private double durationOfLeave;
    private String status;
    private Date leaveDate;
    private int staffid;
    
    // Default constructor
    public LeaveRecord() {}
    
    // Full constructor
    public LeaveRecord(int leaveRecordID, String reasonOfLeave, double durationOfLeave, 
                      String status, Date leaveDate, int staffid) {
        this.leaveRecordID = leaveRecordID;
        this.reasonOfLeave = reasonOfLeave;
        this.durationOfLeave = durationOfLeave;
        this.status = status;
        this.leaveDate = leaveDate;
        this.staffid = staffid;
    }
    
    // Getters and setters
    public int getLeaveRecordID() {
        return leaveRecordID;
    }
    
    public void setLeaveRecordID(int leaveRecordID) {
        this.leaveRecordID = leaveRecordID;
    }
    
    public String getReasonOfLeave() {
        return reasonOfLeave;
    }
    
    public void setReasonOfLeave(String reasonOfLeave) {
        this.reasonOfLeave = reasonOfLeave;
    }
    
    public double getDurationOfLeave() {
        return durationOfLeave;
    }
    
    public void setDurationOfLeave(double durationOfLeave) {
        this.durationOfLeave = durationOfLeave;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getLeaveDate() {
        return leaveDate;
    }
    
    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }
    
    public int getStaffid() {
        return staffid;
    }
    
    public void setStaffid(int staffid) {
        this.staffid = staffid;
    }
}