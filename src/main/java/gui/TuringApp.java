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

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import logging.Log;
import org.apache.log4j.*;

/**
 * The main class of the application.
 *  @author Ludovic Favre
 */
public class TuringApp extends SingleFrameApplication {
    private static Logger log = Logger.getLogger(Log.FILENAME);
    private static boolean debug = false; // default disabled
    

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        if(log.isDebugEnabled()){
            log.info("Starting the main frame");
        }
        
        show(new TuringMainWindow(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of TuringApp
     */
    public static TuringApp getApplication() {
        return Application.getInstance(TuringApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        PropertyConfigurator.configure(Log.PROPERTIES);
        debug = log.isDebugEnabled();
        
        launch(TuringApp.class, args);
    }

}
