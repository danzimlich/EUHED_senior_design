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

public class ResourceList extends LinkedList{

//====================FIELDS====================================================

int patientID; //set if a ResourceList pertains to a specific patientID; (i.e., this ResourceList instance is a set of required resources for patientID)

//===================CONSTRUCTORS===============================================

public ResourceList(){
    
}
public ResourceList(int id){
    this.patientID = id;
}

//===================METHODS====================================================

public void add(Resource resource){
        super.add(resource);
    }

    @Override
public Resource get(int index){
        Resource resourceFromList = (Resource) super.get(index);
        return resourceFromList;
    }

public Resource getByID(int id){
        int currentID;
        int matchingIdx=0;
        for(int i = 0; i<this.size(); i++){
            currentID = this.get(i).getID();
            if (currentID == id){
                matchingIdx = i;
                break;
            }
        }
        Resource resourceFromList = (Resource) this.get(matchingIdx);
        return resourceFromList;
    }

public int getID(){
    try{
        return patientID;
    }catch(NullPointerException e){
        System.out.println("ID for "+ this +" is null!  Returning negative ID...");
        return -1;
    }
}
}
