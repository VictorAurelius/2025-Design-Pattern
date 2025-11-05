# Plan Task: Tạo Bài Toán Mới Cho Composite Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Composite Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Composite Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích code hiện tại (để hiểu vấn đề)
- Đọc tất cả file Java trong folder `3-Composite-DP/`
- Đọc file `Solution/Composite.md` để hiểu cấu trúc solution hiện tại
- Phân tích:
  - Hiểu bài toán hiện tại đang giải quyết vấn đề gì
  - Xác định điểm yếu của bài toán hiện tại (nếu có)

### Bước 2: Học cách implement Composite Pattern từ tài liệu chuẩn
- Đọc file `Solution/Composite-Lecture.pdf` để:
  - Hiểu BẢN CHẤT của Composite Pattern (không phải học bài toán cụ thể)
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram có đầy đủ phương thức
  - Hiểu mối quan hệ giữa Component, Leaf, và Composite
  - Hiểu tree structure và recursive composition

- Đọc code trong `Code-Sample/CompositePattern-Project/` để:
  - Học cách đặt tên class, interface, method
  - Học coding convention và code style
  - Học cách triển khai Composite pattern đúng chuẩn
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 3: Sáng tạo bài toán mới

#### 3.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Composite Pattern:
- **KHÔNG dùng**: Task/Project Management (đã có trong Solution/Composite.md)
- **KHÔNG dùng**: File/Folder System (ví dụ phổ biến nhất)
- **KHÔNG dùng**: Graphics Shapes (ví dụ phổ biến trong sách)

**Gợi ý các lĩnh vực có thể dùng**:
- **Organization Structure**: Employee, Manager, Department, Division
- **Menu System**: MenuItem, SubMenu, MenuBar
- **Product Categories**: Product, Category, SuperCategory
- **HTML DOM**: Element, Container, Document
- **Military Structure**: Soldier, Unit, Battalion, Division
- **Geographic Structure**: City, Province, Region, Country
- **Company Structure**: Individual, Team, Department, Company
- **Book Structure**: Paragraph, Section, Chapter, Book
- **Course Structure**: Lesson, Module, Course, Program
- **Retail Chain**: Store, District, Region, Corporation
- **Music Library**: Song, Album, Playlist, Library
- **Restaurant Menu**: Dish, Category, Menu Section

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 3.2. Yêu cầu bài toán phải có
1. **Cấu trúc cây**: Phân cấp rõ ràng từ đơn giản đến phức tạp
2. **Uniform treatment**: Leaf và Composite được xử lý giống nhau
3. **Recursive composition**: Composite có thể chứa Composite khác
4. **Common interface**: Component chứa các method chung cho Leaf và Composite

**Đặc điểm của Composite Pattern**:
- Component: Interface/abstract class định nghĩa method chung
- Leaf: Đối tượng đơn giản không có con
- Composite: Đối tượng phức tạp chứa các Component con (Leaf hoặc Composite khác)
- Client: Tương tác với Component mà không cần biết là Leaf hay Composite
- Tree structure: Cấu trúc phân cấp nhiều tầng

#### 3.3. Thiết kế các thành phần
- **Component**: Abstract class hoặc interface với method chung
- **Leaf**: Class đơn giản, không có con
- **Composite**: Class phức tạp, chứa danh sách các Component con
- **Client/Main**: Class demo việc xây dựng tree và traverse

**Lưu ý**:
- Component có method chung (ví dụ: `display()`, `calculate()`, `getSize()`, etc.)
- Leaf implement method đơn giản
- Composite implement method bằng cách gọi recursive trên các con
- Composite có methods: `add()`, `remove()`, `getChild()`
- Client xây dựng tree structure và gọi method trên root

### Bước 4: Viết lại Solution/Composite.md

Tạo lại file `Solution/Composite.md` với cấu trúc:

#### 4.1. Mô tả mẫu Composite
- Giữ nguyên phần mô tả tổng quan về Composite Pattern
- Các thành phần chính: Component, Leaf, Composite, Client
- Khi nào sử dụng
- Đặc điểm quan trọng về tree structure và uniform treatment

#### 4.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 3
- Bài toán cần:
  - Có cấu trúc phân cấp rõ ràng
  - Nêu rõ vấn đề: cần xử lý đồng nhất Leaf và Composite
  - Giải thích tại sao cần Composite pattern
  - Có tình huống cụ thể với ví dụ minh họa
  - Thể hiện rõ tree structure với nhiều tầng

#### 4.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Các đối tượng đơn giản (Leaf)
- Các đối tượng phức tạp chứa nhiều đối tượng con (Composite)
- Cần xử lý cả hai loại đối tượng một cách đồng nhất

**Problem**: Vấn đề phức tạp cần giải quyết
- Cần biểu diễn cấu trúc phân cấp dạng cây
- Client phải phân biệt giữa Leaf và Composite
- Code xử lý Leaf và Composite khác nhau, phức tạp
- Khó mở rộng khi thêm loại đối tượng mới

**Solution**: Cách Composite pattern giải quyết
- Tạo Component interface/abstract class chung
- Leaf implement Component
- Composite implement Component và chứa danh sách Component con
- Client xử lý Leaf và Composite thông qua Component interface

**Expected Output**: Kết quả mong đợi
- Client code đơn giản, không cần phân biệt Leaf vs Composite
- Dễ dàng thêm tầng mới vào tree
- Code reusable và maintainable
- Traverse tree một cách đồng nhất

#### 4.4. Hiệu quả của việc sử dụng Composite Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh trước và sau khi dùng pattern
- Khả năng mở rộng tree structure
- Uniform treatment của Leaf và Composite
- Trade-offs (nếu có)

#### 4.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Mỗi class có:
  - Comment đầy đủ (nếu cần)
  - Tên biến, method rõ ràng
  - Logic đúng với Composite pattern
  - Coding style học từ code sample
- Code phải hoàn chỉnh và có thể compile

#### 4.6. Kết quả chạy chương trình
- Output khi chạy main class
- Giải thích cách pattern hoạt động qua output
- Thể hiện tree structure qua output
- Demo recursive traversal

#### 4.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Liệt kê các class và mối quan hệ
- Nêu rõ Component, Leaf, Composite
- Thể hiện composition relationship

#### 4.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này

### Bước 5: Viết code Java cho bài toán mới trong 3-Composite-DP/

#### 5.1. Xóa code cũ
- Xóa tất cả file Java cũ không còn phù hợp với bài toán mới

#### 5.2. Tạo code mới
Dựa trên bài toán đã thiết kế, tạo các file:
- **Component class** (abstract class hoặc interface) - Định nghĩa interface chung
- **Leaf class** (1-2 classes) - Đối tượng đơn giản
- **Composite class** (1-2 classes) - Đối tượng phức tạp chứa Component con
- **Client/Main class** - Demo pattern

**Ví dụ cấu trúc**:
```
3-Composite-DP/
├── Component.java      (abstract class hoặc interface)
├── LeafA.java         (Leaf)
├── LeafB.java         (Leaf - optional)
├── CompositeNode.java (Composite)
└── Main.java          (Client)
```

#### 5.3. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: chỉ khi cần (ArrayList, List, etc.)
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Sử dụng ArrayList hoặc List để chứa children trong Composite

#### 5.4. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Composite pattern
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Solution/Composite.md
- Demo rõ ràng tree structure với nhiều tầng

### Bước 6: Tạo file package.bluej với UML diagram

Tạo file `3-Composite-DP/package.bluej` với:

#### 6.1. Yêu cầu UML diagram
- Hiển thị tất cả các class của bài toán MỚI
- Hiển thị đầy đủ methods cho mỗi class (BlueJ sẽ tự động đọc từ source code)
- Hiển thị relationship đúng:
  - Leaf → Component (inheritance)
  - Composite → Component (inheritance)
  - Composite → Component (aggregation - chứa list Component)
  - Main → Component (dependency)
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 6.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=LeafA
dependency1.to=Component
dependency1.type=UsesDependency

dependency2.from=CompositeNode
dependency2.to=Component
dependency2.type=UsesDependency

dependency3.from=Main
dependency3.to=Component
dependency3.type=UsesDependency

... (các dependencies khác)

# Targets với showInterface=true
target1.name=Component
target1.type=AbstractTarget (hoặc InterfaceTarget)
target1.showInterface=true    # QUAN TRỌNG
target1.width=200
target1.height=120
target1.x=...
target1.y=...

... (các targets khác)
```

#### 6.3. Layout gợi ý
```
Sắp xếp theo mô hình Composite Pattern:
- Component: trung tâm trên
- Leaf classes: bên trái dưới
- Composite: bên phải dưới
- Main: góc trên bên trái

Hoặc:
- Component: trung tâm
- Leaf: trái
- Composite: phải (với arrow trở về Component để thể hiện aggregation)
- Main: trên
```

## Deliverables

### 1. File Solution/Composite.md
- Hoàn chỉnh theo cấu trúc ở Bước 4 (8 sections)
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting

### 2. Folder 3-Composite-DP/
Chứa các file Java cho bài toán MỚI:
- 1 Component class (abstract/interface)
- 1-2 Leaf classes
- 1-2 Composite classes
- 1 Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Không còn code cũ

### 3. File 3-Composite-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ inheritance và aggregation

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Cấu trúc cây rõ ràng**: Có ít nhất 3 tầng trong ví dụ demo
✅ **Uniform treatment**: Leaf và Composite xử lý qua interface chung
✅ **Recursive composition**: Composite chứa Composite khác
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung cấu trúc cây
✅ **Đúng pattern**: Thể hiện rõ "part-whole hierarchy"
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Composite
✅ **Khác biệt**: Không trùng với lecture hay code sample
✅ **Đơn giản vừa đủ**: Đủ để minh họa pattern, không quá phức tạp

### Bài toán nên tránh:
❌ Copy Task/Project từ Solution/Composite.md hiện tại
❌ Copy File/Folder (ví dụ quá phổ biến)
❌ Copy Graphics/Shape (ví dụ phổ biến trong sách)
❌ Quá đơn giản: chỉ có 2 tầng
❌ Không rõ ràng: khó hiểu cấu trúc cây
❌ Leaf và Composite quá khác nhau về interface
❌ Component có quá nhiều methods không cần thiết

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "tree structure" và "uniform treatment"

### Về Composite Pattern
- Component **PHẢI** có interface chung cho Leaf và Composite
- Leaf **KHÔNG** chứa children
- Composite **PHẢI** chứa danh sách Component (có thể là Leaf hoặc Composite)
- Composite methods thường recursive (gọi method trên các children)
- Client **KHÔNG** phân biệt Leaf và Composite

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Component thường là abstract class (với default implementation cho add/remove throw exception)
- Hoặc Component là interface (Leaf và Composite implement khác nhau)
- Composite dùng ArrayList<Component> để chứa children
- Composite method thường loop qua children và gọi method
- Code phải demo ít nhất 3 tầng trong tree

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Thể hiện rõ inheritance: Leaf → Component, Composite → Component
- Thể hiện rõ aggregation: Composite chứa danh sách Component
- Format phải giống lecture
- Kích thước boxes đủ lớn để chứa methods

### Về documentation
- Solution/Composite.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- Giải thích rõ ràng tree structure
- So sánh code có vs không có Composite
- Có ví dụ minh họa tree với nhiều tầng

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Organization Structure
- **Component**: Employee (abstract)
- **Leaf**: Developer, Designer, Tester
- **Composite**: Manager, Department
- **Method chung**: `getSalary()`, `showDetails()`
- **Tree**: Company → Department → Team → Individual

### Ví dụ 2: Menu System
- **Component**: MenuComponent (abstract)
- **Leaf**: MenuItem
- **Composite**: Menu, SubMenu
- **Method chung**: `display()`, `getPrice()`
- **Tree**: MenuBar → Menu → SubMenu → MenuItem

### Ví dụ 3: Product Categories
- **Component**: Catalog (interface)
- **Leaf**: Product
- **Composite**: Category, SuperCategory
- **Method chung**: `getTotalPrice()`, `showItems()`
- **Tree**: Store → SuperCategory → Category → Product

### Ví dụ 4: Military Structure
- **Component**: MilitaryUnit (abstract)
- **Leaf**: Soldier
- **Composite**: Squad, Platoon, Company, Battalion
- **Method chung**: `getStrength()`, `showHierarchy()`
- **Tree**: Battalion → Company → Platoon → Squad → Soldier

### Ví dụ 5: Course Structure
- **Component**: CourseComponent (abstract)
- **Leaf**: Lesson, Video, Quiz
- **Composite**: Module, Course, Program
- **Method chung**: `getDuration()`, `display()`
- **Tree**: Program → Course → Module → Lesson

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/interface khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết method phù hợp

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Composite
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán có cấu trúc cây rõ ràng
- [ ] Component có interface chung cho Leaf và Composite
- [ ] Leaf là đối tượng đơn giản không có con
- [ ] Composite chứa danh sách Component con
- [ ] Solution/Composite.md có đầy đủ 8 sections
- [ ] Code trong 3-Composite-DP/ là code MỚI cho bài toán mới
- [ ] Có Component + Leaf + Composite + Main
- [ ] Code compile và chạy được
- [ ] Demo ít nhất 3 tầng trong tree
- [ ] Code phù hợp với bài toán trong Solution/Composite.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram format giống lecture
- [ ] Đã xóa hết code cũ không liên quan
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng tree structure và recursive composition
