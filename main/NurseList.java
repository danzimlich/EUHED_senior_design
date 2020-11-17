/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Dan Zimlich 2010
 */

public class NurseList extends LinkedList {

//====================FIELDS====================================================



//===================CONSTRUCTORS===============================================



//===================METHODS====================================================

public boolean isNurseAvailable(){
    boolean available = false;
    int amountAvailable = 0;
    for(Object nurseObj : this){
        Nurse nurse = (Nurse) nurseObj;
        if (nurse.isAvailable() == true){
            amountAvailable++;
        }
    }
    
    if(amountAvailable > 0){
        available = true;
    }

    return available;
}

public Nurse getAvailableNurse(){
    Nurse availableNurse = null;

    for(Object nurseObj : this){
        Nurse nurse = (Nurse) nurseObj;
        if(nurse.isAvailable() ==true){
            availableNurse = nurse;
            break;
        }
    }

    return availableNurse;
}



}
