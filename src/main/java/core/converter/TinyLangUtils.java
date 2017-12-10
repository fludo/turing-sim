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
package core.converter;

/**
 * <p>
 * Utility class for TinyLangToTM
 * @author Ludovic Favre
 */
public class TinyLangUtils {

    public static String intToBinary(int integer) {
        // java already provides such a method
        String res = Integer.toBinaryString(integer);
        return res;

    }

    public static int binaryToInt(String binaryRepresentation) {
        // same for binary to integer
        int res = Integer.parseInt(binaryRepresentation, 2);
        return res;
    }

    public static String removeLeadindZeros(String binaryString) {
        if (binaryString.startsWith("0")) {
            return binaryString.replaceFirst("([0]+)", "");
        } else {
            return binaryString;
        }
    }

    public static String twosComplement(int i) {
        /*
        // simpler solution from : http://www.velocityreviews.com/forums/t148264-2s-complement-binary-form-of-an-int.html
         * but doesn't work, wrote mine :)
        System.out.println("Complement of  :"+i+" ("+intToBinary(i)+")");
        //String orig;
        StringBuilder sb = new StringBuilder();
        //orig = intToBinary(i);
        for (int j = 0; j < 32; ++j) {
            sb.append('0' + (i & 0x80000000) >>> 31);
            i <<= 1;
        }
        String res = sb.toString();
        System.out.println("is "+res);
        //System.out.println(orig+" becomes "+res+"-->"+removeLeadindZeros(res));
        return res;
         * */
        String value = binaryTo10lenght(intToBinary(i));
        value = value.replaceAll("1", "2").replaceAll("0", "1").replaceAll("2", "0");
        value = intToBinary(binaryToInt(value)+1);
        value = binaryTo10lenght(value);

        return value;
    }

    public static String twosComplement(String binaryString) {
        return twosComplement(binaryToInt(binaryString));
    }

    public static String binaryTo10lenght(String binaryString){
        if(binaryString.length() == 10){
            return binaryString;
        }
        else{
            while(binaryString.length()<10){
                binaryString = "0"+binaryString;
            }
            return binaryString;
        }
    }
}
