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
import core.MultiTapeTuringMachine;
import core.io.XMLReader;
import junit.framework.TestCase;

import java.io.File;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the MultiTape reading classes
 *</p>
 */
public class TestMultiTapeXMLReader extends TestCase{

        public TestMultiTapeXMLReader(String name){
                super(name);
        }

        public TestMultiTapeXMLReader(){
                super("XMLReader class test");
        }

        public void testParsingMultiTape(){
            XMLReader rd = new XMLReader();
            try{
                MultiTapeTuringMachine tm = (MultiTapeTuringMachine)rd.read(
                        new File(getClass().getClassLoader().getResource("examples/demo-multitape.xml").getFile()));
                assertNotNull(tm);
            }
            catch(Exception e){
                fail("An error occured during xml-read because TuringMachine returned is null:"+e.getMessage()+"\n");
                e.printStackTrace();
            }

        }

}

