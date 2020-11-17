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

public class OperationList extends LinkedList {

//====================FIELDS====================================================



//===================CONSTRUCTORS===============================================

    public OperationList(){
        
    }

//===================METHODS====================================================


public void add(Operation operation){
        super.add(operation);
    }
    
    @Override
public Operation get(int index){
        Operation operationFromList = (Operation) super.get(index);
        return operationFromList;
    }

public Operation getByID(int id){
        int currentID;
        int matchingIdx=0;
        for(int i = 0; i<this.size(); i++){
            currentID = this.get(i).getID();
            if (currentID == id){
                matchingIdx = i;
                break;
            }
        }
        Operation opFromList = (Operation) this.get(matchingIdx);
        return opFromList;
    }

public Operation getByIDs(int resID, int patID){
        int currentResID;
        int currentPatID;
        int matchingIdx = -1;
        
        for(int i = 0; i<this.size(); i++){
            currentResID = this.get(i).getResourceID();
            currentPatID = this.get(i).getPatientID();
            if (currentResID == resID){
                if(currentPatID == patID){
                    matchingIdx = i;
                break;
                }
            }
        }
        if(matchingIdx == -1){
            Operation op = new Operation(-1,-1,-1,0,false);
            return op;
        }
        else{
            Operation opFromList = (Operation) this.get(matchingIdx);
            return opFromList;
        }
        
    }

public int getOpCountForPatient(int patientID){
    int count=0;
    for(int o = 0; o < this.size(); o++){
        if(patientID == this.get(o).getPatientID()){
            count++;
        }
    }
    return count;
}

public OperationList getRequiredOpListForPatient(int patientID){
    OperationList theList = new OperationList();
    
    for(int o = 0; o < this.size(); o++){
        if(patientID == this.get(o).getPatientID()){
            theList.add(this.get(o));
        }
    }
    return theList;
}

public int getLTRPOM(int patientID, int excludeResourceID, OperationList unscheduledOpList){
    int intLTRPOM = 0;
    for(int i = 0; i<unscheduledOpList.size(); i++){
        if(this.get(i).getPatientID() == patientID){
            if (this.get(i).isScheduled()==false){
                if(this.get(i).getResourceID() != excludeResourceID){
                    intLTRPOM = intLTRPOM + this.get(i).getProcessingTime();
                }
            }
            else{
                intLTRPOM = 0;
            }
        }
    }
    return intLTRPOM;
}

public Operation getSPTOpID(PatientList list, int resource){
    int theSPT = 10000;
    int currentIdxProcTime = 0;
    int currentIdxID;
    Operation theSPTOp = null;
    Operation currentOp;
    for(int i = 0; i<list.size(); i++){
        currentOp = this.getByIDs(resource, list.get(i).getID());
        currentIdxProcTime = currentOp.getProcessingTime();
//        currentIdxID = currentOp.getID();
        if (currentIdxProcTime < theSPT){
            theSPT = currentIdxProcTime;
            theSPTOp = currentOp;
        }
    }
    
    return theSPTOp;
}

public OperationList getUnscheduledOperationList(){
    OperationList theList = new OperationList();
    for (int i =0; i < this.size(); i++){
        if(this.get(i).isScheduled() == false){
            theList.add(this.get(i));
        }
    }
    return theList;
}

public OperationList getScheduledOperationList(){
    OperationList theList = new OperationList();
    for (int i =0; i < this.size(); i++){
        if(this.get(i).isScheduled() == true){
            theList.add(this.get(i));
        }
    }
    return theList;
}


public void removeByID(int id){
    int currentID;
        int matchingIdx=0;
        for(int i = 0; i<this.size(); i++){
            currentID = this.get(i).getID();
            if (currentID == id){
                this.remove(i);
                break;
            }
        }
}

/**
 * Sorts the list by order of operation start times beginning at t=0
 * @return
 */
public OperationList sortByStartTime(){
    OperationList sortedOpList = new OperationList();
    int currentOpStartTime;
    for(int o = 0; o < this.size(); o++){
//        System.out.println("sorting op "+this.get(o).getID());
        currentOpStartTime = this.get(o).getStartTime();
        if (sortedOpList.size()==0){
            sortedOpList.add(this.get(o));
        }
        else{
            if (currentOpStartTime == 0){
                sortedOpList.addFirst(this.get(o));
            }
            else{
                for(int so = sortedOpList.size()-1; so > -1; so--){
                    if (currentOpStartTime > sortedOpList.get(so).getStartTime()){
                        sortedOpList.add(so+1, this.get(o));
                        break;
                    }
                    else if(so == 0){
                        sortedOpList.addFirst(this.get(o));
                    }
                }
            }
        }   
    }
    return sortedOpList;
}

/**
 * Sorts the list by order of ID
 * @return
 */
public static void sortByID(OperationList operationList){
    Collections.sort(operationList, new OpIDComparator());
}


}

class OpIDComparator implements Comparator{

    public int compare(Object o1, Object o2){
        int o1ID = 0;
        int o2ID = 0;
        if(o1 instanceof Operation){
            o1ID = ( (Operation) o1).getID();
            o2ID = ( (Operation) o2).getID();

            }
        else if( o1 instanceof Patient){
            o1ID = ( (Patient) o1).getID();
            o2ID = ( (Patient) o2).getID();
        }
        if( o1ID > o2ID )
                return 1;
        else if( o1ID < o2ID )
            return -1;
        else
            return 0;

    }

}

