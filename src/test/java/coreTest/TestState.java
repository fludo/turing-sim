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
import junit.framework.TestCase;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the State.java class from core package
 *</p>
 */
public class TestState extends TestCase{

        public TestState(String name){
                super(name);
        }

        public TestState(){
                super("State class test");
        }

        public void testToString(){
            State s = new State("testing");
            assertEquals("testing", s.toString());
            assertEquals("testing", s.label());
            String str = s.label();
            str = "something else";
            assertEquals("testing", s.label());
            assertEquals("testing", s.toString());
        }



}

