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

import core.exception.InvalidTransitionException;
import core.exception.StateNotFoundException;
import core.exception.TapeBoundReachedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import logging.Log;
import org.apache.log4j.*;

/**
 *
 * @author Ludovic Favre
 */
public class MultiTapeRunner {

    private static Logger log = Logger.getLogger(Log.FILENAME);
    private static boolean debug = false; // default disabled
    private MultiTapeTuringMachine tm_;
    private HashSet<String> tapeNames_;
    private HashMap<String, HashSet<MultiTapeTransition>> transitions_;

    public MultiTapeRunner(MultiTapeTuringMachine tm) throws Exception {
        this.tm_ = tm;
        tm.checkRep();
        this.tapeNames_ = new HashSet<String>(this.tm_.tapes().keySet());
        this.transitions_ = new HashMap<String, HashSet<MultiTapeTransition>>();
        for (String string : tapeNames_) {
            this.transitions_.put(string, this.tm_.transitions(string));
        }
        this.reset();
    }

    public void reset() throws Exception {
        //this.tm_.currentStateIs(this.tm_.startState().label());
        this.tm_.init();
    }

    public boolean doStep() throws InvalidTransitionException, StateNotFoundException,TapeBoundReachedException {
        if (debug) {
            log.info("Preparing to go for next step");
        }
        State current = this.tm_.currentState();
        if (debug) {
            log.info("Found current state : " + current.label());
        }


        //LinkedList<Transition> transitions = this.tm_.transitions();
        if (this.transitions_.isEmpty()) {
            log.warn("Transitions set  'transitions_' is empty !");
        }

        State nextState = null; // the next state has to be common to all tapes, otherwhise we have an error in the Turing Machine...
        boolean stateChanged = false;
        //general "preparation" stops here, now each transition has to be computer per-tape
        for (String tapeIndice : this.transitions_.keySet()) {
            Tape tape = this.tm_.tape(tapeIndice);
            HashSet<MultiTapeTransition> tapeTransL = this.transitions_.get(tapeIndice);

            LinkedList<MultiTapeTransition> tapeTrans = new LinkedList<MultiTapeTransition>(tapeTransL);
            
            char currentSymbol = tape.currentSymbol();

            LinkedList<MultiTapeTransition> availableTransitions = filterByCurrentState(tapeTrans, current);

            if (debug) {
                if (availableTransitions.isEmpty()) {
                    log.warn("No transition matching current State");
                } else {
                    log.info("Found : " + availableTransitions.size() + " possible transistions");
                }
            }

            LinkedList<MultiTapeTransition> possibleTransitions = filterByCurrentSymbol(availableTransitions, currentSymbol);

            if (possibleTransitions.isEmpty()) {
            if (debug) {
                log.warn("No transitions matching current State " + current.label() + " with current symbol " + currentSymbol);

            }
            return false;
        //throw new InvalidTransitionException("Cannot find a transition for current state "+current.label()+" : symbol = "+symbol);

        } else if (possibleTransitions.size() > 1) {
            if (debug) {
                log.warn("Multiple match for currentState with current Symbol");
            }
            throw new InvalidTransitionException("Multiple transitions found for current State and Symbol combination in tape "+tapeIndice);
        } else {
            if (debug) {
                log.info("Single/No matching transition found, proceeding...");
            }
            if(possibleTransitions.size() == 0){
                // no transition found, maybe we reached the end of possible transitions ? -> reject
            }
            else{
                stateChanged = true;
                if(nextState==null){ // first time a transition is computed...
                    nextState = possibleTransitions.getFirst().nextState();
                }
                else{
                    // compare by-label since recopy is done
                    if(!nextState.label().equals(possibleTransitions.getFirst().nextState().label())){
                        throw new InvalidTransitionException("The transition found for tape "+tapeIndice+" does not fit the previous found transitions for other tapes");
                    }
                }
                MultiTapeDirection dir = possibleTransitions.getFirst().direction();

                if(dir == MultiTapeDirection.RIGHT){
                    this.tm_.moveForward(tapeIndice, possibleTransitions.getFirst().nextChar());
                }
                else if (dir == MultiTapeDirection.LEFT){
                    this.tm_.moveBackward(tapeIndice, possibleTransitions.getFirst().nextChar());
                }
                else if (dir == MultiTapeDirection.S){
                    //nothing to do
                }
                else {
                    //nothing to do here again
                }
            }


        }

        
    }// end loop, change state if ok

    if(!stateChanged){
        return false;
    }else{
       this.tm_.currentStateIs(nextState.label());
            if (debug) {
                log.info("Changed state successfuly");
            }

            return true;
    }


            
    }

    protected LinkedList<MultiTapeTransition> filterByCurrentState(LinkedList<MultiTapeTransition> fullList, State currentState) {
        LinkedList<MultiTapeTransition> retList = new LinkedList<MultiTapeTransition>();

        for (Iterator it = fullList.iterator(); it.hasNext();) {
            MultiTapeTransition transition = (MultiTapeTransition) it.next();
            if (transition.currentState().label().equals(currentState.label())) {
                if (debug) {
                    log.info("Found a transition matching current State : " + transition.toString());
                }
                retList.add(transition);
            }
        }
        return retList;
    }

    protected LinkedList<MultiTapeTransition> filterByCurrentSymbol(LinkedList<MultiTapeTransition> fullList, char symbol) {
        LinkedList<MultiTapeTransition> retList = new LinkedList<MultiTapeTransition>();

        for (Iterator it = fullList.iterator(); it.hasNext();) {
            MultiTapeTransition transition = (MultiTapeTransition) it.next();
            if (transition.currentChar() == symbol) {
                if (debug) {
                    log.info("Found a transition matching current symbol : " + transition.toString());
                }
                retList.add(transition);
            }
        }
        return retList;
    }
}
