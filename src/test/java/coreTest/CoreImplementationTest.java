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

import junit.framework.*;

/**
 * ImplementationTest is a test suite used to encapsulate all
 * tests.
 * @author Ludovic Favre
 *
 */
public final class CoreImplementationTest extends TestSuite
{
    public static Test suite() { return new CoreImplementationTest(); }
    public CoreImplementationTest() { this("Turing Machine Core Tests"); }
    public CoreImplementationTest(String s)
    {
        super(s);
        addTestSuite(TestAlphabet.class);
        addTestSuite(TestXMLReader.class);
        addTestSuite(TestTape.class);
        addTestSuite(TestState.class);
        addTestSuite(TestXMLReaderAndWriter.class);
        addTestSuite(TestTuringMachine.class);
        addTestSuite(TestRunner.class);
        addTestSuite(TestMultiTapeXMLReader.class);
        addTestSuite(TestXMLMultiTapeReaderAndWriter.class);
        addTestSuite(TestXmlInstructions.class);
        addTestSuite(TestLangInstructions.class);
        
    }
}
