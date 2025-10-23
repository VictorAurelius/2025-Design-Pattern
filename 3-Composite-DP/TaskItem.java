public abstract class TaskItem {
    public abstract double getTime();

    public void add(TaskItem item) {
        throw new UnsupportedOperationException();
    }

    public void remove(TaskItem item) {
        throw new UnsupportedOperationException();
    }

    public TaskItem getChild(int index) {
        throw new UnsupportedOperationException();
    }
}
