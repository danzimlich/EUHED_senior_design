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

public class Block extends LinkedList{

//====================FIELDS====================================================

int id;

//===================CONSTRUCTORS===============================================

public Block(int id){
    this.id = id;
}

//===================METHODS====================================================

    @Override
public Operation get(int idx){
        Operation operationFromList = (Operation) super.get(idx);
        return operationFromList;
}

public int getID(){
    return this.id;
}

public void setID(int id){
    this.id = id;
}

}
