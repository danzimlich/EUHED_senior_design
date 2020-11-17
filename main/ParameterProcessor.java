/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is where the local OPR lists are maintained.  Any update to one of
 * the lists from other class objects will need to update the corresponding list
 * field in this class.
 *
 * This class also interfaces with the RRPT database
 *
 * ***THERE SHOULD BE ONLY ONE INSTANCE OF THIS CLASS***
 *
 * @author Dan Zimlich 2010
 */

public class ParameterProcessor {

//====================FIELDS====================================================

public ResourceList resourceList = new ResourceList();
public OperationList operationList = new OperationList();
public PatientList patientList = new PatientList();
public NurseList nurseList = new NurseList();

private SchedulerGUI schedulerGUI;

//==========SENSITIVITY PARAMETERS============
/**
 * @NOTE: These are default values
 */
private int nurseCount = 5;

private int acuity1Multiplier = 50000;
private int acuity2Multiplier = 4000;
private int acuity3Multiplier = 300;
private int acuity4Multiplier = 20;
private int acuity5Multiplier = 1;

private int availableNurseCount = 4;

/**
 * @NOTE: Database location must be specified here.
 */
//private String uri = "jdbc:mysql://localhost:3306/seniordesign";
//private String uri = "jdbc:mysql://69.180.30.58:3306/seniordesign";
//private java.sql.Connection connection;

private String exceptionDescription = "";

//===================CONSTRUCTORS===============================================
public ParameterProcessor(SchedulerGUI schedulerGUI) throws SQLException{
    this.schedulerGUI = schedulerGUI;
//    connectToDB();
    /**
     * @TODO: uncomment the initialize method for non-testing use
     */
    initialize();
}


//===================METHODS====================================================

    @SuppressWarnings("empty-statement")
    public void initialize(){
    
    
    /**
     * Clear Local Lists
     */
    if(operationList.isEmpty() == false){
        operationList.clear();
    }
    if(nurseList.isEmpty() == false){
        nurseList.clear();
    }
    if(patientList.isEmpty() == false){
        patientList.clear();
    }
    if(resourceList.isEmpty() == false){
        resourceList.clear();
    }

    /**
     * Create Local ResourceList
     */
    resourceList.add(new Resource("EKG",                0, 1, false));   
    resourceList.add(new Resource("Urine",              1, 1, false));
    resourceList.add(new Resource("Blood",              2, 1, false));
    resourceList.add(new Resource("XR",                 3, 1, false));   
    resourceList.add(new Resource("XR Portable",        4, 1, false));   
    resourceList.add(new Resource("CT w/o Contrast",    5, 1, false));   
    resourceList.add(new Resource("CT w/ Contrast",     6, 1, false));   
    resourceList.add(new Resource("US",                 7, 1, false));   
    resourceList.add(new Resource("MRI",                8, 1, false));   
    resourceList.add(new Resource("MRA",                9, 1, false));   

    resourceList.add(new Resource("MD Consultation",    10, 3, false));
    resourceList.add(new Resource("Undefined",          11, 1, false));

    /**
     * Create NurseList (for nurse availability constraints)
     */
    //public Nurse(int nurseID, String name, int section)

    nurseList.add(new Nurse(0, "Nurse 0", 99));
    nurseList.add(new Nurse(1, "Nurse 1", 99));
    nurseList.add(new Nurse(2, "Nurse 2", 99));
    nurseList.add(new Nurse(3, "Nurse 3", 99));
    nurseList.add(new Nurse(4, "Nurse 4", 99));


//patientList.add(new Patient("1448 9375150029",1448,2,85,0,119,1,0,0,1,0,0,0,1,0,0));

//operationList.add(new Operation(2515,0,1448,43, false));
    //int opID, int resID, int patID, int procTime

    
}
    
//    public void connectToDB() throws SQLException{
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection(uri, "seniordesign", "seniordesign");
//            System.out.println("TargetURI: " + uri);
//            System.out.println("Connection: " + connection);
//
//        } catch (Exception ex) {
//            Logger.getLogger(ParameterProcessor.class.getName()).log(Level.SEVERE, null, ex);
//            exceptionDescription = ("Failed to connect to database at: " + uri);
//            ExceptionDialog exceptionDialog = new ExceptionDialog(schedulerGUI, true);
//            exceptionDialog.getErrorTextArea().append(exceptionDescription);
//            exceptionDialog.getErrorTextArea().append("");
//            exceptionDialog.setVisible(true);
//
//        }
//    }


    public void addPatient() throws SQLException{
            int newPatientID;
            /**
             * @TODO: fix this query to INSERT
             */

//            Statement statement = (Statement) connection.createStatement();
//            statement.executeQuery("INSERT INTO PATIENT VALUES ()");

            //This query will get the ID assigned to the new patient by the DB's AUTO_INCREMENT function
//            ResultSet resultSet = statement.executeQuery("SELECT patientID FROM PATIENT WHERE (name=\"\" AND age=\"\" AND acuity = \"\"");
//            while (resultSet.next() == true){
//                newPatientID = Integer.valueOf(resultSet.getString(0));
//            }

            /**
         * @TODO: uncomment this and make sure it makes INSERT query to DB and also
         * updates OPR Lists in ParameterProcessor
         */

         //patientList.add();


//        Patient(String name, int patID, int acuity, int age, int sex, int complaint)
//        Patient patientToAdd = new Patient(patientName, patientAcuity, patientAge, patientSex, patientComplaint);


        /**
         * END COMMENT
         */
    }
    
    public void setAllLists(OperationList oL, PatientList pL, ResourceList rL, NurseList nL){
        this.operationList = oL;
        this.patientList = pL;
        this.resourceList = rL;
        this.nurseList = nL;
    }
    
    public OperationList getOperationList() {
        return operationList;
    }

    public OperationList getOperationListForPatient(int patientID){
        OperationList opList = new OperationList();

        for(Object opObj : operationList){
            Operation op = (Operation) opObj;
            if (op.getPatientID() == patientID){
                opList.add(op);
            }
        }
        return opList;
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

    public int getNurseCount() {
        return nurseCount;
    }

    public void setNurseCount(int nurseCount) {
        this.nurseCount = nurseCount;
    }

    public int getAcuity1Multiplier() {
        return acuity1Multiplier;
    }

    public void setAcuity1Multiplier(int acuity1Multiplier) {
        this.acuity1Multiplier = acuity1Multiplier;
    }

    public int getAcuity2Multiplier() {
        return acuity2Multiplier;
    }

    public void setAcuity2Multiplier(int acuity2Multiplier) {
        this.acuity2Multiplier = acuity2Multiplier;
    }

    public int getAcuity3Multiplier() {
        return acuity3Multiplier;
    }

    public void setAcuity3Multiplier(int acuity3Multiplier) {
        this.acuity3Multiplier = acuity3Multiplier;
    }

    public int getAcuity4Multiplier() {
        return acuity4Multiplier;
    }

    public void setAcuity4Multiplier(int acuity4Multiplier) {
        this.acuity4Multiplier = acuity4Multiplier;
    }

    public int getAcuity5Multiplier() {
        return acuity5Multiplier;
    }

    public void setAcuity5Multiplier(int acuity5Multiplier) {
        this.acuity5Multiplier = acuity5Multiplier;
    }



}
