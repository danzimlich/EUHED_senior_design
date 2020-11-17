
package main;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Dan Zimlich 2010
 */
public class Operation {
    
    private int     operationID;
    private int     startTime = 0; // =
    private long    startEpochTime = -1; //the epoch time equivalent of startHour:startMinute for the date
    private int     startHour;
    private int     startMinute;
    private int     startDate;
    private int     startMonth;
    private int     startYear;
    
    private int     endTime;  // =
    private int     endHour;
    private int     endMinute;
    private long    endEpochTime;
    private int     endDate;
    
    private int         patientID;
    private int         resourceID;
    private Calendar    possibleStartTimes[];
    private Calendar    possibleEndTimes[];
    private int         processingTime;
    private boolean     scheduled = false;
    
    private int         weight = 1; //associated weight with operation.  can vary by type of resource/patient acuity.  Calculated in parameterProcessor.
   
    public static int   instances = 0;

    /**
     * Creates a copy of operation
     * @param op
     */
  public Operation(Operation op){
        this.operationID =      op.getOperationID();
        this.resourceID =       op.getResourceID();
        this.patientID =        op.getPatientID();
        this.processingTime =   op.getProcessingTime();
        this.scheduled =        op.isScheduled();

        this.startTime =        op.getStartTime();  //from t = 0 = scheduleStart
        this.startEpochTime =   op.getStartEpochTime();
        this.startHour =        op.getStartHour();
        this.startMinute =      op.getStartMinute();
        this.startYear =        op.getStartYear();
        this.startMonth =       op.getStartMonth();
        this.startDate =        op.getStartDate();

        this.endTime =          op.getEndTime();
        this.endHour =          op.getEndHour();
        this.endMinute =        op.getEndMinute();
        this.endEpochTime =     op.getEndEpochTime();

        this.endDate =        op.getEndDate();

        this.weight =           op.getWeight();

        instances++;

  }
    
//    
//    public Operation(int opID, int resID, int patID, int start, int end){
//        this.operationID = opID;
//        this.resourceID = resID;
//        this.patientID = patID;
//        this.startTime = start;
//        this.endTime = end;
//        
//    }
//    
    public Operation(int opID, int resID, int patID, int procTime, boolean scheduled){
        this.operationID = opID;
        this.resourceID = resID;
        this.patientID = patID;
        this.processingTime = procTime;
        this.scheduled = scheduled;
        instances++;
//        System.out.println("Operation class instances: "+instances);
    }

    public boolean isScheduled(){
        return this.scheduled;
    }
    public void setScheduled(boolean bool){
        this.scheduled = bool;
    }
    
    /**
     * 
     * @return
     */
    public int getEndTime() {
        return endTime;
    }

    /**
     * 
     * @param end
     */
//    public void setEndTime(int end) {
//        this.endTime = end;
//    }

    /**
     * 
     * @return
     */
    public int getID() {
        return operationID;
    }

    /**
     * 
     * @param id
     */
    public void setId(int id) {
        this.operationID = id;
    }

    /**
     * 
     * @return
     */
    public int getStartTime() {
        return startTime;
    }



    public void setStartCalendar(int incStartHour, int incStartMinute, int incStartYear, int incStartMonth, int incStartDate){
        startHour = incStartHour;
        startMinute = incStartMinute;
        startDate = incStartDate;
        startMonth = incStartMonth;
        startYear = incStartYear;
        

        

        int tempEndMinute = incStartMinute + processingTime;
        endMinute = tempEndMinute;
        if(endMinute >= 240){
            endHour = startHour+4;
            endMinute = tempEndMinute-240;
        }else if(endMinute >= 180){
            endHour = startHour+3;
            endMinute = tempEndMinute-180;
        }else if(endMinute >= 120){
            endHour = startHour+2;
            endMinute = tempEndMinute-120;
        }else if(endMinute >= 60){
            endHour = startHour+1;
            endMinute = tempEndMinute - 60;
        }else{
            endHour = startHour;
        }

        endDate = startDate;
        
        if(startHour >= 24){
            startHour = 0;
            startDate = startDate+1;
        }
        if((startHour > endHour) || (endHour >= 24)){
            endHour = 0;
            this.endDate = endDate+1;
        }

        Calendar calendar = new GregorianCalendar(2010, startMonth, startDate, startHour, startMinute);
//        calendar.set(Calendar.HOUR_OF_DAY, incStartHour);
//        calendar.set(Calendar.MINUTE, incStartMinute);
//        calendar.set(Calendar.MONTH, incStartMonth);

        this.startEpochTime = calendar.getTimeInMillis();
        this.endEpochTime = startEpochTime + (processingTime*60*1000);

//        System.out.println("scheduled operation "+ operationID +" with ymd: "+startYear+"-"+startMonth+"-"+startDate+" "+startHour+":"+startMinute);
//        System.out.println("scheduled operation "+ operationID +" with ymd: "+startYear+"-"+startMonth+"-"+endDate+" "+endHour+":"+endMinute);

    }

    /**
     * 
     * @return
     */
    public int getOperationID() {
        return operationID;
    }

    /**
     * 
     * @param operationID
     */
    public void setOperationID(int operationID) {
        this.operationID = operationID;
    }

    /**
     * 
     * @return
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * 
     * @param patientID
     */
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /**
     * 
     * @return
     */
    public Calendar[] getPossibleEndTimes() {
        return possibleEndTimes;
    }

    /**
     * 
     * @param possibleEndTimes
     */
    public void setPossibleEndTimes(Calendar[] possibleEndTimes) {
        this.possibleEndTimes = possibleEndTimes;
    }

    /**
     * 
     * @return
     */
    public Calendar[] getPossibleStartTimes() {
        return possibleStartTimes;
    }

    /**
     * 
     * @param possibleStartTimes
     */
    public void setPossibleStartTimes(Calendar[] possibleStartTimes) {
        this.possibleStartTimes = possibleStartTimes;
    }

    /**
     * 
     * @return
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * 
     * @param resourceID
     */
    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }
    
    public int getInstancesCount(){
        return instances;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getStartEpochTime() {
        return startEpochTime;
    }

    public void setStartEpochTime(long startEpochTime) {
        this.startEpochTime = startEpochTime;
        long procTimeMillis = (long) (processingTime*60*1000);
        endEpochTime = startEpochTime+procTimeMillis;
    }

    public long getEndEpochTime() {
        return endEpochTime;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }


    

}
