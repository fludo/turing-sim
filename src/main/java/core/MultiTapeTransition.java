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
package core;

/**
 *
 * @author Ludovic Favre
 */
public class MultiTapeTransition implements Comparable<MultiTapeTransition>{
    private State currentState;
    private State nextState;
    private char currentChar;
    private char nextChar;
    private boolean breakpoint_;
    private MultiTapeDirection direction;

    public MultiTapeTransition(State current, State next, char currentC, char nextC, MultiTapeDirection dir){
        this.currentState = current;
        this.nextState = next;
        this.currentChar = currentC;
        this.nextChar = nextC;
        this.direction = dir;
        this.breakpoint_ = false;
    }

    public MultiTapeTransition(MultiTapeTransition transition){
        this.currentState = new State(transition.currentState());
        this.nextState = new State(transition.nextState());
        this.currentChar = transition.currentChar();
        this.nextChar = transition.nextChar();
        this.direction = transition.direction();
        this.breakpoint_ = transition.breakpoint();
    }

    
    public MultiTapeDirection direction(){
        return this.direction;
    }
    public State currentState(){
        return this.currentState;
    }

    public State nextState(){
        return this.nextState;
    }

    public char currentChar(){
        return this.currentChar;
    }

    public char nextChar(){
        return this.nextChar;
    }

    public boolean breakpoint(){
        return this.breakpoint_;
    }

    public void breakpointIs(boolean flag){
        this.breakpoint_ = false;
    }


    @Override
    public String toString(){
        String res = "("+this.currentState+","+this.currentChar()+") ---> ("+this.nextState+","+this.nextChar()+","+this.direction+")";
        return res;
    }


    @Override
    public int compareTo(MultiTapeTransition that){
        return (this.currentState().toString()+this.currentChar).compareTo(that.currentState().toString()+that.currentChar);
    }

}
