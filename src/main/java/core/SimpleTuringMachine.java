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

import core.exception.InvalidStateException;
import core.exception.InvalidTransitionException;
import core.exception.NullAttributeException;
import core.exception.StateNotFoundException;
import core.exception.TapeBoundReachedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;

/**
 * <p>
 * A turing machine is a 7-tuple :
 *  1/ Set of states
 *  2/ Input alphabet not bontaining the blank symbol
 *  3/ Tape alphabet where blank belongs to it and 2/ in inside this alphabet
 *  4/ Transition function : State X Tape Symbol -> State X Tape Symbol X Direction
 *  5/ q0 : start state
 *  6/ qaccept : acceping state
 *  7/ qreject : rejecting state
 * </p>
 * @author Ludovic Favre
 */
public class SimpleTuringMachine extends TuringMachine {

    protected static final char BLANK = '\u2423';
    private Set<Transition> transitions_ = new HashSet<Transition>();
    private Tape tape_ = new Tape(false); //tape alphabet is contained in the tape class
    //private Alphabet alphabet_ = null; //its a subset of tape alphabet without blank symbol
    //containing symbols to write
    protected State start_ = State.undefined;
    protected State current_ = State.undefined;
    protected HashMap<String, State> accepting_ = new HashMap<String, State>();
    protected HashMap<String, State> rejecting_ = new HashMap<String, State>();
    protected HashMap<String, State> states_ = new HashMap<String, State>();

    public SimpleTuringMachine() {
        this.name_ = "";
        this.description_ = "";

    }

    /**
     * Return true if TM is accepting
     * @return
     */
    public boolean accepting(){
        return this.isAcceptingState(this.current_);
    }

    /**
     * Checks if given state is an accepting state
     * @param state
     * @return
     */
    public boolean isAcceptingState(State state){
        return this.accepting_.get(state.label())!=null;
    }

    /**
     * Return true if TM is rejecting
     * @return
     */
    public boolean rejecting(){
        return (!this.accepting()) || this.isRejectingState(this.current_);
    }

    /**
     * Checks if given state is rejecting
     * @param state
     * @return
     */
    public boolean isRejectingState(State state){
        return this.rejecting_.get(state.label())!=null;
    }

    /**
     * Set the alphabet
     * @param al
     */
    public void tapeAlphabetIs(Alphabet al) {
        this.tape_.alphabetIs(al);
    }

    /**
     * return the alphabet
     * @return
     */
    public Alphabet tapeAlphabet() {
        return this.tape_.alphabet();
    }

    /**
     * Set tape content
     * @param content
     */
    public void tapeContentIs(String content) {
        this.tape_.contentIs(content);
    }

    /**
     * Reset tape to its default content
     * @return
     */
    public String tapeDefaultContent() {
        return this.tape_.defaultContent();
    }

    /**
     * Get current symbol
     * @return
     */
    public char currentSymbol() {
        return this.tape_.currentSymbol();
    }

    /**
     * Get integer value for current position
     * @return
     */
    public int currentSymbolPointerValue() {
        return this.tape_.position();
    }

    /**
     * Return a string from the tape content
     * @return
     */
    public String tapeString() {
        return this.tape_.toString();
    }

    /**
     * Return a string from the tape content with head position highlighted by []
     * @return
     */
    public String tapePositionString() {
        return this.tape_.positionString();
    }

    /**
     * Move forward
     * @param newChar
     */
    public void moveForward(char newChar) {
        this.tape_.moveForward(newChar);
    }

    /**
     * Move backward
     * @param newChar
     * @throws core.exception.TapeBoundReachedException
     */
    public void moveBackward(char newChar) throws TapeBoundReachedException {
        this.tape_.moveBackward(newChar);
    }

    /**
     * Set bound value of the tape
     * @param bound
     */
    public void tapeVariantIsBound(boolean bound){
        this.tape_.boundValueIs(bound);
    }

    /**
     * Return true if tape is bound
     * @return
     */
    public boolean tapeVariant(){
        return this.tape_.bound();
    }

    /**
     * Add a state
     * @param state
     * @throws core.exception.InvalidStateException
     */
    public void addState(State state) throws InvalidStateException{
        if(this.states_.get(state.label())!=null){
            throw new InvalidStateException(("State label "+state.label()+" is already used !"));
        }
        else{
            this.states_.put(state.label(), state);
        }  
    }

    /**
     * Remove a state
     * @param state
     * @return
     */
    public boolean removeState(State state) {
        this.states_.remove(state.label());
        return true;
    }


    /**
     * Get a list of all states
     * @return
     */
    public LinkedList<State> states() {
        LinkedList<State> list = new LinkedList<State>();
        Object[] statesArray = this.states_.values().toArray();
        for (int i = 0; i < statesArray.length; i++) {
            State objState = (State) statesArray[i];
            list.add(objState);
        }
        return list;
    }

    /**
     * Checks if TM contains the given state
     * @param state
     * @return
     */
    public boolean containsState(State state) {
        return (this.states_.get(state.label()) != null);
    }

    /**
     * Checks if TM contains the given state
     * @param name
     * @return
     */
    public boolean containsState(String name) {
        return (this.states_.get(name) != null);
    }

    /**
     * Checks if given state is accepting
     * @param label
     * @return
     */
    public boolean containsAcceptingState(String label) {
        return (this.accepting_.get(label) != null);
    }

    /**
     * Checks if given state is rejecting
     * @param label
     * @return
     */
    public boolean containsRejectingState(String label) {
        return (this.rejecting_.get(label) != null);
    }

    /**
     * Set the start state
     * @param state
     * @throws core.exception.StateNotFoundException
     */
    public void startStateIs(State state) throws StateNotFoundException {
        if (!this.containsState(state)) {
            throw new StateNotFoundException("Cannot set state " + state + " as starting state because it has not been declared in state list");
        } else {
            this.start_ = state;
        }
    }

    /**
     * Returns the start state
     * @return
     */
    public State startState() {
        return new State(this.start_.label());
    }

    /**
     * Returns the current state
     * @return
     */
    public State currentState() {
        return new State(this.current_);
    }

    /**
     * Set the current state
     * @param label
     * @throws core.exception.StateNotFoundException
     */
    public void currentStateIs(String label) throws StateNotFoundException {
        this.current_ = this.getState(label);
    }

    /**
     * Add an accepting state
     * @param label
     * @throws java.lang.Exception
     */
    public void addAcceptingState(String label) throws Exception {
        if (this.containsState(label)) {
            this.accepting_.put(label, this.states_.get(label));
            if (debug) {
                log.log(Level.INFO, "Added accepting state : " + this.states_.get(label) + " to the TM");
            }
        } else {
            throw new StateNotFoundException("Cannot add accepting state : " + label + " because it has not been declared in state list !");
        }
    }

    /**
     * Add a rejecting state
     * @param label
     * @throws java.lang.Exception
     */
    public void addRejectingState(String label) throws Exception {
        if (this.containsState(label)) {
            this.rejecting_.put(label, this.states_.get(label));
        } else {
            throw new Exception("Cannot add rejecting state : " + label + " because it has not been declared in state list !");
        }
    }


    /**
     * Returns the list of accepting states
     * @return
     */
    public LinkedList<State> acceptingStates() {
        LinkedList<State> list = new LinkedList<State>();
        Set<String> itSet = this.accepting_.keySet();
        if (debug) {
            if (itSet.isEmpty()) {
                log.log(Level.WARNING, "List of accepting state(s) is empty ! ");
            } else {
                log.log(Level.INFO, "Accepting states list size is : " + itSet.size());
            }
        }
        for (Iterator<String> it = itSet.iterator(); it.hasNext();) {
            String stateLabel = it.next();
            State state = this.accepting_.get(stateLabel);
            list.add(new State(state));
        }
        return list;
    }

    /**
     * Returns the list of rejecting states
     * @return
     */
    public LinkedList<State> rejectingStates() {
        LinkedList<State> list = new LinkedList<State>();
        Set<String> itSet = this.rejecting_.keySet();
        if (debug) {
            if (itSet.isEmpty()) {
                log.log(Level.WARNING, "List of rejecting state(s) is empty ! ");
            } else {
                log.log(Level.INFO, "Rejecting states list size is : " + itSet.size());
            }
        }
        for (Iterator<String> it = itSet.iterator(); it.hasNext();) {
            String stateLabel = it.next();
            State state = this.rejecting_.get(stateLabel);
            list.add(new State(state));
        }
        return list;
    }

    /**
     * Get a state by its label
     * @param stateName
     * @return
     * @throws core.exception.StateNotFoundException
     */
    public State getState(String stateName) throws StateNotFoundException {
        if (containsState(stateName)) {
            return this.states_.get(stateName);
        } else {
            throw new StateNotFoundException("The state \"" + stateName + "\" is not part of the Turing Machine, you have to add it first !\n" +
                    this.states_.toString());
        }
    }

    /**
     * Add a transition
     * @param current
     * @param next
     * @param currentC
     * @param nextC
     * @param dir
     * @throws core.exception.InvalidTransitionException
     */
    public void addTransition(State current, State next, char currentC, char nextC, Direction dir) throws InvalidTransitionException {
        Transition t = new Transition(current, next, currentC, nextC, dir);
        this.addTransition(t);
    }

    /**
     * Add a transition
     * @param t
     * @throws core.exception.InvalidTransitionException
     */
    public void addTransition(Transition t) throws InvalidTransitionException {
        if (!isValid(t)) {
            throw new InvalidTransitionException("A transition with identical 'from State' and 'input symbol' already exists");
        } else {
            this.transitions_.add(t);
        }

    }

    /**
     * Remove a transition
     * @param t
     */
    public void removeTransition(Transition t) {
        // since transitions can be diffents objets, a search is required...
        this.transitions_.remove(findTransition(t));
    }

    /**
     * Find a transition
     * @param t
     * @return
     */
    private Transition findTransition(Transition t) {
        Transition tret = null;
        for (Transition transition : this.transitions_) {
            if (t.currentChar() == transition.currentChar() &&
                    t.currentState().label().equals(transition.currentState().label())) {

                //deeper checking
                if (t.nextChar() == transition.nextChar() &&
                        t.nextState().label().equals(transition.nextState().label())) {
                    tret = transition;
                }


            }
        }

        return tret;
    }

    /**
     * Find a transtion with given state source and given current Symbol
     * @param fromState
     * @param inputSymbol
     * @return
     */
    private HashSet<Transition> findTransitionWith(State fromState, char inputSymbol) {
        HashSet<Transition> ret = new HashSet<Transition>();

        for (Transition transition : this.transitions_) {
            if (inputSymbol == transition.currentChar() &&
                    fromState.label().equals(transition.currentState().label())) {

                ret.add(transition);



            }
        }

        return ret;

    }

    /**
     * Checks if transition is valid
     * @param t
     * @return
     */
    private boolean isValid(Transition t) {
        if (findTransitionWith(t.currentState(), t.currentChar()).size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * List all transitions
     * @return
     */
    public LinkedList<Transition> transitions() {
        LinkedList<core.Transition> list = new LinkedList<core.Transition>();
        for (Iterator<Transition> it = this.transitions_.iterator(); it.hasNext();) {
            Transition tr = it.next();
            list.add(new Transition(tr));
        }
        return list;
    }

    /**
     * Reset the TM
     */
    public void init() {
        this.current_ = this.startState();
        this.tape_.reset();
    }

    @Override
    public String toString() {
        return this.name_;
    }

    /**
     * Checks consistence
     * @throws java.lang.IllegalStateException
     * @throws java.lang.Exception
     */
    public void checkRep() throws IllegalStateException, Exception {
        // first step is to check that all elements have been set
        try {
            this.checkSettings();
            this.checkAllTransitions();
        } catch (NullAttributeException e) {
            if (debug) {
                log.log(Level.SEVERE, "NullAttributeException : " + e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            if (debug) {
                log.log(Level.SEVERE, "Exception in Transitions check : " + e.getMessage());
                throw e;
            }
        }


    }

    /**
     * <p>
     * Checks general variables settings of the Turing Machine class.
     * Will throw an IllegalStateException if the Turing Machine is in
     * a state in which it cannot run correctly.
     * </p>
     * @throws java.lang.IllegalStateException
     */
    private void checkSettings() throws NullAttributeException {
        if (this.alphabet_ == null) {
            throw new NullAttributeException("The TM alphabet is not set ! value is : " + null, "alphabet_");
        }
        if (this.description_ == null) {
            throw new NullAttributeException("The TM description is invalid : " + null, "description_");
        }
        if (this.start_ == null) {
            throw new NullAttributeException("The starting state cannot be null", "start_");
        }
        if (this.start_ == State.undefined) {
            throw new NullAttributeException("The starting state has not been defined", "start_");
        }
        if (this.accepting_ == null) {
            throw new NullAttributeException("Invalid accepting states set", "accepting_");
        }
        if (this.rejecting_ == null) {
            throw new NullAttributeException("Invalid rejecting states set", "rejecting_");
        }
        if (this.alphabet_ == null) {
            throw new NullAttributeException("The TM alphabet is not set ! value is : " + null, "alphabet_");
        }
        if (this.tape_ == null) {
            throw new NullAttributeException("The tape is not correctly set !", "tape_");
        }
        if (this.transitions_ == null) {
            throw new NullAttributeException("Transisitions set is not correctly set !", "transitions_");
        }
    }

    /**
     * Checks transitions
     * @throws java.lang.Exception
     */
    private void checkAllTransitions() throws Exception {
        for (Iterator<Transition> it = transitions_.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (!this.alphabet_.contains(transition.nextChar())) {
                throw new Exception("Invalid output symbol `" + transition.nextChar() + "` for transition (not in the alphabet " +
                        this.alphabet_.toString() + "): " + transition.toString());
            }
            if (!this.tape_.alphabet().contains(transition.currentChar())) {
                throw new Exception("Invalid input symbol `" + transition.currentChar() + "` for transition (not in tape alphabet " +
                        this.tape_.alphabet().toString() + "): " + transition.toString());
            }

        }
    }
}
