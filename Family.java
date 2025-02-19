/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcomsassignment;

import java.io.Serializable;

//hi
public class Family implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int famID;
    private String guardianName;
    private String guardianContact;
    private String relationship;
    private int staffid;
    
    // Default constructor
    public Family() {}
    
    // Full constructor
    public Family(int famID, String guardianName, String guardianContact, 
                 String relationship, int staffid) {
        this.famID = famID;
        this.guardianName = guardianName;
        this.guardianContact = guardianContact;
        this.relationship = relationship;
        this.staffid = staffid;
    }
    
    // Getters and setters
    public int getFamID() {
        return famID;
    }
    
    public void setFamID(int famID) {
        this.famID = famID;
    }
    
    public String getGuardianName() {
        return guardianName;
    }
    
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
    
    public String getGuardianContact() {
        return guardianContact;
    }
    
    public void setGuardianContact(String guardianContact) {
        this.guardianContact = guardianContact;
    }
    
    public String getRelationship() {
        return relationship;
    }
    
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
    
    public int getStaffid() {
        return staffid;
    }
    
    public void setStaffid(int staffid) {
        this.staffid = staffid;
    }
}