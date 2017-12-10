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

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Ludovic Favre
 *
 * <p>
 * A simple class able to stock the representation of an alphabet.
 * The alphabet can either be the tape input aphabet of the output alphabet
 * </p>
 *
 *
 */
public class Alphabet {
    private HashSet<Integer> alphabet_ ;

    /**
     * Constructor without parameters.
     * The alphabet will be empty.
     */
    public Alphabet(){
        this.alphabet_ = new HashSet<Integer>();
    }

    /**
     * Construct an alphabet using an already existing alphabet.
     * The new alphabet will be built by copying each element of the
     * old alphabet.
     * @param old
     */
    public Alphabet(Alphabet old){
        this.alphabet_ = new HashSet<Integer>();
        for (Integer symb : old.alphabet_) {
            this.add((char)symb.intValue());
        }
    }

    /**
     * Build an alphabet from a String extracted from xml for example (Symbol separated by ,)
     * @param formattedString
     */
    public Alphabet(String formattedString){
        String[] split = formattedString.split(",");
        Alphabet al = new Alphabet();
        for (String string : split) {
            al.add(string.charAt(0));
        }
        this.alphabet_ = al.alphabet_;
    }

    /**
     * Add a symbol to the alphabet.
     * Symbol should be entered as char.
     * @param character
     */
    public void add(char character){
        this.alphabet_.add(new Integer((int)character));
    }

    /**
     * Checks if the alphabet contains the given symbol
     * @param character
     * @return true if it contains the given symbol, false else
     */
    public boolean contains(char character){
        return this.alphabet_.contains(new Integer((int)character));
    }

    /**
     * Remove the given symbol from the alphabet
     * @param character
     */
    public void remove(char character){
        this.alphabet_.remove(new Integer((int)character));
    }

    /**
     * Create a String representation of the alphabet
     * @return a string reprensentation of the alphabet
     */
    @Override
    public String toString(){
        String s = "";
        for (Iterator<Integer> it = alphabet_.iterator(); it.hasNext();) {
            Integer symb = it.next();
            s += (char)symb.intValue();
            if(it.hasNext()) s+= ',';
        }
        return s;
    }

    public String[] toStringArray(){
        String[] array = new String[this.alphabet_.size()];
        int i=0;
        for (Integer character : this.alphabet_) {
            array[i] = ""+((char)character.intValue());
            i++;
        }
        return array;
    }


}
