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
package core.io;

import core.Alphabet;
import core.MultiTapeTuringMachine;
import core.MultiTapeDirection;
import core.MultiTapeTransition;
import core.State;
import core.Tape;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import logging.Log;
import org.jdom.Element;

/**
 *
 * @author Ludovic Favre
 */
public class XMLMultiTapeReader {

    private static Logger log = Logger.getLogger(Log.FILENAME);
    private static boolean debug = false; // default disabled
    private org.jdom.Element root_ = null;
    private MultiTapeTuringMachine tm_ = null;
    private boolean bound_ = false;

    /**
     * 
     * @param root
     */
    public XMLMultiTapeReader(org.jdom.Element root) {
        this.root_ = root;
        this.tm_ = new MultiTapeTuringMachine();
    }



    /**
     *
     * @return
     * @throws java.lang.Exception
     */
    public MultiTapeTuringMachine buildTM() throws Exception {
        
            String tmName = getValueOf("name");
            String bound = getValueOf("variant");
            if(bound.equalsIgnoreCase("bound")){
                this.bound_ = true;
            }
            this.tm_.nameIs(tmName);
            String tmDescription = getValueOf("description");
            this.tm_.descriptionIs(tmDescription);
            if (debug) {
                log.info("Name found : " + tmName + " and Description : " + tmDescription);
            }

            readStates(this.root_.getChild("states"));

            //get the starting, accepting and rejecting states
            Element startingElmt = this.root_.getChild("starting");
            checkNotNull("starting", startingElmt);
            String startingName = startingElmt.getAttributeValue("state");
            this.tm_.startStateIs(new State(startingName));

            Element acceptingElmt = this.root_.getChild("accepting");
            checkNotNull("accepting", acceptingElmt);
            String accepting = acceptingElmt.getAttributeValue("states");
            LinkedList<String> accList = readStateList(accepting);
            for (Iterator<String> it = accList.iterator(); it.hasNext();) {
                String string = it.next();
                this.tm_.addAcceptingState(string);
                if (debug) {
                    log.info("Added accepting state : " + string);
                }
            }

            Element rejectingElmt = this.root_.getChild("rejecting");
            checkNotNull("rejecting", rejectingElmt);
            String rejecting = rejectingElmt.getAttributeValue("states");
            LinkedList<String> rejList = readStateList(rejecting);
            for (Iterator<String> it = rejList.iterator(); it.hasNext();) {
                String string = it.next();
                this.tm_.addRejectingState(string);
                if (debug) {
                    log.info("Added rejecting state : " + string);
                }
            }

            //get the input alphabet
            Alphabet al = readAlphabet(root_.getChild("input"));
            this.tm_.alphabetIs(al);


            /*
             * Recalle of the XML structure :
             <tapes>
                  <tape indice="0" alphabet="1,0,#" content="1010010101010010101#">
                    <transitions>
                            <transition from_state="q0" tapeSymbol="0" to_state="q1" new_tapeSymbol="1" direction="L"/>
                            <transition from_state="q0" tapeSymbol="1" to_state="q1" new_tapeSymbol="0" direction="L"/>
                            <transition from_state="q1" tapeSymbol="0" to_state="q1" new_tapeSymbol="1" direction="L"/>
                            <transition from_state="q1" tapeSymbol="1" to_state="q1" new_tapeSymbol="0" direction="L"/>
                            <transition from_state="q1" tapeSymbol="#" to_state="q2" new_tapeSymbol="#" direction="L"/>
                            <transition from_state="q0" tapeSymbol="#" to_state="q2" new_tapeSymbol="#" direction="L"/>
                    </transitions>
                  </tape>
                  <tape indice="1" alphabet="1,0,#" content="">
                    <transitions>
                            <transition from_state="q1" tapeSymbol="1" to_state="q1" new_tapeSymbol="0" direction="S"/>
                    </transitions>
                  </tape>
                </tapes>
                */
            Element tapes = root_.getChild("tapes");
            List<Element> tapeList = tapes.getChildren("tape");
            for (Element element : tapeList) {
                String indice = getValueOf(element,"indice");
                String alphabet = getValueOf(element,"alphabet");
                String content = getValueOf(element,"content");

                Tape t = new Tape(this.bound_);

                // set the alphabet
                Alphabet alp = new Alphabet();
                String[] symbols = alphabet.split(",");
                for (int i = 0; i < symbols.length; i++) {
                    alp.add(symbols[i].charAt(0));
                }
                t.alphabetIs(alp);

                // set the content
                t.contentIs(content);

                HashSet<MultiTapeTransition> transitions = new HashSet<MultiTapeTransition>();

                List<Element> transitionList = element.getChild("transitions").getChildren("transition");
                //System.out.println("Number of transitions : "+transitionList.size());
                for (Element element1 : transitionList) {
                String from = getValueOf(element1, "from_state");
                String tapeSymbol = getValueOf(element1, "tapeSymbol");
                String to = getValueOf(element1, "to_state");
                String new_tapeSymbol = getValueOf(element1, "new_tapeSymbol");
                String direction = getValueOf(element1, "direction");
                MultiTapeDirection dir = MultiTapeDirection.UNSET;
                if (direction.charAt(0) == 'L') {
                    dir = MultiTapeDirection.LEFT;
                } else if (direction.charAt(0) == 'R') {
                    dir = MultiTapeDirection.RIGHT;
                } else if (direction.charAt(0) == 'S') {
                    dir = MultiTapeDirection.S;
                }
                else {
                    if (debug) {
                        log.severe("Failed to determine direction for a transition ! Got : " + direction);
                    }
                }
                transitions.add(new MultiTapeTransition(new State(from),new State(to),tapeSymbol.charAt(0),new_tapeSymbol.charAt(0),
                        dir));

                if (debug) {
                    log.info("Transition added : " + from + " X " + tapeSymbol + " -> " + to + " X " + new_tapeSymbol + " X " + direction);
                }
                }

                this.tm_.addTransitionsWithTape(indice, transitions, t);

        }


        return this.tm_;

    }

    private String getValueOf(String name) throws Exception {
        String value = this.root_.getAttributeValue(name);
        if (value == null) {
            throw new Exception("Unable to get attribute value of : " + name);
        }
        return value;
    }

    private String getValueOf(org.jdom.Element root, String name) throws Exception {
        String value = root.getAttributeValue(name);
        if (value == null) {
            throw new Exception("Unable to get attribute value of : " + name);
        }
        return value;
    }

    private void checkNotNull(String name, Element elem) throws Exception {
        if (elem == null) {
            if (debug) {
                log.severe("Got a null child for Child name : " + name);
            }
            throw new Exception("Got a null Element for Child name : " + name);
        }
    }

    private String[] getStateNameList(Element rt, String attribute) throws Exception {

        String raw_string = getValueOf(rt, "states");
        String[] split_array = raw_string.split(",");

        return split_array;
    }

    private void readStates(org.jdom.Element root) {
        List<org.jdom.Element> states = root.getChildren();
        for (Iterator<Element> it = states.iterator(); it.hasNext();) {
            Element element = it.next();
            String label = element.getAttributeValue("label");
            if (debug) {
                log.info("Added state with label : " + label);
            }
            try{
                this.tm_.addState(new core.State(label));
            }
            catch(Exception e){
                log.severe("Failed to add state : "+label+" to the Turing Machine");
            }
            
        }
    }

    private Alphabet readAlphabet(org.jdom.Element root) throws Exception {
        Alphabet al = new Alphabet();
        String alphabetString = getValueOf(root, "alphabet");
        if (debug) {
            log.info("Getting one alphabet String  : " + alphabetString);
        }
        // alphabet symbols are separated by ',' so we just have to split it
        String[] symbols = alphabetString.split(",");
        for (int i = 0; i < symbols.length; i++) {
            al.add(symbols[i].charAt(0));
        }
        return al;
    }



    private void readTapesAndTransitions(org.jdom.Element root) throws Exception{
        /* La forme du document xml est :
         * <tapes>
          <tape indice="0" alphabet="1,0,#" content="1010010101010010101#">
            <transitions>
                    <transition from_state="q0" tapeSymbol="0" to_state="q1" new_tapeSymbol="1" direction="L"/>
                    <transition from_state="q0" tapeSymbol="1" to_state="q1" new_tapeSymbol="0" direction="S"/>
            </transitions>
          </tape>

         */
        org.jdom.Element tapesRoot = root.getChild("tapes");
        LinkedList<org.jdom.Element> tapesList = (LinkedList<org.jdom.Element>)tapesRoot.getChildren("tape");
        if(tapesList == null){
            log.warning("Not child(ren) <tape> found in XML file !");
        }else{
            //for each tape, read its informations and its transitions...
            for (Element element : tapesList) {
                String tapeIdentifier = getValueOf(element, "indice");
                String alphabetString = getValueOf(element, "alphabet");
                String tapeContent = getValueOf(element, "content");
                if(debug){
                    log.info("Got tape datas : indice = "+tapeIdentifier+" | alphabet = "+alphabetString+" | content = "+tapeContent);
                }
                Tape tape = new Tape(bound_);
                //add alphabet to the tape
                Alphabet al = new Alphabet();
                String[] alphabetArray = alphabetString.split(",");
                for (String string : alphabetArray) {
                    if(string.length()!=0){
                        log.severe("Got alphabet symbol : "+string+"\n" +
                                "Alphabet has to be specified using ',' as separator with only 1 character per symbol !");

                    }
                    else{
                        al.add(string.charAt(0));
                        if(debug){
                            log.info("Added symbol :"+string.charAt(0));
                        }
                    }

                }
                tape.alphabetIs(al);
                //alphabet added, add content
                tape.contentIs(tapeContent);
                //tape content added, add transitions...

                LinkedList<Element> transitions = (LinkedList<Element>)element.getChild("transitions").getChildren("transition");
                if(transitions == null){
                    log.warning("No transition found for tape !");
                }
                HashSet<MultiTapeTransition> transitionsSet = new HashSet<MultiTapeTransition>();
                /* Recall of the syntax :
                 *   <transitions>
                 *       <transition from_state="q0" tapeSymbol="0" to_state="q1" new_tapeSymbol="1" direction="L"/>
                 *       <transition from_state="q0" tapeSymbol="1" to_state="q1" new_tapeSymbol="0" direction="S"/>
                 *   </transitions>
                 */
                for (Element element1 : transitions) {
                    String fromStr = element1.getAttributeValue("from_state");
                    State current = this.tm_.state(fromStr);

                    String tapeSymbolStr = element1.getAttributeValue("tapeSymbol");
                    char currentC = tapeSymbolStr.charAt(0);
                    if(tape.alphabet().contains(currentC) || tapeSymbolStr.length()!=1){
                        log.severe("Invalid Symbol specification for tapeSymbol : "+tapeSymbolStr);
                    }

                    String toStr =element1.getAttributeValue("to_state");
                    State next = this.tm_.state(toStr);

                    String newSymbolStr = element1.getAttributeValue("new_tapeSymbol");
                    char nextC = newSymbolStr.charAt(0);
                    if(!this.tm_.alphabet().contains(currentC) || newSymbolStr.length()!=1){
                        log.severe("Invalid Symbol specification for tape Symbol to write : "+newSymbolStr);
                    }

                    String directionStr = element1.getAttributeValue("direction");
                    char direction = directionStr.charAt(0);
                    MultiTapeDirection dir;
                    switch(direction){
                        case 'L':
                            dir = MultiTapeDirection.LEFT;
                            break;
                        case 'R':
                            dir = MultiTapeDirection.RIGHT;
                            break;
                        case 'S':
                            dir = MultiTapeDirection.S;
                            break;
                        default :
                            dir = MultiTapeDirection.UNSET;
                            break;
                    }
                    MultiTapeTransition t = new MultiTapeTransition(current, next, currentC, nextC, dir);
                    transitionsSet.add(t);
                } //end of all transitions for one tape


                this.tm_.addTransitionsWithTape(tapeIdentifier, transitionsSet, tape);
            } //end of all tapes datas/infos
        
            
        }//end of child <tapes ..>
    }





    private LinkedList<String> readStateList(String stateString) {
        LinkedList<String> list = new LinkedList<String>();
        String[] states = stateString.split(",");
        for (int i = 0; i < states.length; i++) {
            String string = states[i];
            list.add(string);
        }

        return list;
    }
}
