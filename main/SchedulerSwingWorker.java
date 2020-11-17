/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import javax.swing.SwingWorker;

/**
 *
 * @author Dan Zimlich 2010
 */

public class SchedulerSwingWorker extends SwingWorker<Schedule, Void> {
   

//====================FIELDS====================================================

private Scheduler scheduler;
private GanttJFrame ganttJFrame;

//===================CONSTRUCTORS===============================================
 
    public SchedulerSwingWorker(Scheduler scheduler, GanttJFrame ganttJFrame){
        this.scheduler = scheduler;
        this.ganttJFrame = ganttJFrame;
    }


//===================METHODS====================================================


    @Override
    protected Schedule doInBackground() {

        /**
         * @FIX: CONFIGURATION FIXPOINT
         *
         * STATIC TESTING:
         * RUNTIME TESTING:
         * IMPLEMENTATION:  Uncomment "scheduler.run();"
         */

//        scheduler.run();

        Schedule schedule = scheduler.getBestSchedule();
        
        return schedule;
    }
    
    @Override
    public void done(){
        try{
            Schedule schedule = get();
//            scheduler.getParameterProcessor().setOperationList(schedule.getOperationList());
//            scheduler.getParameterProcessor().setPatientList(schedule.getPatientList());
//            scheduler.getParameterProcessor().setResourceList(schedule.getResourceList());

            ganttJFrame = new GanttJFrame(scheduler.getParameterProcessor(), schedule, scheduler);


            System.out.println("Plotting Gantt for best schedule with objective value of: " + (scheduler.calculateObjectiveValue(schedule)));
//            System.out.println("best schedule start: "+schedule.getScheduleStartHour()+":"+schedule.getScheduleStartMin());
            ganttJFrame.setVisible(true);
//            System.out.println("SchedulerSwingWorker: done()");
            
            
            
        }catch(InterruptedException ignore) {}
        catch (java.util.concurrent.ExecutionException e) {
            
        }

}

}