/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author Dan Zimlich 2010
 */

public class Nurse {

//====================FIELDS====================================================
    private int nurseID;
    private String name;
    private int section;

    private boolean available;

    private int nextAvailableTime;
    private int nextAvailableHour;
    private int nextAvailableMinute;
    private int nextAvailableDate;
    private long nextAvailableEpochTime;


//===================CONSTRUCTORS===============================================


public Nurse(int nurseID, String name, int section) {
        this.name = name;
        this.nurseID = nurseID;
        this.section = section;
        
        available = true;

    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextAvailableDate() {
        return nextAvailableDate;
    }

    public void setNextAvailableDate(int nextAvailableDate) {
        this.nextAvailableDate = nextAvailableDate;
    }

    public long getNextAvailableEpochTime() {
        return nextAvailableEpochTime;
    }

    public void setNextAvailableEpochTime(long nextAvailableEpochTime) {
        this.nextAvailableEpochTime = nextAvailableEpochTime;
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

    public int getNextAvailableTime() {
        return nextAvailableTime;
    }

    public void setNextAvailableTime(int nextAvailableTime) {
        this.nextAvailableTime = nextAvailableTime;
    }

    public int getNurseID() {
        return nurseID;
    }

    public void setNurseID(int nurseID) {
        this.nurseID = nurseID;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

//===================METHODS====================================================





}
