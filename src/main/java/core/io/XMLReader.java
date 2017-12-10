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
import core.Direction;
import core.State;
import core.SimpleTuringMachine;
import core.TuringMachine;
import core.exception.InvalidStateException;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.Log;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Ludovic Favre
 */
public class XMLReader {

    private static Logger log = Logger.getLogger(Log.FILENAME);
    private static boolean debug = false; // default disabled
    private org.jdom.Element root_ = null;
    private SimpleTuringMachine tm_ = null;
    private boolean bound_ = false;

    /**
     *
     */
    public XMLReader() {
        this.tm_ = new SimpleTuringMachine();
    }

    /**
     * 
     * @param filename
     * @return
     * @throws java.lang.Exception
     */
    public TuringMachine read(String filename) throws Exception {


        org.jdom.Document document;
        File tmFile = new File(filename);

        if (tmFile.exists() || tmFile.canRead()) {
            SAXBuilder sxb = new SAXBuilder();
            try {
                // create JDOM document and get its root
                document = sxb.build(tmFile);
                if (debug) {
                    log.log(Level.INFO, filename + " opened without problem");
                }
                this.root_ = document.getRootElement();
                //log.log(Level.INFO, filename+" root element found");

                String tmType = getValueOf("type");
                if (debug) {
                    log.log(Level.INFO, "Turing machine of type : " + tmType);
                }
                if (tmType.equals("multitape")) {
                    log.log(Level.FINE, "Found a multitape turing machine descriptor in xml file");
                    return new XMLMultiTapeReader(this.root_).buildTM();
                }
                if (debug) {
                    log.log(Level.INFO, "Found a simple Turing Machine in xml file");
                    
                }
                this.buildTM();
            } catch (Exception e) {
                log.log(Level.SEVERE, "An error has occured while reading XML file :" + e.getMessage());
                throw e;
            }

        } else {
            log.log(Level.SEVERE, "Cannot read file : " + filename);
            throw new IllegalArgumentException("Cannot read file : " + filename);
        }
        //System.out.println("OUTPUT ALPHABET IS "+this.tm_.alphabet().toString());
        this.tm_.checkRep();
        return this.tm_;
    }

    /**
     *
     * @param tmFile
     * @return
     * @throws java.lang.Exception
     */
    public TuringMachine read(File tmFile) throws Exception {


        org.jdom.Document document;
        String filename = tmFile.getName();

        if (tmFile.exists() || tmFile.canRead()) {
            SAXBuilder sxb = new SAXBuilder();
            try {
                // create JDOM document and get its root
                document = sxb.build(tmFile);
                if (debug) {
                    log.log(Level.INFO, filename + " opened without problem");
                }
                this.root_ = document.getRootElement();
                //log.log(Level.INFO, filename+" root element found");

                String tmType = getValueOf("type");
                if (debug) {
                    log.log(Level.INFO, "Turing machine of type : " + tmType);
                }
                if (tmType.equals("multitape")) {
                    log.log(Level.FINE, "Found a multitape turing machine descriptor in xml file");
                    return new XMLMultiTapeReader(this.root_).buildTM();
                }
                if (debug) {
                    log.log(Level.INFO, "Found a simple Turing Machine in xml file");
                    
                }
                this.buildTM();
            } catch (Exception e) {
                log.log(Level.SEVERE, "An error has occured while reading XML file :" + e.getMessage());
                throw e;
            }

        } else {
            log.log(Level.SEVERE, "Cannot read file : " + filename);
            throw new IllegalArgumentException("Cannot read file : " + filename);
        }
        //System.out.println("OUTPUT ALPHABET IS "+this.tm_.alphabet().toString());
        this.tm_.checkRep();
        return this.tm_;
    }

    private void buildTM() throws Exception {
        String tmType = getValueOf("type");
        if (!tmType.equals("simple")) {
            throw new Exception("Unsupported type of turing machine : " + tmType);
        } else {
            String tmName = getValueOf("name");
            String bound = getValueOf("variant");
            if(bound.equals("bound")){
                this.bound_=true;
            }
            this.tm_.nameIs(tmName);
            String tmDescription = getValueOf("description");
            this.tm_.descriptionIs(tmDescription);
            if (debug) {
                log.log(Level.INFO, "Name found : " + tmName + " and Description : " + tmDescription);
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
                    log.log(Level.INFO, "Added accepting state : " + string);
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
                    log.log(Level.INFO, "Added rejecting state : " + string);
                }
            }

            // read the alphabets (Turing Machine and Its Tape)
            this.tm_.alphabetIs(readAlphabet(this.root_.getChild("input")));
            if (debug) {
                log.log(Level.INFO, "Input Alphabet has been set with success to  " + this.tm_.alphabet().toString());
            }
            this.tm_.tapeAlphabetIs(readAlphabet(this.root_.getChild("tape")));
            if (debug) {
                log.log(Level.INFO, "Tape Alphabet has been set with success to  " + this.tm_.tapeAlphabet().toString());
            }
            readTapeContent(this.root_.getChild("tape"));
            readTransitions(this.root_.getChild("transitions"));
        }

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
                log.log(Level.SEVERE, "Got a null child for Child name : " + name);
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
                log.log(Level.INFO, "Added state with label : " + label);
            }
            try {
                this.tm_.addState(new core.State(label));
            } catch (InvalidStateException e) {
                log.severe(e.getMessage());
            }
        }
    }

    private Alphabet readAlphabet(org.jdom.Element root) throws Exception {
        Alphabet al = new Alphabet();
        String alphabetString = getValueOf(root, "alphabet");
        if (debug) {
            log.log(Level.INFO, "Getting one alphabet String  : " + alphabetString);
        }
        // alphabet symbols are separated by ',' so we just have to split it
        String[] symbols = alphabetString.split(",");
        for (int i = 0; i < symbols.length; i++) {
            al.add(symbols[i].charAt(0));
        }
        return al;
    }

    private void readTapeContent(org.jdom.Element root) throws Exception {
        String tape = getValueOf(root, "content");
        this.tm_.tapeContentIs(tape);
        this.tm_.tapeVariantIsBound(this.bound_);

    }

    private void readTransitions(org.jdom.Element root) throws Exception {
        List<org.jdom.Element> transList = root.getChildren();
        for (Element transition : transList) {
            try {
                String from = getValueOf(transition, "from_state");
                String tapeSymbol = getValueOf(transition, "tapeSymbol");
                String to = getValueOf(transition, "to_state");
                String new_tapeSymbol = getValueOf(transition, "new_tapeSymbol");
                String direction = getValueOf(transition, "direction");
                Direction dir = Direction.UNSET;
                if (direction.charAt(0) == 'L') {
                    dir = Direction.LEFT;
                } else if (direction.charAt(0) == 'R') {
                    dir = Direction.RIGHT;
                } else {
                    if (debug) {
                        log.log(Level.SEVERE, "Failed to determine direction for a transition ! Got : " + direction);
                    }
                }
                if (debug) {
                    log.log(Level.INFO, "Transition added : " + from + " X " + tapeSymbol + " -> " + to + " X " + new_tapeSymbol + " X " + direction);
                }
                this.tm_.addTransition(this.tm_.getState(from), this.tm_.getState(to), tapeSymbol.charAt(0), new_tapeSymbol.charAt(0), dir);

            } catch (Exception e) {
                if (debug) {
                    log.log(Level.SEVERE, "Error while reading a transition : " + e.getMessage());
                }
                throw e;
            }





        }

        if (debug) {
            log.log(Level.INFO, "All transitions have been parsed");
        }
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
