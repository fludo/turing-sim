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
import core.converter.TinyLangUtils;

/**
 *
 * @author Ludovic Favre <ludovic.favre@epfl.ch>
 */
public class Sub_notm implements TInstruction {



    private MultiTapeTuringMachine tm_;
    private final int inSize_ = 1;
    private final int outSize_ = 1;
    private String[] input_;

    public Sub_notm(String[] input, ITypes[] types) {
        //this.tm_ = new MultiTapeTuringMachine();
        this.paramsAre(input, types);
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
        return TinyLangUtils.intToBinary(TinyLangUtils.binaryToInt(this.input_[0])-TinyLangUtils.binaryToInt(this.input_[1]));
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
