# Plan Task: Tạo Bài Toán Mới Cho Flyweight Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Flyweight Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## 🎯 YÊU CẦU ĐẶC BIỆT: Context Liên Kết (Tiếp tục)

Để dễ nhớ (vì có 24 design patterns), bài toán nên chọn **context liên kết** với các patterns đã làm:

### Contexts đã sử dụng:
1. **Adapter**: Media Player (Audio/Video players)
2. **Facade**: Home Theater System (Movie watching)
3. **Composite**: Restaurant Menu System (Golden Fork Restaurant)
4. **Bridge**: Notification System (Messages)
5. **Singleton**: Configuration Manager (EnterpriseSoft ERP)
6. **Observer**: YouTube Channel (TechReview Pro - Emma)
7. **Mediator**: Smart Home Automation (James's apartment)
8. **Proxy**: Video Streaming Platform (StreamFlix - Lisa)
9. **Chain of Responsibility**: Customer Support System (EnterpriseSoft ERP)

### 🌟 RECOMMENDED Contexts cho Flyweight (có liên kết):

#### Option 1: **Video Player UI Icons** ⭐ BEST (liên kết với Video domain)
- **Liên kết**: StreamFlix (Proxy), YouTube (Observer), Media Player (Adapter)
- **Use case**:
  - Video player interface với thousands of videos
  - Mỗi video hiển thị play button, pause, share, like icons
  - Icons giống nhau → share objects thay vì create thousands
  - Intrinsic state: icon image, color, type
  - Extrinsic state: position (x, y) for each video
- **Ví dụ**: VideoIcon flyweight, IconFactory, VideoPlayerUI
- **Ưu điểm**: Perfect cho Flyweight, links to 3 video/media patterns

#### Option 2: **Restaurant Menu Item Icons** (liên kết với Restaurant)
- **Liên kết**: Golden Fork Restaurant (Composite)
- **Use case**:
  - Menu display system với hundreds of items
  - Mỗi item có category icon (vegetarian, spicy, chef's special)
  - Share icons across menu items
  - Intrinsic: icon image, symbol
  - Extrinsic: position, menu item
- **Ví dụ**: MenuIcon flyweight, IconFactory

#### Option 3: **ERP Dashboard Widgets** (liên kết với Enterprise)
- **Liên kết**: EnterpriseSoft ERP (Singleton, Chain of Responsibility)
- **Use case**:
  - Dashboard với hundreds of widgets
  - Each widget có status icon (success, warning, error)
  - Share icon objects
  - Intrinsic: icon type, color
  - Extrinsic: position, widget ID
- **Ví dụ**: StatusIcon flyweight

#### Option 4: **Smart Home Device Icons** (liên kết với Smart Home)
- **Liên kết**: Smart Home Automation (Mediator)
- **Use case**:
  - Smart home dashboard với many devices
  - Each device type có icon (light bulb, thermostat, camera)
  - Share icons for same device types
  - Intrinsic: device icon
  - Extrinsic: location, device ID

#### Option 5: **Document Text Characters** (classic Flyweight)
- **Use case**:
  - Text editor với thousands of characters
  - Share character objects (same char, same font = same object)
  - Intrinsic: character value, font, size
  - Extrinsic: position in document
- **Note**: Classic example nhưng không link với patterns đã học

### 💡 Recommendation:
**Chọn Option 1 (Video Player UI Icons)** vì:
- ✅ Liên kết MẠNH với Video/Media domain (Proxy, Observer, Adapter)
- ✅ Tạo "memory cluster": Video Platform = Proxy + Observer + Adapter + Flyweight
- ✅ Flyweight rất phù hợp: thousands of videos = thousands of icons
- ✅ Realistic: YouTube, Netflix thực tế dùng Flyweight cho UI
- ✅ Easy to understand và visualize
- ✅ Clear separation: intrinsic (icon image) vs extrinsic (position)

**Alternative**: Option 2 (Restaurant Icons) nếu muốn link với Composite

## Yêu cầu đầu ra
Đối với Flyweight Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu

- Đọc file `Documents/Lectures/Flyweight.pdf` để:
  - Hiểu BẢN CHẤT của Flyweight Pattern
  - Hiểu intrinsic state vs extrinsic state
  - Hiểu cách share objects để save memory
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram với Flyweight, FlyweightFactory
  - Hiểu khi nào dùng và không dùng Flyweight

- Đọc code trong `Code-Sample/Flyweight-Pattern-Project/` để:
  - Học cách đặt tên class, method, interface
  - Học coding convention và code style
  - Học cách implement Flyweight interface
  - Học cách implement FlyweightFactory (object pool)
  - Học cách separate intrinsic và extrinsic state
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Flyweight:
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: Code compiler (có thể có trong code sample)
- **KHÔNG dùng**: Simple text characters (quá đơn giản)

**⭐ RECOMMENDED: Chọn context có liên kết với patterns đã học**

**Gợi ý các lĩnh vực có thể dùng (với liên kết)**:

### 🎬 Video/Media Domain (liên kết Proxy + Observer + Adapter):

1. **Video Player UI Icons System** ⭐ BEST
   - Thousands of videos displayed in grid
   - Each video has: play icon, pause icon, share icon, like icon
   - Icons are identical → share flyweight objects
   - Intrinsic: icon type, image data, color
   - Extrinsic: position (x, y), video ID
   - Real-world: YouTube, Netflix UI
   - **Memory savings**: 10,000 videos × 4 icons × 500KB = 20GB WITHOUT flyweight
   - **With flyweight**: 4 shared icons × 500KB = 2MB (10,000x reduction!)

2. **Video Thumbnail Border Styles**
   - Video grid với different border styles (new, trending, watched)
   - Share border style objects
   - Intrinsic: border style, color, thickness
   - Extrinsic: video position, video ID

3. **Video Quality Badge Icons**
   - Videos có quality badges (HD, 4K, 8K)
   - Share badge objects
   - Intrinsic: badge image
   - Extrinsic: position on video

### 🍽️ Restaurant Domain (liên kết Composite):

4. **Menu Item Category Icons**
   - Hundreds of menu items
   - Each item có category icons (vegetarian 🌱, spicy 🌶️, chef's special ⭐)
   - Share icon objects
   - Intrinsic: icon image, category type
   - Extrinsic: menu item, position

5. **Table Reservation Icons**
   - Restaurant floor plan với table status icons
   - Icons: available, reserved, occupied
   - Share icon objects

### 💼 Enterprise Domain (liên kết Singleton + Chain):

6. **ERP Dashboard Status Icons**
   - Dashboard với hundreds of widgets/metrics
   - Each widget có status icon (✓ success, ⚠ warning, ✗ error)
   - Share icon objects
   - Intrinsic: icon type, color
   - Extrinsic: widget position, metric ID

7. **Email Status Flags**
   - Email list với thousands of emails
   - Each email có flags: unread, important, starred
   - Share flag icons

### 🏠 Smart Home Domain (liên kết Mediator):

8. **Smart Home Device Icons**
   - Dashboard displaying many devices
   - Device type icons: light 💡, thermostat 🌡️, camera 📷, lock 🔒
   - Share icon objects for same device types
   - Intrinsic: device type icon
   - Extrinsic: device location, device ID

9. **Notification Type Icons**
   - Smart home notifications
   - Icon types: info, warning, alert
   - Share notification icons

### 📱 General Options:

10. **Game Particle System**
    - Game với thousands of particles (bullets, explosions, rain drops)
    - Share particle types (same image, behavior)
    - Intrinsic: particle type, image, physics
    - Extrinsic: position, velocity

11. **Map Marker System**
    - Map với thousands of markers
    - Marker types: restaurant, hotel, gas station
    - Share marker icon objects
    - Intrinsic: marker icon, type
    - Extrinsic: GPS coordinates

12. **Forest Rendering**
    - 3D game forest với thousands of trees
    - Tree types: oak, pine, maple
    - Share tree models
    - Intrinsic: tree model, texture
    - Extrinsic: position, scale, rotation

13. **Document Font Styles**
    - Text editor với thousands of characters
    - Share font style objects
    - Intrinsic: font family, size, weight
    - Extrinsic: character position

14. **Chess Game Pieces**
    - Multiple chess games running
    - Share piece objects (white pawn, black knight, etc.)
    - Intrinsic: piece type, color, icon
    - Extrinsic: board position, game ID

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có

**Đặc điểm của Flyweight Pattern**:

1. **Flyweight Interface/Class**:
   - Defines methods that accept extrinsic state
   - Contains intrinsic state (shared)
   - Stateless or immutable for intrinsic parts

2. **ConcreteFlyweight**:
   - Implements Flyweight interface
   - Stores intrinsic state (shared among instances)
   - Methods accept extrinsic state as parameters
   - MUST be shareable (immutable intrinsic state)

3. **FlyweightFactory**:
   - Creates and manages flyweight objects
   - Returns existing flyweight if exists (object pool)
   - Creates new flyweight only if needed
   - Uses HashMap/Map to store flyweights
   - **Key method**: `getFlyweight(key)` → returns shared object

4. **Client**:
   - Maintains extrinsic state
   - Calls flyweight methods với extrinsic state
   - Doesn't know about sharing (transparent)

**Bài toán phải demonstrate**:
- ✅ Large number of objects (thousands)
- ✅ Objects có shared state (intrinsic) và unique state (extrinsic)
- ✅ Clear separation: intrinsic vs extrinsic
- ✅ FlyweightFactory manages object pool
- ✅ Memory savings significant (show before/after)
- ✅ Flyweights are immutable (intrinsic state)

**Quan trọng**:
- Intrinsic state PHẢI immutable và shareable
- Extrinsic state PHẢI passed as parameters (not stored in flyweight)
- Factory PHẢI reuse existing flyweights (object pool pattern)
- PHẢI có nhiều objects (ít nhất hàng trăm, tốt nhất hàng nghìn)
- Memory savings PHẢI significant (ít nhất 10x, tốt nhất 100x+)

#### 2.3. Thiết kế các thành phần

**Flyweight Interface** (nếu cần):
```java
public interface Icon {
    void render(int x, int y, String context);
}
```

**ConcreteFlyweight**:
```java
public class PlayIcon implements Icon {
    // Intrinsic state (shared)
    private final String iconImage;
    private final String color;
    private final int size;

    public PlayIcon(String iconImage, String color, int size) {
        this.iconImage = iconImage;
        this.color = color;
        this.size = size;
        // Heavy object - simulate loading
        System.out.println("Creating PlayIcon object (500KB)");
    }

    @Override
    public void render(int x, int y, String context) {
        // Use intrinsic state + extrinsic state (x, y, context)
        System.out.println("Rendering play icon at (" + x + "," + y + ") for " + context);
    }
}
```

**FlyweightFactory**:
```java
public class IconFactory {
    private static Map<String, Icon> iconPool = new HashMap<>();

    public static Icon getIcon(String type) {
        Icon icon = iconPool.get(type);
        if (icon == null) {
            // Create new flyweight
            switch (type) {
                case "play":
                    icon = new PlayIcon("play.png", "white", 64);
                    break;
                case "pause":
                    icon = new PauseIcon("pause.png", "white", 64);
                    break;
                // ... more types
            }
            iconPool.put(type, icon);
            System.out.println("Created NEW flyweight: " + type);
        } else {
            System.out.println("Reused EXISTING flyweight: " + type);
        }
        return icon;
    }

    public static int getPoolSize() {
        return iconPool.size();
    }
}
```

**Client**:
```java
public class VideoPlayer {
    private List<Video> videos;

    public void displayGrid() {
        for (Video video : videos) {
            // Get shared icon from factory
            Icon playIcon = IconFactory.getIcon("play");
            Icon likeIcon = IconFactory.getIcon("like");

            // Render with extrinsic state (position)
            playIcon.render(video.getX(), video.getY(), video.getTitle());
            likeIcon.render(video.getX() + 50, video.getY(), video.getTitle());
        }
    }
}
```

### Bước 3: Viết Documents/Solutions/Flyweight.md

Tạo file `Documents/Solutions/Flyweight.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Flyweight
- Giữ phần mô tả tổng quan về Flyweight Pattern
- Các thành phần chính: Flyweight, ConcreteFlyweight, FlyweightFactory
- Khi nào sử dụng: large number of objects, shared state
- Đặc điểm quan trọng: intrinsic vs extrinsic state, object pooling

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- **Nhấn mạnh liên kết với context đã học** (nếu có)
- Bài toán cần:
  - Có large number of similar objects
  - Objects có shared state (intrinsic) và unique state (extrinsic)
  - Nêu rõ vấn đề memory nếu không dùng Flyweight
  - Giải thích tại sao cần share objects
  - Có tình huống cụ thể minh họa với numbers (trước/sau memory)

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Large number of objects cần tạo (thousands)
- Objects có similar properties (intrinsic)
- Objects có unique properties (extrinsic)
- Memory constraints (limited memory)

**Problem**: Vấn đề phức tạp cần giải quyết
- Creating thousands of objects = huge memory usage
- Slow performance (object creation overhead)
- Memory limit exceeded
- Duplicate data stored many times
- Inefficient resource usage

**Solution**: Cách Flyweight giải quyết
- Share objects với same intrinsic state
- Factory manages object pool
- Reuse existing objects instead of creating new
- Pass extrinsic state as parameters
- Significant memory savings

**Expected Output**: Kết quả mong đợi
- Memory usage reduced dramatically (show numbers)
- Faster object creation (reuse from pool)
- Same functionality with shared objects
- Transparent to client (doesn't know about sharing)

#### 3.4. Hiệu quả của việc sử dụng Flyweight Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh memory usage: Before vs After (with numbers!)
- Performance improvement (creation time)
- Trade-offs: complexity vs memory savings
- Khi nào nên và không nên dùng

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Phải có:
  - 1 Flyweight interface hoặc abstract class (optional)
  - 3-4 ConcreteFlyweight classes
  - 1 FlyweightFactory class (with HashMap pool)
  - 1 Client class (uses extrinsic state)
  - 1 Demo/Main class
- Code phải hoàn chỉnh và có thể compile
- Coding style học từ code sample

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Demo creating thousands of objects
- Show: "Created NEW flyweight" vs "Reused EXISTING flyweight"
- Show memory usage before/after
- Show pool size (số lượng unique flyweights)
- Giải thích cách pattern hoạt động qua output

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Phải có:
  - Flyweight interface/abstract class (optional)
  - Multiple ConcreteFlyweights implementing Flyweight
  - FlyweightFactory class
  - Client class
  - Relationships (Factory creates/manages Flyweights)
- Thể hiện object pool trong Factory

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Flyweight
- Trade-offs: memory vs complexity
- Alternatives: Object pooling, Prototype pattern

### Bước 4: Viết code Java cho bài toán mới trong 10-Flyweight-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `10-Flyweight-DP/`:

**Interface** (optional):
- Icon.java (hoặc Flyweight.java)

**Concrete Flyweights**:
- PlayIcon.java
- PauseIcon.java
- LikeIcon.java
- ShareIcon.java

**Factory**:
- IconFactory.java (manages object pool)

**Client**:
- Video.java (has extrinsic state)
- VideoPlayer.java (uses flyweights)

**Demo**:
- FlyweightDemo.java (Main class)

**Ví dụ cấu trúc** (Video Player Icons):
```
10-Flyweight-DP/
├── Icon.java                  (Flyweight interface)
├── PlayIcon.java              (ConcreteFlyweight)
├── PauseIcon.java             (ConcreteFlyweight)
├── LikeIcon.java              (ConcreteFlyweight)
├── ShareIcon.java             (ConcreteFlyweight)
├── IconFactory.java           (FlyweightFactory with pool)
├── Video.java                 (Context - has extrinsic state)
└── FlyweightDemo.java         (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần
- Import statements: java.util.HashMap, java.util.Map
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Factory PHẢI có HashMap để store flyweights
- Intrinsic state PHẢI immutable (final fields)
- Extrinsic state passed as method parameters

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Flyweight pattern
- Demo rõ ràng object reuse
- Show memory savings với numbers
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Flyweight.md

#### 4.4. Demo Requirements
Demo phải thể hiện:
1. Create large number of objects (100-10,000)
2. Show flyweight creation: "Created NEW" vs "Reused EXISTING"
3. Calculate memory savings (before/after)
4. Show pool size (number of unique flyweights)
5. Show that flyweights are shared (same object references)
6. Demonstrate extrinsic state usage (different positions)
7. Print statistics: total objects vs shared flyweights

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `10-Flyweight-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Flyweight interface (nếu có)
- Hiển thị ConcreteFlyweights
- Hiển thị FlyweightFactory
- Hiển thị Client/Context classes
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationships:
  - ConcreteFlyweights implement Flyweight
  - Factory creates/manages Flyweights (composition)
  - Client uses Factory and Flyweights
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=PlayIcon
dependency1.to=Icon
dependency1.type=UsesDependency

dependency2.from=IconFactory
dependency2.to=Icon
dependency2.type=UsesDependency

dependency3.from=VideoPlayer
dependency3.to=IconFactory
dependency3.type=UsesDependency

# Similar for other flyweights...

# Targets với showInterface=true
target1.name=Icon
target1.type=InterfaceTarget
target1.showInterface=true
...
```

#### 5.3. Layout gợi ý
```
Top:
- Icon (interface)

Middle (horizontal):
- PlayIcon, PauseIcon, LikeIcon, ShareIcon (all implement Icon)

Bottom left:
- IconFactory (with pool HashMap)

Bottom right:
- Video (context với extrinsic state)
- VideoPlayer (client)
- FlyweightDemo (main)
```

## Deliverables

### 1. File Documents/Solutions/Flyweight.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- **Nhấn mạnh liên kết với context đã học** (Video Player UI → StreamFlix, YouTube, Media Player)
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ intrinsic vs extrinsic state
- Minh họa memory savings với NUMBERS cụ thể

### 2. Folder 10-Flyweight-DP/
Chứa các file Java cho bài toán MỚI:
- Flyweight interface (optional)
- 3-4 ConcreteFlyweights
- FlyweightFactory với HashMap pool
- Client/Context classes
- 1 Demo/Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Demo rõ object reuse và memory savings

### 3. File 10-Flyweight-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ Factory pattern trong FlyweightFactory
- Thể hiện relationships giữa components

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Có liên kết với context đã học**: Video/Restaurant/Enterprise/Smart Home (preferred)
✅ **Large number of objects**: Ít nhất hundreds, tốt nhất thousands
✅ **Clear intrinsic state**: Shared, immutable properties
✅ **Clear extrinsic state**: Unique properties passed as parameters
✅ **FlyweightFactory**: Manages object pool với HashMap
✅ **Object reuse**: Demo "Created NEW" vs "Reused EXISTING"
✅ **Memory savings**: Show before/after với numbers (10x+ savings)
✅ **Realistic**: Vấn đề có thể gặp trong thực tế
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Flyweight, Factory, immutable intrinsic state
✅ **Khác biệt**: Không trùng với lecture hay code sample

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy Code compiler (có thể có trong sample)
❌ Copy simple text characters (quá đơn giản)
❌ Context hoàn toàn mới không liên kết (khó nhớ)
❌ Too few objects (< 100) - không thể hiện memory savings
❌ No clear intrinsic/extrinsic separation
❌ Factory doesn't pool objects (just creates new)
❌ Mutable intrinsic state (không shareable)
❌ Extrinsic state stored in flyweight (sai concept)
❌ No memory savings demonstration

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **NÊN có liên kết**: Chọn context liên kết với patterns đã học để dễ nhớ
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "object sharing" concept
- PHẢI có numbers cho memory savings

### Về Flyweight Pattern
- Intrinsic state **PHẢI** immutable và shareable
- Extrinsic state **PHẢI** passed as method parameters
- Factory **PHẢI** use HashMap/Map to pool objects
- Factory **PHẢI** return existing object if found
- Factory **PHẢI** create new object only if not found
- ConcreteFlyweights **PHẢI** be shareable (stateless for shared parts)

### Về Intrinsic vs Extrinsic State
**Intrinsic State** (shared, stored IN flyweight):
- Properties that are SAME for many objects
- Immutable (final fields)
- Context-independent
- Examples: icon image, color, font style, type

**Extrinsic State** (unique, passed TO flyweight):
- Properties that are DIFFERENT for each object
- Context-dependent
- Passed as method parameters
- NOT stored in flyweight
- Examples: position (x, y), object ID, context

### Về Implementation
- **Recommended**: Interface/abstract class cho Flyweight
- ConcreteFlyweights implement/extend Flyweight
- Factory uses `Map<String, Flyweight>` to pool
- Factory's `getFlyweight(key)` returns shared object
- Client maintains extrinsic state
- Client calls flyweight methods với extrinsic parameters

### Về Memory Savings
- Calculate memory BEFORE flyweight:
  - Example: 10,000 objects × 500KB = 5,000 MB (5GB)
- Calculate memory AFTER flyweight:
  - Example: 4 shared flyweights × 500KB = 2 MB
- Show savings: 5GB → 2MB = 2,500x reduction!
- Must demonstrate với numbers trong demo

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Factory PHẢI có HashMap to store flyweights
- Check if flyweight exists before creating new
- Print "Created NEW" vs "Reused EXISTING" để demo
- Show pool size at end
- Calculate and show memory savings

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Icon: InterfaceTarget (nếu dùng interface)
- ConcreteFlyweights: ClassTarget implementing Icon
- Factory: ClassTarget với pool field
- Client: ClassTarget
- Format phải giống lecture

### Về documentation
- Documents/Solutions/Flyweight.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- **Highlight liên kết với context đã học** trong phần mô tả bài toán
- Giải thích rõ ràng intrinsic vs extrinsic
- Show memory calculation với numbers
- Nêu rõ trade-offs (complexity vs memory)

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

### ⭐ RECOMMENDED: Video Player UI Icons (liên kết Video domain)

**Context**: Liên kết với StreamFlix (Proxy), YouTube (Observer), Media Player (Adapter)

**Problem**:
- Video streaming platform displays 10,000 videos in grid
- Each video has 4 icons: play, pause, like, share
- Each icon = 500KB image data
- Total: 10,000 × 4 icons × 500KB = 20GB memory!
- Browser crashes, slow performance

**Solution**:
- Share icon objects (only 4 unique icons)
- Intrinsic state: icon type, image data, color (stored IN flyweight)
- Extrinsic state: position (x, y), video ID (passed TO flyweight)
- With flyweight: 4 icons × 500KB = 2MB
- Savings: 20GB → 2MB = 10,000x reduction!

**Classes**:
```java
interface Icon {
    void render(int x, int y, String videoTitle);
}

class PlayIcon implements Icon {
    // Intrinsic state (shared, immutable)
    private final String iconImage;  // Heavy: 500KB
    private final String color;

    public PlayIcon() {
        this.iconImage = loadImage("play.png");  // 500KB
        this.color = "white";
    }

    @Override
    public void render(int x, int y, String videoTitle) {
        // Use intrinsic + extrinsic state
        System.out.println("Rendering play icon at (" + x + "," + y + ") for " + videoTitle);
    }
}

class IconFactory {
    private static Map<String, Icon> pool = new HashMap<>();

    public static Icon getIcon(String type) {
        Icon icon = pool.get(type);
        if (icon == null) {
            // Create new flyweight
            icon = createIcon(type);
            pool.put(type, icon);
            System.out.println("Created NEW flyweight: " + type);
        } else {
            System.out.println("Reused EXISTING flyweight: " + type);
        }
        return icon;
    }
}

class Video {
    // Extrinsic state (unique to each video)
    private String title;
    private int x, y;  // Position in grid
}

class VideoPlayer {
    public void displayGrid(List<Video> videos) {
        for (Video video : videos) {
            // Get shared flyweights
            Icon play = IconFactory.getIcon("play");
            Icon like = IconFactory.getIcon("like");

            // Render with extrinsic state
            play.render(video.getX(), video.getY(), video.getTitle());
            like.render(video.getX() + 50, video.getY(), video.getTitle());
        }
    }
}
```

**Demo**:
1. Create 10,000 video objects (extrinsic state)
2. Each video requests 4 icons from factory
3. Factory creates only 4 flyweights (first time)
4. All subsequent requests reuse existing flyweights
5. Show: 40,000 icon requests → 4 flyweight objects
6. Show memory: 20GB → 2MB

### Other Examples (chỉ structure, KHÔNG copy):

**Restaurant Menu Icons** (liên kết Composite):
- Problem: 500 menu items × 3 category icons × 100KB = 150MB
- Solution: Share 3 icon flyweights = 300KB
- Savings: 150MB → 300KB = 500x

**Smart Home Device Icons** (liên kết Mediator):
- Problem: 200 devices × 10 device type icons × 200KB = 400MB
- Solution: Share 10 icon flyweights = 2MB
- Savings: 400MB → 2MB = 200x

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết intrinsic/extrinsic state
- Demo rõ memory savings với numbers

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Flyweight
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Đã chọn context có liên kết với patterns đã học (nếu có thể)
- [ ] Bài toán có large number of objects (100+, ideally 1000+)
- [ ] Có clear intrinsic state (immutable, shared)
- [ ] Có clear extrinsic state (passed as parameters)
- [ ] Factory có HashMap pool
- [ ] Factory reuses existing flyweights
- [ ] Demo shows "Created NEW" vs "Reused EXISTING"
- [ ] Calculated memory savings với numbers
- [ ] Documents/Solutions/Flyweight.md có đầy đủ 8 sections
- [ ] Code trong 10-Flyweight-DP/ là code MỚI
- [ ] Demo thể hiện object reuse rõ ràng
- [ ] Demo shows memory savings (before/after)
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong markdown
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram có Flyweight interface/abstract
- [ ] UML diagram có FlyweightFactory với pool
- [ ] UML diagram có ConcreteFlyweights
- [ ] UML diagram format giống lecture
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng memory savings
- [ ] Đã giải thích trade-offs (complexity vs memory)

## Common Pitfalls cần tránh

### Flyweight có thể sai nếu:
❌ Intrinsic state is mutable (cannot be shared!)
❌ Extrinsic state stored in flyweight (defeats purpose)
❌ Factory creates new objects every time (no pooling)
❌ Too few objects (< 100) - overhead > savings
❌ No clear intrinsic/extrinsic separation
❌ Objects not actually shared (different instances)

### Best Practices:
✅ Intrinsic state MUST be immutable (final fields)
✅ Extrinsic state ALWAYS passed as parameters
✅ Factory MUST check pool before creating
✅ Use large number of objects (1000+) to show benefit
✅ Calculate and show memory savings
✅ Log "Created" vs "Reused" for demonstration
✅ Consider thread safety for factory (if needed)

### When to use Flyweight:
✅ Large number of similar objects (thousands)
✅ Objects have shared state (intrinsic)
✅ Memory is a constraint
✅ Object creation is expensive
✅ Most object state can be extrinsic
✅ Examples: game particles, UI icons, text characters, map markers

### When NOT to use Flyweight:
❌ Small number of objects (< 100)
❌ All state is unique (no sharing possible)
❌ Memory is not a concern
❌ Complexity overhead > memory savings
❌ Objects are not similar enough
❌ Cannot separate intrinsic/extrinsic state

## Flyweight vs Object Pool vs Prototype

### Key Differences:

**Flyweight Pattern**:
- **Purpose**: Share objects để save memory
- **Focus**: Separate intrinsic/extrinsic state
- **Structure**: Factory pools flyweights, clients pass extrinsic state
- **Example**: Video player icons, game particles

**Object Pool Pattern**:
- **Purpose**: Reuse expensive objects for performance
- **Focus**: Avoid creation/destruction overhead
- **Structure**: Pool checks out/returns objects
- **Example**: Database connections, thread pools

**Prototype Pattern**:
- **Purpose**: Clone objects instead of creating from scratch
- **Focus**: Object copying
- **Structure**: Objects can clone themselves
- **Example**: Game entity spawning

### When to use which:

**Use Flyweight when**:
- Large number of similar objects
- Memory is the concern
- Objects have shareable state

**Use Object Pool when**:
- Creation/destruction is expensive
- Performance is the concern
- Objects are fully independent

**Use Prototype when**:
- Complex object initialization
- Want to avoid subclassing Factory
- Objects can be copied

## Memory Calculation Examples

### Example 1: Video Player Icons
**Without Flyweight**:
- 10,000 videos
- 4 icons per video (play, pause, like, share)
- 500KB per icon
- Total: 10,000 × 4 × 500KB = 20,000,000 KB = 20GB

**With Flyweight**:
- 4 shared icon objects
- 500KB per icon
- Total: 4 × 500KB = 2,000 KB = 2MB
- **Savings: 20GB → 2MB = 10,000x reduction!**

### Example 2: Game Forest
**Without Flyweight**:
- 50,000 trees
- 1MB per tree model
- Total: 50,000 × 1MB = 50GB

**With Flyweight**:
- 10 tree types (oak, pine, maple, etc.)
- 1MB per type
- Total: 10 × 1MB = 10MB
- Extrinsic: position (x, y, z) = 12 bytes × 50,000 = 600KB
- **Savings: 50GB → 10.6MB = 4,700x reduction!**

### Example 3: Text Editor
**Without Flyweight**:
- 100,000 characters
- 50KB per character object (with font data)
- Total: 100,000 × 50KB = 5GB

**With Flyweight**:
- 500 unique character+font combinations
- 50KB per flyweight
- Total: 500 × 50KB = 25MB
- Extrinsic: position = 8 bytes × 100,000 = 800KB
- **Savings: 5GB → 25.8MB = 194x reduction!**

**Key takeaway**: Flyweight works best when you have:
1. Large N (number of objects)
2. Small M (number of unique types)
3. Heavy intrinsic state (large shared data)
4. Light extrinsic state (small unique data)

Savings = N × IntrinsicSize → M × IntrinsicSize + N × ExtrinsicSize
