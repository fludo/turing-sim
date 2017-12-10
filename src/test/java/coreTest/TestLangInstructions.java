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

import core.lang.instructions.Add;
import core.lang.instructions.Dec;
import core.lang.instructions.Equ;
import core.lang.instructions.Even;
import core.lang.instructions.ITypes;
import core.lang.instructions.Inc;
import core.lang.instructions.Odd;
import core.lang.instructions.Sub;
import junit.framework.TestCase;

/**
 *
 * @author Ludovic Favre <ludovic.favre@epfl.ch>
 */
public class TestLangInstructions extends TestCase {

    public TestLangInstructions(String name) {
        super(name);
    }

    public void testInc() {
        String[] input = {"11001"};
        ITypes[] types = {ITypes.Integer};
        try {
            Inc inc = new Inc(input, types);
            inc.execute();
            assertTrue(inc.accepts());
            assertEquals("11010", inc.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testInc2() {
        String[] input = {"11001111"};
        ITypes[] types = {ITypes.Integer};
        try {
            Inc inc = new Inc(input, types);
            inc.execute();
            assertTrue(inc.accepts());
            assertEquals("11010000", inc.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testDec() {
        String[] input = {"11010"};
        ITypes[] types = {ITypes.Integer};
        try {
            Dec dec = new Dec(input, types);
            dec.execute();
            assertTrue(dec.accepts());
            assertEquals("11001", dec.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testDec2() {
        String[] input = {"11001111"};
        ITypes[] types = {ITypes.Integer};
        try {
            Dec dec = new Dec(input, types);
            dec.execute();
            assertTrue(dec.accepts());
            assertEquals("11001110", dec.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testEqu() {
        String[] input = {"11001111", "11001111"};
        ITypes[] types = {ITypes.Integer, ITypes.Integer};
        try {
            Equ inc = new Equ(input, types);
            inc.execute();
            assertTrue(inc.accepts());
            assertFalse(inc.rejects());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testEqu2() {
        String[] input = {"11001111", "11001110"};
        ITypes[] types = {ITypes.Integer, ITypes.Integer};
        try {
            Equ inc = new Equ(input, types);
            inc.execute();
            assertFalse(inc.accepts());
            assertTrue(inc.rejects());
        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testOdd() {
        String[] input = {"11001111"};
        ITypes[] types = {ITypes.Integer};
        try {
            Odd odd = new Odd(input, types);
            odd.execute();
            assertTrue(odd.accepts());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testOdd2() {
        String[] input = {"110011110"};
        ITypes[] types = {ITypes.Integer};
        try {
            Odd odd = new Odd(input, types);
            odd.execute();
            assertFalse(odd.accepts());
            assertTrue(odd.rejects());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testEven() {
        String[] input = {"110011110"};
        ITypes[] types = {ITypes.Integer};
        try {
            Even even = new Even(input, types);
            even.execute();
            assertTrue(even.accepts());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testEven2() {
        String[] input = {"1100111101"};
        ITypes[] types = {ITypes.Integer};
        try {
            Even even = new Even(input, types);
            even.execute();
            assertFalse(even.accepts());
            assertTrue(even.rejects());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testAdd() {
        String[] input = {"1001", "1000"};
        ITypes[] types = {ITypes.Integer, ITypes.Integer};
        try {
            Add add = new Add(input, types);
            add.execute();
            assertTrue(add.accepts());
            assertEquals("10001", add.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testAdd2() {
        String[] input = {"1001", "1001"};
        ITypes[] types = {ITypes.Integer, ITypes.Integer};
        try {
            Add add = new Add(input, types);
            add.execute();
            assertTrue(add.accepts());
            assertEquals("10010", add.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testSub() {
        String[] input = {"10011", "11"};
        ITypes[] types = {ITypes.Integer, ITypes.Integer};
        try {
            Sub sub = new Sub(input, types);
            sub.execute();
            assertTrue(sub.accepts());
            assertEquals("10000", sub.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }

    public void testSub2() {
        String[] input = {"1001", "1001"};
        ITypes[] types = {ITypes.Integer, ITypes.Integer};
        try {
            Sub sub = new Sub(input, types);
            sub.execute();
            assertTrue(sub.accepts());
            assertEquals("0", sub.output());

        } catch (Exception e) {
            fail("Inc execution failed :  " + e.getMessage());
        }

    }
}
