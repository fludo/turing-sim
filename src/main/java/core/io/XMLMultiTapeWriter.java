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

import core.MultiTapeTransition;
import core.MultiTapeTuringMachine;
import core.State;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Ludovic Favre
 */
public class XMLMultiTapeWriter extends XMLWriter {

    private MultiTapeTuringMachine tm_;

    /**
     *
     */
    public XMLMultiTapeWriter() {
    }

    private void checkRep() throws IllegalArgumentException {
        if (this.tm_ == null) {
            throw new IllegalArgumentException("Cannot continue with a null TuringMachine argument to save !");
        }
        if (this.filename_ == null) {
            throw new IllegalArgumentException("Invalid String speficied for filename ! (cannot be null)");
        }
        try {
            this.tm_.checkRep();
        } catch (Exception e) {
            if (debug) {
                log.log(Level.SEVERE, "Invalid Turing Machine settings : " + e.getMessage());
                e.printStackTrace();
            }
            throw new IllegalArgumentException("Cannot save the Turing Machine since its settings are not coherent");
        }
    }

    /**
     * 
     * @param tm
     * @param filename
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public boolean save(MultiTapeTuringMachine tm, String filename) throws Exception {
        this.filename_ = filename;
        this.tm_ = tm;
        checkRep();

        //build the XML tree to write in a file
        Element root = new Element("turing");
        //set attributes for turing section
        root.setAttribute("type", "simple");
        root.setAttribute("name", tm.name());
        root.setAttribute("description", tm.description());

        root.setAttribute("variant", "unbound");

        if (debug) {
            log.log(Level.INFO, "Document root created");
        }
        //set input alphabet
        Element inputAlphabet = new Element("input");
        inputAlphabet.setAttribute("alphabet", tm.alphabet().toString());
        root.addContent(inputAlphabet);
        if (debug) {
            log.log(Level.INFO, "Added input alphabet");
        }
        //set tape alphabet and content
        /*Element tape = new Element("tape");
        tape.setAttribute("alphabet", tm.tapeAlphabet().toString());
        tape.setAttribute("content", tm.tapeDefaultContent());
        root.addContent(tape);
        if(debug){
        log.log(Level.INFO, "Tape content has been set");
        }
         */
        //set states list
        Element states = new Element("states");
        for (Iterator it = tm.states().iterator(); it.hasNext();) {
            State st = (State) it.next();
            states.addContent(buildStateElement(st));
        }
        root.addContent(states);
        if (debug) {
            log.log(Level.INFO, "Added states list");
        }

        //set the start state
        Element start = new Element("starting");
        start.setAttribute("state", tm.startState().label());
        root.addContent(start);
        if (debug) {
            log.log(Level.INFO, "Starting state set as " + tm.startState().label());
        }

        //set the accepting state(s)
        Element accepting = new Element("accepting");
        accepting.setAttribute("states", stateStringFromList(tm.acceptingStates()));
        root.addContent(accepting);
        if (debug) {
            log.log(Level.INFO, "Accepting state(s) set is : " + stateStringFromList(tm.acceptingStates()));
        }

        //set the rejecting state(s)
        Element rejecting = new Element("rejecting");
        rejecting.setAttribute("states", stateStringFromList(tm.rejectingStates()));
        root.addContent(rejecting);
        if (debug) {
            log.log(Level.INFO, "Rejecting state(s) set is : " + stateStringFromList(tm.rejectingStates()));
        }

        Element tapesRoot = new Element("tapes");
        for (Iterator<String> it = tm.tapes().keySet().iterator(); it.hasNext();) {
            //iterate on tape identifiers
            String tapeId = it.next();
            Element tape = new Element("tape");
            tape.setAttribute("indice", tapeId);

            if (debug) {
                log.log(Level.INFO, "Adding new tape : " + tapeId);
            }
            String tapeAlphabet = tm.tapes().get(tapeId).alphabet().toString();
            tape.setAttribute("alphabet", tapeAlphabet);

            String tapeContent = tm.tapes().get(tapeId).toString();
            tape.setAttribute("content", tapeContent);

            //add transitions for this tape
            Element transitionsRoot = new Element("transitions");
            HashSet<MultiTapeTransition> transitionsSet = tm.transitions(tapeId);
            for (Iterator<MultiTapeTransition> ite = transitionsSet.iterator(); ite.hasNext();) {
                if (debug) {
                    log.log(Level.FINEST, "Preparing a new transition to add");
                }
                MultiTapeTransition tr = ite.next();
                Element transi_elem = new Element("transition");
                transi_elem.setAttribute("from_state", tr.currentState().label());
                transi_elem.setAttribute("tapeSymbol", Character.toString(tr.currentChar()));
                transi_elem.setAttribute("to_state", tr.nextState().label());
                transi_elem.setAttribute("new_tapeSymbol", Character.toString(tr.nextChar()));
                switch (tr.direction()) {
                    case LEFT:
                        transi_elem.setAttribute("direction", "L");
                        break;
                    case RIGHT:
                        transi_elem.setAttribute("direction", "R");
                        break;
                    case S:
                        transi_elem.setAttribute("direction", "S");
                        break;
                    case UNSET:
                        if (debug) {
                            log.log(Level.SEVERE, "A transition had an unset direction, defaulting to S");
                        }
                        transi_elem.setAttribute("direction", "S");
                }

                transitionsRoot.addContent(transi_elem);
                if (debug) {
                    log.log(Level.INFO, "Added transition : " + tr.toString());
                }
            }

            tape.addContent(transitionsRoot);
            tapesRoot.addContent(tape);
        }

        root.addContent(tapesRoot);

        if (debug) {
            log.log(Level.INFO, "Added root <tapes>");
        }

        // Output the document, use standard formatter
        XMLOutputter fmt = new XMLOutputter(Format.getPrettyFormat().setIndent("      "));
        Document doc = new Document(root);
        //fmt.output(doc, System.out);
        //saving XML document
        File outputFile = new File(filename_);
        FileOutputStream out = new FileOutputStream(outputFile);
        if (out == null) {
            if (debug) {
                log.log(Level.SEVERE, "FileOutputStream is null !");
            }
        }
        if (doc == null) {
            if (debug) {
                log.log(Level.SEVERE, "Doc is null !");
            }
        }
        fmt.output(doc, out);
        out.flush();
        out.close();
        if (debug) {
            log.log(Level.INFO, "File has been written");
        }
        //return true if success, false else
        return true;
    }
}
