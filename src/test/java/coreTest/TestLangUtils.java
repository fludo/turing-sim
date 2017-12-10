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
import core.converter.TinyLangUtils;

/**
 *<p>
 * Tests for the TinylangUtils class
 * </p>
 * @author Ludovic Favre
 */
public class TestLangUtils extends TestCase {

    public TestLangUtils(String name) {
        super(name);
    }

    public void testIntToBinary() {
        assertEquals("1010", TinyLangUtils.intToBinary(10));
        assertEquals("100010", TinyLangUtils.intToBinary(34));
        assertEquals("101011100", TinyLangUtils.intToBinary(348));
        assertEquals("11000001001001100", TinyLangUtils.intToBinary(98892));
    }

    public void testBinaryToInt() {
        assertEquals(10, TinyLangUtils.binaryToInt("1010"));
        assertEquals(34, TinyLangUtils.binaryToInt("100010"));
        assertEquals(348, TinyLangUtils.binaryToInt("101011100"));
        assertEquals(98892, TinyLangUtils.binaryToInt("11000001001001100"));
    }

    public void testRemoveLeadingZeros() {
        assertEquals("100", TinyLangUtils.removeLeadindZeros("000100"));
        assertEquals("100", TinyLangUtils.removeLeadindZeros("100"));
        assertEquals("1010", TinyLangUtils.removeLeadindZeros("01010"));
        assertEquals("1011100", TinyLangUtils.removeLeadindZeros("000001011100"));
        assertEquals("101", TinyLangUtils.removeLeadindZeros("000000000000101"));

    }

    public void testBinaryTo32Length() {
        assertEquals("0000000001", TinyLangUtils.binaryTo10lenght("1"));
        assertEquals("0000000001", TinyLangUtils.binaryTo10lenght("0001"));
        assertEquals("0000000101", TinyLangUtils.binaryTo10lenght("101"));
        assertEquals("0000000101", TinyLangUtils.binaryTo10lenght("0000000101"));
        assertEquals("1000000101", TinyLangUtils.binaryTo10lenght("1000000101"));
    }

    public void testTwosComplement() {
        assertEquals("1111111100", TinyLangUtils.removeLeadindZeros(TinyLangUtils.twosComplement("100")));
        assertEquals("1111111101", TinyLangUtils.twosComplement(TinyLangUtils.binaryTo10lenght("0000000011")));
        assertEquals("1110011100", TinyLangUtils.twosComplement("01100100"));
    }
}
