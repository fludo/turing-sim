/*
 *   Copyright (©) 2009 | 16 January 2009 | EPFL (Ecole Polytechnique fédérale de Lausanne)
 *
 *   TuringSim is free software ; you can redistribute it and/or modify it under the terms of the
 *   GNU General Public License as published by the Free Software Foundation ; either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   TuringSim is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY ;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with TuringSim ;
 *   if not, write to the Free Software Foundation,
 *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 *
 *   Author : Ludovic Favre <ludovic.favre@epfl.ch>
 *
 *   Project supervisor : Mahdi Cheraghchi <mahdi.cheraghchi@epfl.ch>
 *
 *   Web site : http://icwww.epfl.ch/~lufavre
 *
 */

package core;

import core.exception.OutOfBoundsException;
import core.exception.TapeBoundReachedException;
import java.util.LinkedList;

/**
 *
 * @author Ludovic Favre
 */
public class Tape {
    private static final char BLANK = '\u2423';
    private Alphabet allowed_symbols_;
    private LinkedList<Integer> tape_representation = new LinkedList<Integer>();
    private String default_content = "";
    private int position_ = 0;
    private boolean bound_ = false;

    public Tape(boolean bound,Alphabet al){
        this.allowed_symbols_ = al;
        if(!this.allowed_symbols_.contains(BLANK)){
            this.allowed_symbols_.add(BLANK);
        }
        this.position_ = 0;
        this.bound_ = bound;
        this.contentIs(""+BLANK);
    }

    /**
     * Constructor (set if Tape is bound or not)
     * @param bound
     */
    public Tape(boolean bound){
        // no argument given, we will create an empty alphabet;
        this.allowed_symbols_ = new Alphabet();
        this.allowed_symbols_.add(BLANK);
        this.position_ = 0;
        this.bound_ = bound;
        this.contentIs(""+BLANK);
    }

    /**
     * Set bound value
     * @param bound
     */
    public void boundValueIs(boolean bound){
        this.bound_ = bound;
    }

    /**
     * Returns true if Tape is bound
     * @return
     */
    public boolean bound(){
        return this.bound_;
    }

    /**
     * Set tape content
     * @param content
     * @throws java.lang.IllegalArgumentException
     */
    public void contentIs(String content) throws IllegalArgumentException{
        LinkedList<Integer> newContent = new LinkedList<Integer>();

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if(this.allowed_symbols_.contains(c)){
                newContent.addLast(new Integer(c));
            }
            else {
                // invalid symbol specified on the tape
                throw new IllegalArgumentException("Invalid tape content : symbol "+c+" does not belong to the tape alphabet");
            }
        }
        this.default_content = content;
        this.tape_representation = newContent;
        this.position_ = 0;
        
    }

    /**
     * Set tape alphabet
     * @param al
     */
    public void alphabetIs(Alphabet al){
        this.allowed_symbols_ = al; 
    }

    /**
     * Returns the alphabet
     * @return
     */
    public Alphabet alphabet(){
        return new Alphabet(this.allowed_symbols_);
    }

    /**
     * Get the default content of the tape
     * @return
     */
    public String defaultContent(){
        return this.default_content;
    }

    /**
     * Reset the head position to the default position
     */
    public void reset(){
        this.position_ = 0;
        this.contentIs(default_content);
    }

    /**
     * Set head position
     * @param position
     * @throws java.lang.IndexOutOfBoundsException
     */
    public void positionIs(int position) throws IndexOutOfBoundsException{
        if(position<0 || position>=tape_representation.size()){
            throw new OutOfBoundsException("Cannot set a position out of the tape");
        }
        this.position_ = position;
    }

    /**
     * Get the position of the head
     * @return
     */
    public int position() {
        return this.position_;
    }

    /**
     * Move forward on the tape (right)
     * @param newChar
     */
    public void moveForward(char newChar){
        this.tape_representation.set(position_, new Integer((int)newChar));
        this.position_++;
        if(this.position_ >= tape_representation.size()){
            tape_representation.addLast(new Integer((int)BLANK));
        }
    }

    /**
     * Move backward on the tape (left)
     * @param newChar
     * @throws core.exception.TapeBoundReachedException
     */
    public void moveBackward(char newChar) throws TapeBoundReachedException{
        this.tape_representation.set(position_, new Integer((int)newChar));
        this.position_--;
        if(this.position_ < 0){
            if(bound_){
                throw new TapeBoundReachedException("Cannot move backward, bound has been reached !");
            }
            tape_representation.addFirst(new Integer((int)BLANK));
            this.position_++;
        }
    }

    /**
     * Get current symbol
     * @return
     */
    public char currentSymbol(){ 
        if(this.position_>=this.tape_representation.size()){
            return BLANK;
        }
        Integer intValue = this.tape_representation.get(position_);
        if(intValue == null){
            return BLANK;
        }
        else{
            return (char)intValue.intValue();
        }
    }
    
    @Override
    public String toString(){
        String ret = "";
        for (Integer symb : tape_representation) {
            ret += (char)symb.intValue();
        }
        return ret;
    }

    /**
     * Returns a String with head position symbolized with []
     * @return
     */
    public String positionString(){
        String res = "";
        for(int i=0;i<position_;i++){
            res += (char)this.tape_representation.get(i).intValue();
        }
        
        res+="["+this.currentSymbol()+"]";

        for(int i= this.position_+1;i<this.tape_representation.size();i++){
            res += (char)this.tape_representation.get(i).intValue();
        }
        return res;
    }
}
