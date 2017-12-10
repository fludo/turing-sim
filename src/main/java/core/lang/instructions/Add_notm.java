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

import core.MultiTapeTuringMachine;
import core.Tape;
import core.converter.TinyLangUtils;
import core.exception.InstructionParameterException;

/**
 *
 * @author Ludovic Favre <ludovic.favre@epfl.ch>
 */
public class Add_notm implements TInstruction {
    private MultiTapeTuringMachine tm_ = null;
    private final int inSize_ = 2;
    private final int outSize_ = 1;
    private String[] input_;

    public Add_notm(String[] input,ITypes[] types) throws InstructionParameterException{
        this.paramsAre(input, types);
        
        // and build a turing machine upon this

        this.buildTm();
    }

    public Add_notm(int[] input, ITypes[] types){
        if(input.length!=this.inSize_){
            throw new IllegalArgumentException("Adder can only add two arguments");
        }
        this.input_[0] = TinyLangUtils.intToBinary(input[0]);
        this.input_[1] = TinyLangUtils.intToBinary(input[1]);

        this.buildTm();
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
        return this.inSize_;
    }

    @Override
    public int outSize() {
        return this.outSize_;
    }

    @Override
    public String output() {
        return TinyLangUtils.intToBinary(TinyLangUtils.binaryToInt(this.input_[0])+TinyLangUtils.binaryToInt(this.input_[1]));
    }

    @Override
    public String[] params() {
        return this.input_;
    }

    @Override
    public void paramsAre(String[] input, ITypes[] types) {
        this.input_ = input;
    }

        @Override
    public void execute() throws Exception {
        
    }

    private void buildTm() {
        
    }

}
