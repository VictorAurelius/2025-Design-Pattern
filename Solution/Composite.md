<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

### 3. Mẫu thiết kế Composite

#### 3.0. Mô tả mẫu Composite

**Mẫu Composite** (Composite Pattern) là mẫu thiết kế cấu trúc cho phép bạn tổ chức các đối tượng thành cấu trúc dạng cây, trong đó các nút cây và các lá được đối xử đồng nhất thông qua giao diện chung. Điều này giúp xử lý nhóm đối tượng và cá thể cùng một cách mà không cần phân biệt giữa chúng.

**Các thành phần chính:**

- **Component**: Giao diện hoặc lớp trừu tượng khai báo các phương thức chung cho cả nút lá và nút nhánh.
- **Leaf (Lá)**: Các đối tượng không có con, triển khai Component.
- **Composite (Nhánh)**: Các đối tượng có thể chứa nhiều Component con, cũng triển khai Component và quản lý các thành phần con.
- **Client**: Sử dụng đối tượng Component mà không phân biệt đó là lá hay nhánh.

**Khi nào sử dụng:**

- Khi cần biểu diễn cấu trúc phân cấp dạng cây
- Khi muốn thao tác với các thành phần đơn lẻ và nhóm thành phần đồng nhất
- Khi muốn giảm thiểu sự phụ thuộc của client vào cấu trúc cây cụ thể.[^1][^2][^3]


#### 3.1. Mô tả bài toán

Giả sử bạn quản lý một dự án phần mềm được chia thành nhiều task (công việc). Mỗi task có thời gian thực hiện riêng, có thể là task đơn hoặc task tổng hợp chứa nhiều task con. Bạn muốn tính tổng thời gian của dự án dựa trên thời gian của các task và các task con bên trong nó. Bài toán thể hiện cấu trúc phân cấp cha - con điển hình, rất thích hợp ứng dụng Composite Pattern.[^3][^5]

#### 3.2. Cài đặt

**TaskItem.java** (Component trừu tượng)

```java
import java.util.ArrayList;

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
```

**Task.java** (Leaf - công việc đơn)

```java
public class Task extends TaskItem {
    private String name;
    private double time; // giờ làm

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
```

**Project.java** (Composite - công việc tổng hợp có nhiều task con)

```java
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
```

**Main.java** (Client sử dụng Composite để tính tổng thời gian)

```java
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
```


#### 3.3. Kết quả chạy chương trình

```
Total time for Main Project: 58.0 hours.
```


### Giải thích:

- Mẫu Composite cho phép xử lý các đối tượng đơn và nhóm các đối tượng đó như một đối tượng duy nhất thông qua interface **TaskItem**.
- `Task` là các đối tượng lá, không có con.
- `Project` là đối tượng composite, có thể chứa nhiều `TaskItem` (cả `Task` hoặc các `Project` con).
- `Main` tính tổng thời gian cho dự án cha mà không cần biết cấu trúc bên trong phức tạp ra sao.

Cách tiếp cận này rất phù hợp với mô hình tổ chức phân cấp, như cây thư mục, phần mềm phân module, hoặc các hệ thống phân cấp tương tự khác.[^2][^5][^1][^3]
<span style="display:none">[^10][^4][^6][^7][^8][^9]</span>

<div align="center">⁂</div>

[^1]: https://viblo.asia/p/composite-design-pattern-tro-thu-dac-luc-cua-developers-Qbq5QBk3KD8

[^2]: https://cystack.net/vi/tutorial/composite-design-pattern-trong-java

[^3]: https://kungfutech.edu.vn/bai-viet/java/composite-pattern-trong-java

[^4]: https://tubean.github.io/2018/12/composite-pattern/

[^5]: https://cafedev.vn/tu-hoc-design-pattern-code-vi-du-composite-design-pattern-trong-java/

[^6]: https://appstechviet.wordpress.com/2015/08/18/design-pattern-the-composite-pattern/

[^7]: https://tedu.com.vn/design-pattern/design-pattern-for-dummies-chuong-8-iterator-composite-139.html

[^8]: https://topdev.vn/blog/bridge-pattern-trong-java-code-vi-du-composite-pattern/

[^9]: https://stackjava.com/design-pattern/composite-pattern.html

[^10]: https://tranghaviet.blogspot.com/2016/11/tut-7-design-pattern-composite.html

