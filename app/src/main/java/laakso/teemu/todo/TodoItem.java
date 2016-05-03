package laakso.teemu.todo;

import java.io.Serializable;

/**
 * Created by teemu on 5/2/2016.
 */
public class TodoItem implements Serializable {
    private String todo;
    private boolean isDone;
    static final long serialVersionUID =12345678920160503L;

    public TodoItem(String todo) {
        this.todo = todo;
        isDone = false;
    }

    public TodoItem(){}

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String toString(){
        return todo;
    }
}
