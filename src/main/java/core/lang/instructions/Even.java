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

import core.SimpleTapeRunner;
import core.SimpleTuringMachine;
import core.exception.InstructionParameterException;
import core.io.XMLReader;

/**
 *
 * @author Ludovic Favre <ludovic.favre@epfl.ch>
 */
public class Even implements TInstruction {

    private SimpleTuringMachine tm_;
    private final int inSize_ = 1;
    private final int outSize_ = 0;
    private String[] input_;

    public Even(String[] input, ITypes[] types) throws InstructionParameterException,Exception {
        this.paramsAre(input, types);
        this.buildTm();
    }

    @Override
    public boolean accepts() {
        return this.tm_.accepting();
    }

    @Override
    public boolean rejects() {
        return this.tm_.rejecting();
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
        return null;
    }

    @Override
    public String[] params() {
        return this.input_;
    }

    @Override
    public void paramsAre(String[] input, ITypes[] types) throws InstructionParameterException {
        if (input.length != types.length) {
            throw new InstructionParameterException("Parameter and type size doesn't match !");
        }
        this.input_ = input;
    }

    private void buildTm() throws Exception {
        this.tm_ = (SimpleTuringMachine) new XMLReader().read("instructionsources/even.xml");
        this.tm_.tapeContentIs(this.input_[0] + "#");

    }

    @Override
    public void execute() throws Exception {

        SimpleTapeRunner runner = new SimpleTapeRunner(this.tm_);

        while (runner.doStep()) {
        }
    }
}
