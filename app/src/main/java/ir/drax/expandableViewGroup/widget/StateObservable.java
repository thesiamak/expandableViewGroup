package ir.drax.expandableViewGroup.widget;


import java.util.Observable;

public class StateObservable extends Observable {
    private Boolean state;
    public StateObservable(Boolean state) {
        this.state=state;
    }

    public void setState(Boolean state) {
        this.state = state;
        setChanged();
        notifyObservers(state);
    }

    public Boolean getState() {
        return state;
    }
}
