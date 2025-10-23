<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

### 4. Mẫu thiết kế Facade

#### 4.0. Mô tả mẫu Facade

**Mẫu Facade** (Facade Pattern) là một mẫu thiết kế cấu trúc (Structural Pattern) cung cấp một giao diện đơn giản, thống nhất để truy cập vào một hệ thống con phức tạp (subsystem). Facade định nghĩa một giao diện cấp cao hơn giúp hệ thống con dễ sử dụng hơn bằng cách che giấu sự phức tạp của các lớp bên trong.[^1][^2][^3]

**Các thành phần chính:**

- **Facade**: Lớp cung cấp giao diện đơn giản, biết cách chuyển hướng yêu cầu của client đến các đối tượng thích hợp trong subsystem
- **Subsystem Classes**: Các lớp thực hiện chức năng thực tế của hệ thống, xử lý công việc được giao bởi Facade. Chúng không biết về sự tồn tại của Facade
- **Client**: Đối tượng sử dụng Facade thay vì gọi trực tiếp các đối tượng trong subsystem

**Sự khác biệt giữa Facade và Adapter:**

- **Facade**: Đơn giản hóa nhiều interface phức tạp thành một interface đơn giản hơn. Làm việc với nhiều component. Mục đích là che giấu sự phức tạp
- **Adapter**: Chuyển đổi một interface thành interface khác mà client mong đợi. Làm việc với một component duy nhất. Mục đích là giải quyết vấn đề không tương thích[^4][^5][^6]

**Khi nào sử dụng:**

- Khi cần cung cấp interface đơn giản cho một hệ thống phức tạp
- Khi có nhiều dependencies giữa client và các lớp implementation
- Khi muốn phân tầng các subsystem, sử dụng facade làm entry point cho mỗi tầng[^2][^3][^4]


#### 4.1. Mô tả bài toán

Một khách hàng muốn xem phim tại nhà với hệ thống rạp gia đình (Home Theater). Để xem được một bộ phim, khách hàng phải thực hiện rất nhiều thao tác phức tạp theo thứ tự:

1. Bật đèn mờ (Dim Lights)
2. Bật màn hình chiếu (Screen Down)
3. Bật máy chiếu (Projector On)
4. Đặt input máy chiếu là DVD
5. Bật âm li (Amplifier On)
6. Đặt input âm li là DVD
7. Điều chỉnh âm lượng lên 5
8. Bật đầu DVD (DVD On)
9. Phát phim từ DVD

Mỗi lần xem phim phải thực hiện đến 9 bước, và khi kết thúc lại phải tắt theo thứ tự ngược lại. Điều này rất phức tạp và dễ nhầm lẫn. Khách hàng chỉ muốn có một nút "Xem phim" và một nút "Tắt hệ thống" đơn giản.

Sử dụng Facade Pattern, ta sẽ tạo một lớp `HomeTheaterFacade` cung cấp giao diện đơn giản `watchMovie()` và `endMovie()`, che giấu toàn bộ sự phức tạp của các thiết bị bên trong.[^3][^7][^1][^4]

#### 4.2. Cài đặt

Các lớp Subsystem - các thiết bị trong hệ thống rạp gia đình:

**DimLights.java**

```java
public class DimLights {
    public void dim() {
        System.out.println("Lights: Dimming to 10%");
    }
    
    public void brightOn() {
        System.out.println("Lights: Turning on to 100%");
    }
}
```

**Screen.java**

```java
public class Screen {
    public void down() {
        System.out.println("Screen: Moving down");
    }
    
    public void up() {
        System.out.println("Screen: Moving up");
    }
}
```

**Projector.java**

```java
public class Projector {
    public void on() {
        System.out.println("Projector: Turning on");
    }
    
    public void setInput(String input) {
        System.out.println("Projector: Setting input to " + input);
    }
    
    public void off() {
        System.out.println("Projector: Turning off");
    }
}
```

**Amplifier.java**

```java
public class Amplifier {
    public void on() {
        System.out.println("Amplifier: Turning on");
    }
    
    public void setInput(String input) {
        System.out.println("Amplifier: Setting input to " + input);
    }
    
    public void setVolume(int level) {
        System.out.println("Amplifier: Setting volume to " + level);
    }
    
    public void off() {
        System.out.println("Amplifier: Turning off");
    }
}
```

**DVDPlayer.java**

```java
public class DVDPlayer {
    public void on() {
        System.out.println("DVD Player: Turning on");
    }
    
    public void play(String movie) {
        System.out.println("DVD Player: Playing \"" + movie + "\"");
    }
    
    public void stop() {
        System.out.println("DVD Player: Stopping");
    }
    
    public void off() {
        System.out.println("DVD Player: Turning off");
    }
}
```

**HomeTheaterFacade.java** - Lớp Facade đơn giản hóa việc sử dụng:

```java
public class HomeTheaterFacade {
    private DimLights lights;
    private Screen screen;
    private Projector projector;
    private Amplifier amplifier;
    private DVDPlayer dvd;
    
    public HomeTheaterFacade(DimLights lights, Screen screen, 
                            Projector projector, Amplifier amplifier, 
                            DVDPlayer dvd) {
        this.lights = lights;
        this.screen = screen;
        this.projector = projector;
        this.amplifier = amplifier;
        this.dvd = dvd;
    }
    
    public void watchMovie(String movie) {
        System.out.println("\n=== Getting ready to watch movie... ===\n");
        lights.dim();
        screen.down();
        projector.on();
        projector.setInput("DVD");
        amplifier.on();
        amplifier.setInput("DVD");
        amplifier.setVolume(5);
        dvd.on();
        dvd.play(movie);
        System.out.println("\n=== Enjoy your movie! ===\n");
    }
    
    public void endMovie() {
        System.out.println("\n=== Shutting down home theater... ===\n");
        dvd.stop();
        dvd.off();
        amplifier.off();
        projector.off();
        screen.up();
        lights.brightOn();
        System.out.println("\n=== Home theater is off ===\n");
    }
}
```

**Main.java** - Chạy chương trình:

```java
public class Main {
    public static void main(String[] args) {
        // Khởi tạo tất cả các thiết bị
        DimLights lights = new DimLights();
        Screen screen = new Screen();
        Projector projector = new Projector();
        Amplifier amplifier = new Amplifier();
        DVDPlayer dvd = new DVDPlayer();
        
        // Tạo Facade
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
            lights, screen, projector, amplifier, dvd
        );
        
        // Client chỉ cần gọi một phương thức đơn giản
        homeTheater.watchMovie("Avatar 2");
        
        // Và một phương thức để tắt
        homeTheater.endMovie();
    }
}
```


#### 4.3. Kết quả chạy chương trình

```
=== Getting ready to watch movie... ===

Lights: Dimming to 10%
Screen: Moving down
Projector: Turning on
Projector: Setting input to DVD
Amplifier: Turning on
Amplifier: Setting input to DVD
Amplifier: Setting volume to 5
DVD Player: Turning on
DVD Player: Playing "Avatar 2"

=== Enjoy your movie! ===


=== Shutting down home theater... ===

DVD Player: Stopping
DVD Player: Turning off
Amplifier: Turning off
Projector: Turning off
Screen: Moving up
Lights: Turning on to 100%

=== Home theater is off ===
```

**Giải thích:**

Trong ví dụ này, Facade Pattern đã giải quyết vấn đề phức tạp:

1. **Subsystem Classes** (DimLights, Screen, Projector, Amplifier, DVDPlayer): Các lớp thiết bị riêng lẻ với giao diện phức tạp, mỗi lớp có nhiều phương thức khác nhau
2. **Facade** (HomeTheaterFacade): Cung cấp hai phương thức đơn giản `watchMovie()` và `endMovie()`, che giấu toàn bộ 9 bước phức tạp bên trong
3. **Client** (Main): Chỉ cần gọi một phương thức duy nhất thay vì phải nhớ và thực hiện 9 bước theo đúng thứ tự

**Lợi ích:**

- **Đơn giản hóa**: Client không cần biết chi tiết các thiết bị và thứ tự thao tác
- **Giảm coupling**: Client chỉ phụ thuộc vào Facade, không phụ thuộc vào từng thiết bị riêng lẻ
- **Dễ bảo trì**: Nếu thêm/bớt thiết bị, chỉ cần sửa Facade, client code không đổi
- **Linh hoạt**: Các thiết bị vẫn có thể được sử dụng trực tiếp nếu cần thiết (Facade không che giấu hoàn toàn subsystem)[^6][^7][^1][^4][^2][^3]
<span style="display:none">[^10][^11][^12][^13][^14][^15][^16][^17][^18][^19][^20][^8][^9]</span>

<div align="center">⁂</div>

[^1]: https://viblo.asia/p/facade-design-pattern-in-java-OeVKBMjE5kW

[^2]: https://viblo.asia/p/facade-design-pattern-tro-thu-dac-luc-cua-developers-924lJBLNlPM

[^3]: https://www.geeksforgeeks.org/system-design/facade-design-pattern-introduction/

[^4]: https://refactoring.guru/design-patterns/facade

[^5]: https://stackoverflow.com/questions/2961307/what-is-the-difference-between-the-facade-and-adapter-pattern

[^6]: https://tubean.github.io/2018/11/facade-design-patterns/

[^7]: https://www.tutorialspoint.com/design_pattern/facade_pattern.htm

[^8]: https://www.geeksforgeeks.org/system-design/difference-between-the-facade-proxy-adapter-and-decorator-design-patterns/

[^9]: https://kungfutech.edu.vn/bai-viet/java/facade-pattern-trong-java

[^10]: https://www.digitalocean.com/community/tutorials/facade-design-pattern-in-java

[^11]: https://www.linkedin.com/advice/0/what-difference-between-facade-adapter-design-pattern-9fqye

[^12]: https://cystack.net/vi/tutorial/mau-thiet-ke-facade

[^13]: https://www.dofactory.com/net/facade-design-pattern

[^14]: https://learn.microsoft.com/en-us/shows/visual-studio-toolbox/design-patterns-adapterfaade

[^15]: https://codesignal.com/learn/courses/backward-compatibility-in-software-development-1/lessons/facade-and-adapter-design-patterns-explained

[^16]: https://www.youtube.com/watch?v=rb89NqO5rGk

[^17]: https://refactoring.guru/design-patterns/facade/python/example

[^18]: https://refactoring.guru/design-patterns/adapter

[^19]: https://topdev.vn/blog/facade-pattern-don-gian-hoa-tat-ca/

[^20]: https://www.youtube.com/watch?v=fHPa5xzbpaA

