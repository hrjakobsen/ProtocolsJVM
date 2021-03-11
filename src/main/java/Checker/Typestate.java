/*
 *     Copyright (C) 2021.  Mathias Jakobsen <m.jakobsen.1@research.gla.ac.uk>
 *
 *     Tropicode is a Java bytecode analyser used to verify object protocols.
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

package Checker;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Typestate implements Cloneable {

    public static Typestate fromString(String protocol) {
        TypestateLexer lexer = new TypestateLexer(protocol);
        TypestateParser parser = new TypestateParser();
        Typestate parsedProtocol = parser.parse(lexer.getTokens());
        HashMap<String, Typestate> implicitConstructorCall = new HashMap<>();
        implicitConstructorCall.put("<init>", parsedProtocol);
        return new Branch(implicitConstructorCall);
    }

    public abstract Typestate deepCopy();

    public abstract boolean isAllowed(String action);
    public abstract Typestate perform(String action);
    protected abstract Typestate unfoldRecursive(String identifier, Typestate ts);

    public static Typestate END = new End();

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Typestate)) {
            return false;
        }
        return other.toString().equals(this.toString());
    }

    public abstract List<String> getOperations();

    private static class End extends Typestate {

        @Override
        public boolean isAllowed(String action) {
            return false;
        }

        @Override
        public Typestate perform(String action) {
            return null;
        }

        @Override
        protected Typestate unfoldRecursive(String identifier, Typestate ts) {
            return this;
        }

        @Override
        public List<String> getOperations() {
            return new ArrayList<>();
        }

        @Override
        public String toString() {
            return "end";
        }

        @Override
        public Typestate deepCopy()  {
            return this;
        }


    }

    static class Branch extends Typestate {
        private HashMap<String, Typestate> branches;

        Branch(HashMap<String, Typestate> branches) {
            this.branches = branches;
        }

        @Override
        public boolean isAllowed(String action) {
            return branches.containsKey(action);
        }

        @Override
        public Typestate perform(String action) {
            return branches.getOrDefault(action, null);
        }

        @Override
        protected Typestate unfoldRecursive(String identifier, Typestate ts) {
            Branch copy = (Branch) this.deepCopy();
            copy.branches.replaceAll((a, v) -> copy.branches.get(a).unfoldRecursive(identifier, ts));
            return copy;
        }

        @Override
        public List<String> getOperations() {
            return new ArrayList<>(branches.keySet());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append("{");
            for (String operation : branches.keySet()) {
                sb.append(operation).append("; ").append(branches.get(operation).toString());
            }
            sb.append("}");
            return sb.toString();
        }

        @Override
        public Typestate deepCopy()  {
            HashMap<String, Typestate> newBranches = new HashMap<>();
            for (String key : this.branches.keySet()) {
                newBranches.put(key, this.branches.get(key).deepCopy());
            }
            return new Branch(newBranches);
        }


    }
    static class Choice extends Typestate {
        private HashMap<String, Typestate> choices;

        Choice(HashMap<String, Typestate> choice) {
            this.choices = choices;
        }

        @Override
        public boolean isAllowed(String action) {
            return choices.containsKey(action);
        }

        @Override
        public Typestate perform(String action) {
            return choices.getOrDefault(action, null);
        }

        @Override
        protected Typestate unfoldRecursive(String identifier, Typestate ts) {
            Choice copy = (Choice) this.deepCopy();
            copy.choices.replaceAll((a, v) -> copy.choices.get(a).unfoldRecursive(identifier, ts));
            return copy;
        }

        @Override
        public List<String> getOperations() {
            return new ArrayList<>(choices.keySet());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append("<");
            for (String operation : choices.keySet()) {
                sb.append(operation).append(": ").append(choices.get(operation).toString());
            }
            sb.append(">");
            return sb.toString();
        }

        @Override
        public Typestate deepCopy() {
            HashMap<String, Typestate> newChoices = new HashMap<>();
            for (String key : this.choices.keySet()) {
                newChoices.put(key, this.choices.get(key).deepCopy());
            }
            return new Choice(newChoices);
        }
    }

    static class Recursive extends Typestate {
        final Typestate next;
        final String identifier;

        Recursive(String identifier, Typestate next) {
            this.next = next;
            this.identifier = identifier;
        }

        @Override
        public Typestate deepCopy() {
            return new Recursive(identifier, next.deepCopy());
        }

        @Override
        public boolean isAllowed(String action) {
            return this.next.unfoldRecursive(this.identifier, this).isAllowed(action);
        }

        @Override
        public Typestate perform(String action) {
            return this.next.unfoldRecursive(this.identifier, this).perform(action);
        }

        @Override
        protected Typestate unfoldRecursive(String identifier, Typestate ts) {
            return next.unfoldRecursive(identifier, ts);
        }

        @Override
        public List<String> getOperations() {
            return next.getOperations();
        }


        @Override
        public String toString() {
            return "rec " + identifier + ". " + next.toString();
        }
    }

    static class Variable extends Typestate {
        final String identifier;

        Variable(String identifier) {
            this.identifier = identifier;
        }


        @Override
        public Typestate deepCopy() {
            return new Variable(this.identifier);
        }

        @Override
        public boolean isAllowed(String action) {
            return false;
        }

        @Override
        public Typestate perform(String action) {
            return null;
        }

        @Override
        protected Typestate unfoldRecursive(String identifier, Typestate ts) {
            if (identifier.equals(this.identifier)) {
                return ts.deepCopy();
            } else {
                return this;
            }
        }

        @Override
        public List<String> getOperations() {
            return new ArrayList<>();
        }

        @Override
        public String toString() {
            return identifier;
        }
    }
}
