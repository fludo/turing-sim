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

import core.MultiTapeRunner;
import core.MultiTapeTuringMachine;
import core.SimpleTuringMachine;
import core.SimpleTapeRunner;
import core.io.XMLReader;
import junit.framework.TestCase;

import java.io.File;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the TuringRunner.java class from core package
 *</p>
 */
public class TestRunner extends TestCase {

    public TestRunner(String name) {
        super(name);
    }

    public TestRunner() {
        super("TuringRunner.java test file");
    }

    public void testTapeInverterExample() {
        XMLReader reader = new XMLReader();
        SimpleTapeRunner tr = null;
        SimpleTuringMachine tm = null;
        String initialTape = null;
        String finalTape = null;
        try {
            tm = (SimpleTuringMachine) reader.read(
                    new File(getClass().getClassLoader().getResource("examples/demo-inverter.xml").getFile()));
            tm.init();
            //System.out.println("Initial tape value : " + tm.tapeString());
            initialTape = tm.tapeString();
            tr = new SimpleTapeRunner(tm);

        } catch (Exception e) {
            fail("Failed to load the turing machine : " + e.getMessage());
        }
        try {
            //System.out.println("Turing machine loaded");
            //System.out.println("Proceeding...");

            while (tr.doStep()) {
            }

            //System.out.println("Final tape value : " + tm.tapeString());
            finalTape = tm.tapeString();
        } catch (Exception e) {
            //System.out.println("Final tape value : " + tm.tapeString());
            finalTape = tm.tapeString();
        //e.printStackTrace();
        }
        //Since Tape class try to prevent over/under-flows on the tape, blank symbol has been added at the end of the tape !
        assertFalse("First pass is supposed to change tape content !", initialTape.equals(finalTape.substring(0, finalTape.length() - 1)));
        //System.out.println("Final tape value is : "+finalTape);
        assertEquals("Inverted tape content should match final tape content",
                initialTape.replace('0', '2').replace('1', '0').replace('2', '1'), finalTape.substring(0, finalTape.length() - 1));

    }

    public void testMultiTapeTapeInverterExample() {
        XMLReader reader = new XMLReader();
        MultiTapeRunner tr = null;
        MultiTapeTuringMachine tm = null;
        String initialTape = null;
        String finalTape = null;
        try {
            tm = (MultiTapeTuringMachine) reader.read(
                    new File(getClass().getClassLoader().getResource("examples/demo-inverter-multitape.xml").getFile()));
            tm.init();
            initialTape = tm.tapeString();
            tr = new MultiTapeRunner(tm);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to load the turing machine : " + e.getMessage());
        }
        try {
            //System.out.println("Turing machine loaded");
            //System.out.println("Proceeding...");

            while (tr.doStep()) {
                //System.out.println("STEP");
                //System.out.println("-------\n" + tm.tapePositionString());
                //System.out.print("*********\n" + tm.tapeString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("An error occured during the run : "+e.getMessage());
        }
    finalTape = tm.tapeString();
    //System.out.println("Final tape : \n"+finalTape+"\n**********");
    finalTape = removeChar(finalTape, '\u2423');
    //System.out.println("Final tape : \n"+finalTape+"\n++++++++++++");
    finalTape = finalTape.replace('0', '2').replace('1', '0').replace('2', '1');

    assertEquals("Inverted tape content should match final tape content",initialTape,finalTape);
    }

    private static String removeChar(String s, char c) {
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                r += s.charAt(i);
            }
        }
        return r;
    }
}
