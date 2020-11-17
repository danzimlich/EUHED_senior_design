/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * @author Dan Zimlich 2010
 */

public class Scheduler implements Runnable {


//<editor-fold defaultstate="collapsed" desc="====================FIELDS====================================================">
private ParameterProcessor parameterProcessor;
private OperationList operationList;
private ResourceList resourceList;
private PatientList patientList;
private NurseList nurseList;

private long currentEpochTime = 0;

private int testCaseDate=0;
private int testCaseHour=0;
private int testCaseMinute=0;

private int currentYear = 2010;
private int currentMonth = 0;
private int currentDate = 0;
private int currentHour = 0; //initialize real-time Calendar reference hour
private int currentMinute = 0; //initialize real-time Calendar reference minute

private int scheduleStartHour = 0;
private int scheduleStartMinute = 0;
private long scheduleStartEpochTime = 0;
private int scheduleStartDate = 0;

private OperationList unscheduledOpList;
private OperationList scheduledOpList;

private PatientList patientListRequiringCurrentResource = new PatientList();

private Schedule baseSchedule;
private Schedule bestSchedule;
private Neighborhood neighborhood = new Neighborhood();

private long weightedObjectiveValue = 0;

Random random = new Random();
Double rand = 0.1;
private int runs = 3000;

private boolean baseScheduleGenerated = false;  //this is used so the scheduler knows whether or not initial values have been generated
private int horizon = 0;  //parameter of time; operations scheduled to occur before the horizon will not be rescheduled upon a 'reschedule' request



/**
 * @FIX: ****FOR TESTING, THIS IS SET TO FALSE TO ENSURE THE SYSTEM STARTS SCHEDULING AT MIDNIGHT.
 * 
 * FOR REAL-TIME USE, THIS MUST BE SET TO *TRUE* OR REMOVED COMPLETELY.
 */
private int realDate;
private int realHour;
private int realMinute;
private long realEpochTime;
private boolean usingEstablishTimeReferenceMethod = true;
//Calendar testCalendar = new GregorianCalendar(2010, Calendar.APRIL, 14);

//</editor-fold>


//<editor-fold defaultstate="collapsed" desc="===================CONSTRUCTORS===============================================">
public Scheduler(ParameterProcessor parameterProcessor){
    this.parameterProcessor = parameterProcessor;
    this.operationList = parameterProcessor.getOperationList();
    this.resourceList = parameterProcessor.getResourceList();
    this.patientList = parameterProcessor.getPatientList();
    this.nurseList = parameterProcessor.getNurseList();

    /**
     * REAL CALENDAR REFERENCE STARTED WHEN SCHEDULER INSTANTSIATED
     * @TODO: REMOVE THIS FOR NON-TEST USE
     */
    Calendar realCalendar = new GregorianCalendar();
    realDate = realCalendar.get(Calendar.DATE);
    realHour = realCalendar.get(Calendar.HOUR_OF_DAY);
    realMinute = realCalendar.get(Calendar.MINUTE);
    realEpochTime = realCalendar.getTimeInMillis();


    System.out.println("Scheduler CONSTRUCTOR has list sizes:");
    System.out.println("operationList: "+operationList.size());
    System.out.println("patientList: "+patientList.size());
    System.out.println("resourceList: "+resourceList.size());
    
}
//</editor-fold>

//===================METHODS====================================================
//<editor-fold defaultstate="collapsed" desc="getters and setters">

    public OperationList getOperationList() {
        return operationList;
    }

    public void setOperationList(OperationList opList) {
        this.operationList = opList;
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

    

    public Schedule getBestSchedule(){
        /***
             * =========================================
             * Individual Schedule print testing 2
             * =========================================
             */


//            OperationList tempOL = bestSchedule.getOperationList();
//            OperationList.sortByID(tempOL);
//            for(Object opOb : tempOL){
//                Operation op = (Operation) opOb;
//                System.out.print(op.getID()+"|"+ op.getStartTime()+", ");
//            }
//            System.out.print("\n\r");


            /**
             * ==============END==========================
             */
        return bestSchedule;
    }

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public Schedule getBaseSchedule() {
        return baseSchedule;
    }

    public void setBaseSchedule(Schedule baseSchedule) {
        this.baseSchedule = baseSchedule;
    }

    public ParameterProcessor getParameterProcessor() {
        return parameterProcessor;
    }

    public void setParameterProcessor(ParameterProcessor parameterProcessor) {
        this.parameterProcessor = parameterProcessor;
    }

    public OperationList getScheduledOpList() {
        return scheduledOpList;
    }

    public void setScheduledOpList(OperationList scheduledOpList) {
        this.scheduledOpList = scheduledOpList;
    }

    public OperationList getUnscheduledOpList() {
        return unscheduledOpList;
    }

    public void setUnscheduledOpList(OperationList unscheduledOpList) {
        this.unscheduledOpList = unscheduledOpList;
    }

    public boolean isUsingEstablishTimeReferenceMethod() {
        return usingEstablishTimeReferenceMethod;
    }

    public void setUsingEstablishTimeReferenceMethod(boolean usingEstablishTimeReferenceMethod) {
        this.usingEstablishTimeReferenceMethod = usingEstablishTimeReferenceMethod;
    }

    public int getTestCaseDate() {
        return testCaseDate;
    }

    public void setTestCaseDate(int testCaseDate) {
        this.testCaseDate = testCaseDate;
    }

    public int getTestCaseHour() {
        return testCaseHour;
    }

    public void setTestCaseHour(int testCaseHour) {
        this.testCaseHour = testCaseHour;
    }

    public int getTestCaseMinute() {
        return testCaseMinute;
    }

    public void setTestCaseMinute(int testCaseMinute) {
        this.testCaseMinute = testCaseMinute;
    }

    public boolean isBaseScheduleGenerated() {
        return baseScheduleGenerated;
    }

    public void setBaseScheduleGenerated(boolean baseScheduleGenerated) {
        this.baseScheduleGenerated = baseScheduleGenerated;
    }

    


    //</editor-fold >
    //================SCHEDULING METHODS==============================
    
    /**
     * Runs the scheduling algorithms of the class
     */        
    public void run(){
        if(baseScheduleGenerated == false){
            baseSchedule = generateBaseSchedule();
            System.out.println("Generated a base schedule");
            bestSchedule = baseSchedule;
        }else{
//            System.out.println("base schedule is already generated");
        }

        getCurrentLists();
        
        Double rand = random.nextDouble();
       
        bestSchedule = generateBestSchedule();
    }


    @SuppressWarnings({"static-access", "empty-statement"})
    public void establishTimeReference(){
        Calendar calendar = new GregorianCalendar();
//        calendar.set(Calendar.YEAR, 2010);
        currentYear =   calendar.get(Calendar.YEAR);
        currentMonth =  calendar.get(Calendar.MONTH);
        currentDate =   calendar.get(Calendar.DATE);

        currentHour =   calendar.get(Calendar.HOUR_OF_DAY) - realHour; //gives real-time Calendar reference hour
        calendar.set(Calendar.HOUR_OF_DAY, currentHour);

        currentMinute = calendar.get(Calendar.MINUTE) - realMinute; //gives real-time Calendar reference minute
        calendar.set(Calendar.MINUTE, currentMinute);

        currentEpochTime = calendar.getTimeInMillis();
    }


    /**
     * Retrieves most current OPR lists from parameterProcessor
     */
    public void getCurrentLists(){
        operationList = parameterProcessor.getOperationList();
        resourceList = parameterProcessor.getResourceList();
        patientList = parameterProcessor.getPatientList();
        nurseList = parameterProcessor.getNurseList();
    }


    /**
     * Sets entity (Operations, Patients, Resources) availability flags based on relative scheduling time.
     * For example, if an operation was previously scheduled and its start time has elapsed past the current
     * system time (the real-world time at which the scheduling algorithm is called), the setEntityAvailability
     * will ensure that the operation will be placed in the "scheduled" operation list
     * and thus the operation will not be considered for start-time adjustment by the scheduling algorithm.  (Because
     * the operation should already be in progress and rescheduling is impractical)
     *
     * @param horizonParam
     */
    private void setEntityAvailability(int horizonParam){
        /**
         * operations already scheduled to occur before the "horizon" parameter will not be rescheduled.
         * Also: operations already started before the current time (currentHour, currentMinute) will not be rescheduled
         */
        
        //initialize booleans to make sure all operations/resources/patients are available/can be scheduled
        for (Object opObject : operationList){          //Really need to make these lists with generics...
            Operation operation = (Operation) opObject;
            operation.setScheduled(false);
            if(operation.getStartEpochTime() != -1){
                if( ((operation.getStartEpochTime()+60000)< currentEpochTime )  &&  ( baseScheduleGenerated == true  )){
//                System.out.println("operationEpochTime: "+operation.getStartEpochTime() +" currentEpochTime: "+currentEpochTime);
                    operation.setScheduled(true);
                }
//                else if((operation.getStartTime() < horizonParam) &&  ( baseScheduleGenerated == true  )){
//                    operation.setScheduled(true);
//                }
                else{
                    operation.setScheduled(false);
                }
            }
        }


        for (Object resObject : resourceList){
            Resource resource = (Resource) resObject;
            resource.setAvailable(true);
        }
        for (Object patObject : patientList){
            Patient patient = (Patient) patObject;
            patient.setAvailable(true);
        }
        for (Object nurseObject : nurseList){
            Nurse nurse = (Nurse) nurseObject;
            nurse.setAvailable(true);
        }

        
        if (unscheduledOpList != null){
            unscheduledOpList.clear();
            scheduledOpList.clear();
        }
        
        unscheduledOpList = operationList.getUnscheduledOperationList();
        scheduledOpList = operationList.getScheduledOperationList();

//        System.out.print("unscheduled operations: ");
//        for(int ix = 0; ix<unscheduledOpList.size(); ix++){
//            System.out.print(unscheduledOpList.get(ix).getID()+", ");
//        }
//        System.out.print("\n\r");
//
//        System.out.print("scheduled operations: ");
//        for(int ix = 0; ix<scheduledOpList.size(); ix++){
//            System.out.print(scheduledOpList.get(ix).getID()+", ");
//        }
//        System.out.print("\n\r");


        /**
         * @todo need to add functionality to ensure availabilities synch with scheduled ops before horizon
         */
        for (Object opObject : scheduledOpList){
            Operation operation = (Operation) opObject;
            if((    (operation.getEndDate() == currentDate) && (operation.getEndHour() == currentHour) && (currentMinute < operation.getEndMinute())   )
                    || ((operation.getEndDate() == currentDate) && (operation.getEndHour() > currentHour))
                    || (operation.getEndDate() > currentDate)){
                
                resourceList.getByID(operation.getResourceID()).setAvailable(false);
                resourceList.getByID(operation.getResourceID()).setNextAvailableHour(operation.getEndHour());
                resourceList.getByID(operation.getResourceID()).setNextAvailableMinute(operation.getEndMinute());
                resourceList.getByID(operation.getResourceID()).setNextAvailableDate(operation.getEndDate());

                patientList.getByID(operation.getPatientID()).setAvailable(false);
                patientList.getByID(operation.getPatientID()).setNextAvailableHour(operation.getEndHour());
                patientList.getByID(operation.getPatientID()).setNextAvailableMinute(operation.getEndMinute());
                patientList.getByID(operation.getPatientID()).setNextAvailableDate(operation.getEndDate());
            }
        }
        
        if (patientListRequiringCurrentResource  != null){
            patientListRequiringCurrentResource.clear();
        }

        parameterProcessor.setAllLists(operationList, patientList, resourceList, nurseList);
        
    }

    //===============================================================================================================================================================
    //||||||||||||||||||||||||||||||||||||||||||||||||||        BEGIN GENERATEBASESCHEDULE METHOD       |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    //===============================================================================================================================================================
    //    <editor-fold defaultstate="collapsed" desc="generateBaseSchedule()">
    /**
     *generateBaseSchedule() uses an LTRP-OM dispatching rule to generate a base schedule.
     *  It does this by iterating through time units (minute) and checking which
     * resources are available.  If resource i is availabe, j with the highest total
     * remaining processing time on OTHER machines is scheduled
     * 
     * Hence, "Longest Total Remaining Processing time on Other Machines" (LTRP-OM)
     */

    @SuppressWarnings("static-access")
    public Schedule generateBaseSchedule() {
        Schedule candidateSchedule;
        if (usingEstablishTimeReferenceMethod == true){
            establishTimeReference();
        }
        else{
            //currentHour, currentMinute, currentEpochTime injected from TestCaseParameterWindow instance);
            Calendar calendar = new GregorianCalendar();
            currentDate = calendar.get(Calendar.DATE);
//            currentDate = currentDate + testCaseDate;
            currentHour = testCaseHour;
            currentMinute = testCaseMinute;

            calendar.set(Calendar.DATE, currentDate);
            calendar.set(Calendar.HOUR_OF_DAY, testCaseHour);
            calendar.set(Calendar.MINUTE, testCaseMinute);

            currentEpochTime = calendar.getTimeInMillis();
        }


//        setEntityAvailability(horizon);
        

        for(Object opObj : operationList){
            Operation op = (Operation) opObj;
            op.setScheduled(false);
        }

//        randomizeLists();
//        randomizeLists();
//            rand = (random.nextDouble()+random.nextDouble())/2;
//            Double randRL = 0.6 * (resourceList.size());
//            int randRLInt = randRL.intValue();
//            Resource resource = resourceList.get(randRLInt);
//            resourceList.remove(resource);
//            resourceList.addFirst(resource);
//
//            resourceList.remove(resource);
//            resourceList.addFirst(resource);
//
//            rand = (random.nextDouble()+random.nextDouble())/2;
//            Double randPL = 0.3 * (patientList.size());
//            int randPLInt = randPL.intValue();
//            Patient patient = patientList.get(randPLInt);
//            patientList.remove(patient);
//            patientList.addFirst(patient);
//
//            patientList.remove(patient);
//            patientList.addFirst(patient);


        unscheduledOpList = operationList.getUnscheduledOperationList();
        scheduledOpList = operationList.getScheduledOperationList();

//        System.out.println("scheduledOpList size = "+scheduledOpList.size());


        /**
         * Set scheduler start hour and min
         */
        //<editor-fold defaultstate="collapsed" desc="Set schedulerStartHour and Minute">
        if (scheduledOpList.isEmpty() == false) {
            long earliestScheduleStartEpochTime = currentEpochTime;
            for (Object opObject : scheduledOpList) {
                Operation operation = (Operation) opObject;
                if (operation.getStartEpochTime() < earliestScheduleStartEpochTime) {
                    earliestScheduleStartEpochTime = operation.getStartEpochTime();
                }
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(earliestScheduleStartEpochTime);

            scheduleStartHour = calendar.get(Calendar.HOUR_OF_DAY);
            scheduleStartMinute = calendar.get(Calendar.MINUTE);
            scheduleStartDate = calendar.get(Calendar.DATE);
            scheduleStartEpochTime = calendar.getTimeInMillis();

        } else {
            scheduleStartHour = currentHour; //initialize real-time Calendar reference hour
            scheduleStartMinute = currentMinute; //initialize real-time Calendar reference minute
            scheduleStartEpochTime = currentEpochTime;
            scheduleStartDate = currentDate;
        }
//        System.out.println("scheduleStartHour: " + scheduleStartHour + " scheduleStartMin: " + scheduleStartMinute);
        //</editor-fold>

        //Generate Information from input operationList
        Operation currentOp;
        int totalProcTime = 0;
        for (Object opObject : operationList) {
            currentOp = (Operation) opObject;

            //Get the max schedule time (sum of all processing times if they happened one-at-a-time)
            totalProcTime = totalProcTime + currentOp.getProcessingTime();
        }
        int scheduleEndMin = scheduleStartMinute + totalProcTime;

        Resource currentResource;
        Operation currentOperation;
        Patient currentPatient;

        int currentPatientLTRP = 0;

        Patient chosenPatient = null;

        int maxLTRP = 0;

//        System.out.println("currEpTime: " +currentEpochTime+", schedStET: "+scheduleStartEpochTime);

        int timeFromStart = (int) (((currentEpochTime - scheduleStartEpochTime) * (0.001)) / 60);
//        System.out.println(totalProcTime);

        int scheduleSearchTimeHour = currentHour;
        int scheduleSearchTimeMinute = currentMinute;
        int scheduleSearchTimeMonth = currentMonth;
        int scheduleSearchTimeDate = currentDate;
        long scheduleSearchTimeEpochTime = currentEpochTime;

        for (int i = horizon; i < (totalProcTime+1000); i++) {    //******HARDCODED********iterate through time units: UPDATED FOR HORIZON
            if (horizon != 0) {
//                  timeFromStart = i-horizon;
            } else {
//                  timeFromStart = i;
            }

//            randomizeLists();
//            randomizeLists();
//            rand = random.nextDouble();
//            randRL = rand * (resourceList.size());
//            randRLInt = randRL.intValue();
//            resource = resourceList.get(randRLInt);
//            resourceList.remove(resource);
//            resourceList.addFirst(resource);
//
//            resourceList.remove(resource);
//            resourceList.addFirst(resource);
//
//            rand = random.nextDouble();
//            randPL = rand * (patientList.size());
//            randPLInt = randPL.intValue();
//            patient = patientList.get(randPLInt);
//            patientList.remove(patient);
//            patientList.addFirst(patient);
//
//            patientList.remove(patient);
//            patientList.addFirst(patient);



            //RELATIVE SEARCH TIME (where t_0 = 00:00 Eastern Time)
//            int scheduleSearchTimeHour = currentHour;
//            int scheduleSearchTimeMinute = currentMinute + i;
//            int scheduleSearchTimeMonth = currentMonth;
//            int scheduleSearchTimeDate = currentDate;
//            long scheduleSearchTimeEpochTime = currentEpochTime;
//            scheduleSearchTimeMinute = currentMinute+i;

            scheduleSearchTimeMinute++;

            if ((scheduleSearchTimeMinute) >= 60){
                scheduleSearchTimeHour++;
                scheduleSearchTimeMinute = scheduleSearchTimeMinute - 60;
//                scheduleSearchTimeMinute = (currentMinute + i)%60;
            }
            if (scheduleSearchTimeHour >= 24){
                scheduleSearchTimeHour = 0;
                scheduleSearchTimeDate = scheduleSearchTimeDate+1;
            }
//            System.out.println("scheduleSearchTime: "+scheduleSearchTimeHour+":"+scheduleSearchTimeMinute);

            if (unscheduledOpList.isEmpty()) {
                break;
            }

            //<editor-fold defaultstate="collapsed" desc="reset availability">
            for (Object resObj : resourceList){
                currentResource = (Resource) resObj;
                if (currentResource.isAvailable() == false) {                        //Reset resource availability for current time index
                        if(   (
                                   (scheduleSearchTimeHour > currentResource.getNextAvailableHour() )
                                && (scheduleSearchTimeDate == currentResource.getNextAvailableDate())
                              )
                            ||
                              (
                                   (scheduleSearchTimeHour == currentResource.getNextAvailableHour())
                                && (scheduleSearchTimeMinute >= currentResource.getNextAvailableMinute())
                              )
                          ){
//                        if(scheduleSearchTimeEpochTime > currentResource.getNextAvailableEpochTime()){
                            currentResource.setAvailable(true);
                        }
                    }
            }

//                    </editor-fold>
            //<editor-fold defaultstate="collapsed" desc="reset patient availability">
            for(Object patObj : patientList){
                currentPatient = (Patient) patObj;
                if (currentPatient.isAvailable() == false) {                        //Reset resource availability for current time index
                        if (((scheduleSearchTimeHour > currentPatient.getNextAvailableHour())
                                && (scheduleSearchTimeDate == currentPatient.getNextAvailableDate()))
                                || ((scheduleSearchTimeHour == currentPatient.getNextAvailableHour())
                                && (scheduleSearchTimeMinute >= currentPatient.getNextAvailableMinute()))) {
//                            if (scheduleSearchTimeEpochTime > currentPatient.getNextAvailableEpochTime()){
                            currentPatient.setAvailable(true);
                        }
                    }
            }

                    //</editor-fold>

            /**
             * @FIX: 'reset nurse availability' -- uncomment for non-testing
             */
//            //<editor-fold defaultstate="collapsed" desc="reset nurse availability">
//            for(Object nurseObj : nurseList){
//                Nurse nurse = (Nurse) nurseObj;
//                if (nurse.isAvailable() == false) {                        //Reset resource availability for current time index
//                        if (((scheduleSearchTimeHour > nurse.getNextAvailableHour())
//                                && (scheduleSearchTimeDate == nurse.getNextAvailableDate()))
//                                || ((scheduleSearchTimeHour == nurse.getNextAvailableHour())
//                                && (scheduleSearchTimeMinute >= nurse.getNextAvailableMinute()))) {
////                            if (scheduleSearchTimeEpochTime > currentPatient.getNextAvailableEpochTime()){
//                            nurse.setAvailable(true);
//                        }
//                    }
//            }
//             //</editor-fold>

            /**
             * skip current iteration if a nurse is not available to assist operation for a patient
             */
            

            for (Object resourceObj : resourceList){
                currentResource = (Resource) resourceObj;
                boolean currentResourceNeeded = false;

                /**
                 * @FIX: uncomment for non-testing
                 *
                 * skip current iteration if a nurse is not available to assist operation for a patient
                 */
//                if(nurseList.isNurseAvailable() == false){
//                        break;
//                    }

                if (patientListRequiringCurrentResource != null) {
                    patientListRequiringCurrentResource.clear();
                }
                for (int uo = 0; uo < unscheduledOpList.size(); uo++) {
//                for (Object operationObj : unscheduledOpList) {
//                    currentOperation = (Operation) operationObj;
                    currentOperation = unscheduledOpList.get(uo);

                    if (currentOperation.getResourceID() == currentResource.getID()) {
                        currentResourceNeeded = true;
                        patientListRequiringCurrentResource.add(patientList.getByID(currentOperation.getPatientID()));
                    }
                }

                if (currentResourceNeeded == false) {
                    //resource is not needed, therefore no reason to check further.
                } else {


                    if (currentResource.isAvailable() == true) {
                        maxLTRP = 0;
                        for (Object patientObj : patientListRequiringCurrentResource){
                            currentPatient = (Patient) patientObj;


//                            //<editor-fold defaultstate="collapsed" desc="reset patient availability">
//
//                            if (currentPatient.isAvailable() == false) {                        //Reset resource availability for current time index
//                                if (   (
//                                           (scheduleSearchTimeHour > currentPatient.getNextAvailableHour() )
//                                        && (scheduleSearchTimeDate == currentPatient.getNextAvailableDate())
//                                      )
//                                    ||
//                                      (
//                                           (scheduleSearchTimeHour == currentPatient.getNextAvailableHour())
//                                        && (scheduleSearchTimeMinute >= currentPatient.getNextAvailableMinute())
//                                      )
//                                  ){
////                            if (scheduleSearchTimeEpochTime > currentPatient.getNextAvailableEpochTime()){
//                                    currentPatient.setAvailable(true);
//                                }
//                            }
//                            //</editor-fold>

                            if (currentPatient.isAvailable() == false) {
                            } else {
                                currentPatientLTRP = operationList.getLTRPOM(currentPatient.getID(), currentResource.getID(), unscheduledOpList);       //select the one with the maxLTRP > 0..

                                if (currentPatientLTRP > maxLTRP) {
                                    maxLTRP = currentPatientLTRP;
                                    chosenPatient = currentPatient;
//                                    System.out.println("chosenPatientID: " + chosenPatientID + " | maxLTRP: " + maxLTRP);
                                } else if (maxLTRP == 0) {
                                    //if LTRP=0 for all j, this means that all j have
                                    //only one operation remaining and thus the scheduler will not
                                    //know what to do; To arbitrate, we resort to SPT dispatching.

                                    Operation theSPTOp = operationList.getSPTOpID(patientListRequiringCurrentResource, currentResource.getID()); //returns the Operation ID with the SPT in the main OperationList<Operation>
                                    chosenPatient = currentPatient;
                                }

                                currentOperation = operationList.getByIDs(currentResource.getID(), chosenPatient.getID());
                                currentOperation.setScheduled(true);

//                                if(testCaseHour > scheduleSearchTimeHour){
//                                    currentDate = currentDate + 1;
//                                }
//                                System.out.println("current date to be fed to operation setStartCalendar method is: "+currentDate);
//                                System.out.println("op:"+currentOperation.getID()+" scheduleSearchTimeDate: "+scheduleSearchTimeDate);



                                currentOperation.setStartCalendar(scheduleSearchTimeHour, scheduleSearchTimeMinute, currentYear, scheduleSearchTimeMonth, scheduleSearchTimeDate);

//                                System.out.println("Scheduler.generateCandidateSchedule() operation: "+currentOperation.getID()
//                                                    +" / getStartEpochTime: "+currentOperation.getStartEpochTime()+
//                                                    " end: "+currentOperation.getEndEpochTime());

//                                System.out.println("In genCandSchedule; op: "+currentOperation.getID()+" | scheduled to start at: "+currentOperation.getStartHour()+":"+currentOperation.getStartMinute());

                                chosenPatient.setAvailable(false);
                                chosenPatient.setNextAvailableDate(currentOperation.getEndDate());
                                chosenPatient.setNextAvailableHour(currentOperation.getEndHour());
                                chosenPatient.setNextAvailableMinute(currentOperation.getEndMinute());
                                chosenPatient.setNextAvailableEpochTime(currentOperation.getEndEpochTime());

                                /**
                                 * @FIX: for non-testing, uncomment code below.
                                 */
//                                Nurse opNurse = null;
//                                for(Object nurseObj : nurseList){
//                                    Nurse nurse = (Nurse) nurseObj;
//                                    if(nurse.isAvailable() == true){
//                                        opNurse = nurse;
//                                        break;
//                                    }
//                                }
//
//                                opNurse.setAvailable(false);
//                                if((currentOperation.getStartMinute()+5) >= 60){
//                                    opNurse.setNextAvailableDate(currentOperation.getStartDate());
//                                    opNurse.setNextAvailableHour(currentOperation.getStartHour()+1);
//                                    opNurse.setNextAvailableMinute((currentOperation.getStartMinute()+5)-60);
//                                }else{
//                                    opNurse.setNextAvailableDate(currentOperation.getStartDate());
//                                    opNurse.setNextAvailableHour(currentOperation.getStartHour());
//                                    opNurse.setNextAvailableMinute(currentOperation.getStartMinute()+5);
//                                }

                                currentResource.setAvailable(false);
                                currentResource.setNextAvailableDate(currentOperation.getEndDate());
                                currentResource.setNextAvailableHour(currentOperation.getEndHour());
                                currentResource.setNextAvailableMinute(currentOperation.getEndMinute());
                                currentResource.setNextAvailableEpochTime(currentOperation.getEndEpochTime());
//                                System.out.println("op: "+currentOperation.getID()+" ends at: "+currentOperation.getEndHour()+":"+currentOperation.getEndMinute());

                                unscheduledOpList.removeByID(currentOperation.getID());
                                scheduledOpList.add(currentOperation);
                                
                                break;
                            }
                        }
                    }  //currentResource.isAvailable() == true
                }
            } //resourceList iteration
        } //time iteration
//        System.out.println("Scheduler.generateCandidateSchedule scheduleStartEpochTime: "+ scheduleStartEpochTime);
        baseSchedule = new Schedule(operationList, patientList, resourceList, nurseList, scheduleStartHour, scheduleStartMinute, scheduleStartEpochTime, scheduleStartDate, currentHour, currentMinute, currentEpochTime, currentDate);
        baseScheduleGenerated = true;

        return baseSchedule;
    } //method end



    //===============================================================================================================================================================
    //|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||       END OF GENERATEBASESCHEDULE METHOD      |||||||||||||||||||||||||||||||||||||||||||||||||||||
    //===============================================================================================================================================================
//    </editor-fold>

    //===============================================================================================================================================================
    //||||||||||||||||||||||||||||||||||||||||||||||||||        BEGIN GENERATECANDIDATESCHEDULE METHOD       |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    //===============================================================================================================================================================
    //    <editor-fold defaultstate="collapsed" desc="generateCandidateSchedule()">
    /**
     *generateBaseSchedule() uses an LTRP-OM dispatching rule to generate a base schedule.
     *  It does this by iterating through time units (minute) and checking which
     * resources are available.  If resource i is availabe, j with the highest total
     * remaining processing time on OTHER machines is scheduled
     *
     * Hence, "Longest Total Remaining Processing time on Other Machines" (LTRP-OM)
     */
    @SuppressWarnings("static-access")
    public Schedule generateCandidateSchedule() {
        Schedule candidateSchedule;
        if (usingEstablishTimeReferenceMethod == true){
            establishTimeReference();
        }
        else{
            //currentHour, currentMinute, currentEpochTime injected from TestCaseParameterWindow instance);
            Calendar calendar = new GregorianCalendar();
//            calendar.set(Calendar.MONTH, 3);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDate = calendar.get(Calendar.DATE);
//            currentDate = currentDate + testCaseDate;
            currentHour = testCaseHour;
            currentMinute = testCaseMinute;
            
            calendar.set(Calendar.DATE, currentDate);
            calendar.set(Calendar.HOUR_OF_DAY, testCaseHour);
            calendar.set(Calendar.MINUTE, testCaseMinute);

            currentEpochTime = calendar.getTimeInMillis();
        }
        

        setEntityAvailability(horizon);

        /**
         * Set scheduler start hour and min
         */
        //<editor-fold defaultstate="collapsed" desc="Set schedulerStartHour and Minute">
        if (scheduledOpList.isEmpty() == false) {
            long earliestScheduleStartEpochTime = currentEpochTime;
            for (Object opObject : scheduledOpList) {
                Operation operation = (Operation) opObject;
                if (operation.getStartEpochTime() < earliestScheduleStartEpochTime) {
                    earliestScheduleStartEpochTime = operation.getStartEpochTime();
                }
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(earliestScheduleStartEpochTime);

            scheduleStartHour = calendar.get(Calendar.HOUR_OF_DAY);
            scheduleStartMinute = calendar.get(Calendar.MINUTE);            
            scheduleStartDate = calendar.get(Calendar.DATE);
            scheduleStartEpochTime = calendar.getTimeInMillis();

        } else {
            scheduleStartHour = currentHour; //initialize real-time Calendar reference hour
            scheduleStartMinute = currentMinute; //initialize real-time Calendar reference minute
            scheduleStartEpochTime = currentEpochTime;
            scheduleStartDate = currentDate;
        }
//        System.out.println("scheduleStartHour: " + scheduleStartHour + " scheduleStartMin: " + scheduleStartMinute);
        //</editor-fold>

        //Generate Information from input operationList
        Operation currentOp;
        int totalProcTime = 0;
        for (Object opObject : operationList) {
            currentOp = (Operation) opObject;

            //Get the max schedule time (sum of all processing times if they happened one-at-a-time)
            totalProcTime = totalProcTime + currentOp.getProcessingTime();
        }
        int scheduleEndMin = scheduleStartMinute + totalProcTime;

        Resource currentResource;
        Operation currentOperation;
        Patient currentPatient;

        int currentPatientLTRP = 0;

        Patient chosenPatient = null;

        int maxLTRP = 0;

//        System.out.println("currEpTime: " +currentEpochTime+", schedStET: "+scheduleStartEpochTime);

        int timeFromStart = (int) (((currentEpochTime - scheduleStartEpochTime) * (0.001)) / 60);
//        System.out.println(totalProcTime);

        int scheduleSearchTimeHour = currentHour;
        int scheduleSearchTimeMinute = currentMinute;
        int scheduleSearchTimeMonth = currentMonth;
        int scheduleSearchTimeDate = currentDate;
        long scheduleSearchTimeEpochTime = currentEpochTime;

        for (int i = horizon; i < (totalProcTime+1000); i++) {    //******HARDCODED********iterate through time units: UPDATED FOR HORIZON
            if (horizon != 0) {
//                  timeFromStart = i-horizon;
            } else {
//                  timeFromStart = i;
            }



            

            //RELATIVE SEARCH TIME (where t_0 = 00:00 Eastern Time)
//            int scheduleSearchTimeHour = currentHour;
//            int scheduleSearchTimeMinute = currentMinute + i;
//            int scheduleSearchTimeMonth = currentMonth;
//            int scheduleSearchTimeDate = currentDate;
//            long scheduleSearchTimeEpochTime = currentEpochTime;
//            scheduleSearchTimeMinute = currentMinute+i;

            scheduleSearchTimeMinute++;

            if ((scheduleSearchTimeMinute) >= 60){
                scheduleSearchTimeHour++;
                scheduleSearchTimeMinute = scheduleSearchTimeMinute - 60;
//                scheduleSearchTimeMinute = (currentMinute + i)%60;
            }
            if (scheduleSearchTimeHour >= 24){
                scheduleSearchTimeHour = 0;
                scheduleSearchTimeDate = scheduleSearchTimeDate+1;
            }
//            System.out.println("scheduleSearchTime: "+scheduleSearchTimeHour+":"+scheduleSearchTimeMinute);

            if (unscheduledOpList.isEmpty()) {
                break;
            }

            //<editor-fold defaultstate="collapsed" desc="reset availability">
            for (Object resObj : resourceList){
                currentResource = (Resource) resObj;
                if (currentResource.isAvailable() == false) {                        //Reset resource availability for current time index
                        if(   (
                                   (scheduleSearchTimeHour > currentResource.getNextAvailableHour() )
                                && (scheduleSearchTimeDate == currentResource.getNextAvailableDate())
                              )
                            ||
                              (
                                   (scheduleSearchTimeHour == currentResource.getNextAvailableHour())
                                && (scheduleSearchTimeMinute >= currentResource.getNextAvailableMinute())
                              )
                          ){
//                        if(scheduleSearchTimeEpochTime > currentResource.getNextAvailableEpochTime()){
                            currentResource.setAvailable(true);
                        }
                    }
            }
                    
//                    </editor-fold>
            //<editor-fold defaultstate="collapsed" desc="reset patient availability">
            for(Object patObj : patientList){
                currentPatient = (Patient) patObj;
                if (currentPatient.isAvailable() == false) {                        //Reset resource availability for current time index
                        if (((scheduleSearchTimeHour > currentPatient.getNextAvailableHour())
                                && (scheduleSearchTimeDate == currentPatient.getNextAvailableDate()))
                                || ((scheduleSearchTimeHour == currentPatient.getNextAvailableHour())
                                && (scheduleSearchTimeMinute >= currentPatient.getNextAvailableMinute()))) {
//                            if (scheduleSearchTimeEpochTime > currentPatient.getNextAvailableEpochTime()){
                            currentPatient.setAvailable(true);
                        }
                    }
            }
                    
                    //</editor-fold>


            for (Object resourceObj : resourceList){
                currentResource = (Resource) resourceObj;
                boolean currentResourceNeeded = false;

                if (patientListRequiringCurrentResource != null) {
                    patientListRequiringCurrentResource.clear();
                }
                for (int uo = 0; uo < unscheduledOpList.size(); uo++) {
//                for (Object operationObj : unscheduledOpList) {
//                    currentOperation = (Operation) operationObj;
                    currentOperation = unscheduledOpList.get(uo);

                    if (currentOperation.getResourceID() == currentResource.getID()) {
                        currentResourceNeeded = true;
                        patientListRequiringCurrentResource.add(patientList.getByID(currentOperation.getPatientID()));
                    }
                }

                if (currentResourceNeeded == false) {
                    //resource is not needed, therefore no reason to check further.
                } else {

                    
                    if (currentResource.isAvailable() == true) {
                        maxLTRP = 0;
                        for (Object patientObj : patientListRequiringCurrentResource){
                            currentPatient = (Patient) patientObj;

                            if (currentPatient.isAvailable() == false) {
                            } else {
                                currentPatientLTRP = operationList.getLTRPOM(currentPatient.getID(), currentResource.getID(), unscheduledOpList);       //select the one with the maxLTRP > 0..

                                if (currentPatientLTRP > maxLTRP) {
                                    maxLTRP = currentPatientLTRP;
                                    chosenPatient = currentPatient;
//                                    System.out.println("chosenPatientID: " + chosenPatientID + " | maxLTRP: " + maxLTRP);
                                } else if (maxLTRP == 0) {
                                    //if LTRP=0 for all j, this means that all j have
                                    //only one operation remaining and thus the scheduler will not
                                    //know what to do; To arbitrate, we resort to SPT dispatching.

                                    Operation theSPTOp = operationList.getSPTOpID(patientListRequiringCurrentResource, currentResource.getID()); //returns the Operation ID with the SPT in the main OperationList<Operation>
                                    chosenPatient = currentPatient;
                                }

                                currentOperation = operationList.getByIDs(currentResource.getID(), chosenPatient.getID());
                                currentOperation.setScheduled(true);

//                                if(testCaseHour > scheduleSearchTimeHour){
//                                    currentDate = currentDate + 1;
//                                }
//                                System.out.println("current date to be fed to operation setStartCalendar method is: "+currentDate);
//                                System.out.println("op:"+currentOperation.getID()+" scheduleSearchTimeDate: "+scheduleSearchTimeDate);



                                currentOperation.setStartCalendar(scheduleSearchTimeHour, scheduleSearchTimeMinute, currentYear, scheduleSearchTimeMonth, scheduleSearchTimeDate);
                                
//                                System.out.println("Scheduler.generateCandidateSchedule() operation: "+currentOperation.getID()
//                                                    +" / getStartEpochTime: "+currentOperation.getStartEpochTime()+
//                                                    " end: "+currentOperation.getEndEpochTime());

//                                System.out.println("In genCandSchedule; op: "+currentOperation.getID()+" | scheduled to start at: "+currentOperation.getStartHour()+":"+currentOperation.getStartMinute());

                                /**
                                 * NURSE AVAILABILITY
                                 */
                                switch(currentResource.getID()){
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        break;
                                    case 5:
                                        break;
                                    case 6:
                                        break;
                                    case 7:
                                        break;
                                    case 8:
                                        break;
                                    case 9:
                                        break;

                                }


                                /**
                                 * PATIENT AVAILABILITY
                                 */
                                chosenPatient.setAvailable(false);
                                chosenPatient.setNextAvailableDate(currentOperation.getEndDate());
                                chosenPatient.setNextAvailableHour(currentOperation.getEndHour());
                                chosenPatient.setNextAvailableMinute(currentOperation.getEndMinute());
                                chosenPatient.setNextAvailableEpochTime(currentOperation.getEndEpochTime());

                                /**
                                 * RESOURCE AVAILABILITY
                                 */
                                currentResource.setAvailable(false);
                                currentResource.setNextAvailableDate(currentOperation.getEndDate());
                                currentResource.setNextAvailableHour(currentOperation.getEndHour());
                                currentResource.setNextAvailableMinute(currentOperation.getEndMinute());
                                currentResource.setNextAvailableEpochTime(currentOperation.getEndEpochTime());
//                                System.out.println("op: "+currentOperation.getID()+" ends at: "+currentOperation.getEndHour()+":"+currentOperation.getEndMinute());

                                unscheduledOpList.removeByID(currentOperation.getID());
                                scheduledOpList.add(currentOperation);

                                break;
                            }
                        }
                    }  //currentResource.isAvailable() == true
                }
            } //resourceList iteration
        } //time iteration
//        System.out.println("Scheduler.generateCandidateSchedule scheduleStartEpochTime: "+ scheduleStartEpochTime);
        candidateSchedule = new Schedule(operationList, patientList, resourceList, nurseList, scheduleStartHour, scheduleStartMinute, scheduleStartEpochTime, scheduleStartDate, currentHour, currentMinute, currentEpochTime, currentDate);
        baseScheduleGenerated = true;

        return candidateSchedule;
    } //method end
    //===============================================================================================================================================================
    //|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||       END OF GENERATECANDIDATESCHEDULE METHOD      |||||||||||||||||||||||||||||||||||||||||||||||||||||
    //===============================================================================================================================================================
    //    </editor-fold>

    /**
     * Randomizes the permutation of OPR lists before the list algorithm runs.  The implemented scheduling algorithms
     * in the Scheduler class are list algorithms and are thus "FIFe-C" (First-In, First-Considered) To understand this better,
     * refer to documentation on how the generateCandidateSchedule() scheduling algorithm schedules operations.
     */
    public void randomizeLists(){
//            getCurrentLists();
//
//            operationList = bestSchedule.getOperationList();
//            resourceList = bestSchedule.getResourceList();
//            patientList = bestSchedule.getPatientList();


//            Double rand = Math.random();
        
//
        rand = random.nextDouble();
            Double randOpList = rand * (operationList.size());
            int randOLInt = randOpList.intValue();
            Operation operation = operationList.get(randOLInt);
            operationList.remove(operation);
            operationList.addFirst(operation);

            operation = operationList.get(randOLInt);
            operationList.remove(operation);
            operationList.addLast(operation);

            operation = operationList.get(randOLInt);
            operationList.remove(operation);
            operationList.addFirst(operation);

//            operation = operationList.get(randOLInt);
//            operationList.remove(operation);
//            operationList.addLast(operation);

//            operation = operationList.get(randOLInt);
//            operationList.remove(operation);
//            operationList.addLast(operation);

//            operation = operationList.get(randOLInt);
//            operationList.remove(operation);
//            operationList.addLast(operation);




//            System.out.print("OpList for this schedule: ");
//            for (int i=0; i < operationList.size(); i++){
//                System.out.print(operationList.get(i).getID()+", ");
//            }
//            System.out.print("\n\r");
//
            rand = (random.nextDouble()+random.nextDouble())/2;
//            System.out.println(rand);
            Double randRL = rand * (resourceList.size());
            int randRLInt = randRL.intValue();
            Resource resource = resourceList.get(randRLInt);
            resourceList.remove(resource);
            resourceList.addFirst(resource);

//            resourceList.remove(resource);
//            resourceList.addFirst(resource);
        
//            System.out.print("ResourceList for this schedule: ");
//            for (int i=0; i < resourceList.size(); i++){
//                System.out.print(resourceList.get(i).getID()+", ");
//            }
//            System.out.print("\n\r");
            

            rand = (random.nextDouble()+random.nextDouble())/2;
            Double randPL = rand * (patientList.size());
            int randPLInt = randPL.intValue();
            Patient patient = patientList.get(randPLInt);
            patientList.remove(patient);
            patientList.addFirst(patient);

//            patientList.remove(patient);
//            patientList.addFirst(patient);

//            System.out.print("patientList for this schedule: ");
//            for (int i=0; i < patientList.size(); i++){
//                System.out.print(patientList.get(i).getID()+", ");
//            }
//            System.out.print("\n\r ============================================================ \n\r");

    }
    
    public Schedule generateBestSchedule(){
        if (neighborhood.isEmpty() == false){
            neighborhood.clear();
        }


        /**
         * @FIX: CONFIGURATION FIXPOINT
         *
         * STATIC TESTING:  bestSchedule = generateBaseSchedule();
         * RUNTIME TESTING: bestSchedule = generateCandidateSchedule();
         * IMPLEMENTATION:  bestSchedule = generateCandidateSchedule();
         */
//        bestSchedule = generateCandidateSchedule();
        bestSchedule = generateBaseSchedule();
        neighborhood.add(bestSchedule);
        

        long bestObjectiveValue = calculateObjectiveValue(bestSchedule);
        for (int i = 0; i < runs; i++){
            if(i>200){
                if(i%1000 == 0){
                    System.out.println("run:"+i);
                }
            }else{
                if(i%10 == 0){
                    System.out.println("run:"+i);
                }
            }


        /**
         * @FIX: CONFIGURATION FIXPOINT
         *
         * STATIC TESTING:  bestSchedule = generateBaseSchedule();
         * RUNTIME TESTING: bestSchedule = generateCandidateSchedule();
         * IMPLEMENTATION:  bestSchedule = generateCandidateSchedule();
         */
//            Schedule schedule = generateCandidateSchedule();
            Schedule schedule = generateBaseSchedule();


            long currentObjectiveValue = calculateObjectiveValue(schedule);
            if(currentObjectiveValue < bestObjectiveValue){
                neighborhood.add(schedule);
                bestSchedule = schedule;
                bestObjectiveValue = currentObjectiveValue;
                System.out.println("New schedule added with value of: "+currentObjectiveValue+" at run "+i);
            }
            

            /***
             * =========================================
             * Individual Schedule print testing 1
             * =========================================
             */
//            System.out.print("schedule "+i+" | ");
//            OperationList tempOL = schedule.getOperationList();
////            OperationList.sortByID(tempOL);
//            for(Object opOb : tempOL){
//                Operation op = (Operation) opOb;
//                System.out.print(op.getID()+"|"+ op.getStartMinute()+", ");
//            }
//            System.out.print("\n\r");

            /**
             * ==============END==========================
             */
             if(operationList.isEmpty() == false){
                 randomizeLists();
             }
        }
        System.out.println("neighborhood size: "+ neighborhood.size());
        long currentObjectiveValue;
        bestObjectiveValue = calculateObjectiveValue(bestSchedule);
        long worstObjectiveValue = calculateObjectiveValue(bestSchedule);
        

//        System.out.println("Worst Objective Value found was: " + (worstObjectiveValue));
//        System.out.println("Best Objective Value found was: " + (bestObjectiveValue));

        parameterProcessor.setAllLists(bestSchedule.getOperationList(), bestSchedule.getPatientList(), bestSchedule.getResourceList(), bestSchedule.getNurseList());

        return bestSchedule;
    }

    public long calculateObjectiveValue(Schedule schedule){

    OperationList nonExpiredOpList = schedule.getOperationList().getUnscheduledOperationList();
    for(Object operationObj : schedule.getOperationList()){
        Operation operation = (Operation) operationObj;
        if(operation.getEndEpochTime() > schedule.getCurrentEpochTime()){
            nonExpiredOpList.add(operation);
        }
    }

    long cal1Millis = 0;
    long cal2Millis = 0;
    long rawObjectiveValue = 0;
    weightedObjectiveValue = 0;
        for (Object patientObj : schedule.getPatientList()){
            Patient patient = (Patient) patientObj;

//            int minStartHour = 25;
//            int minStartMinute = 61;
//            int minStartDate = 32;
            int minStartHour = patient.getArrivalHour();
        int minStartMinute = patient.getArrivalMinute();
        int minStartDate = currentDate;

            int maxEndHour = -1;
            int maxEndMinute = -1;
            int maxEndDate = -1;



            for(Object operationObj : schedule.getOperationList()){ //get start and end times for patient (patient's makespan time bounds)
                Operation operation = (Operation) operationObj;

                if(operation.getPatientID() == patient.getID()){

//                    if(operation.getStartDate() < minStartDate){
//                        minStartDate = operation.getStartDate();
////                        System.out.println("Schedule.getObjValue() minStartDate: "+minStartDate);
//                    }
                    if(operation.getEndDate() > maxEndDate){
                        maxEndDate = operation.getEndDate();
//                        System.out.println("Schedule.getObjValue() maxEndDate: "+maxEndDate);
                    }

//                    if((operation.getStartDate() == minStartDate) && (operation.getStartHour() <= minStartHour)){
//                        minStartHour = operation.getStartHour();
//                        if (operation.getStartMinute() < minStartMinute){
//                            minStartMinute = operation.getStartMinute();
//                        }
//                    }

                    if((operation.getEndDate() == maxEndDate) && (operation.getEndHour() >= maxEndHour)){
                        maxEndHour = operation.getEndHour();
                        if (operation.getEndMinute() > maxEndMinute){
                            maxEndMinute = operation.getEndMinute();
                        }
                    }
                }
            }
//            System.out.println("Schedule.getObjValue() patient: "+patient.getID()+" minStart: "+minStartHour+":"+minStartMinute);
//            System.out.println("Schedule.getObjValue() patient: "+patient.getID()+" maxEnd: "+maxEndHour+":"+maxEndMinute);
            Calendar cal1 = new GregorianCalendar();
            cal1.set(Calendar.YEAR, cal1.get(Calendar.YEAR)-1900);
            cal1.set(Calendar.DATE, minStartDate);
            cal1.set(Calendar.HOUR_OF_DAY, minStartHour);
            cal1.set(Calendar.MINUTE, minStartMinute);
            cal1Millis = cal1.getTimeInMillis();
//            System.out.println("Patient "+patient.getID()+" minStartDate: "+minStartDate+" minStartHour: "+minStartHour+"   minStartMinute: "+minStartMinute);

            Calendar cal2 = new GregorianCalendar();
            cal2.set(Calendar.YEAR, cal2.get(Calendar.YEAR)-1900);
            cal2.set(Calendar.DATE, maxEndDate);
            cal2.set(Calendar.HOUR_OF_DAY, maxEndHour);
            cal2.set(Calendar.MINUTE, maxEndMinute);
            cal2Millis = cal2.getTimeInMillis();
//            System.out.println("Patient "+patient.getID()+" minEndDate:   "+maxEndDate+" minEndHour:   "+maxEndHour+"   minEndMinute:   "+maxEndMinute);

            long calculatedMakespan = (cal2Millis - cal1Millis)/60000;

            if(maxEndMinute != -1){
                patient.setMakespan(calculatedMakespan);
                rawObjectiveValue = rawObjectiveValue+calculatedMakespan;

                /**
                 * Weights patient by acuity and generates an objective Value for this.
                 */
                long weightMultiplier = 1;
                switch(patient.getAcuity()){
                    case 1:
                        weightMultiplier = parameterProcessor.getAcuity1Multiplier();
                        break;
                    case 2:
                        weightMultiplier = parameterProcessor.getAcuity2Multiplier();
                        break;
                    case 3:
                        weightMultiplier = parameterProcessor.getAcuity3Multiplier();
                        break;
                    case 4:
                        weightMultiplier = parameterProcessor.getAcuity4Multiplier();
                        break;
                    case 5:
                        weightMultiplier = parameterProcessor.getAcuity5Multiplier();
                        break;
                }
                weightedObjectiveValue = weightedObjectiveValue + (calculatedMakespan * weightMultiplier);
            }
        }

        return weightedObjectiveValue;
    }

    public long calculatePatientLOS(Patient patient){
        long cal1Millis = 0;
        long cal2Millis = 0;

//        int minStartHour = 25;
//        int minStartMinute = 61;
//        int minStartDate = 32;
        int minStartHour = patient.getArrivalHour();
        int minStartMinute = patient.getArrivalMinute();
        
        int minStartDate = currentDate - (patient.getAssignDate()-patient.getArrivalDate());


        int maxEndHour = -1;
        int maxEndMinute = -1;
        int maxEndDate = -1;

            for(Object operationObj : parameterProcessor.getOperationList()){ //get start and end times for patient (patient's makespan time bounds)
                Operation operation = (Operation) operationObj;

                if(operation.getPatientID() == patient.getID()){

//                    if(operation.getStartDate() < minStartDate){
//                        minStartDate = operation.getStartDate();
////                        System.out.println("Schedule.getObjValue() minStartDate: "+minStartDate);
//                    }
                    if(operation.getEndDate() > maxEndDate){
                        maxEndDate = operation.getEndDate();
//                        System.out.println("Schedule.getObjValue() maxEndDate: "+maxEndDate);
                    }

//                    if((operation.getStartDate() == minStartDate) && (operation.getStartHour() <= minStartHour)){
//                        minStartHour = operation.getStartHour();
//                        if (operation.getStartMinute() < minStartMinute){
//                            minStartMinute = operation.getStartMinute();
//                        }
//                    }

                    if((operation.getEndDate() == maxEndDate) && (operation.getEndHour() >= maxEndHour)){
                        maxEndHour = operation.getEndHour();
                        if (operation.getEndMinute() > maxEndMinute){
                            maxEndMinute = operation.getEndMinute();
                        }
                    }
                }
            }
//            System.out.println("Schedule.getObjValue() patient: "+patient.getID()+" minStart: "+minStartHour+":"+minStartMinute);
//            System.out.println("Schedule.getObjValue() patient: "+patient.getID()+" maxEnd: "+maxEndHour+":"+maxEndMinute);
            Calendar cal1 = new GregorianCalendar();
            cal1.set(Calendar.DATE, minStartDate);
            cal1.set(Calendar.HOUR_OF_DAY, minStartHour);
            cal1.set(Calendar.MINUTE, minStartMinute);
            cal1Millis = cal1.getTimeInMillis();
//            System.out.println("Patient "+patient.getID()+" minStartDate: "+minStartDate+" minStartHour: "+minStartHour+"   minStartMinute: "+minStartMinute);

            Calendar cal2 = new GregorianCalendar();
            cal2.set(Calendar.DATE, maxEndDate);
            cal2.set(Calendar.HOUR_OF_DAY, maxEndHour);
            cal2.set(Calendar.MINUTE, maxEndMinute);
            cal2Millis = cal2.getTimeInMillis();
//            System.out.println("Patient "+patient.getID()+" maxEndDate:   "+maxEndDate+" maxEndHour:   "+maxEndHour+"   maxEndMinute:   "+maxEndMinute);

            long calculatedMakespan = (cal2Millis - cal1Millis)/60000;

            if((maxEndMinute != -1) || (maxEndMinute != -1) || (maxEndDate != -1)){
                patient.setMakespan(calculatedMakespan);
            }else{
                calculatedMakespan = 0;
            }

            if(calculatedMakespan == 0){
                Calendar cal3 = new GregorianCalendar();
                cal3.set(Calendar.YEAR, 2010);
//                cal3.set(Calendar.DATE, (currentDate - (patient.getAssignDate()-patient.getArrivalDate())));
                cal3.set(Calendar.DATE, currentDate);
                cal3.set(Calendar.HOUR_OF_DAY, patient.getAssignHour());
                cal3.set(Calendar.MINUTE, patient.getAssignMinute());
                long cal3Millis = cal3.getTimeInMillis();

                calculatedMakespan = (cal3Millis - cal1Millis)/60000;
            }

        return calculatedMakespan;
    }

    //<editor-fold defaultstate="collapsed" desc="Time getters/setters">
    //<editor-fold defaultstate="collapsed" desc="Current Time">

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

    public int getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int currentDate) {
        this.currentDate = currentDate;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Schedule Start Time">
    public int getScheduleStartHour() {
        return currentHour;
    }

    public void setScheduleStartHour(int scheduleStartHour) {
        this.currentHour = scheduleStartHour;
    }

    public int getScheduleStartMin() {
        return currentMinute;
    }

    public void setScheduleStartMin(int scheduleStartMin) {
        this.currentMinute = scheduleStartMin;
    }

    public long getScheduleStartEpochTime() {
        return scheduleStartEpochTime;
    }

    public void setScheduleStartEpochTime(long scheduleStartEpochTime) {
        this.scheduleStartEpochTime = scheduleStartEpochTime;
    }
    //</editor-fold>
    //</editor-fold>

}
