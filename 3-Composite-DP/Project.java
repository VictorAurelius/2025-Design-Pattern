import java.util.ArrayList;

public class Project extends TaskItem {
    private String name;
    private ArrayList<TaskItem> subTasks = new ArrayList<>();

    public Project(String name) {
        this.name = name;
    }

    @Override
    public double getTime() {
        double total = 0;
        for (TaskItem task : subTasks) {
            total += task.getTime();
        }
        return total;
    }

    @Override
    public void add(TaskItem task) {
        subTasks.add(task);
    }

    @Override
    public void remove(TaskItem task) {
        subTasks.remove(task);
    }

    @Override
    public TaskItem getChild(int index) {
        return subTasks.get(index);
    }

    public String getName() {
        return name;
    }
}
