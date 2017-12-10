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
import java.util.Iterator;
import java.util.LinkedList;

import logging.Log;
import org.apache.log4j.*;

/**
 *
 * @author Ludovic Favre
 */
public class SimpleTapeRunner {

    private static Logger log = Logger.getLogger(Log.FILENAME);
    private static boolean debug = false; // default disabled
    private SimpleTuringMachine tm_;

    private class TuringRunnerThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try{
                    doStep();
                }
                catch(InvalidTransitionException e1){
                    e1.printStackTrace();
                }
                catch(StateNotFoundException e2){
                    e2.printStackTrace();
                }
                catch(TapeBoundReachedException e3){
                    e3.printStackTrace();
                }
            }
        }
    }

    public SimpleTapeRunner(SimpleTuringMachine tm) throws Exception {
        this.tm_ = tm;
        tm.checkRep();
        this.reset();
    }

    public void reset() throws Exception {
        //this.tm_.currentStateIs(this.tm_.startState().label());
        this.tm_.init();
    }

    public boolean doStep() throws InvalidTransitionException,StateNotFoundException,TapeBoundReachedException {
        if (debug) {
            log.log(Level.INFO, "Preparing to go for next step");
        }
        State current = this.tm_.currentState();
        if (debug) {
            log.log(Level.INFO, "Found current state : " + current.label());
        }

        LinkedList<Transition> transitions = this.tm_.transitions();
        if (transitions.isEmpty()) {
            log.warn("Transitions set is empty !");
        }

        LinkedList<Transition> availableTransitions = filterByCurrentState(transitions, current);

        if (debug) {
            if (availableTransitions.isEmpty()) {
                log.warn("No transition matching current State");
            } else {
                log.log(Level.INFO, "Found : " + availableTransitions.size() + " possible transistions");
            }
        }

        char symbol = this.tm_.currentSymbol();
        //for (Transition transition : availableTransitions) {
        //    System.out.println(transition);
        //}
        LinkedList<Transition> possibleTransitions = filterByCurrentSymbol(availableTransitions, symbol);

        if (possibleTransitions.isEmpty()) {
            if (debug) {
                log.warn("No transitions matching current State "+current.label()+" with current symbol "+symbol );

            }
            return false;
            //throw new InvalidTransitionException("Cannot find a transition for current state "+current.label()+" : symbol = "+symbol);

        } else if (possibleTransitions.size() != 1) {
            if (debug) {
                log.warn("Multiple match for currentState with current Symbol");
            }
            throw new InvalidTransitionException("Multiple transitions found for current State and Symbol combination");
        } else {
            if (debug) {
                log.log(Level.INFO, "Single matching transition found, proceeding...");
            }
            if(possibleTransitions.getFirst().direction()==Direction.RIGHT){
                this.tm_.moveForward(possibleTransitions.getFirst().nextChar());
            }
            else{
                this.tm_.moveBackward(possibleTransitions.getFirst().nextChar());
            }
            

            this.tm_.currentStateIs(possibleTransitions.getFirst().nextState().label());
            if(debug){
                log.info("Changed state successfuly");
            }

            return true;

        }

    }

    protected LinkedList<Transition> filterByCurrentState(LinkedList<Transition> fullList, State currentState) {
        LinkedList<Transition> retList = new LinkedList<Transition>();

        for (Iterator it = fullList.iterator(); it.hasNext();) {
            Transition transition = (Transition) it.next();
            if (transition.currentState().label().equals(currentState.label())) {
                if (debug) {
                    log.log(Level.INFO, "Found a transition matching current State : " + transition.toString());
                }
                retList.add(transition);
            }
        }
        return retList;
    }

    protected LinkedList<Transition> filterByCurrentSymbol(LinkedList<Transition> fullList, char symbol) {
        LinkedList<Transition> retList = new LinkedList<Transition>();

        for (Iterator it = fullList.iterator(); it.hasNext();) {
            Transition transition = (Transition) it.next();
            if (transition.currentChar() == symbol) {
                if (debug) {
                    log.log(Level.INFO, "Found a transition matching current symbol : " + transition.toString());
                }
                retList.add(transition);
            }
        }
        return retList;
    }
}
