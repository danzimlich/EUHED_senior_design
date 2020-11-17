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

//public class Neighborhood extends LinkedHashMap{
public class Neighborhood extends LinkedList{

//====================FIELDS====================================================

public static int instances = 0;

//===================CONSTRUCTORS===============================================

public Neighborhood(){
    instances++;
//    System.out.println("\n\rNeighborhood class instances: "+instances);
    
}

    

    public void add(Schedule e) {
//        System.out.println("Neighborhood.Class: added schedule with obj value of "+e.getObjectiveValue());
        
        super.add(e);
    }

    
//    public void add(int index, Schedule element) {
//        super.add(index, element);
//    }
//
//    @Override
//    public Schedule get(int index) {
//        Schedule schedule = (Schedule) super.get(index);
//        return schedule;
//    }

//===================METHODS====================================================





}
