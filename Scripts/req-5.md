# Plan Task: Tạo Bài Toán Mới Cho Singleton Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Singleton Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Singleton Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu (vì chưa có code hiện tại)
- Đọc file `Documents/Lectures/Singleton.pdf` để:
  - Hiểu BẢN CHẤT của Singleton Pattern (không phải học bài toán cụ thể)
  - Hiểu vấn đề "multiple instances" và global access
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram có đầy đủ phương thức
  - Hiểu các cách implement khác nhau: Eager, Lazy, Thread-safe, Double-checked locking, Enum
  - Hiểu thread-safety concerns

- Đọc code trong `Code-Sample/SingletonPattern-Project/` để:
  - Học cách đặt tên class, method
  - Học coding convention và code style
  - Học các cách implement Singleton pattern đúng chuẩn
  - Học cách implement thread-safe Singleton
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Singleton Pattern - chỉ cần 1 instance duy nhất:
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: Database Connection (quá phổ biến)
- **KHÔNG dùng**: Logger (ví dụ phổ biến nhất)

**Gợi ý các lĩnh vực có thể dùng (cần 1 instance duy nhất)**:

1. **Application Configuration Manager**:
   - Quản lý cấu hình ứng dụng (settings, properties)
   - Chỉ nên có 1 instance để đảm bảo consistency
   - Ví dụ: AppConfig, ConfigurationManager

2. **Cache Manager**:
   - Quản lý cache toàn ứng dụng
   - Chỉ có 1 cache duy nhất cho toàn bộ app
   - Ví dụ: CacheManager, MemoryCache

3. **Print Spooler**:
   - Quản lý hàng đợi in ấn
   - Chỉ có 1 print queue để tránh conflict
   - Ví dụ: PrintSpooler, PrintQueue

4. **Device Manager**:
   - Quản lý thiết bị (hardware)
   - Chỉ có 1 manager điều khiển device
   - Ví dụ: DeviceManager, HardwareController

5. **Session Manager**:
   - Quản lý user sessions
   - Chỉ có 1 manager theo dõi tất cả sessions
   - Ví dụ: SessionManager, UserSessionTracker

6. **File System Manager**:
   - Quản lý file operations
   - Chỉ có 1 manager để tránh race conditions
   - Ví dụ: FileSystemManager, FileController

7. **Thread Pool Manager**:
   - Quản lý thread pool
   - Chỉ có 1 pool duy nhất
   - Ví dụ: ThreadPoolManager, WorkerPool

8. **License Manager**:
   - Quản lý license/activation
   - Chỉ có 1 instance kiểm tra license
   - Ví dụ: LicenseManager, ActivationController

9. **Performance Monitor**:
   - Theo dõi performance metrics
   - Chỉ có 1 monitor tập trung
   - Ví dụ: PerformanceMonitor, MetricsTracker

10. **Application State Manager**:
    - Quản lý trạng thái ứng dụng
    - Chỉ có 1 state duy nhất
    - Ví dụ: ApplicationState, AppStateManager

11. **Resource Pool**:
    - Quản lý shared resources
    - Chỉ có 1 pool để quản lý
    - Ví dụ: ResourcePool, SharedResourceManager

12. **Event Bus**:
    - Quản lý event communication
    - Chỉ có 1 central event bus
    - Ví dụ: EventBus, MessageBroker

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có
1. **Single instance**: Chỉ được tạo 1 instance duy nhất
2. **Global access point**: Truy cập từ bất kỳ đâu trong ứng dụng
3. **Lazy initialization**: Tạo instance khi cần (optional nhưng nên có)
4. **Thread-safety**: Đảm bảo an toàn trong môi trường multi-thread

**Đặc điểm của Singleton Pattern**:
- **Private constructor**: Không cho phép tạo instance từ bên ngoài
- **Private static instance**: Lưu instance duy nhất
- **Public static getInstance()**: Method để lấy instance
- **Thread-safe**: Đảm bảo chỉ tạo 1 instance dù có nhiều threads
- **Lazy or Eager initialization**: Tùy yêu cầu

**Quan trọng**:
- Constructor phải private
- Không cho phép clone
- Không cho phép serialize (hoặc phải override readResolve())
- Thread-safe nếu dùng trong multi-thread environment

#### 2.3. Thiết kế các thành phần

**Singleton Class**:
- Private constructor
- Private static instance variable
- Public static getInstance() method
- Business methods (theo domain)

**Ví dụ cấu trúc**:
```java
public class MySingleton {
    // Private static instance
    private static MySingleton instance;

    // Private constructor
    private MySingleton() {
        // Initialization
    }

    // Public static getInstance()
    public static MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }

    // Business methods
    public void doSomething() {
        // ...
    }
}
```

**Các cách implement** (chọn cách phù hợp):
1. **Eager Initialization**: Tạo instance ngay khi load class
2. **Lazy Initialization**: Tạo khi cần (basic, không thread-safe)
3. **Thread-Safe Lazy**: Synchronized method
4. **Double-Checked Locking**: Synchronized block với check 2 lần
5. **Bill Pugh (Inner Static Class)**: Recommended approach
6. **Enum Singleton**: Best practice theo Joshua Bloch

### Bước 3: Viết Documents/Solutions/Singleton.md

Tạo file `Documents/Solutions/Singleton.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Singleton
- Giữ phần mô tả tổng quan về Singleton Pattern
- Các thành phần chính: Private constructor, Static instance, getInstance()
- Khi nào sử dụng
- Đặc điểm quan trọng: single instance, global access, thread-safety

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- Bài toán cần:
  - Có yêu cầu chỉ 1 instance duy nhất
  - Nêu rõ vấn đề nếu có nhiều instances
  - Giải thích tại sao cần global access point
  - Có tình huống cụ thể minh họa

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Cần một object quản lý tài nguyên/trạng thái chung
- Nhiều nơi trong code cần truy cập object này
- Phải đảm bảo consistency (chỉ 1 instance)

**Problem**: Vấn đề phức tạp cần giải quyết
- Nếu có nhiều instances: inconsistent state, wasted resources
- Không có cách truy cập global: phải pass reference khắp nơi
- Race conditions trong multi-thread environment
- Khó kiểm soát lifecycle của object

**Solution**: Cách Singleton pattern giải quyết
- Private constructor → không tạo instance từ ngoài
- Static getInstance() → global access point
- Lazy initialization → tiết kiệm resource
- Thread-safe implementation → an toàn trong multi-thread

**Expected Output**: Kết quả mong đợi
- Chỉ có 1 instance duy nhất trong suốt lifecycle
- Dễ dàng truy cập từ mọi nơi
- Thread-safe
- Resource efficient

#### 3.4. Hiệu quả của việc sử dụng Singleton Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh với cách không dùng Singleton
- Thread-safety
- Resource management
- Trade-offs (nhược điểm của Singleton)

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Implement theo cách phù hợp (nên dùng Bill Pugh hoặc Enum)
- Mỗi class có:
  - Comment đầy đủ (nếu cần)
  - Tên biến, method rõ ràng
  - Logic đúng với Singleton pattern
  - Thread-safe
  - Coding style học từ code sample
- Code phải hoàn chỉnh và có thể compile

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Giải thích cách pattern hoạt động qua output
- Demo chỉ có 1 instance (hashCode giống nhau)
- Demo thread-safety (nếu có)

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Liệt kê các class và members
- Nêu rõ private constructor, static instance, getInstance()
- Thể hiện relationships nếu có classes khác

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Singleton

### Bước 4: Viết code Java cho bài toán mới trong 5-Singleton-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `5-Singleton-DP/`:

**Singleton class**:
- 1 main Singleton class

**Helper/Demo classes** (optional):
- 1-2 classes sử dụng Singleton (nếu cần minh họa)
- 1 Main/Demo class

**Ví dụ cấu trúc**:
```
5-Singleton-DP/
├── ConfigurationManager.java    (Singleton)
├── Application.java              (Demo - uses Singleton)
└── SingletonDemo.java            (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: chỉ khi cần
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- **Private constructor** - QUAN TRỌNG
- **Thread-safe implementation** - NÊN CÓ

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Singleton pattern
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Singleton.md
- Demo rõ ràng chỉ có 1 instance

#### 4.4. Recommended Implementation
Nên sử dụng **Bill Pugh Singleton** (Inner Static Helper Class):
```java
public class MySingleton {

    private MySingleton() {
        // Private constructor
    }

    // Inner static helper class
    private static class SingletonHelper {
        private static final MySingleton INSTANCE = new MySingleton();
    }

    public static MySingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```

**Ưu điểm**:
- Lazy initialization
- Thread-safe without synchronized
- Performance tốt
- Đơn giản, dễ hiểu

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `5-Singleton-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Singleton class với đầy đủ members
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationship với classes khác (nếu có)
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets
- Thể hiện rõ private constructor (-), static instance, static getInstance() method

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies (if any)
dependency1.from=SingletonDemo
dependency1.to=MySingleton
dependency1.type=UsesDependency

# Targets với showInterface=true
target1.name=MySingleton
target1.type=ClassTarget
target1.showInterface=true    # QUAN TRỌNG
target1.width=200
target1.height=140
target1.x=...
target1.y=...

target2.name=SingletonDemo
target2.type=ClassTarget
target2.showInterface=true
target2.width=160
target2.height=80
target2.x=...
target2.y=...
```

#### 5.3. Layout gợi ý
```
Sắp xếp đơn giản vì Singleton chỉ có 1 class chính:
- Singleton class: trung tâm
- Demo/Client class: bên trái hoặc trên
- Helper classes (nếu có): bên phải
```

## Deliverables

### 1. File Documents/Solutions/Singleton.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ tại sao cần Singleton

### 2. Folder 5-Singleton-DP/
Chứa các file Java cho bài toán MỚI:
- 1 Singleton class (main)
- 0-2 helper/demo classes (optional)
- 1 Main/Demo class
- Code sạch, đúng chuẩn
- Compile và run được
- Thread-safe implementation

### 3. File 5-Singleton-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ private constructor, static members

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Yêu cầu rõ ràng về single instance**: Thấy rõ tại sao chỉ cần 1 instance
✅ **Global access**: Nhiều nơi cần truy cập object này
✅ **Vấn đề nếu có nhiều instances**: Nêu rõ vấn đề sẽ xảy ra
✅ **Thread-safe**: Implementation an toàn trong multi-thread
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Private constructor + static getInstance()
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Singleton
✅ **Khác biệt**: Không trùng với lecture hay code sample
✅ **Đơn giản vừa đủ**: Đủ để minh họa pattern, không quá phức tạp

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy Database Connection (quá phổ biến)
❌ Copy Logger (ví dụ phổ biến nhất)
❌ Không rõ ràng tại sao cần chỉ 1 instance
❌ Không thread-safe
❌ Quá đơn giản: chỉ có getInstance() mà không có business logic
❌ Quá phức tạp: quá nhiều business logic không liên quan

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "chỉ cần 1 instance duy nhất"

### Về Singleton Pattern
- Constructor **PHẢI** là private
- **PHẢI** có static getInstance() method
- **NÊN** thread-safe (synchronized hoặc Bill Pugh approach)
- **KHÔNG** cho phép clone
- **KHÔNG** cho phép serialize (hoặc override readResolve())
- Instance variable **PHẢI** là static

### Về Implementation
- **Recommended**: Bill Pugh Singleton (inner static class)
- **Alternative**: Enum Singleton (best practice theo Joshua Bloch)
- **Avoid**: Basic lazy initialization without synchronization (not thread-safe)
- **Avoid**: Synchronized method (performance issue)
- **OK**: Double-checked locking (nhưng phức tạp)

### Về Thread-Safety
- Bill Pugh: Thread-safe by default (class loading mechanism)
- Enum: Thread-safe by default (JVM guarantee)
- Synchronized method: Thread-safe nhưng slow
- Double-checked locking: Thread-safe nhưng tricky
- Basic lazy: KHÔNG thread-safe

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Constructor private, có thể throw exception nếu gọi lại
- getInstance() static, return cùng 1 instance
- Code phải demo được chỉ có 1 instance (hashCode giống nhau)

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Thể hiện rõ private constructor: `-MySingleton()`
- Thể hiện rõ static members: underline hoặc <<static>>
- Format phải giống lecture
- Kích thước boxes đủ lớn để chứa methods

### Về documentation
- Documents/Solutions/Singleton.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- Giải thích rõ ràng tại sao cần Singleton
- So sánh với cách không dùng Singleton
- Nêu rõ trade-offs (nhược điểm)

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Application Configuration Manager
- Quản lý config settings (database URL, API keys, etc.)
- Chỉ cần 1 instance để đảm bảo consistency
- Methods: getProperty(), setProperty(), loadConfig(), saveConfig()

### Ví dụ 2: Cache Manager
- Quản lý application cache
- Chỉ có 1 cache để tránh duplication
- Methods: put(), get(), remove(), clear(), size()

### Ví dụ 3: Print Spooler
- Quản lý print queue
- Chỉ có 1 queue để tránh print conflicts
- Methods: addJob(), removeJob(), processJobs(), getQueueSize()

### Ví dụ 4: License Manager
- Kiểm tra license/activation
- Chỉ có 1 instance kiểm tra
- Methods: validateLicense(), isActivated(), getLicenseInfo()

### Ví dụ 5: Resource Pool Manager
- Quản lý connection pool hoặc resource pool
- Chỉ có 1 pool duy nhất
- Methods: getResource(), releaseResource(), getPoolSize()

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết methods phù hợp
- Demo rõ single instance

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Singleton
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán có yêu cầu rõ ràng về single instance
- [ ] Constructor là private
- [ ] Có static getInstance() method
- [ ] Implementation thread-safe (Bill Pugh hoặc Enum)
- [ ] Documents/Solutions/Singleton.md có đầy đủ 8 sections
- [ ] Code trong 5-Singleton-DP/ là code MỚI cho bài toán mới
- [ ] Có Singleton class + Demo class
- [ ] Code compile và chạy được
- [ ] Demo được chỉ có 1 instance (hashCode)
- [ ] Code phù hợp với bài toán trong Documents/Solutions/Singleton.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram format giống lecture
- [ ] UML thể hiện rõ private constructor và static members
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng single instance behavior
- [ ] Đã giải thích trade-offs của Singleton

## Anti-patterns cần tránh

### Singleton có thể là anti-pattern nếu:
❌ Dùng như global variable (coupling cao)
❌ Khó test (không thể mock dễ dàng)
❌ Vi phạm Single Responsibility Principle
❌ Tạo hidden dependencies
❌ Gây khó khăn trong multi-threading

### Khi nào KHÔNG nên dùng Singleton:
- Khi cần nhiều instances với configuration khác nhau
- Khi cần lifecycle management phức tạp
- Khi cần dependency injection
- Khi cần unit testing dễ dàng
- Khi cần stateless behavior

### Best Practices:
✅ Chỉ dùng khi thực sự cần 1 instance duy nhất
✅ Consider dependency injection thay vì Singleton
✅ Dùng Enum hoặc Bill Pugh implementation
✅ Document rõ ràng tại sao cần Singleton
✅ Cẩn thận với serialization và cloning
