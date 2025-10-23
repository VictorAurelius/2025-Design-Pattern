public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Design Module A", 10);
        Task task2 = new Task("Implementation Module A", 20);
        Task task3 = new Task("Testing Module A", 5);

        Project moduleA = new Project("Module A");
        moduleA.add(task1);
        moduleA.add(task2);
        moduleA.add(task3);

        Task task4 = new Task("Design Module B", 8);
        Task task5 = new Task("Implementation Module B", 15);

        Project moduleB = new Project("Module B");
        moduleB.add(task4);
        moduleB.add(task5);

        Project mainProject = new Project("Main Project");
        mainProject.add(moduleA);
        mainProject.add(moduleB);

        System.out.println("Total time for " + mainProject.getName() + ": " + mainProject.getTime() + " hours.");
    }
}
