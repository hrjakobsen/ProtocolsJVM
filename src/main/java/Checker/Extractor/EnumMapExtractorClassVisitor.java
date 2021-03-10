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

package Checker.Extractor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


/**
 * This class extracts the integer array used for switch tables with the call to .ordinal().
 * It is used to track which enum label corresponds to which jump label
 *
 */
// TODO: 07/03/2021 Implement
public class EnumMapExtractorClassVisitor extends ClassVisitor {
    public EnumMapExtractorClassVisitor() {
        super(Opcodes.ASM8);
    }

    public EnumMapExtractorClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM8, cv);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible){
        return super.visitAnnotation(desc, visible);
    }

}