/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Dan Zimlich 2010
 */

public class Schedule {

//====================FIELDS====================================================

private OperationList operationList = new OperationList();

private ResourceList resourceList = new ResourceList();

private PatientList patientList = new PatientList();

private NurseList nurseList = new NurseList();

private int scheduleStartDate = 0;
private int scheduleStartHour = 0; //gives real-time Calendar reference hour
private int scheduleStartMin = 0; //gives real-time Calendar reference minute
private long scheduleStartEpochTime ;

private int currentHour;
private int currentMinute;
private int currentDate;
private long currentEpochTime;

public long rawObjectiveValue = 0;

//===================CONSTRUCTORS===============================================


public Schedule (OperationList oL, PatientList pL, ResourceList rL, NurseList nL, int scheduleStartHour, int scheduleStartMin, long scheduleStartEpochTime, int scheduleStartDate, int currentHour, int currentMinute, long currentEpochTime, int currentDate){

    this.scheduleStartEpochTime = scheduleStartEpochTime;
    
    this.nurseList = nL;

    for(int o = 0; o < oL.size(); o++){

        Operation operation = new Operation(oL.get(o));
        operationList.add(operation);

    }
    for(int p = 0; p < pL.size(); p++){
        Patient patient = pL.get(p);
        patientList.add(patient);
    }
    for(int r = 0; r < rL.size(); r++){
        Resource resource = rL.get(r);
        resourceList.add(resource);
    }

    this.scheduleStartDate = scheduleStartDate;
    this.scheduleStartHour = scheduleStartHour;
    this.scheduleStartMin = scheduleStartMin;
    
    this.currentHour = currentHour;
    this.currentMinute = currentMinute;
    this.currentEpochTime = currentEpochTime;
    this.currentDate = currentDate;
    
//    getObjectiveValue();
}

//===================METHODS====================================================

//public long getObjectiveValue(){
//
//    OperationList nonExpiredOpList = operationList.getUnscheduledOperationList();
//    for(Object operationObj : operationList){
//        Operation operation = (Operation) operationObj;
//        if(operation.getEndEpochTime() > currentEpochTime){
//            nonExpiredOpList.add(operation);
//        }
//    }
//
//    long cal1Millis = 0;
//    long cal2Millis = 0;
//    rawObjectiveValue = 0;
//        for (Object patientObj : patientList){
//            Patient patient = (Patient) patientObj;
//
//            int minStartHour = 25;
//            int minStartMinute = 61;
//            int minStartDate = 32;
//
//            int maxEndHour = -1;
//            int maxEndMinute = -1;
//            int maxEndDate = -1;
//
//
//
//            for(Object operationObj : operationList){ //get start and end times for patient (patient's makespan time bounds)
//                Operation operation = (Operation) operationObj;
//
//                if(operation.getPatientID() == patient.getID()){
//
//                    if(operation.getStartDate() < minStartDate){
//                        minStartDate = operation.getStartDate();
////                        System.out.println("Schedule.getObjValue() minStartDate: "+minStartDate);
//                    }
//                    if(operation.getStartDate() > maxEndDate){
//                        maxEndDate = operation.getEndDate();
////                        System.out.println("Schedule.getObjValue() maxEndDate: "+maxEndDate);
//                    }
//
//                    if((operation.getStartDate() == minStartDate) && (operation.getStartHour() <= minStartHour)){
//                        minStartHour = operation.getStartHour();
//                        if (operation.getStartMinute() < minStartMinute){
//                            minStartMinute = operation.getStartMinute();
//                        }
//                    }
//
//                    if((operation.getEndDate() == maxEndDate) && (operation.getEndHour() >= maxEndHour)){
//                        maxEndHour = operation.getEndHour();
//                        if (operation.getEndMinute() > maxEndMinute){
//                            maxEndMinute = operation.getEndMinute();
//                        }
//                    }
//                }
//            }
//            System.out.println("Schedule.getObjValue() patient: "+patient.getID()+" minStart: "+minStartHour+":"+minStartMinute);
//            System.out.println("Schedule.getObjValue() patient: "+patient.getID()+" maxEnd: "+maxEndHour+":"+maxEndMinute);
//            Calendar cal1 = new GregorianCalendar();
//            cal1.set(Calendar.DATE, minStartDate);
//            cal1.set(Calendar.HOUR_OF_DAY, minStartHour);
//            cal1.set(Calendar.MINUTE, minStartMinute);
//            cal1Millis = cal1.getTimeInMillis();
////            System.out.println("Patient "+patient.getID()+" minStartDate: "+minStartDate+" minStartHour: "+minStartHour+"   minStartMinute: "+minStartMinute);
//
//            Calendar cal2 = new GregorianCalendar();
//            cal2.set(Calendar.DATE, maxEndDate);
//            cal2.set(Calendar.HOUR_OF_DAY, maxEndHour);
//            cal2.set(Calendar.MINUTE, maxEndMinute);
//            cal2Millis = cal2.getTimeInMillis();
////            System.out.println("Patient "+patient.getID()+" minEndDate:   "+maxEndDate+" minEndHour:   "+maxEndHour+"   minEndMinute:   "+maxEndMinute);
//
//            long calculatedMakespan = (cal2Millis - cal1Millis)/60000;
//
//            if(maxEndMinute != -1){
//                patient.setMakespan(calculatedMakespan);
//                rawObjectiveValue = rawObjectiveValue+calculatedMakespan;
//
//                /**
//                 * Weights patient by acuity and generates an objective Value for this.
//                 */
//                long weightMultiplier = 1;
//                switch(patient.getAcuity()){
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//                    case 3:
//                        break;
//                    case 4:
//                        break;
//                    case 5:
//                        break;
//                    default:
//
//                }
//            }
//        }
//
//        return rawObjectiveValue;
//    }

/**
     * Returns a LinkedList partitioning operations into blocks.  If jobs j and k
     * are processed on the same machine, they are in the same block.
     * @return LinkedList
     */
    public BlockList getBlocks(){
        BlockList blockList = new BlockList();
        
        for (int r = 0; r < resourceList.size(); r++){
            Block block = new Block(resourceList.get(r).getID()); //creates a new block with an ID equal to the current resourceID
            blockList.add(block);
        }
        
        for (int o = 0; o < operationList.size(); o++){
            Block block = blockList.getByID(operationList.get(o).getResourceID()); //get the block with the right resourceID
            block.add(operationList.get(o)); //add current operation to block
        }
        
        //verify proper functioning of method with println
        for (int b = 0; b < blockList.size(); b++){
            Block block = blockList.get(b);
//            System.out.print("Block " + block.getID() + ": ");
            for (int bo = 0; bo < block.size(); bo++){
                System.out.print(block.get(bo).getID()+ ", ");
            }
//            System.out.print("\n\r");
        }
        
        return blockList;
    }
    
    /**
     * Returns a LinkedList partitioning operations into blocks.  If jobs j and k
     * are processed on the same machine, they are in the same block.
     * @return LinkedList
     */
    public BlockList getGroups(){
        BlockList groupList = new BlockList();
        groupList.add(new Block(0));        
        for (int o = 0; o < operationList.size(); o++){
            Block group = groupList.get(0); //get the group
            group.add(operationList.get(o)); //add current operation to group
        }
        
        //verify proper functioning of method with println
        for (int g = 0; g < groupList.size(); g++){
            Block group = groupList.get(g);
            System.out.print("Group " + group.getID() + ": ");
            for (int bo = 0; bo < group.size(); bo++){
                System.out.print(group.get(bo).getID()+ ", ");
            }
            System.out.print("\n\r");
        }
        
        return groupList;
    }
    
    /**
     * //Order of patients per resource in current schedule
     * //Order of resources per patient in current schedule
     */
    public void generatePermutationLists(){
//        System.out.println("generating permutation lists for "+operationList.size()+ " operations on "+resourceList.size()+" resources and "+patientList.size()+ " patients.");
        
        //Sort resource requiredOpLists by startTime
        for (int r = 0; r < resourceList.size(); r++){
            resourceList.get(r).setRequiredOpList(resourceList.get(r).getRequiredOpList().sortByStartTime());
        }
        
        //Sort patient requiredOpLists by startTime
        for (int p = 0; p < patientList.size(); p++){
            patientList.get(p).setRequiredOpList(patientList.get(p).getRequiredOpList().sortByStartTime());
//            System.out.println("sorted "+patientList.get(p).getRequiredOpList().size() + " operations for patient "+patientList.get(p).getID());
        }   
        
        //Test to make sure it worked...
        
        for (int r = 0; r < resourceList.size(); r++){
//            System.out.print("Resource"+resourceList.get(r).getID()+" processes operations by order of time: ");
            for (int o = 0; o < resourceList.get(r).getRequiredOpList().size(); o++){
                System.out.print(resourceList.get(r).getRequiredOpList().get(o).getID()+", ");
            }
//            System.out.print("\n\r");
        }
        for (int p = 0; p < patientList.size(); p++){
//            System.out.print("Patient"+patientList.get(p).getID()+" is processed in operations by order of time: ");
            for (int o = 0; o < patientList.get(p).getRequiredOpList().size(); o++){
                System.out.print(patientList.get(p).getRequiredOpList().get(o).getID()+", ");
            }
//            System.out.print("\n\r");
        }  
    }
    
    /**
     * calculateValues() calculates the values for operation o essential to defining and manipulating
     * neighborhoods for the local search method.  These values are derived from:
     * "A Fast Tabu Search Algorithm for the Group Shop Scheduling Problem" 
     * by S.Q. Liu et al. appearing in Advances in Engineering Software 36 (2005)
     */
    public int[] calculateValues(int o){
        
        Operation operation = operationList.getByID(o);
        int patientID = operation.getPatientID();
        int resourceID = operation.getResourceID();
        
        int e_o; // length of the longest path from node U to node o excluding d_o
        int d_o; // processing time of operation o
        int l_o; // length of the longest path from node o to node V excluding d_o
        int R_o; // resource that processes operation o
        int P_o; // patient pertaining to operation o
        int PR_o; // operation processed by resource R_o just BEFORE operation o if it exists
        int SR_o; // operation processed by resource R_o just AFTER operation o if it exists
        int PP_o; // operation belonging to patient P_o that just precedes operation o if it exists
        int SP_o; // operation belonging to patient P_o that just follows operation o if it exists
        int idx;  // index used to get preceding/succeding operations for PR, SR, PP, SP variables
        
        //        e_o;
        e_o = -1;
        
        //        d_o;
        d_o = operation.getProcessingTime();
        
        //        l_o;
        l_o = -1;
        
        //        R_o;
        R_o = resourceID;
        
        //        P_o;
        P_o = patientID;
        
        //        PR_o;
        idx = resourceList.getByID(resourceID).getRequiredOpList().indexOf(operation);
        if (idx-1 == -1){
            PR_o = -1;
        }
        else{
            PR_o = resourceList.getByID(resourceID).getRequiredOpList().get(idx-1).getID();
        }
        
        //        SR_o;
        idx = resourceList.getByID(resourceID).getRequiredOpList().indexOf(operation);
        if (idx+2 > resourceList.getByID(resourceID).getRequiredOpList().size()){
            SR_o = -1;
        }
        else{
            SR_o = resourceList.getByID(resourceID).getRequiredOpList().get(idx+1).getID();
        }
        
        //        PP_o;
        idx = patientList.getByID(patientID).getRequiredOpList().indexOf(operation);
        if (idx-1 == -1){
            PP_o = -1;
        }
        else{
            PP_o = patientList.getByID(patientID).getRequiredOpList().get(idx-1).getID();
        }
        
        //        SP_o;
        idx = patientList.getByID(patientID).getRequiredOpList().indexOf(operation);
        if (idx+2 > patientList.getByID(patientID).getRequiredOpList().size()){
            SP_o = -1;
        }
        else{
            SP_o = patientList.getByID(patientID).getRequiredOpList().get(idx+1).getID();
        }
        
        int [] valueArray = {e_o, d_o, l_o, R_o, P_o, PR_o, SR_o, PP_o, SP_o};
        System.out.println("Values for operation "+operation.getID()+ " {"+
                "e_o: " + e_o +
                ", d_o: " + d_o + 
                ", l_o: " + l_o + 
                ", R_o: " + R_o + 
                ", P_o: " + P_o + 
                ", PR_o: " + PR_o + 
                ", SR_o: " + SR_o + 
                ", PP_o: " + PP_o + 
                ", SP_o: " + SP_o +"}");
        return valueArray;        
    }

    //<editor-fold desc="getters and setters">
    public OperationList getOperationList() {
        return operationList;
    }

    public void setOperationList(OperationList operationList) {
        this.operationList = operationList;
    }

    public PatientList getPatientList() {
        return patientList;
    }

    public void setPatientList(PatientList patientList) {
        this.patientList = patientList;
    }

    public ResourceList getResourceList() {
        return resourceList;
    }

    public void setResourceList(ResourceList resourceList) {
        this.resourceList = resourceList;
    }

    public NurseList getNurseList() {
        return nurseList;
    }

    public void setNurseList(NurseList nurseList) {
        this.nurseList = nurseList;
    }

    

    public int getScheduleStartHour() {
        return scheduleStartHour;
    }

    public void setScheduleStartHour(int scheduleStartHour) {
        this.scheduleStartHour = scheduleStartHour;
    }

    public int getScheduleStartMin() {
        return scheduleStartMin;
    }

    public void setScheduleStartMin(int scheduleStartMin) {
        this.scheduleStartMin = scheduleStartMin;
    }

    public int getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int currentDate) {
        this.currentDate = currentDate;
    }

    public long getCurrentEpochTime() {
        return currentEpochTime;
    }

    public void setCurrentEpochTime(long currentEpochTime) {
        this.currentEpochTime = currentEpochTime;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public void setCurrentMinute(int currentMinute) {
        this.currentMinute = currentMinute;
    }

    public long getRawObjectiveValue() {
        return rawObjectiveValue;
    }

    public void setRawObjectiveValue(long rawObjectiveValue) {
        this.rawObjectiveValue = rawObjectiveValue;
    }

    public long getScheduleStartEpochTime() {
        return scheduleStartEpochTime;
    }

    public void setScheduleStartEpochTime(long scheduleStartEpochTime) {
        this.scheduleStartEpochTime = scheduleStartEpochTime;
    }
    //</editor-fold>
    
    

}
