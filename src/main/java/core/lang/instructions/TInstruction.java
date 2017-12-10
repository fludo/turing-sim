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
package core.lang.instructions;

import core.exception.InstructionParameterException;

/**
 *
 * @author Ludovic Favre <ludovic.favre@epfl.ch>
 */
public interface TInstruction {

    /**
     * <p> Input size
     * @return
     */
    public int inSize();
    
    /**
     * <p> The output of the "instruction" Turing Block. Can be 0 or 1
     * @return
     */
    public int outSize();

    /**
     * <p> Results of the Turing Block (accept or reject ~ !accept)
     * @return
     */
    public boolean accepts();

    /**
     * <p> Result of the Turing Block (accept or reject)
     * @return
     */
    public boolean rejects();

    /**
     * <p>
     * Set the params of the Turing Block
     * @param input The "raw" input
     * @param types The type of each input
     */
    public void paramsAre(String[] input,ITypes[] types) throws InstructionParameterException;

    /**
     * <p>
     * Returns the parameters of the Turing Block
     * @return
     */
    public String[] params();
    
    /**
     * <p> Returns the ouput of the turing machine. If there is not output,
     * null is returned
     * @return
     */
    public String output();

    /**
     * <p> Executes the instruction (ie run the Turing Machine)
     * @throws java.lang.Exception
     */
    public void execute() throws Exception;

}
