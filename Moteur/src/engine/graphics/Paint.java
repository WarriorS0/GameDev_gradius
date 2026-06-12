package engine.graphics;

import oop.tasks.Runtime;
import oop.tasks.Task;
import oop.graphics.Canvas;
import oop.tasks.Runnable;

public class Paint implements Runnable {
    
    private Task task;
    private Canvas c;
    
    public Paint(Canvas c) {
        this.task = Runtime.newTask("GameLoopTask");
        this.c = c;
    }
    
    public void start() {
        this.task.post(this);
    }
    
    @Override
    public void run() throws Exception {
        c.repaint();
        task.post(this, 17);
    }
}