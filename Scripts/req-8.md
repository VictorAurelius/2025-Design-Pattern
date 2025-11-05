# Plan Task: Tạo Bài Toán Mới Cho Proxy Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Proxy Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## 🎯 YÊU CẦU ĐẶC BIỆT: Context Liên Kết
Để dễ nhớ (vì có 24 design patterns), bài toán nên chọn **context liên kết** với các patterns đã làm:

### Contexts đã sử dụng:
1. **Adapter**: Media Player (Audio/Video players)
2. **Facade**: Home Theater System (Movie watching)
3. **Composite**: Restaurant Menu System
4. **Bridge**: Notification System (Messages)
5. **Singleton**: Configuration Manager (Enterprise software)
6. **Observer**: YouTube Channel (Video platform)
7. **Mediator**: Smart Home Automation (IoT devices)

### 🌟 RECOMMENDED Contexts cho Proxy (có liên kết):

#### Option 1: **Video Streaming Platform** (BEST - liên kết với Observer + Adapter)
- **Liên kết**: YouTube Channel (Observer), Media Player (Adapter)
- **Use case**:
  - **Virtual Proxy**: Lazy loading video content (không load video cho đến khi play)
  - **Protection Proxy**: Access control cho premium/paid content
  - **Caching Proxy**: Cache video thumbnails/metadata
- **Ví dụ**: StreamingVideo (RealSubject), VideoProxy (Proxy)
- **Ưu điểm**: Rất phù hợp với Proxy pattern, realistic, dễ hiểu

#### Option 2: **Smart Home Security System** (liên kết với Mediator)
- **Liên kết**: Smart Home Automation (Mediator)
- **Use case**:
  - **Protection Proxy**: Access control cho smart home devices (chỉ owner mới control)
  - **Remote Proxy**: Remote access to home devices
  - **Logging Proxy**: Log all access attempts
- **Ví dụ**: SmartDevice (RealSubject), SecureDeviceProxy (Proxy)

#### Option 3: **Restaurant Image Gallery** (liên kết với Composite)
- **Liên kết**: Restaurant Menu System (Composite)
- **Use case**:
  - **Virtual Proxy**: Lazy load high-res food images
  - **Caching Proxy**: Cache displayed images
- **Ví dụ**: FoodImage (RealSubject), ImageProxy (Proxy)

#### Option 4: **Document Management System** (liên kết với Singleton)
- **Liên kết**: Configuration Manager (Singleton)
- **Use case**:
  - **Protection Proxy**: Access control based on user role
  - **Virtual Proxy**: Load documents on demand
- **Ví dụ**: Document (RealSubject), SecureDocumentProxy (Proxy)

### 💡 Recommendation:
**Chọn Option 1 (Video Streaming Platform)** vì:
- ✅ Liên kết mạnh với 2 patterns đã học (Observer + Adapter)
- ✅ Proxy pattern rất phù hợp với video/media scenarios
- ✅ Có thể demo nhiều types của Proxy (Virtual, Protection, Caching)
- ✅ Realistic và dễ hiểu
- ✅ User đã quen với video/media domain

## Yêu cầu đầu ra
Đối với Proxy Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu

- Đọc file `Documents/Lectures/Proxy.pdf` để:
  - Hiểu BẢN CHẤT của Proxy Pattern (không phải học bài toán cụ thể)
  - Hiểu surrogate/placeholder concept
  - Hiểu các types of proxy: Virtual, Protection, Remote, Smart
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram với Subject, RealSubject, Proxy
  - Hiểu khi nào dùng proxy nào

- Đọc code trong `Code-Sample/ProxyPattern-Project/` để:
  - Học cách đặt tên class, method, interface
  - Học coding convention và code style
  - Học cách implement Subject interface
  - Học cách Proxy delegates to RealSubject
  - Học cách add additional functionality trong Proxy
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Proxy (placeholder/surrogate):
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: Image Proxy (quá phổ biến)
- **KHÔNG dùng**: Report Generator (có trong code sample)

**⭐ RECOMMENDED: Chọn context có liên kết với patterns đã học**

**Gợi ý các lĩnh vực có thể dùng (với liên kết)**:

### 🎬 Video/Media Domain (liên kết Observer + Adapter):

1. **Video Streaming Platform** ⭐ BEST
   - Subject: Video interface
   - RealSubject: StreamingVideo (actual video với heavy data)
   - Proxy: VideoProxy (placeholder, lazy loading, access control)
   - Types: Virtual Proxy (lazy load), Protection Proxy (premium content)
   - Ví dụ: Netflix, YouTube video loading

2. **Music/Audio Streaming**
   - Liên kết: Media Player (Adapter)
   - Virtual Proxy cho audio files
   - Caching Proxy cho frequently played songs

3. **Live TV Streaming**
   - Remote Proxy cho TV channels
   - Protection Proxy cho subscription channels

### 🏠 Smart Home Domain (liên kết Mediator):

4. **Smart Home Device Access**
   - Protection Proxy cho device control
   - Remote Proxy cho remote access
   - Logging Proxy cho security

5. **Smart Camera Feed**
   - Virtual Proxy cho camera streams
   - Caching Proxy cho recorded footage

### 🍽️ Restaurant Domain (liên kết Composite):

6. **Restaurant Menu Images**
   - Virtual Proxy cho high-res food images
   - Caching Proxy để improve performance

7. **Online Food Ordering**
   - Protection Proxy cho payment processing
   - Virtual Proxy cho menu loading

### 💼 Enterprise Software Domain (liên kết Singleton):

8. **Document Management**
   - Virtual Proxy cho large documents
   - Protection Proxy dựa trên user permissions
   - Logging Proxy cho audit trail

9. **Database Access**
   - Virtual Proxy cho connection pooling
   - Protection Proxy cho access control

### 📱 General Options (nếu không muốn liên kết):

10. **Cloud Storage Files**
    - Virtual Proxy cho file downloads
    - Caching Proxy cho frequently accessed files

11. **Bank Account Operations**
    - Protection Proxy cho transactions
    - Logging Proxy cho audit

12. **E-book Reader**
    - Virtual Proxy cho book content
    - Protection Proxy cho purchased books

13. **3D Model Viewer**
    - Virtual Proxy cho large 3D models
    - Caching Proxy cho viewed models

14. **Social Media Posts**
    - Virtual Proxy cho post loading
    - Caching Proxy cho timeline

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có

**Proxy Pattern có 4 types chính**:

**1. Virtual Proxy** (Lazy Initialization):
- Placeholder cho expensive object
- Object chỉ được tạo khi thực sự cần
- Save memory và time
- Example: Load video chỉ khi user clicks play

**2. Protection Proxy** (Access Control):
- Kiểm tra permissions trước khi access real object
- Based on user role, subscription, etc.
- Security và authorization
- Example: Premium content chỉ cho paid users

**3. Remote Proxy** (Distributed Systems):
- Local representative của remote object
- Hide complexity of network communication
- Example: API calls, RPC

**4. Smart Proxy** (Additional Functionality):
- Add extra functionality: caching, logging, reference counting
- Example: Cache results, log access times

**Bài toán nên demonstrate ít nhất 1-2 types of proxy**

**Đặc điểm của Proxy Pattern**:
- **Subject Interface**: Common interface cho RealSubject và Proxy
- **RealSubject**: Actual object doing real work
- **Proxy**: Surrogate/placeholder, same interface as RealSubject
- **Delegation**: Proxy delegates to RealSubject khi cần
- **Additional Logic**: Proxy có thể add logic before/after delegation
- **Transparent**: Client không biết đang dùng Proxy hay RealSubject

**Quan trọng**:
- Proxy và RealSubject PHẢI implement cùng interface
- Proxy maintains reference đến RealSubject
- Proxy controls access to RealSubject
- Client code uses Proxy như dùng RealSubject

#### 2.3. Thiết kế các thành phần

**Subject Interface**:
```java
public interface Subject {
    void request();
    // Common methods for both RealSubject and Proxy
}
```

**RealSubject**:
```java
public class RealSubject implements Subject {
    public RealSubject() {
        // Expensive initialization
        System.out.println("Creating RealSubject - expensive operation");
    }

    @Override
    public void request() {
        System.out.println("RealSubject handling request");
    }
}
```

**Proxy**:
```java
public class Proxy implements Subject {
    private RealSubject realSubject;

    @Override
    public void request() {
        // Additional logic before delegation
        if (realSubject == null) {
            realSubject = new RealSubject();  // Lazy initialization
        }

        // Delegate to RealSubject
        realSubject.request();

        // Additional logic after delegation
    }
}
```

#### 2.4. Types of Proxy Examples

**Virtual Proxy Example** (Lazy Loading):
```java
public class VideoProxy implements Video {
    private RealVideo realVideo;
    private String filename;

    public VideoProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void play() {
        if (realVideo == null) {
            realVideo = new RealVideo(filename);  // Load only when needed
        }
        realVideo.play();
    }
}
```

**Protection Proxy Example** (Access Control):
```java
public class ProtectedVideoProxy implements Video {
    private RealVideo realVideo;
    private User currentUser;

    @Override
    public void play() {
        if (currentUser.hasPremiumAccess()) {
            if (realVideo == null) {
                realVideo = new RealVideo(filename);
            }
            realVideo.play();
        } else {
            System.out.println("Access denied - Premium required");
        }
    }
}
```

### Bước 3: Viết Documents/Solutions/Proxy.md

Tạo file `Documents/Solutions/Proxy.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Proxy
- Giữ phần mô tả tổng quan về Proxy Pattern
- Các thành phần chính: Subject, RealSubject, Proxy
- Khi nào sử dụng: expensive objects, access control, remote objects
- Đặc điểm quan trọng: surrogate, same interface, delegation, transparency
- 4 types of proxy: Virtual, Protection, Remote, Smart

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- **Nhấn mạnh liên kết với context đã học** (nếu có)
- Bài toán cần:
  - Có expensive/sensitive resource cần proxy
  - Nêu rõ vấn đề nếu không dùng Proxy
  - Giải thích tại sao cần placeholder/surrogate
  - Có tình huống cụ thể minh họa ít nhất 1-2 proxy types

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Có expensive object hoặc sensitive resource
- Cần control access hoặc lazy initialization
- Client code cần access resource
- Same interface cho real object và proxy

**Problem**: Vấn đề phức tạp cần giải quyết
- Creating expensive objects upfront wastes resources
- Uncontrolled access to sensitive resources
- Direct access to remote objects is complex
- No logging/caching/additional functionality

**Solution**: Cách Proxy pattern giải quyết
- Proxy as placeholder/surrogate
- Lazy initialization (Virtual Proxy)
- Access control (Protection Proxy)
- Hide remote complexity (Remote Proxy)
- Add extra functionality (Smart Proxy)
- Transparent to client

**Expected Output**: Kết quả mong đợi
- Expensive objects created only when needed
- Access properly controlled
- Additional functionality added transparently
- Same interface for client code
- Performance improved (lazy loading, caching)

#### 3.4. Hiệu quả của việc sử dụng Proxy Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh với cách không dùng Proxy (direct access)
- Performance benefits (lazy loading, caching)
- Security benefits (access control)
- Transparency to client
- Trade-offs: extra indirection, complexity

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Phải có:
  - 1 Subject interface
  - 1 RealSubject class (actual implementation)
  - 1-2 Proxy classes (demonstrate different types)
  - 1 Demo/Main class
- Code phải hoàn chỉnh và có thể compile
- Coding style học từ code sample
- Demo multiple scenarios

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Demo lazy loading (Virtual Proxy)
- Demo access control (Protection Proxy) nếu có
- Demo caching/logging (Smart Proxy) nếu có
- Giải thích cách pattern hoạt động qua output
- Show performance benefits

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Phải có:
  - Subject interface
  - RealSubject implementing Subject
  - Proxy (or multiple Proxies) implementing Subject
  - Proxy maintains reference to RealSubject
  - Client uses Proxy
- Thể hiện delegation relationship

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Proxy
- Comparison với Decorator pattern

### Bước 4: Viết code Java cho bài toán mới trong 8-Proxy-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `8-Proxy-DP/`:

**Interfaces**:
- Subject.java (interface)

**Concrete Classes**:
- RealSubject.java (actual implementation)
- Proxy1.java (Virtual Proxy hoặc Protection Proxy)
- Proxy2.java (optional - different type of proxy)

**Demo**:
- ProxyDemo.java (Main class)

**Ví dụ cấu trúc** (Video Streaming):
```
8-Proxy-DP/
├── Video.java                  (Subject interface)
├── RealVideo.java              (RealSubject - actual video)
├── VideoProxy.java             (Virtual Proxy - lazy loading)
├── ProtectedVideoProxy.java    (Protection Proxy - access control)
└── ProxyDemo.java              (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: nếu cần
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Proxy và RealSubject implement cùng interface
- Proxy delegates to RealSubject

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Proxy pattern
- Demo rõ ràng benefits của proxy
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Proxy.md

#### 4.4. Demo Requirements
Demo phải thể hiện:
1. Create Proxy (not RealSubject yet)
2. Call methods on Proxy
3. Proxy creates RealSubject when needed (lazy loading)
4. Proxy delegates to RealSubject
5. Show access control (if Protection Proxy)
6. Show caching/logging (if Smart Proxy)
7. Demonstrate transparency (same interface)

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `8-Proxy-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Subject interface
- Hiển thị RealSubject và Proxy(ies)
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationships:
  - RealSubject implements Subject
  - Proxy implements Subject
  - Proxy maintains reference to RealSubject (aggregation)
  - Client uses Proxy
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=RealSubject
dependency1.to=Subject
dependency1.type=ImplementsDependency

dependency2.from=Proxy
dependency2.to=Subject
dependency2.type=ImplementsDependency

dependency3.from=Proxy
dependency3.to=RealSubject
dependency3.type=UsesDependency

dependency4.from=ProxyDemo
dependency4.to=Subject
dependency4.type=UsesDependency

# Targets với showInterface=true
target1.name=Subject
target1.type=InterfaceTarget
target1.showInterface=true
...

target2.name=RealSubject
target2.type=ClassTarget
target2.showInterface=true
...

target3.name=Proxy
target3.type=ClassTarget
target3.showInterface=true
...
```

#### 5.3. Layout gợi ý
```
Top center:
- Subject interface

Middle:
- RealSubject (left, dưới Subject)
- Proxy (right, dưới Subject)
- Arrow từ Proxy → RealSubject (aggregation)

Bottom:
- ProxyDemo (client)
```

## Deliverables

### 1. File Documents/Solutions/Proxy.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- **Nhấn mạnh liên kết với context đã học** (nếu chọn Video/Smart Home/Restaurant/Enterprise)
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ surrogate/placeholder concept
- Minh họa ít nhất 1-2 types of proxy

### 2. Folder 8-Proxy-DP/
Chứa các file Java cho bài toán MỚI:
- Subject interface
- 1 RealSubject
- 1-2 Proxy classes (different types)
- 1 Demo/Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Demo rõ lazy loading hoặc access control

### 3. File 8-Proxy-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ Subject interface và implementations
- Thể hiện rõ Proxy → RealSubject aggregation

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Có liên kết với context đã học**: Video/Smart Home/Restaurant/Enterprise (preferred)
✅ **Expensive or sensitive resource**: Cần proxy để control access
✅ **Same interface**: RealSubject và Proxy implement chung interface
✅ **Delegation**: Proxy delegates to RealSubject
✅ **Additional logic**: Proxy add logic (lazy loading, access control, caching, logging)
✅ **Transparency**: Client không biết đang dùng Proxy
✅ **Demonstrate 1-2 types**: Virtual Proxy và/hoặc Protection Proxy
✅ **Performance benefit**: Show lazy loading saves resources
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Subject, RealSubject, Proxy structure
✅ **Khác biệt**: Không trùng với lecture hay code sample

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy Image Proxy (quá phổ biến)
❌ Copy Report Generator (có trong code sample)
❌ Context hoàn toàn mới không liên kết (khó nhớ)
❌ Không có expensive operation (không thấy benefit)
❌ Proxy không add value (chỉ forward calls)
❌ Quá đơn giản: không demo proxy benefits
❌ Quá phức tạp: quá nhiều business logic không liên quan

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **NÊN có liên kết**: Chọn context liên kết với patterns đã học để dễ nhớ
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "surrogate/placeholder" concept

### Về Proxy Pattern
- Subject interface **PHẢI** có chung cho RealSubject và Proxy
- Proxy **PHẢI** maintain reference đến RealSubject
- **PHẢI** có delegation từ Proxy đến RealSubject
- Proxy **NÊN** add additional logic (lazy loading, access control, caching, logging)
- Client code uses Proxy như dùng RealSubject (transparency)
- Demo **PHẢI** thể hiện benefits của proxy

### Về Types of Proxy
- **Virtual Proxy**: Focus on lazy initialization, save resources
- **Protection Proxy**: Focus on access control, security
- **Remote Proxy**: Focus on hiding network complexity (optional)
- **Smart Proxy**: Focus on caching, logging, reference counting (optional)
- Nên implement ít nhất 1-2 types trong bài toán

### Về Implementation
- **Recommended**: Implement Virtual Proxy + Protection Proxy
- RealSubject: Expensive to create (heavy object, load từ file/network)
- Proxy: Lightweight placeholder, creates RealSubject khi cần
- Interface: Common methods cho both RealSubject và Proxy
- Demo: Show creation time, access control, performance

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Subject interface với common methods
- RealSubject: Expensive initialization trong constructor
- Proxy: Lazy create RealSubject, delegate calls
- Demo rõ ràng: create proxy → call methods → show lazy loading

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Subject: InterfaceTarget
- RealSubject và Proxy: ClassTarget implementing Subject
- Association: Proxy → RealSubject (aggregation diamond)
- Format phải giống lecture

### Về documentation
- Documents/Solutions/Proxy.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- **Highlight liên kết với context đã học** trong phần mô tả bài toán
- Giải thích rõ ràng surrogate/placeholder concept
- So sánh với cách không dùng Proxy (direct access)
- Nêu rõ trade-offs (indirection overhead)
- Compare với Decorator pattern

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

### ⭐ RECOMMENDED: Video Streaming Platform (liên kết Observer + Adapter)

**Context**: Liên kết với YouTube Channel (Observer pattern) và Media Player (Adapter pattern)

**Problem**:
- User browse video catalog với 1000+ videos
- Loading tất cả videos upfront → waste memory và bandwidth
- Some videos are premium → need access control
- Videos are large (100MB-1GB)

**Solution**:
- **Virtual Proxy**: VideoProxy không load actual video cho đến khi user clicks play
- **Protection Proxy**: PremiumVideoProxy checks subscription trước khi play
- Video interface: `display()`, `play()`, `pause()`
- RealVideo: Load actual video file (expensive)
- VideoProxy: Lightweight placeholder, lazy load

**Classes**:
```java
interface Video { void display(); void play(); }
class RealVideo implements Video { /* Heavy loading */ }
class VideoProxy implements Video { /* Lazy loading */ }
class PremiumVideoProxy implements Video { /* Access control */ }
```

**Demo**:
1. Browse 100 videos → only proxies created (fast)
2. Click video 1 → RealVideo loaded (slow first time)
3. Click video 2 (premium) without subscription → access denied
4. Subscribe → access granted

### Other Examples (chỉ structure, KHÔNG copy):

**Smart Home Device Access** (liên kết Mediator):
- Problem: Direct access to smart devices → security risk
- Solution: SecureDeviceProxy checks authentication
- Classes: SmartDevice interface, RealDevice, SecureDeviceProxy

**Restaurant Image Gallery** (liên kết Composite):
- Problem: High-res food images are large → slow loading
- Solution: ImageProxy lazy loads images khi user scrolls
- Classes: Image interface, RealImage, ImageProxy

**Document Management** (liên kết Singleton):
- Problem: Large documents and access control needed
- Solution: DocumentProxy with lazy loading + permission check
- Classes: Document interface, RealDocument, SecureDocumentProxy

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết methods phù hợp
- Demo rõ proxy benefits

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Proxy
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Đã chọn context có liên kết với patterns đã học (nếu có thể)
- [ ] Bài toán có expensive/sensitive resource cần proxy
- [ ] Có Subject interface cho RealSubject và Proxy
- [ ] Proxy delegates to RealSubject
- [ ] Proxy adds additional logic (lazy loading, access control, etc.)
- [ ] Demo ít nhất 1-2 types of proxy
- [ ] Documents/Solutions/Proxy.md có đầy đủ 8 sections
- [ ] Code trong 8-Proxy-DP/ là code MỚI cho bài toán mới
- [ ] Demo thể hiện lazy loading hoặc access control
- [ ] Demo thể hiện transparency (same interface)
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong Documents/Solutions/Proxy.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram có Subject interface
- [ ] UML diagram có RealSubject và Proxy implementing Subject
- [ ] UML diagram có aggregation từ Proxy → RealSubject
- [ ] UML diagram format giống lecture
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng proxy benefits
- [ ] Đã giải thích trade-offs của Proxy (indirection)
- [ ] Đã so sánh với Decorator pattern

## Common Pitfalls cần tránh

### Proxy Pattern có thể gây vấn đề nếu:
❌ Proxy chỉ forward calls (không add value)
❌ Interface quá generic (không meaningful)
❌ Overhead của proxy lớn hơn benefit
❌ Proxy và RealSubject không cùng interface
❌ Proxy không truly transparent
❌ Too many proxy layers (confusion)

### Best Practices:
✅ Proxy và RealSubject share same interface
✅ Proxy adds meaningful value (lazy loading, security, caching)
✅ Keep proxy lightweight
✅ Document what type of proxy it is
✅ Consider performance tradeoffs
✅ Use factory nếu có multiple proxy types

### When to use Proxy:
✅ Khi object expensive to create (Virtual Proxy)
✅ Khi cần access control (Protection Proxy)
✅ Khi object ở remote location (Remote Proxy)
✅ Khi cần add functionality transparently (Smart Proxy)
✅ Khi cần lazy initialization
✅ Khi cần logging/caching

### When NOT to use Proxy:
❌ Khi object không expensive (overhead không worth it)
❌ Khi không cần lazy loading hoặc access control
❌ Khi có thể dùng simpler solution
❌ Khi proxy adds no value
❌ Khi transparency không quan trọng

## Proxy vs Decorator

### Key Differences:

**Proxy Pattern**:
- **Purpose**: Control access, lazy loading, surrogate
- **Focus**: Control và manage access to object
- **Creation**: Proxy creates/manages RealSubject
- **Intent**: Provide placeholder/surrogate
- **Example**: Lazy loading video, access control

**Decorator Pattern**:
- **Purpose**: Add functionality, enhance behavior
- **Focus**: Add new responsibilities dynamically
- **Creation**: Decorator wraps existing object
- **Intent**: Extend functionality without subclassing
- **Example**: Add scrollbar to window, add encryption

### Similarities:
- Both have same interface as wrapped object
- Both delegate to wrapped object
- Both add additional logic

### When to use which:

**Use Proxy when**:
- Need control access
- Need lazy initialization
- Need placeholder/surrogate
- Focus on managing object lifecycle

**Use Decorator when**:
- Need add functionality
- Need multiple combinations
- Need runtime enhancement
- Focus on extending behavior

**Key distinction**: Proxy controls access, Decorator adds responsibility.
