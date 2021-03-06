/*
 *     Copyright (C) 2021.  Mathias Jakobsen <m.jakobsen.1@research.gla.ac.uk>
 *
 *     SPDX-License-Identifier: BSD-3-Clause
 */

package JVM.Instructions;

import JVM.JvmContext;
import JVM.JvmOpCode;

public class JvmReturnOperation extends JvmOperation {

    public JvmReturnOperation(JvmOpCode opcode) {
        super(opcode);
    }

    @Override
    public void evaluateInstruction(JvmContext ctx) {
        ctx.deallocateFrame();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
