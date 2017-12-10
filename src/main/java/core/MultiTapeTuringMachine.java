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
import core.exception.InvalidTuringMachineSettings;
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
 * MultiTape Turing Machine class to allow the emulation of this evolved kind
 * of Turing Machine.
 * </p>
 * @author Ludovic Favre
 */
public class MultiTapeTuringMachine extends TuringMachine{


    //multitape : each tape should have its own transitions. Each tape has an identifier !
    private HashMap<String,HashSet<MultiTapeTransition>> transitions_ = new HashMap<String,HashSet<MultiTapeTransition>>();
    private HashMap<String,Tape> tapes_ = new HashMap<String,Tape>(); //tape alphabet is contained in the tape class
    
    
    protected State start_ = State.undefined;
    protected State current_ = State.undefined;
    protected HashMap<String,State> accepting_ = new HashMap<String,State>();
    protected HashMap<String,State> rejecting_ = new HashMap<String,State>();
    protected HashMap<String,State> states_ = new HashMap<String,State>();


    public MultiTapeTuringMachine(){
        this.name_ = "";
        this.description_ = "";
    }


    /**
     * Initialise the Turing Machine to its start state and reste the tape
     */
    public void init(){
        this.current_ = this.start_;
        for (Tape t : this.tapes_.values()) {
            t.reset();
        }
    }

    /**
     * Return if the TM is accepting or not
     * @return
     */
    public boolean accepting(){
        return this.isAcceptingState(this.current_);
    }

    /**
     * Checks if the given state is accepting
     * @param state
     * @return
     */
    public boolean isAcceptingState(State state){
        return this.accepting_.get(state.label())!=null;
    }

    /**
     * Returns if TM is rejecting
     * @return
     */
    public boolean rejecting(){
        
        return this.isRejectingState(this.current_) || (!this.accepting());
    }

    /**
     * Checks if given state is rejecting
     * @param state
     * @return
     */
    public boolean isRejectingState(State state){
        return this.rejecting_.get(state.label())!=null;
    }

    @Override
    public void alphabetIs(Alphabet al){
        this.alphabet_ = al;
    }

    @Override
    public Alphabet alphabet(){
        return new Alphabet(this.alphabet_);
    }

    /**
     * Add a state
     * @param state
     * @throws core.exception.InvalidStateException
     */
    public void addState(State state) throws InvalidStateException{ 
        if(this.states_.get(state.label())!=null){
            throw new InvalidStateException("State "+state.label()+" already exists !");
        }
        else{
            this.states_.put(state.label(),state);
        }
    }

    /**
     * Remove a state
     * @param state
     * @return
     */
    public boolean removeState(State state){
        this.states_.remove(state.label());
        return true;
    }

    /**
     * Remove a tape according to its identifier
     * @param identifier
     */
    public void removeTape(String identifier){
        this.tapes_.remove(identifier);
        this.transitions_.remove(identifier);
    }


    //may return null
    /**
     * Return a state (copy) from this TM
     * @param label
     * @return
     */
    public State state(String label){
        return this.states_.get(label)==null ? null : new State(label);
    }

    /**
     * Returns all states
     * @return
     */
    public LinkedList<State> states(){
        LinkedList<State> list = new LinkedList<State>();
        Object[]  statesArray = this.states_.values().toArray();
        for (int i = 0; i < statesArray.length; i++) {
            State objState = (State)statesArray[i];
            list.add(objState);
        }
        return list;
    }

    /**
     * Checks if TM contains a given state
     * @param state
     * @return
     */
    public boolean containsState(State state){
        return (this.states_.get(state.label())!= null);
    }

    /**
     * Checks if TM contains a given state identified by its label
     * @param name
     * @return
     */
    public boolean containsState(String name){
        return (this.states_.get(name)!=null);
    }

    /**
     * Checks if given label refere an accepting state
     * @param label
     * @return
     */
    public boolean containsAcceptingState(String label){
        return (this.accepting_.get(label) != null);
    }

    /**
     * Checks if given label refere a rejecting state
     * @param label
     * @return
     */
    public boolean containsRejectingState(String label){
        return (this.rejecting_.get(label) != null);
    }

    /**
     * Set the state
     * @param state
     * @throws core.exception.StateNotFoundException
     */
    public void startStateIs(State state) throws StateNotFoundException{
        if(!this.containsState(state)){
            throw new StateNotFoundException("Cannot set state "+state+" as starting state because it has not been declared in state list");
        }else{
            this.start_ = state;
        }
    }

    /**
     * Return the start start
     * @return
     */
    public State startState() {
        return new State(this.start_.label());
    }

    /**
     * Return the current state
     * @return
     */
    public State currentState(){
        return new State(this.current_);
    }

    /**
     * Set the current State
     * @param label
     * @throws core.exception.StateNotFoundException
     */
    public void currentStateIs(String label) throws StateNotFoundException{
        this.current_ = this.getState(label);
    }

    /**
     * Add a state as accepting
     * @param label
     * @throws java.lang.Exception
     */
    public void addAcceptingState(String label) throws Exception{
        if(this.containsState(label)){
            this.accepting_.put(label, this.states_.get(label));
            if(debug){
                log.log(Level.INFO, "Added accepting state : "+this.states_.get(label)+" to the TM");
            }
        }
        else{
            throw new StateNotFoundException("Cannot add accepting state : "+label+" because it has not been declared in state list !");
        }
    }

    /**
     * Add a state as rejectings
     * @param label
     * @throws java.lang.Exception
     */
    public void addRejectingState(String label) throws Exception{
    if(this.containsState(label)){
            this.rejecting_.put(label, this.states_.get(label));
        }
        else{
            throw new Exception("Cannot add rejecting state : "+label+" because it has not been declared in state list !");
        }
    }

    /**
     * List all acceptings states
     * @return
     */
    public LinkedList<State> acceptingStates(){
        LinkedList<State> list = new LinkedList<State>();
        Set<String> itSet = this.accepting_.keySet();
        if(debug){
            if(itSet.isEmpty()){
                log.log(Level.WARNING, "List of accepting state(s) is empty ! ");
            }
            else{
                log.log(Level.INFO,"Accepting states list size is : "+itSet.size());
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
     * List all rejectings states
     * @return
     */
    public LinkedList<State> rejectingStates(){
        LinkedList<State> list = new LinkedList<State>();
        Set<String> itSet = this.rejecting_.keySet();
        if(debug){
            if(itSet.isEmpty()){
                log.log(Level.WARNING, "List of rejecting state(s) is empty ! ");
            }
            else{
                log.log(Level.INFO,"Rejecting states list size is : "+itSet.size());
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
     * Get a state by name
     * @param stateName
     * @return
     * @throws core.exception.StateNotFoundException
     */
    public State getState(String stateName) throws StateNotFoundException{
        if(containsState(stateName)){
            return this.states_.get(stateName);
        } else {
            throw new StateNotFoundException("The state \""+stateName+"\" is not part of the Turing Machine, you have to add it first !\n"+
                    this.states_.toString());
        }
    }

    /**
     * Add transitions with a tape
     * @param identifier
     * @param transitions
     * @param tape
     * @throws core.exception.InvalidTuringMachineSettings
     */
    public void addTransitionsWithTape(String identifier, HashSet<MultiTapeTransition> transitions, Tape tape) throws InvalidTuringMachineSettings{
        this.transitions_.put(identifier, transitions);
        this.tapes_.put(identifier, tape);
        if(this.transitions_.size() != this.tapes_.size()){
            throw new InvalidTuringMachineSettings("Transition set size doesn't match the tape sets size !");
        }
    }

    /**
     * Add a transition to a tape
     * @param identifier
     * @param transition
     * @throws core.exception.InvalidTransitionException
     */
    public void addTransitionToTape(String identifier, MultiTapeTransition transition) throws InvalidTransitionException{
        if(findTransition(identifier, transition)!=null){
            throw new InvalidTransitionException("Similar current symbol + current state transition already exists, cannot add it ! ");
        }
        this.transitions_.get(identifier).add(transition);
    }
    
    /**
     * Remove the given transition for the given tape. If it doesn't exist, it is just not removed.
     * @param tapeIdentifier
     * @param transition
     */
    public void removeTransition(String tapeIdentifier, MultiTapeTransition transition){
        this.transitions_.get(tapeIdentifier).remove(findTransition(tapeIdentifier, transition));
    }

    /**
     * Find a transition for a particular tape. Tape are compared by each of their attributes.
     * @param tapeIdentifier
     * @param t
     * @return MultiTapeTransition
     */
    private MultiTapeTransition findTransition(String tapeIdentifier,MultiTapeTransition t) {
        MultiTapeTransition tret = null;
        for (MultiTapeTransition transition : this.transitions_.get(tapeIdentifier)) {
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
     * Number of transitions set (on set for each tape)
     * @return
     */
    public int transitionsSetSize(){
        return this.transitions_.size();
    }

    /**
     * Retrieve the number of tape in this turing machine
     * @return
     */
    public int tapesSetSize(){
        return this.tapes_.size();
    }

    /**
     * Move the head on the given tape forward
     * @param identifier
     * @param newChar
     */
    public void moveForward(String identifier,char newChar){
        this.tapes_.get(identifier).moveForward(newChar);
    }

    /**
     * Move the head on the given tape backward
     * @param identifier
     * @param newChar
     */
    public void moveBackward(String identifier,char newChar) throws TapeBoundReachedException{
        this.tapes_.get(identifier).moveBackward(newChar);
    }
    

    /**
     * Return a String to allow a global vision of all tapes
     * @return A concatenation of all tape Strings
     */
    public String tapeString(){
        String ret = "";
        for (Tape tape : this.tapes_.values()) {
            ret += "\n"+tape.toString();
        }
        ret = ret.substring(1);
        return ret;
    }


    /**
     * Returns all tapes with they corresponding indice / name
     * @return
     */
    public HashMap<String,Tape> tapes(){
        return this.tapes_; 
    }

    /**
     * Returns one big string representing all tapes state
     * @return
     */
    public String tapePositionString(){
        String ret ="";
        for(Tape tape: this.tapes_.values()){
            ret+= "\n"+tape.positionString();
        }
        ret = ret.substring(1);
        return ret;
    }

    /**
     * Returns the tape which indice is tapeIndice
     * @param tapeIndice
     * @return
     */
    public Tape tape(String tapeIndice){
        return this.tapes_.get(tapeIndice);
    }

    /**
     * <p> Set tape content with tapeIndice to "content"
     * @param tapeIndice
     * @param content
     */
    public void tapeContentIs(String tapeIndice, String content) throws IllegalArgumentException{
        Tape t = this.tapes_.get(tapeIndice);
        t.contentIs(content);
        this.tapes_.put(tapeIndice, t);
    }

    /**
     * Return all transition for the given tape
     * @param tapeIndice
     * @return
     */
    public HashSet<MultiTapeTransition> transitions(String tapeIndice){
        return this.transitions_.get(tapeIndice);
    }

    
    /**
     * Check MultiTape Turing Machine consistencies.
     * This check should guarantee that the turing machine is usable by
     * other classes like XML writer and the Runner.
     * @throws java.lang.IllegalStateException
     * @throws java.lang.Exception
     */
    public void checkRep() throws IllegalStateException, Exception {
        // first step is to check that all elements have been set
        if (this.transitions_.size() != this.tapes_.size()) {
            throw new InvalidTuringMachineSettings("Transition set size has to match the number of tapes !");
        }
        this.checkSettings();
        this.checkAllTransitions();

    }

    /**
     * <p>
     * Checks general variables settings of the Turing Machine class.
     * Will throw an IllegalStateException if the Turing Machine is in
     * a state in which it cannot run correctly.
     * </p>
     * @throws java.lang.IllegalStateException
     */
    private void checkSettings() throws NullAttributeException,InvalidTuringMachineSettings {
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
        if (this.tapes_ == null) {
            throw new NullAttributeException("The tape is not correctly set !", "tape_");
        }
        if (this.transitions_ == null) {
            throw new NullAttributeException("Transisitions set is not correctly set !", "transitions_");
        }
        if (this.transitions_.size() != this.tapes_.size()){
            throw new InvalidTuringMachineSettings("Transitions set size doesn't match the tape set size !");
        }
    }

    private void checkAllTransitions() throws Exception {
    /*    for (int i = 0; i < transitions_.size(); i++) {
            Tape t = tapes_.get(i);
            HashSet<MultiTapeTransition> trans = transitions_.get(i);

            for (Iterator<MultiTapeTransition> it = trans.iterator(); it.hasNext();) {
                MultiTapeTransition transition = it.next();
                if (!this.alphabet_.contains(transition.nextChar())) {
                    throw new Exception("Invalid output symbol for transition (not in the alphabet): " + transition.toString());
                }
                if (t.alphabet().contains(transition.currentChar())) {
                    throw new Exception("Invalid input symbol for transition (not in tape alphabet): " + transition.toString());
                }

            }

        } */
    }

    
}
