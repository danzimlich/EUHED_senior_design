/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author Dan
 */
public class Resource {
    private int id;
//    private int start = 0;
//    private int end = 0;
    private String name;
    private boolean available;
    private int nextAvailableTime;

    private int nextAvailableHour;
    private int nextAvailableMinute;
    private int nextAvailableDate;
    private long nextAvailableEpochTime;

    private OperationList requiredOpList;
    
//    public Resource(String name, int id, int start, int end){
//        this.name = name;
//        this.id = id;
//        this.start = start;
//        this.end = end;
//    }
    
    public Resource(String name, int id, int capacity, boolean available){
        this.name = name;
        this.id = id;
        this.available = available;
    }
    
    public boolean isAvailable(){
        return available;
    }
    public void setAvailable(boolean boo){
        this.available=boo;
    }
    
    public OperationList getRequiredOpList() {
        return requiredOpList;
    }

    public void setRequiredOpList(OperationList requiredOpList) {
        this.requiredOpList = requiredOpList;
    }

//    public int getEnd() {
//        return end;
//    }
//
//    public void setEnd(int end) {
//        this.end = end;
//    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getStart() {
//        return start;
//    }
//
//    public void setStart(int start) {
//        this.start = start;
//    }

    public int getNextAvailableTime() {
        return nextAvailableTime;
    }

    public void setNextAvailableTime(int nextAvailableTime) {
        this.nextAvailableTime = nextAvailableTime;
    }

    public int getNextAvailableMinute() {
        return nextAvailableMinute;
    }

    public void setNextAvailableMinute(int nextAvailableMinute) {
        this.nextAvailableMinute = nextAvailableMinute;
    }

    public int getNextAvailableHour() {
        return nextAvailableHour;
    }

    public void setNextAvailableHour(int nextAvailableHour) {
        this.nextAvailableHour = nextAvailableHour;
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


    
    
}
