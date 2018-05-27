package de.hpi.parser.service;

import java.util.LinkedList;

class Path extends LinkedList<PathID> {

    Path() {
        add(new PathID());
    }

}
