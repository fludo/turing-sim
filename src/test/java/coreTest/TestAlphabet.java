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
import junit.framework.TestCase;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the Alphabet.java class from core package
 *</p>
 */
public class TestAlphabet extends TestCase{

        public TestAlphabet(String name){
                super(name);
        }

        public TestAlphabet(){
                super("Alphabet class test");
        }

        public void testContains(){
            Alphabet a = new Alphabet();
            a.add('x');
            a.add('y');
            assertEquals("Expecting x to belongs to the alphabet", true, a.contains('x'));
            assertEquals("Expecting z not belonging to the alphabet at this stage",false, a.contains('a'));
            a.add('z');
            assertEquals("Expecting z to belong to the alphabet now",true, a.contains('z'));
        }

        public void testRemove(){

            Alphabet a = new Alphabet();
            a.add('x');
            a.add('y');
            assertEquals(true, a.contains('x'));
            assertEquals(true, a.contains('y'));
            a.remove('x');
            assertEquals("X shouldn't be in the alphabet because it has been removed",false, a.contains('x'));
            a.add('x');
            a.add('x');
            a.remove('x');
            assertEquals("X shouldn't be in the alphabet because it has been removed",false, a.contains('x'));
        }

        public void testDupplication(){
            Alphabet a = new Alphabet();
            Alphabet b = new Alphabet();

            a.add('x');
            b.add('y');
            assertFalse("a and b shouldn't be the same",a.equals(b));
            b = new Alphabet(a);
            assertFalse("b is a copy of a but shouldn't be the same",a.equals(b));
            assertTrue("Alphabet a dupplication has not been made correctly", b.contains('x'));
        }



}

