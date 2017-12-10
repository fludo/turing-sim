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
package logging;
import org.apache.log4j.*;


/**
 *
 * @author Ludovic Favre
 */
public class Log {
  public static String FILENAME = "turingsim.log";
  public static String PROPERTIES = "src/logging/logs.properties";
  static Logger log = Logger.getLogger(FILENAME);

  public Log()
  {
    log.info("Application is alive.");
  }

  public static void main(String[] args)
  {
    PropertyConfigurator.configure(PROPERTIES);

    log.warn("Starting application");
    Log e4 = new Log();
    log.warn("Exiting application");
    log.fatal("A fatal error");
    log.info("in the file ?");
  }

}
