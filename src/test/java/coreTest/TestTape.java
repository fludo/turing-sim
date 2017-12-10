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

import core.Alphabet;
import core.Tape;
import core.exception.TapeBoundReachedException;
import junit.framework.TestCase;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the Tape.java class from core package
 *</p>
 */
public class TestTape extends TestCase {

    private static final char BLANK = '\u2423';

    public TestTape(String name) {
        super(name);
    }

    public TestTape() {
        super("Tape class test");
    }

    public void testToString() {
        Alphabet al = new Alphabet();
        al.add('1');
        al.add('0');
        Tape t = new Tape(false, al);
        String str = "01010011000010100";
        t.contentIs(str);
        assertEquals(str, t.toString());
    }

    public void testSimpleOverFlow() {
        Alphabet al = new Alphabet();
        al.add('1');
        al.add('0');
        Tape t = new Tape(false, al);
        String str = "010";
        t.contentIs(str);
        t.moveForward('1');// tape is no 1>1<0
        t.moveForward('0');// ... 10>0<
        t.moveForward('1');// ... 101>BLANK<
        assertEquals("OverFlow has not triggered BLANK adding", "101" + BLANK, t.toString());
    }

    public void testSimpleUnderFlow() {
        Alphabet al = new Alphabet();
        al.add('1');
        al.add('0');
        Tape t = new Tape(false, al);
        String str = "010";
        t.contentIs(str);
        try {
            t.moveBackward('1');
            t.moveBackward('0');
        } catch (TapeBoundReachedException e) {
            fail(e.getMessage());
        }

        assertEquals("OverFlow has not triggered BLANK adding", BLANK + "0110", t.toString());
    }

    public void testSimpleUnderAndOverFlow() {
        Alphabet al = new Alphabet();
        al.add('1');
        al.add('0');
        Tape t = new Tape(false, al);
        String str = "010";
        t.contentIs(str);
        t.moveForward('1');// tape is no 1>1<0
        t.moveForward('0');// ... 10>0<
        t.moveForward('1');// ... 101>BLANK<
        t.positionIs(0);
        try {
            t.moveBackward('1');
            t.moveBackward('0');
        } catch (TapeBoundReachedException e) {
            fail(e.getMessage());
        }
        assertEquals("OverFlow has not triggered BLANK adding", BLANK + "0101" + BLANK, t.toString());
    }

    public void testMultipleOverFlow() {
        Alphabet al = new Alphabet();
        al.add('1');
        al.add('0');
        Tape t = new Tape(false, al);
        String str = "010";
        t.contentIs(str);
        t.moveForward('1');// tape is no 1>1<0
        t.moveForward('0');// ... 10>0<
        t.moveForward('1');// ... 101>BLANK<
        t.moveForward('1');
        t.moveForward('0');
        t.moveForward('1');
        assertEquals("OverFlow has not triggered BLANK adding", "101101" + BLANK, t.toString());
    }

    public void testMultipleUnderFlow() {
        Alphabet al = new Alphabet();
        al.add('1');
        al.add('0');
        Tape t = new Tape(false, al);
        String str = "010";
        t.contentIs(str);
        try {
            t.moveBackward('0');
            t.moveBackward('0');
            t.moveBackward('0');
            t.moveBackward('1');
        } catch (TapeBoundReachedException e) {
            fail(e.getMessage());
        }

        assertEquals("OverFlow has not triggered BLANK adding", BLANK + "100010", t.toString());
    }
}

