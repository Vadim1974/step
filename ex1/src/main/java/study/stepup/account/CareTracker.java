package study.stepup.account;

import java.util.ArrayList;
import java.util.List;
 class CareTracker {
    private final List<Object> mementos = new ArrayList<>();

    public void addMemento(Object memento) {
        this.mementos.add(memento);
    }

    public Object getMemento(int index) {
        return this.mementos.get(index);
    }
}
