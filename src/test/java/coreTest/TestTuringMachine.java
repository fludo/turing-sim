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
package coreTest;

import core.State;
import core.SimpleTuringMachine;
import java.util.LinkedList;
import junit.framework.TestCase;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the TuringMachine.java class from core package
 *</p>
 */
public class TestTuringMachine extends TestCase {

    public TestTuringMachine(String name) {
        super(name);
    }

    public TestTuringMachine() {
        super("TuringMachine class test");
    }

    public void testAcceptingSetAndGet() {
        SimpleTuringMachine tm = new SimpleTuringMachine();
        try {
            State s = new State("someLabel");
            State t = new State("anotherLabel");
            tm.addState(s);
            assertTrue("Failed to add state "+s.label()+" to the turing machine", tm.containsState(s));
            assertTrue("Failed to add state "+s.label()+" to the turing machine", tm.containsState(s.label()));
            tm.addState(t);
            assertTrue("Failed to add state "+s.label()+" to the turing machine", tm.containsState(s));
            assertTrue("Failed to add state "+s.label()+" to the turing machine", tm.containsState(s.label()));
            assertTrue("Failed to add state "+t.label()+" to the turing machine", tm.containsState(t));
            assertTrue("Failed to add state "+t.label()+" to the turing machine", tm.containsState(t.label()));
            tm.addAcceptingState("someLabel");
            tm.addAcceptingState("anotherLabel");
            LinkedList<State> list = tm.acceptingStates();
            assertTrue("TuringMachine failed to memorize accepting state : "+s.label(), tm.containsAcceptingState(s.label()));
            assertTrue("TuringMachine failed to memorize accepting state : "+t.label(), tm.containsAcceptingState(t.label()));
        } catch (Exception e) {
            fail("An error occured : " + e.getMessage());
        }

    }
}

