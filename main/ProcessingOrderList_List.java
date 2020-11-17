/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.LinkedList;

/**
 *
 * @author Dan Zimlich 2010
 */

public class ProcessingOrderList_List extends LinkedList{

//====================FIELDS====================================================



//===================CONSTRUCTORS===============================================



//===================METHODS====================================================

  /**
   * Retrieves ResourceList by index in list if the PermutationList holds ResourceList types (and not PatientList types)
   * @param index
   * @return
   */
    public ResourceList getFromRPOL_List(int index){
        ResourceList rLFromList = (ResourceList) super.get(index);
        return rLFromList;
    }
    
    /**
     * Retrieves ResourceList by PatientID if the PermutationList holds ResourceList types (and not PatientList types)
     * @param id
     * @return ResourceList
     */
    public ResourceList getFromRPOL_ListByID(int id){
    int currentID;
    int matchingIdx=0;
    for(int i = 0; i<this.size(); i++){
        currentID = this.getFromRPOL_List(i).getID();
        if (currentID == id){
            matchingIdx = i;
            break;
        }
    }
    ResourceList rL = (ResourceList) this.get(matchingIdx);            
    return rL;
}
    
    /**
   * Retrieves PatientList by index in list if the PermutationList holds PatientList types (and not ResourceList types)
   * @param index
   * @return
   */
    public PatientList getFromPPOL_List(int index){
        PatientList pLFromList = (PatientList) super.get(index);
        return pLFromList;
    }
    
    /**
     * Retrieves PatientList by ResourceID in list if the PermutationList holds PatientList types (and not ResourceList types)
     * @param id
     * @return PatientList
     */
    public PatientList getFromPPOL_ListByID(int id){
    int currentID;
    int matchingIdx=0;
    for(int i = 0; i<this.size(); i++){
        currentID = this.getFromPPOL_List(i).getID();
        if (currentID == id){
            matchingIdx = i;
            break;
        }
    }
    PatientList pL = (PatientList) this.get(matchingIdx);            
    return pL;
}



}
