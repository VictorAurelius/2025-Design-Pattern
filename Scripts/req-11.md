# Plan Task: Tạo Bài Toán Mới Cho Builder Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Builder Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

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
10. **Flyweight**: Video Player UI Icons (StreamFlix)

### 🌟 RECOMMENDED Contexts cho Builder (có liên kết):

#### Option 1: **Video Upload Configuration Builder** ⭐ BEST (liên kết với Video domain)
- **Liên kết**: StreamFlix (Proxy, Flyweight), YouTube (Observer), Media Player (Adapter)
- **Use case**:
  - Upload video với MANY configuration options
  - Required: title, file path
  - Optional: description, tags, thumbnail, privacy, quality, subtitles, category, monetization
  - Complex object với 10+ parameters
  - Step-by-step configuration process
  - Validation logic for different combinations
- **Ví dụ**: VideoUpload builder, VideoUploadDirector
- **Ưu điểm**: Perfect for Builder, links to 4 video/media patterns, realistic

#### Option 2: **Restaurant Complex Order Builder** (liên kết với Restaurant)
- **Liên kết**: Golden Fork Restaurant (Composite)
- **Use case**:
  - Build complex restaurant order với customizations
  - Main dish, side dishes, drinks, desserts
  - Many customization options per item
  - Special dietary requirements
  - Build order step-by-step
- **Ví dụ**: Order builder, OrderDirector

#### Option 3: **ERP Report Configuration Builder** (liên kết với Enterprise)
- **Liên kết**: EnterpriseSoft ERP (Singleton, Chain of Responsibility)
- **Use case**:
  - Generate complex reports với many options
  - Date range, filters, grouping, sorting, format
  - Different report types (sales, inventory, support)
  - Step-by-step report configuration
- **Ví dụ**: Report builder, ReportDirector

#### Option 4: **Smart Home Scene Builder** (liên kết với Smart Home)
- **Liên kết**: Smart Home Automation (Mediator)
- **Use case**:
  - Create custom scenes (Morning, Night, Movie, Party)
  - Configure multiple devices in scene
  - Set device states, timings, conditions
  - Complex scene với many parameters
- **Ví dụ**: Scene builder, SceneDirector

#### Option 5: **Notification Message Builder** (liên kết với Notification)
- **Liên kết**: Notification System (Bridge)
- **Use case**:
  - Build complex notification messages
  - Title, body, priority, attachments, actions
  - Different channels (email, SMS, push)
  - Rich formatting options
- **Ví dụ**: Notification builder

### 💡 Recommendation:
**Chọn Option 1 (Video Upload Configuration Builder)** vì:
- ✅ Liên kết MẠNH với Video/Media domain (Adapter, Observer, Proxy, Flyweight)
- ✅ Tạo "memory cluster" hoàn chỉnh: Video Platform = 5 patterns!
- ✅ Builder rất phù hợp: video upload có NHIỀU optional parameters
- ✅ Realistic: YouTube/StreamFlix video upload thực tế rất phức tạp
- ✅ Easy to understand và visualize
- ✅ Clear separation: required vs optional parameters
- ✅ Demonstrates telescoping constructor problem

**Alternative**: Option 2 (Restaurant Order) nếu muốn link với Composite

## Yêu cầu đầu ra
Đối với Builder Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu

- Đọc file `Documents/Lectures/Builder.pdf` để:
  - Hiểu BẢN CHẤT của Builder Pattern
  - Hiểu telescoping constructor problem
  - Hiểu cách build complex objects step-by-step
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram với Builder, ConcreteBuilder, Director, Product
  - Hiểu khi nào dùng và không dùng Builder

- Đọc code trong `Code-Sample/Builder-Pattern-Project/` để:
  - Học cách đặt tên class, method, interface
  - Học coding convention và code style
  - Học cách implement Builder interface
  - Học cách implement ConcreteBuilder với fluent interface
  - Học cách implement Director (optional)
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Builder:
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: Car builder (quá phổ biến, có thể có trong sample)
- **KHÔNG dùng**: Simple objects with few parameters

**⭐ RECOMMENDED: Chọn context có liên kết với patterns đã học**

**Gợi ý các lĩnh vực có thể dùng (với liên kết)**:

### 🎬 Video/Media Domain (liên kết Proxy + Observer + Adapter + Flyweight):

1. **Video Upload Configuration Builder** ⭐ BEST
   - User uploads video to StreamFlix/YouTube
   - MANY configuration options (10+ parameters):
     - **Required**: title, file path
     - **Optional**: description, tags[], thumbnail, privacy (public/private/unlisted),
       quality (480p/720p/1080p/4K), category, language, subtitles[],
       monetization, age restriction, comments enabled, etc.
   - **Problem**: Constructor với 15 parameters → telescoping constructor hell!
   - **Solution**: Builder pattern với fluent interface
   - Real-world: YouTube video upload form

2. **Video Player Configuration**
   - Configure video player với many options
   - Quality, autoplay, loop, controls, subtitles, playback speed
   - Build player step-by-step

### 🍽️ Restaurant Domain (liên kết Composite):

3. **Restaurant Complex Order Builder**
   - Build restaurant order với customizations
   - Main dish + customizations (no onions, extra cheese, well-done)
   - Multiple side dishes, drinks, desserts
   - Special dietary requirements (vegan, gluten-free)
   - Many optional items and preferences

4. **Restaurant Meal Combo Builder**
   - Build meal combos (starter + main + dessert + drink)
   - Many combination options
   - Customizations for each item

### 💼 Enterprise Domain (liên kết Singleton + Chain):

5. **ERP Report Configuration Builder**
   - Generate reports với complex configuration
   - Report type, date range, filters, grouping, sorting
   - Output format (PDF, Excel, CSV)
   - Many optional parameters

6. **ERP User Account Builder**
   - Create user accounts với permissions
   - Basic info + roles + permissions + preferences
   - Many optional settings

### 🏠 Smart Home Domain (liên kết Mediator):

7. **Smart Home Scene Builder**
   - Create automation scenes
   - Configure multiple devices
   - Set device states, timings, conditions
   - Complex configuration

8. **Smart Home Device Setup**
   - Configure new smart device
   - Network settings, preferences, schedules
   - Many optional configurations

### 📱 General Options:

9. **Email Message Builder**
   - Compose complex emails
   - To, CC, BCC, subject, body, attachments, priority, formatting
   - Many optional fields

10. **Database Query Builder**
    - Build SQL queries programmatically
    - SELECT, FROM, WHERE, JOIN, ORDER BY, GROUP BY
    - Step-by-step query construction

11. **HTTP Request Builder**
    - Build HTTP requests
    - URL, method, headers, body, timeout, auth
    - Many optional parameters

12. **Game Character Builder**
    - Create game character
    - Class, race, attributes, skills, equipment
    - Many customization options

13. **Document Builder**
    - Build complex documents
    - Sections, paragraphs, images, tables, formatting
    - Step-by-step document construction

14. **Pizza Order Builder**
    - Build custom pizza
    - Size, crust, sauce, cheese, toppings[]
    - Classic Builder example

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có

**Đặc điểm của Builder Pattern**:

1. **Product (Complex Object)**:
   - Object với NHIỀU parameters (5+ parameters, ideally 10+)
   - Mix of required và optional parameters
   - Complex validation logic
   - Immutable (final fields) - preferred

2. **Builder (Interface/Abstract Class)**:
   - Defines building steps
   - Methods return Builder (fluent interface)
   - `build()` method returns Product
   - Can have validation logic

3. **ConcreteBuilder**:
   - Implements building steps
   - Stores intermediate state
   - `build()` creates final Product
   - Fluent interface (method chaining)

4. **Director (Optional)**:
   - Orchestrates building process
   - Defines common building sequences
   - Encapsulates construction logic
   - Client can bypass Director

**Bài toán phải demonstrate**:
- ✅ Object với many parameters (10+ preferred)
- ✅ Mix of required and optional parameters
- ✅ Telescoping constructor problem (show before/after)
- ✅ Fluent interface (method chaining)
- ✅ Step-by-step object construction
- ✅ Validation logic
- ✅ Immutable final product

**Quan trọng**:
- Product PHẢI có nhiều parameters (ít nhất 5, tốt nhất 10+)
- Builder methods PHẢI return `this` (fluent interface)
- Builder PHẢI có `build()` method return Product
- Demonstrate telescoping constructor problem
- Show validation (e.g., required fields)
- Product NÊN immutable (final fields)

#### 2.3. Thiết kế các thành phần

**Product Class** (Complex Object):
```java
public class VideoUpload {
    // Required parameters
    private final String title;
    private final String filePath;

    // Optional parameters (with defaults)
    private final String description;
    private final List<String> tags;
    private final String thumbnail;
    private final String privacy;  // public/private/unlisted
    private final String quality;  // 480p/720p/1080p/4K
    private final String category;
    private final boolean monetizationEnabled;
    private final boolean commentsEnabled;

    // Private constructor - only Builder can create
    private VideoUpload(VideoUploadBuilder builder) {
        this.title = builder.title;
        this.filePath = builder.filePath;
        this.description = builder.description;
        this.tags = builder.tags;
        // ... set all fields
    }

    // Getters only (immutable)
    public String getTitle() { return title; }
    // ... other getters

    // Inner Builder class
    public static class VideoUploadBuilder {
        // Same fields as Product
        private String title;
        private String filePath;
        private String description = "";  // Default
        // ... other fields with defaults

        // Constructor for required parameters
        public VideoUploadBuilder(String title, String filePath) {
            this.title = title;
            this.filePath = filePath;
        }

        // Fluent setters (return this)
        public VideoUploadBuilder description(String description) {
            this.description = description;
            return this;
        }

        public VideoUploadBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        // ... other setters

        // Build method
        public VideoUpload build() {
            // Validation
            if (title == null || title.isEmpty()) {
                throw new IllegalStateException("Title is required");
            }
            return new VideoUpload(this);
        }
    }
}
```

**Client Usage**:
```java
// Fluent interface - method chaining
VideoUpload video = new VideoUpload.VideoUploadBuilder("My Video", "/path/to/video.mp4")
    .description("This is my video")
    .tags(Arrays.asList("tutorial", "java", "coding"))
    .privacy("public")
    .quality("1080p")
    .monetizationEnabled(true)
    .build();
```

**Director (Optional)**:
```java
public class VideoUploadDirector {
    public VideoUpload buildQuickUpload(String title, String filePath) {
        // Quick upload với default settings
        return new VideoUpload.VideoUploadBuilder(title, filePath)
            .privacy("unlisted")
            .quality("720p")
            .build();
    }

    public VideoUpload buildFullUpload(String title, String filePath,
                                       String description, List<String> tags) {
        // Full upload với all common settings
        return new VideoUpload.VideoUploadBuilder(title, filePath)
            .description(description)
            .tags(tags)
            .privacy("public")
            .quality("1080p")
            .monetizationEnabled(true)
            .commentsEnabled(true)
            .build();
    }
}
```

### Bước 3: Viết Documents/Solutions/Builder.md

Tạo file `Documents/Solutions/Builder.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Builder
- Giữ phần mô tả tổng quan về Builder Pattern
- Các thành phần chính: Product, Builder, Director
- Khi nào sử dụng: complex objects, many parameters, telescoping constructor
- Đặc điểm quan trọng: fluent interface, step-by-step construction, immutability

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- **Nhấn mạnh liên kết với context đã học** (nếu có)
- Bài toán cần:
  - Có object với many parameters (10+)
  - Mix of required và optional parameters
  - Nêu rõ vấn đề telescoping constructor
  - Giải thích tại sao cần Builder
  - Có tình huống cụ thể minh họa

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Need to create complex objects
- Objects có many configuration options
- Mix of required and optional parameters
- Different construction workflows (quick vs full)

**Problem**: Vấn đề phức tạp cần giải quyết
- Telescoping constructor problem (too many parameters)
- Hard to remember parameter order
- Many constructor overloads needed
- Difficult to add new parameters
- Error-prone (easy to swap parameters)
- Not readable (what does 'true' mean?)

**Solution**: Cách Builder giải quyết
- Step-by-step object construction
- Fluent interface (method chaining)
- Clear parameter names (self-documenting)
- Easy to add new parameters
- Validation in build() method
- Immutable final product

**Expected Output**: Kết quả mong đợi
- Clean, readable object construction
- Flexible configuration
- Type-safe
- Maintainable code
- Easy to extend

#### 3.4. Hiệu quả của việc sử dụng Builder Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh với telescoping constructor (before/after code)
- Readability improvement
- Maintainability improvement
- Trade-offs: more classes, verbosity

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Phải có:
  - 1 Product class (complex object với 10+ parameters)
  - 1 Builder class (usually inner class)
  - Optional: Director class (for common workflows)
  - 1 Demo/Main class
- Code phải hoàn chỉnh và có thể compile
- Coding style học từ code sample
- Demonstrate fluent interface

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Demo different construction scenarios
- Show quick upload vs full upload
- Show validation (missing required fields)
- Giải thích cách pattern hoạt động qua output

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Phải có:
  - Product class
  - Builder class (inner class relationship)
  - Director class (optional)
  - Relationships (Builder creates Product)
- Thể hiện fluent interface

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Builder
- Alternatives: Telescoping constructor, JavaBeans pattern
- Best practices

### Bước 4: Viết code Java cho bài toán mới trong 11-Builder-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `11-Builder-DP/`:

**Product**:
- VideoUpload.java (với inner Builder class)

**Director** (optional):
- VideoUploadDirector.java

**Demo**:
- BuilderDemo.java (Main class)

**Ví dụ cấu trúc** (Video Upload):
```
11-Builder-DP/
├── VideoUpload.java               (Product + Builder inner class)
├── VideoUploadDirector.java       (Director - optional)
└── BuilderDemo.java               (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần
- Import statements: java.util.List, java.util.ArrayList, java.util.Arrays
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Builder methods PHẢI return `this` (fluent interface)
- Product PHẢI immutable (final fields, only getters)
- Builder as inner static class (preferred)

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Builder pattern
- Demo rõ fluent interface
- Show validation logic
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Builder.md

#### 4.4. Demo Requirements
Demo phải thể hiện:
1. Telescoping constructor problem (before - comment out)
2. Builder solution với fluent interface (after)
3. Simple upload (required parameters only)
4. Full upload (with optional parameters)
5. Director usage (quick upload, full upload)
6. Validation (missing required field)
7. Show readability improvement

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `11-Builder-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Product class
- Hiển thị Builder class (inner class notation)
- Hiển thị Director class (nếu có)
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationships:
  - Builder creates Product
  - Director uses Builder
  - Inner class relationship (Builder in Product)
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=VideoUpload
dependency1.to=VideoUpload.VideoUploadBuilder
dependency1.type=UsesDependency

dependency2.from=VideoUploadDirector
dependency2.to=VideoUpload.VideoUploadBuilder
dependency2.type=UsesDependency

dependency3.from=VideoUploadDirector
dependency3.to=VideoUpload
dependency3.type=UsesDependency

# Similar for other dependencies...

# Targets với showInterface=true
target1.name=VideoUpload
target1.type=ClassTarget
target1.showInterface=true
...
```

#### 5.3. Layout gợi ý
```
Top:
- VideoUpload (Product) với inner Builder class indicator

Middle:
- VideoUpload.VideoUploadBuilder (Builder inner class)

Bottom:
- VideoUploadDirector (Director)
- BuilderDemo (Main)
```

## Deliverables

### 1. File Documents/Solutions/Builder.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- **Nhấn mạnh liên kết với context đã học** (Video Upload → StreamFlix, YouTube)
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ telescoping constructor problem
- Minh họa fluent interface với examples
- Show before/after comparison

### 2. Folder 11-Builder-DP/
Chứa các file Java cho bài toán MỚI:
- Product class với inner Builder class
- Director class (optional nhưng recommended)
- 1 Demo/Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Demo rõ fluent interface và validation

### 3. File 11-Builder-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ inner class relationship
- Thể hiện Builder creates Product
- Thể hiện Director uses Builder

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Có liên kết với context đã học**: Video/Restaurant/Enterprise/Smart Home (preferred)
✅ **Many parameters**: Ít nhất 7-8 parameters, tốt nhất 10+
✅ **Required + Optional**: Mix of required and optional parameters
✅ **Telescoping constructor problem**: Demonstrate the problem
✅ **Fluent interface**: Method chaining (builder.setX().setY().build())
✅ **Validation**: Check required fields in build()
✅ **Immutability**: Product has final fields
✅ **Director**: Show common construction workflows (optional but good)
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Product, Builder, fluent interface
✅ **Khác biệt**: Không trùng với lecture hay code sample

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy Car builder (có thể có trong sample)
❌ Too few parameters (< 5) - not complex enough
❌ All parameters required - no need for Builder
❌ No validation logic
❌ Mutable product - should be immutable
❌ No fluent interface - defeats purpose
❌ Context hoàn toàn mới không liên kết (khó nhớ)
❌ Quá đơn giản: chỉ 2-3 parameters
❌ Không demonstrate telescoping constructor problem

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **NÊN có liên kết**: Chọn context liên kết với patterns đã học để dễ nhớ
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "fluent interface" concept

### Về Builder Pattern
- Product PHẢI có many parameters (7-10+)
- Builder PHẢI có fluent interface (return `this`)
- Builder PHẢI có `build()` method
- Product NÊN immutable (final fields, private constructor)
- Builder THƯỜNG là inner static class
- Validation logic trong `build()` method
- Required parameters trong Builder constructor

### Về Fluent Interface
**Required for Builder Pattern**:
```java
// Good - fluent interface
VideoUpload video = new VideoUpload.VideoUploadBuilder("title", "path")
    .description("desc")
    .privacy("public")
    .quality("1080p")
    .build();

// Bad - no fluent interface
VideoUploadBuilder builder = new VideoUploadBuilder("title", "path");
builder.setDescription("desc");
builder.setPrivacy("public");
builder.setQuality("1080p");
VideoUpload video = builder.build();
```

### Về Implementation
**Recommended**: Builder as inner static class
```java
public class Product {
    private final String field1;
    private final String field2;

    private Product(Builder builder) {
        this.field1 = builder.field1;
        this.field2 = builder.field2;
    }

    // Inner static Builder class
    public static class Builder {
        private String field1;
        private String field2;

        public Builder(String field1) {  // Required params
            this.field1 = field1;
        }

        public Builder field2(String field2) {  // Optional params
            this.field2 = field2;
            return this;  // Fluent interface
        }

        public Product build() {
            // Validation
            if (field1 == null) throw new IllegalStateException();
            return new Product(this);
        }
    }
}
```

### Về Validation
- Check required fields trong `build()`
- Throw `IllegalStateException` if validation fails
- Can have business logic validation
- Example: title not empty, file path exists, privacy in valid values

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Product: private constructor, final fields, only getters
- Builder: fluent methods return `this`, `build()` returns Product
- Demo rõ ràng: show before/after, validation, Director usage

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- VideoUpload: ClassTarget
- VideoUploadBuilder: ClassTarget (inner class)
- Director: ClassTarget
- Show inner class relationship
- Format phải giống lecture

### Về documentation
- Documents/Solutions/Builder.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- **Highlight liên kết với context đã học** trong phần mô tả bài toán
- Giải thích rõ ràng telescoping constructor problem
- Show before/after code comparison
- Nêu rõ trade-offs (more classes vs readability)

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

### ⭐ RECOMMENDED: Video Upload Configuration (liên kết Video domain)

**Context**: Liên kết với StreamFlix (Proxy, Flyweight), YouTube (Observer), Media Player (Adapter)

**Problem**:
- User uploads video với MANY configuration options:
  - Required: title, file path
  - Optional: description, tags[], thumbnail, privacy, quality, category,
    language, subtitles[], monetization, age restriction, comments enabled, etc.
- Total: 12+ parameters!

**Telescoping Constructor Problem**:
```java
// BAD - Telescoping constructor nightmare!
public VideoUpload(String title, String filePath) { ... }
public VideoUpload(String title, String filePath, String description) { ... }
public VideoUpload(String title, String filePath, String description, List<String> tags) { ... }
public VideoUpload(String title, String filePath, String description, List<String> tags,
                   String privacy) { ... }
// ... need 100+ constructors for all combinations!

// Client code - hard to read!
VideoUpload video = new VideoUpload("My Video", "/path", "Description",
    Arrays.asList("tag1", "tag2"), "public", "1080p", "Entertainment",
    true, true, "English", true);
// What does 'true' mean? Which parameter is which?
```

**Solution with Builder**:
```java
// GOOD - Builder with fluent interface
VideoUpload video = new VideoUpload.VideoUploadBuilder("My Video", "/path/to/video.mp4")
    .description("This is my awesome video")
    .tags(Arrays.asList("tutorial", "java", "coding"))
    .privacy("public")
    .quality("1080p")
    .category("Entertainment")
    .monetizationEnabled(true)
    .commentsEnabled(true)
    .build();

// Much more readable! Clear what each parameter means.
```

**Classes**:
```java
public class VideoUpload {
    // Required
    private final String title;
    private final String filePath;

    // Optional with defaults
    private final String description;
    private final List<String> tags;
    private final String privacy;
    private final String quality;
    // ... more fields

    private VideoUpload(VideoUploadBuilder builder) {
        this.title = builder.title;
        this.filePath = builder.filePath;
        // ... copy from builder
    }

    public static class VideoUploadBuilder {
        private String title;
        private String filePath;
        private String description = "";
        // ... fields with defaults

        public VideoUploadBuilder(String title, String filePath) {
            this.title = title;
            this.filePath = filePath;
        }

        public VideoUploadBuilder description(String description) {
            this.description = description;
            return this;
        }

        // ... other fluent setters

        public VideoUpload build() {
            // Validation
            if (title == null || title.isEmpty()) {
                throw new IllegalStateException("Title required");
            }
            return new VideoUpload(this);
        }
    }
}
```

**Director**:
```java
public class VideoUploadDirector {
    public VideoUpload buildQuickUpload(String title, String filePath) {
        return new VideoUpload.VideoUploadBuilder(title, filePath)
            .privacy("unlisted")
            .quality("720p")
            .build();
    }

    public VideoUpload buildPublicUpload(String title, String filePath,
                                         String description, List<String> tags) {
        return new VideoUpload.VideoUploadBuilder(title, filePath)
            .description(description)
            .tags(tags)
            .privacy("public")
            .quality("1080p")
            .monetizationEnabled(true)
            .commentsEnabled(true)
            .build();
    }
}
```

**Demo**:
1. Show telescoping constructor problem (commented out)
2. Build simple upload (required only)
3. Build full upload (with optional parameters)
4. Use Director for quick upload
5. Try validation (missing title)

### Other Examples (chỉ structure, KHÔNG copy):

**Restaurant Complex Order** (liên kết Composite):
- Problem: Order với main dish + customizations + sides + drinks + desserts
- 10+ parameters with many options
- Builder: fluent interface for step-by-step order

**Smart Home Scene** (liên kết Mediator):
- Problem: Configure scene với multiple devices and settings
- Device states, timings, conditions, triggers
- Builder: step-by-step scene configuration

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết parameters và validation
- Demo rõ fluent interface

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Builder
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Đã chọn context có liên kết với patterns đã học (nếu có thể)
- [ ] Bài toán có object với many parameters (7-10+)
- [ ] Có mix of required và optional parameters
- [ ] Demonstrated telescoping constructor problem
- [ ] Builder có fluent interface (return `this`)
- [ ] Builder có `build()` method
- [ ] Product is immutable (final fields)
- [ ] Builder là inner static class
- [ ] Có validation trong `build()`
- [ ] Director class (optional nhưng recommended)
- [ ] Documents/Solutions/Builder.md có đầy đủ 8 sections
- [ ] Code trong 11-Builder-DP/ là code MỚI
- [ ] Demo thể hiện fluent interface rõ ràng
- [ ] Demo shows validation
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong markdown
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram có Product và Builder (inner class)
- [ ] UML diagram có Director (nếu có)
- [ ] UML diagram format giống lecture
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng fluent interface
- [ ] Đã giải thích trade-offs

## Common Pitfalls cần tránh

### Builder có thể sai nếu:
❌ Too few parameters (< 5) - no need for Builder
❌ Builder methods don't return `this` - no fluent interface
❌ No `build()` method
❌ Product is mutable - defeats purpose
❌ No validation logic
❌ Required parameters not in constructor
❌ Builder is not inner class (not wrong but less convenient)

### Best Practices:
✅ Many parameters (7-10+ ideal)
✅ Fluent interface (return `this`)
✅ Product immutable (final fields)
✅ Builder as inner static class
✅ Required params in Builder constructor
✅ Validation in `build()`
✅ Clear parameter names (self-documenting)
✅ Director for common workflows

### When to use Builder:
✅ Object has many parameters (5+)
✅ Many optional parameters
✅ Need to ensure immutability
✅ Object construction is complex
✅ Want readable, maintainable code
✅ Examples: Configuration objects, complex DTOs, API requests

### When NOT to use Builder:
❌ Object has few parameters (< 5)
❌ All parameters are required
❌ Simple object with no validation
❌ Performance critical (Builder adds overhead)
❌ Object construction is simple

## Builder vs JavaBeans vs Telescoping Constructor

### Key Differences:

**Telescoping Constructor**:
- **Approach**: Multiple constructor overloads
- **Pros**: Immutable, thread-safe
- **Cons**: Unreadable, hard to maintain, many constructors
- **Example**: `new Video(a, b, c, d, e, f, g, h, i, j)`

**JavaBeans Pattern**:
- **Approach**: Setters for each field
- **Pros**: Easy to write, familiar
- **Cons**: Mutable, not thread-safe, object in inconsistent state
- **Example**:
```java
Video video = new Video();
video.setTitle("title");
video.setDescription("desc");
// Object in inconsistent state between setters!
```

**Builder Pattern**:
- **Approach**: Fluent interface with builder
- **Pros**: Readable, immutable, validation, flexible
- **Cons**: More verbose, extra class
- **Example**:
```java
Video video = new Video.Builder("title")
    .description("desc")
    .build();
```

### When to use which:

**Use Telescoping Constructor when**:
- 2-3 parameters only
- Simple object
- Immutability is critical

**Use JavaBeans when**:
- Framework requires it (e.g., JSP, Hibernate)
- Mutability is acceptable
- Simple setters/getters

**Use Builder when**:
- 5+ parameters
- Many optional parameters
- Want immutability AND readability
- Complex validation logic
- **BEST for most cases!**

## Builder Variations

### 1. Inner Static Builder (Recommended)
```java
public class Product {
    public static class Builder {
        // Builder implementation
    }
}

// Usage
Product p = new Product.Builder().build();
```

### 2. Separate Builder Class
```java
public class Product { }
public class ProductBuilder { }

// Usage
Product p = new ProductBuilder().build();
```

### 3. Generic Builder (Advanced)
```java
public interface Builder<T> {
    T build();
}

public class ProductBuilder implements Builder<Product> {
    public Product build() { ... }
}
```

### 4. Step Builder (Advanced)
Enforces order of method calls using interfaces.

**Recommendation**: Use Inner Static Builder (Option 1) - most common and convenient!
