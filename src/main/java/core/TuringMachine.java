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
import java.util.logging.Level;
import java.util.logging.Logger;
import logging.Log;
/**
 *
 * @author Ludovic Favre
 */
public class TuringMachine {
    protected static Logger log = Logger.getLogger(Log.FILENAME);
    protected static boolean debug = false; // default disabled

    protected Alphabet alphabet_ = new Alphabet(); //this has to be a subset of tape alphabet without blank symbol
    
    protected String name_;
    protected String description_;

    public void nameIs(String name){
        this.name_ = name;
    }

    public String name(){
        return this.name_;
    }

    public void descriptionIs(String description){
        this.description_ = description;
    }

    public String description(){
        return this.description_;
    }

    public void alphabetIs(Alphabet al){
        if(debug) log.log(Level.INFO, "Added alphabet : "+al.toString());
        this.alphabet_ = al;
    }

    public Alphabet alphabet(){
        return this.alphabet_;
        
    }
    
}
