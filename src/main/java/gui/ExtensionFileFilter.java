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

import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 *<p>
 * A filter for extensions of file (used by OpenFileBox class)
 * @author Ludovic Favre
 */
class ExtensionFileFilter extends FileFilter{
  String description;

  String extensions[];

  /**
   * Constructor
   * @param description
   * @param extension
   */
  public ExtensionFileFilter(String description, String extension) {
    this(description, new String[] { extension });
  }

  /**
   * <p>Constructor
   * @param description
   * @param extensions
   */
  public ExtensionFileFilter(String description, String[] extensions) {
    if (description == null) {
      this.description = extensions[0];
    } else {
      this.description = description;
    }
    this.extensions = (String[]) extensions.clone();
    toLower(this.extensions);
  }

  /**
   * <p>Put a string array to lower case
   * @param array
   */
  private void toLower(String array[]) {
    for (int i = 0, n = array.length; i < n; i++) {
      array[i] = array[i].toLowerCase();
    }
  }

  /**
   * <p> Returns description
   * @return
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Decides wheter the file satisfy the filter
   * @param file
   * @return
   */
  @Override
  public boolean accept(File file) {
    if (file.isDirectory()) {
      return true;
    } else {
      String path = file.getAbsolutePath().toLowerCase();
      for (int i = 0, n = extensions.length; i < n; i++) {
        String extension = extensions[i];
        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
          return true;
        }
      }
    }
    return false;
  }
}