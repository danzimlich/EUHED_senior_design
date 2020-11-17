/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author Dan Zimlich 2010
 */

public class PatientList extends LinkedList{

//====================FIELDS====================================================
Patient patient;
public int resourceID;


//===================CONSTRUCTORS===============================================

    public PatientList(){
        
    }
    public PatientList(int id){
        this.resourceID = id;
    }


//===================METHODS====================================================

    public void add(Patient patient){
        super.add(patient);
    }

  
    
    @Override
    public Patient get(int index){
        Patient patientFromList = (Patient) super.get(index);
        return patientFromList;
    }

    public Patient getByID(int id){
        int currentID;
        int matchingIdx=0;
        for(int i = 0; i<this.size(); i++){
            currentID = this.get(i).getID();
            
            if (currentID == id){
                matchingIdx = i;
                break;
            }
        }
        Patient patientFromList = (Patient) this.get(matchingIdx);
        return patientFromList;
    }
    
    public int getID(){
    try{
        return resourceID;
    }catch(NullPointerException e){
        System.out.println("ID for "+ this +" is null!  Returning negative ID...");
        return -1;
    }
}

    public static void sortByID(PatientList patientList){
        Collections.sort(patientList, new IDComparator());
    }
    
}
class IDComparator implements Comparator{

    public int compare(Object p1, Object p2){
        int p1ID = ( (Patient) p1).getID();
        int p2ID = ( (Patient) p2).getID();
        if( p1ID > p2ID )
            return 1;
        else if( p1ID < p2ID )
            return -1;
        else
            return 0;

    }

}