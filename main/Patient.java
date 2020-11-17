/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Dan
 */
public class Patient {
    //==============VARIABLES=================
    private int patientID;
    private int acuity;
    private int age;
    private int sex; //0-female, 1-male
    private ArrayList complaints;
    private ResourceList requiredResList = new ResourceList();
    private OperationList requiredOpList = new OperationList();
    private String name;
    private int intComplaint;  //TEMP FIX FOR THE ARRAYLIST ISSUE
    
    private boolean available;

    private int nextAvailableTime;
    private int nextAvailableHour;
    private int nextAvailableMinute;
    private int nextAvailableDate;
    private long nextAvailableEpochTime;

    private long makespan = 10000;

    private int arrivalDate;
    private int arrivalHour;
    private int arrivalMinute;

    private int assignDate;
    private int assignHour;
    private int assignMinute;

    private int arrivalAssignMinuteDifference;


//    public Patient(int patID, int acuity, int age, int sex, ArrayList complaints, ResourceList resourceList, OperationList operationList) {
//        this.patientID = patID;
//        this.acuity = acuity;
//        this.age = age;
//        this.sex = sex;
//        this.complaints = complaints;
//        this.requiredOpList = operationList;
//        this.requiredResList = resourceList;
//    }
    
//    public Patient(String name, int patID, int acuity, int age, int sex, ArrayList complaints) {
//        this.name = name;
//        this.patientID = patID;
//        this.acuity = acuity;
//        this.age = age;
//        this.sex = sex;
//        this.complaints = complaints;
//    }
    
    //TEMPORARY FIX FOR THE COMPLAINT ARRAYLIST ISSUE
    public Patient(String name, int patID, int acuity, int age, int sex, int complaint,
            int arrivalDate, int arrivalHour, int arrivalMinute,
            int assignDate, int assignHour, int assignMinute, int arrivalAssignMinuteDifference,
            int dischargeDate, int dischargeHour, int dischargeMinute) {
        this.name = name;
        this.patientID = patID;
        this.acuity = acuity;
        this.age = age;
        this.sex = sex;
        this.intComplaint = complaint;

        this.arrivalDate = arrivalDate;
        this.arrivalHour = arrivalHour;
        this.arrivalMinute = arrivalMinute;

        this.assignDate = assignDate;
        this.assignHour = assignHour;
        this.assignMinute = assignMinute;

        this.arrivalAssignMinuteDifference = arrivalAssignMinuteDifference;

//        Calendar arrivalCalendar = new GregorianCalendar();


    }
    
    //====================METHODS=========================
    

    
    //================GETTERS/SETTERS====================

    public int getAcuity() {
        return acuity;
    }

    public int getID() {
        return patientID;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getNextAvailableTime() {
        return nextAvailableTime;
    }

    public void setNextAvailableTime(int nextAvailableTime) {
        this.nextAvailableTime = nextAvailableTime;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public OperationList getRequiredOpList() {
        return requiredOpList;
    }

    public void setRequiredOpList(OperationList requiredOpList) {
        this.requiredOpList = requiredOpList;
    }

    public ResourceList getRequiredResList() {
        return requiredResList;
    }

    public void setRequiredResList(ResourceList requiredResList) {
        this.requiredResList = requiredResList;
    }

    public void setAcuity(int acuity) {
        this.acuity = acuity;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList getComplaints() {
        return complaints;
    }

    public void setComplaints(ArrayList complaints) {
        this.complaints = complaints;
    }

    public OperationList getOperationList() {
        return requiredOpList;
    }

    public void setOperationList(OperationList operationList) {
        this.requiredOpList = operationList;
    }

    public ResourceList getResourceList() {
        return requiredResList;
    }

    public void setResourceList(ResourceList resourceList) {
        this.requiredResList = resourceList;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextAvailableHour() {
        return nextAvailableHour;
    }

    public void setNextAvailableHour(int nextAvailableHour) {
        this.nextAvailableHour = nextAvailableHour;
    }

    public int getNextAvailableMinute() {
        return nextAvailableMinute;
    }

    public void setNextAvailableMinute(int nextAvailableMinute) {
        this.nextAvailableMinute = nextAvailableMinute;
    }

    public int getNextAvailableDate() {
        return nextAvailableDate;
    }

    public void setNextAvailableDate(int nextAvailableDate) {
        this.nextAvailableDate = nextAvailableDate;
    }

    public long getMakespan() {
        return makespan;
    }

    public void setMakespan(long makespan) {
       
            this.makespan = makespan;
        
    }

    public int getArrivalAssignMinuteDifference() {
        return arrivalAssignMinuteDifference;
    }

    public void setArrivalAssignMinuteDifference(int arrivalAssignMinuteDifference) {
        this.arrivalAssignMinuteDifference = arrivalAssignMinuteDifference;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(int arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public int getArrivalMinute() {
        return arrivalMinute;
    }

    public void setArrivalMinute(int arrivalMinute) {
        this.arrivalMinute = arrivalMinute;
    }

    public int getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(int assignDate) {
        this.assignDate = assignDate;
    }

    public int getAssignHour() {
        return assignHour;
    }

    public void setAssignHour(int assignHour) {
        this.assignHour = assignHour;
    }

    public int getAssignMinute() {
        return assignMinute;
    }

    public void setAssignMinute(int assignMinute) {
        this.assignMinute = assignMinute;
    }

    public long getNextAvailableEpochTime() {
        return nextAvailableEpochTime;
    }

    public void setNextAvailableEpochTime(long nextAvailableEpochTime) {
        this.nextAvailableEpochTime = nextAvailableEpochTime;
    }



    
    

    
    
    
            

}
