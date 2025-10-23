public class Task extends TaskItem {
    private String name;
    private double time; // hours

    public Task(String name, double time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public double getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
