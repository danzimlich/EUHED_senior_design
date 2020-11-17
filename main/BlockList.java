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

public class BlockList extends LinkedList{

//====================FIELDS====================================================



//===================CONSTRUCTORS===============================================



//===================METHODS====================================================

    @Override
    public Block get(int index){
        Block blockFromList = (Block) super.get(index);
        return blockFromList;
    }
    
public Block getByID(int id){
    int currentID;
    int matchingIdx=0;
    for(int i = 0; i<this.size(); i++){
        currentID = this.get(i).getID();
        if (currentID == id){
            matchingIdx = i;
            break;
        }
    }
    Block block = (Block) this.get(matchingIdx);            
    return block;
}



}
