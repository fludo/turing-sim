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
public class Init implements TInstruction{
    private String[] input_;

    /**
     *
     * @param input {variableName, intValue}
     * @param types {ITypes.Variable, ITypes.Integer}
     */
    public Init(String[] input,ITypes[] types) throws InstructionParameterException{
        this.paramsAre(input, types);
       
    }
    @Override
    public boolean accepts() {
        return true;
    }

        @Override
    public boolean rejects(){
        return false;
    }

    @Override
    public int inSize() {
        return 2;
    }

    @Override
    public int outSize() {
        return 0;
    }

    @Override
    public String output() {
        return null;
    }

    public String variable(){
        return this.input_[0];
    }

    public String value(){
        return this.input_[1];
    }

    @Override
    public String[] params() {
        return input_;
    }

    @Override
    public void paramsAre(String[] input, ITypes[] types) throws InstructionParameterException{
        if(types[0]!=ITypes.Variable || types[1]!=ITypes.Integer){
            throw new InstructionParameterException("Variable + Integer expected !");
        }
         this.input_ = input;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

        @Override
    public void execute() throws Exception {
            //nothing to do
    }

}
