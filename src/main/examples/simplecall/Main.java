/*
 *     Copyright (C) 2021.  Mathias Jakobsen <m.jakobsen.1@research.gla.ac.uk>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package simplecall;

import Annotations.ArrayKeyLoad;
import Annotations.KeySet;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        C1 c = new C1();
        c.question();
        int in = 48; // System.in.read(); // 0 = ASCII(48)
        if (in == 48) {
            c.Branch2();
            c.Branch1();
        } else {
            c.Branch1();
            c.Branch2();
        }
        c.stop();
    }
}