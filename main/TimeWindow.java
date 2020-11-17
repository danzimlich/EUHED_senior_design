/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author Dan Zimlich 2010
 */

public class TimeWindow {
    

//====================FIELDS====================================================

int startTime;
int endTime;
int processingTime;



//===================CONSTRUCTORS===============================================

    public TimeWindow(int startTime, int processingTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.processingTime = processingTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

//===================METHODS====================================================





}
