/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.sql.SQLException;

/**
 * This is the main class that creates single static objects such as GUI frames and
 * worker threads for the GUIs.  It also instantiates the JDBC conncector and ParameterProcessor.
 * 
 * The main GUI and JDBC connector are initialized from this class.
 * @author Dan
 */
public class Main {

    //Create single-instance objects
//    static ParameterProcessor parameterProcessor = new ParameterProcessor();
//    static VariableFactory variableFactory = new VariableFactory();
//    static Scheduler scheduler = new Scheduler();
    
    //create GUI classes and threads
//    static SchedulerSwingWorker schedulerThread = new SchedulerSwingWorker();
    /**
     * From API "SwingWorker is only designed to be executed once.
     * Executing a SwingWorker more than once will not result in invoking the doInBackground method twice."
     */

//    static SchedulerGUI schedulerGUI = new SchedulerGUI();
//    static GanttJFrame ganttJFrame = new GanttJFrame();

//    static DeleteOperationDialog deleteOperationDialog = new DeleteOperationDialog();
//    static AddOperationDialog addOperationDialog = new AddOperationDialog(ganttJFrame, true);
    
    /**
     * @param args the command line arguments
     */



    public static void main(String[] args) throws SQLException {
//        deleteOperationDialog.setTitle("WARNING: Altering current schedule");
//        parameterProcessor.initialize();
//        guiThread.start();
//        schedulerThread.start();
        SchedulerGUI schedulerGUI = new SchedulerGUI();
//          schedulerGUI.run();
        schedulerGUI.setVisible(true);
    }

}
