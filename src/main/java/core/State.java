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

/**
 * <p>
 * The State class purpose is to give an abstract representation of States in
 * a Turing Machine so that is is as intuitive as possible.
 * Note that States are identified by a label.
 * The reserved label "undefined" shouldn't be used as the undefined state
 * is provided by a static variable in the class
 * </p>
 * @author Ludovic Favre
 */
public class State implements Comparable<State>{
    public static State undefined = new State("undefined");
    /* Label for this state */
    private String label_ = "";
    /* Transition going from this state */
    //private Set<Transition> transitions = new HashSet<Transition>(); not needed ? so remove

    /**
     * Constructor
     * @param label The label the State will carry
     */
    public State(String label){
        if(label.equals("undefined")){
            // because undefined label is reserved
            this.label_ = "undefined_1";
        }
        else{
            this.label_ = label;
        }

    }

    /**
     * Constructor for a state using an already using State
     * @param state The state to "copy"
     */
    public State(State state){
        this.label_ = state.label_;
    }

    /**
     * Return the label of the current state
     * @return label of current state
     */
    public String label(){
        return this.label_;
    }

    /**
     * Return the current state label
     * @return label
     */
    @Override
    public String toString(){
        return this.label_;
    }

    /**
     * Required method to allow comparison between states
     * @param that The state to compare with
     * @return integer corresponding to label comparison
     */
    @Override
    public int compareTo(State that){
        return this.label_.compareTo(that.label_);
    }

}
