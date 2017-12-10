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
import core.SimpleTapeRunner;
import core.SimpleTuringMachine;
import core.converter.TinyLangUtils;
import core.exception.InvalidTransitionException;
import core.io.XMLReader;
import junit.framework.TestCase;

/**
 *
 * @author Ludovic Favre <ludovic.favre@epfl.ch>
 */
public class TestXmlInstructions extends TestCase {

    public TestXmlInstructions(String name) {
        super(name);
    }

    public void testInc() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/inc.xml");
            tm.tapeContentIs("0#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            runner.reset();
            while (runner.doStep()) {
                //System.out.println("Runner step...");
            }
            assertEquals("1#", tm.tapeString());
            assertTrue(tm.accepting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testInc2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/inc.xml");
            tm.tapeContentIs("101#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            runner.reset();
            while (runner.doStep()) {
            }

            assertEquals("110#", tm.tapeString());
            assertTrue(tm.accepting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testInc3() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/inc.xml");
            tm.tapeContentIs("11001#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }

            assertEquals("11010#", tm.tapeString());
            assertTrue(tm.accepting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testDec() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/dec.xml");
            tm.tapeContentIs("1#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }
            if (tm.accepting()) {
                assertEquals("0#", tm.tapeString().replaceAll("\u2423", ""));
                assertTrue(tm.accepting());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testDec2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/dec.xml");
            tm.tapeContentIs("10010#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }
            if (tm.accepting()) {
                assertEquals("10001#", tm.tapeString().replaceAll("\u2423", ""));
                assertTrue(tm.accepting());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testEven() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/even.xml");
            tm.tapeContentIs("10010#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }
            assertEquals("10010#", tm.tapeString().replaceAll("\u2423", ""));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testEven2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/even.xml");
            tm.tapeContentIs("100101#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }
            if (tm.accepting()) {
                assertEquals("100101#", tm.tapeString());
                assertTrue(tm.rejecting());
                assertFalse(tm.accepting());
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testOdd() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/odd.xml");
            tm.tapeContentIs("100101#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }

            assertEquals("100101#", tm.tapeString());
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testOdd2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/odd.xml");
            tm.tapeContentIs("1001010#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
            }

            assertEquals("1001010#", tm.tapeString());
            assertTrue(tm.rejecting());
            assertFalse(tm.accepting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testCmp() {
        try {
            MultiTapeTuringMachine tm = (MultiTapeTuringMachine) new XMLReader().read("src/test/resources/instructionsources/equ.xml");
            tm.tapeContentIs("0", "1001010#");
            tm.tapeContentIs("1", "1001010#");
            MultiTapeRunner r = new MultiTapeRunner(tm);

            while (r.doStep()) {
            }

            // assertEquals("1001010#", tm.ta);
            //System.out.println("Current state: "+tm.currentState().label());
            //System.out.println(tm.tapeString());
            assertFalse(tm.rejecting());
            assertTrue(tm.accepting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testCmp2() {
        try {
            MultiTapeTuringMachine tm = (MultiTapeTuringMachine) new XMLReader().read("src/test/resources/instructionsources/equ.xml");
            tm.tapeContentIs("0", "10010101#");
            tm.tapeContentIs("1", "10010100#");
            MultiTapeRunner r = new MultiTapeRunner(tm);
            while (r.doStep()) {
            }

            // assertEquals("1001010#", tm.ta);
            assertTrue(tm.rejecting());
            assertFalse(tm.accepting());

        } catch (InvalidTransitionException e1) {
            // nothing, should fail
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAddNoCarry() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S1010X#0101#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.currentState().label()+"\t"+tm.tapePositionString());
            }

            assertEquals("1111", tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", ""));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAddZero() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S00X#00#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.currentState().label()+"\t"+tm.tapePositionString());
            }

            assertEquals("00", tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", ""));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAddOne() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S00X#01#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.currentState().label() + "\t" + tm.tapePositionString());
            }

            assertEquals("01", tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", ""));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAddOne2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S01X#00#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.currentState().label() + "\t" + tm.tapePositionString());
            }

            assertEquals("01", tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", ""));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAddOneToOne() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S01X#01#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.currentState().label() + "\t" + tm.tapePositionString());
            }

            assertEquals("10", tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", ""));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAddOneToTwo() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S011X#01#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }

            assertEquals("100", TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "")));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAdd1() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S0111X#0111#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }

            assertEquals("1110", TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "")));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAdd2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S0101011X#0101011#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }

            assertEquals("1010110", TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "")));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

        public void testAdd3() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S011001X#1#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }

            assertEquals("11010", TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "")));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

                public void testAdd4() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/add.xml");
            tm.tapeContentIs("S0011X#1#");
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }

            assertEquals("100", TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "")));
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testSub() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/sub.xml");
            int x = 10;
            int y = 1;
            String content = "S" + TinyLangUtils.binaryTo10lenght(TinyLangUtils.intToBinary(x)) +
                    "X#" + TinyLangUtils.twosComplement(TinyLangUtils.intToBinary(y)) + "#";
            //System.out.println(content);
            tm.tapeContentIs(content);
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }
            //System.out.println(tm.tapeString());
            int res = TinyLangUtils.binaryToInt(TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "").replaceAll("\u2423", "")));
            //System.out.println(res);
            assertEquals(x - y, res);
            //System.out.println(tm.currentState().label()+"\t"+tm.tapePositionString());
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testSub2() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/sub.xml");
            int x = 110;
            int y = 1;
            String content = "S" + TinyLangUtils.binaryTo10lenght(TinyLangUtils.intToBinary(x)) +
                    "X#" + TinyLangUtils.twosComplement(TinyLangUtils.intToBinary(y)) + "#";
            //System.out.println(content);
            tm.tapeContentIs(content);
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }
            //System.out.println(tm.tapeString());
            int res = TinyLangUtils.binaryToInt(TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "").replaceAll("\u2423", "")));
            //System.out.println(res);
            assertEquals(x - y, res);
            //System.out.println(tm.currentState().label()+"\t"+tm.tapePositionString());
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testSub3() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/sub.xml");
            int x = 119;
            int y = 3;
            String content = "S" + TinyLangUtils.binaryTo10lenght(TinyLangUtils.intToBinary(x)) +
                    "X#" + TinyLangUtils.twosComplement(TinyLangUtils.intToBinary(y)) + "#";
            //System.out.println(content);
            tm.tapeContentIs(content);
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }
            //System.out.println(tm.tapeString());
            int res = TinyLangUtils.binaryToInt(TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "").replaceAll("\u2423", "")));
            //System.out.println(res);
            assertEquals(x - y, res);
            //System.out.println(tm.currentState().label()+"\t"+tm.tapePositionString());
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testSub4() {
        try {
            SimpleTuringMachine tm = (SimpleTuringMachine) new XMLReader().read("src/test/resources/instructionsources/sub.xml");
            int x = 245;
            int y = 24;
            String content = "S" + TinyLangUtils.binaryTo10lenght(TinyLangUtils.intToBinary(x)) +
                    "X#" + TinyLangUtils.twosComplement(TinyLangUtils.intToBinary(y)) + "#";
            //System.out.println(content);
            tm.tapeContentIs(content);
            SimpleTapeRunner runner = new SimpleTapeRunner(tm);
            while (runner.doStep()) {
                //System.out.println(tm.tapePositionString()+"\t"+tm.currentState().label());
            }
            //System.out.println(tm.tapeString());
            int res = TinyLangUtils.binaryToInt(TinyLangUtils.removeLeadindZeros(tm.tapeString().replaceAll("#", "").replaceAll("S", "").replaceAll("X", "").replaceAll("\u2423", "")));
            //System.out.println(res);
            assertEquals(x - y, res);
            //System.out.println(tm.currentState().label()+"\t"+tm.tapePositionString());
            assertTrue(tm.accepting());
            assertFalse(tm.rejecting());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
