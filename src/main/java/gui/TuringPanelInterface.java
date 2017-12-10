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
package gui;

/**
 *
 * @author Ludovic Favre
 */
public interface TuringPanelInterface {

    /**
     * Delete a state from the panel and the internal turing machine representation
     * @param state
     */
    public void deleteState(core.State state);

    /**
     * Add a state to the panel and the internal turing machine representation
     * @param state
     * @param starting
     * @param accepting
     * @param rejecting
     */
    public void addStateToList(core.State state, boolean starting, boolean accepting, boolean rejecting);

    /**
     * Save the current active turing machine to its file
     * If the file doesn't exist, it may ask for a location to save the turing machine
     */
    public void save();

    /**
     * Save the current active turing machine to a particular file (a popup asking for destination will appear).
     */
    public void saveTo();

    /**
     * Add text int he bottom console
     * @param text
     */
    public void consoleAppend(String text);

    /**
     * Change the input alphabet
     * @param newAlphabet
     */
    public void newAlphabetIs(String newAlphabet);


}
