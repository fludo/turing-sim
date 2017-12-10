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
import core.io.XMLWriter;
import junit.framework.TestCase;

import java.io.File;

/**
 * @author Ludovic Favre
 *<p>
 * A JUnit test case to test behavior of the Alphabet.java class from core package
 *</p>
 */
public class TestXMLMultiTapeReaderAndWriter extends TestCase{

        public TestXMLMultiTapeReaderAndWriter(String name){
                super(name);
        }

        public TestXMLMultiTapeReaderAndWriter(){
                super("XMLReader and XMLWriter class test");
        }

        public void testMultiTapeWriteAfterRead(){
            XMLReader rd = new XMLReader();
            try{
                MultiTapeTuringMachine tm = (MultiTapeTuringMachine)rd.read(
                        new File(getClass().getClassLoader().getResource("examples/demo-multitape.xml").getFile()));
                XMLWriter wr = new XMLWriter();
                wr.save(tm, "demo-multitape_saved.xml");
            }
            catch(Exception e){
                e.printStackTrace();
                fail("An error occured during xml-read :"+e.getMessage()+"\n"+e.getStackTrace().toString());
                e.printStackTrace();

            }

        }

}

