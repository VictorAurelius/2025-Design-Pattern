# Hướng Dẫn Vẽ UML Command Pattern Trên StarUML

## Tổng Quan
Mẫu Command Pattern có 4 thành phần chính:
1. **Command Interface** - Giao diện chung cho tất cả commands
2. **Concrete Commands** - Các lệnh cụ thể (AddTextCommand, AdjustBrightnessCommand, v.v.)
3. **Invoker** - Lớp gọi commands (VideoEditor)
4. **Receiver** - Lớp nhận và thực hiện operations (VideoClip)

---

## Bước 1: Tạo Dự Án Mới

### 1.1 Mở StarUML
- Khởi động StarUML
- Chọn **File → New**
- Chọn **UML Standard Profile**
- Đặt tên project: "Command Pattern - Video Editor"

### 1.2 Tạo Class Diagram
- Trong **Model Explorer**, click chuột phải vào **Model**
- Chọn **Add → Class Diagram**
- Đặt tên: "Command Pattern Structure"

---

## Bước 2: Vẽ Command Interface

### 2.1 Tạo Interface Command
1. **Kéo thả Interface:**
   - Từ **Toolbox**, kéo **Interface** vào diagram
   - Đặt tên: `Command`
   - Vị trí: Góc trên bên trái

2. **Thêm Methods:**
   - Click chuột phải vào Interface Command
   - Chọn **Add → Operation**
   - Thêm 3 methods:
     ```
     + execute(): void
     + undo(): void
     + getDescription(): String
     ```

3. **Định dạng Interface:**
   - Click vào Interface Command
   - Trong **Properties**, đặt **stereotype** = `<<interface>>`
   - **Font**: Arial 10pt, Bold cho tên interface

---

## Bước 3: Vẽ Concrete Commands

### 3.1 Tạo AddTextCommand Class
1. **Kéo thả Class:**
   - Từ **Toolbox**, kéo **Class** vào diagram
   - Đặt tên: `AddTextCommand`
   - Vị trí: Dưới Interface Command, bên trái

2. **Thêm Attributes:**
   ```
   - video: VideoClip
   - text: String
   - x: int
   - y: int
   - overlayIndex: int
   ```

3. **Thêm Methods:**
   ```
   + AddTextCommand(video: VideoClip, text: String, x: int, y: int)
   + execute(): void
   + undo(): void
   + getDescription(): String
   ```

### 3.2 Tạo AdjustBrightnessCommand Class
1. **Kéo thả Class:**
   - Đặt tên: `AdjustBrightnessCommand`
   - Vị trí: Cạnh AddTextCommand

2. **Thêm Attributes:**
   ```
   - video: VideoClip
   - adjustment: int
   - previousBrightness: int
   ```

3. **Thêm Methods:**
   ```
   + AdjustBrightnessCommand(video: VideoClip, adjustment: int)
   + execute(): void
   + undo(): void
   + getDescription(): String
   ```

### 3.3 Tạo MacroCommand Class
1. **Kéo thả Class:**
   - Đặt tên: `MacroCommand`
   - Vị trí: Bên phải các concrete commands

2. **Thêm Attributes:**
   ```
   - commands: List<Command>
   - name: String
   ```

3. **Thêm Methods:**
   ```
   + MacroCommand(name: String)
   + addCommand(command: Command): void
   + removeCommand(command: Command): void
   + execute(): void
   + undo(): void
   + getDescription(): String
   ```

---

## Bước 4: Vẽ Invoker (VideoEditor)

### 4.1 Tạo VideoEditor Class
1. **Kéo thả Class:**
   - Đặt tên: `VideoEditor`
   - Vị trí: Góc trên bên phải

2. **Thêm Attributes:**
   ```
   - video: VideoClip
   - undoStack: Stack<Command>
   - redoStack: Stack<Command>
   - history: List<Command>
   - MAX_HISTORY: int = 50
   ```

3. **Thêm Methods:**
   ```
   + VideoEditor(video: VideoClip)
   + executeCommand(command: Command): void
   + undo(): void
   + redo(): void
   + showHistory(): void
   + canUndo(): boolean
   + canRedo(): boolean
   ```

---

## Bước 5: Vẽ Receiver (VideoClip)

### 5.1 Tạo VideoClip Class
1. **Kéo thả Class:**
   - Đặt tên: `VideoClip`
   - Vị trí: Dưới VideoEditor

2. **Thêm Attributes:**
   ```
   - filename: String
   - duration: String
   - brightness: int
   - contrast: int
   - filter: String
   - textOverlays: List<String>
   - volume: int
   ```

3. **Thêm Methods:**
   ```
   + VideoClip(filename: String, duration: String)
   + applyFilter(filterType: String): void
   + setBrightness(value: int): void
   + setContrast(value: int): void
   + addTextOverlay(text: String, x: int, y: int): void
   + removeTextOverlay(index: int): void
   + setVolume(value: int): void
   + showState(): void
   ```

---

## Bước 6: Vẽ Các Mối Quan Hệ (Relationships)

### 6.1 Realization (Implements) - Command Interface
**Các concrete commands implement Command interface**

1. **AddTextCommand implements Command:**
   - Từ **Toolbox**, chọn **Realization**
   - Click vào `AddTextCommand`
   - Kéo đến `Command` interface
   - **Loại đường nối:** Đường gạch ngang với mũi tên tam giác trống

2. **AdjustBrightnessCommand implements Command:**
   - Tương tự, từ `AdjustBrightnessCommand` đến `Command`

3. **MacroCommand implements Command:**
   - Từ `MacroCommand` đến `Command`

**Chi tiết vẽ đường Realization:**
- **Màu:** Đen
- **Kiểu:** Đường liền nét
- **Mũi tên:** Tam giác trống (không tô màu)
- **Label:** Có thể thêm `<<implements>>` (tùy chọn)

### 6.2 Association - VideoEditor uses Command
**VideoEditor có quan hệ với Command**

1. **VideoEditor → Command:**
   - Từ **Toolbox**, chọ **Association**
   - Click vào `VideoEditor`
   - Kéo đến `Command` interface
   - **Loại đường nối:** Đường thẳng với mũi tên đơn

2. **Cấu hình Association:**
   - Click vào đường nối
   - Trong **Properties**, đặt:
     - **End2 Role:** `commands`
     - **End2 Multiplicity:** `*` (many)
     - **End2 Navigability:** `navigable`

**Chi tiết vẽ Association:**
- **Màu:** Đen
- **Kiểu:** Đường liền nét
- **Mũi tên:** Mũi tên đơn →
- **Label near VideoEditor:** (không có)
- **Label near Command:** `commands *`

### 6.3 Association - VideoEditor uses VideoClip
**VideoEditor có một VideoClip**

1. **VideoEditor → VideoClip:**
   - Từ `VideoEditor` đến `VideoClip`
   - **End2 Role:** `video`
   - **End2 Multiplicity:** `1`

### 6.4 Association - Commands use VideoClip
**Các concrete commands có reference đến VideoClip**

1. **AddTextCommand → VideoClip:**
   - Từ `AddTextCommand` đến `VideoClip`
   - **End2 Role:** `video`
   - **End2 Multiplicity:** `1`

2. **AdjustBrightnessCommand → VideoClip:**
   - Tương tự từ `AdjustBrightnessCommand` đến `VideoClip`

### 6.5 Composition - MacroCommand contains Commands
**MacroCommand chứa nhiều Command (Composite pattern)**

1. **MacroCommand ◆→ Command:**
   - Từ **Toolbox**, chọn **Composition**
   - Click vào `MacroCommand`
   - Kéo đến `Command` interface
   - **Loại đường nối:** Đường với hình thoi đặc (♦) ở MacroCommand

**Chi tiết vẽ Composition:**
- **Màu:** Đen
- **Kiểu:** Đường liền nét
- **Hình thoi:** Đặc, màu đen ở `MacroCommand`
- **End2 Role:** `commands`
- **End2 Multiplicity:** `*`

---

## Bước 7: Thêm Ghi Chú (Notes)

### 7.1 Tạo Note cho Command Pattern
1. **Kéo thả Note:**
   - Từ **Toolbox**, kéo **Note** vào diagram
   - Vị trí: Góc trên diagram

2. **Nội dung Note:**
   ```
   Command Pattern
   ===============
   - Encapsulates requests as objects
   - Supports undo/redo operations
   - Enables macro commands
   - Decouples invoker from receiver
   ```

### 7.2 Tạo Note cho Execution Flow
1. **Kéo thả Note thứ 2:**
   - Vị trí: Bên cạnh VideoEditor

2. **Nội dung:**
   ```
   Execution Flow:
   1. VideoEditor.executeCommand()
   2. Command.execute()
   3. VideoClip performs operation
   4. Save to undo stack
   ```

### 7.3 Link Notes với Classes
1. **Note Connector:**
   - Từ **Toolbox**, chọ **Note Connector** (đường gạch gạch)
   - Từ Note đến các classes liên quan

---

## Bước 8: Sắp Xếp Layout

### 8.1 Bố Cục Tổng Thể
```
+---------------+    +------------------+    +---------------+
|   Command     |    |   VideoEditor    |    |   VideoClip   |
| <<interface>> |    |   (Invoker)      |    |  (Receiver)   |
+---------------+    +------------------+    +---------------+
        ▲                       |                     ▲
        |                       |                     |
        |                       ▼                     |
+---------------+    +------------------+              |
| AddTextCommand|    |                  |              |
+---------------+    |                  |              |
        |            |                  |              |
+---------------+    |                  |              |
|AdjustBrightness|   |                  |              |
|    Command    |    |                  |              |
+---------------+    +------------------+              |
        |                                              |
+---------------+                                     |
| MacroCommand  |◆────────────┘                      |
+---------------+                                     |
        |                                             |
        +─────────────────────────────────────────────┘
```

### 8.2 Căn Chỉnh
1. **Select multiple objects:**
   - Ctrl + Click để chọn nhiều elements
   - **Format → Align → Align Left** (căn trái)
   - **Format → Distribute → Distribute Vertically** (phân bố đều)

2. **Resize consistency:**
   - Chọn tất cả classes
   - **Format → Size → Same Width**

---

## Bước 9: Định Dạng Cuối Cùng

### 9.1 Colors và Styling
1. **Interface Command:**
   - **Background:** Light Blue (#E6F3FF)
   - **Border:** Blue (#0066CC)
   - **Text:** Bold

2. **Concrete Commands:**
   - **Background:** Light Green (#E6FFE6)
   - **Border:** Green (#00AA00)

3. **VideoEditor (Invoker):**
   - **Background:** Light Orange (#FFE6CC)
   - **Border:** Orange (#CC6600)

4. **VideoClip (Receiver):**
   - **Background:** Light Pink (#FFE6F0)
   - **Border:** Pink (#CC0066)

5. **MacroCommand:**
   - **Background:** Light Yellow (#FFFEE6)
   - **Border:** Gold (#CCAA00)

### 9.2 Stereotypes
1. **Command Interface:**
   - Thêm `<<interface>>` stereotype

2. **MacroCommand:**
   - Thêm `<<composite>>` stereotype

---

## Bước 10: Validations và Best Practices

### 10.1 Kiểm Tra Relationships
- ✅ **Realization arrows:** Từ concrete commands đến Command interface
- ✅ **Associations:** VideoEditor → Command, Commands → VideoClip
- ✅ **Composition:** MacroCommand ◆→ Command
- ✅ **Multiplicities:** Đúng số lượng (1, *, etc.)

### 10.2 Naming Conventions
- ✅ **Classes:** PascalCase (VideoEditor, AddTextCommand)
- ✅ **Methods:** camelCase (execute, undo, getDescription)
- ✅ **Attributes:** camelCase (video, text, undoStack)

### 10.3 Visibility Indicators
- ✅ **Public:** + (execute, undo, getDescription)
- ✅ **Private:** - (video, text, undoStack)
- ✅ **Protected:** # (nếu có inheritance)

---

## Bước 11: Thêm Sequence Diagram (Tùy Chọn)

### 11.1 Tạo Sequence Diagram
1. **Add New Diagram:**
   - Click chuột phải vào Model
   - **Add → Sequence Diagram**
   - Đặt tên: "Command Execution Sequence"

### 11.2 Lifelines
Tạo các lifelines theo thứ tự:
1. **Client** (actor)
2. **VideoEditor** (object)
3. **AddTextCommand** (object)
4. **VideoClip** (object)

### 11.3 Messages
1. **Client → VideoEditor:** `executeCommand(addTextCmd)`
2. **VideoEditor → AddTextCommand:** `execute()`
3. **AddTextCommand → VideoClip:** `addTextOverlay(text, x, y)`
4. **VideoEditor → VideoEditor:** `undoStack.push(addTextCmd)`

---

## Checklist Hoàn Thành

### ✅ Structural Elements
- [ ] Command Interface với 3 methods
- [ ] AddTextCommand class với attributes và methods
- [ ] AdjustBrightnessCommand class
- [ ] MacroCommand class (composite)
- [ ] VideoEditor class (invoker)
- [ ] VideoClip class (receiver)

### ✅ Relationships
- [ ] Realization: Concrete Commands → Command Interface
- [ ] Association: VideoEditor → Command
- [ ] Association: VideoEditor → VideoClip  
- [ ] Association: Commands → VideoClip
- [ ] Composition: MacroCommand ◆→ Command

### ✅ Styling
- [ ] Stereotypes (`<<interface>>`, `<<composite>>`)
- [ ] Color coding theo roles
- [ ] Consistent naming conventions
- [ ] Proper visibility indicators (+, -, #)

### ✅ Documentation
- [ ] Notes explaining pattern purpose
- [ ] Execution flow note
- [ ] Note connectors linking notes to relevant classes

---

## Tips Vẽ UML Hiệu Quả

### 🎯 Layout Strategy
1. **Top-down approach:** Interface ở trên, implementations ở dưới
2. **Left-to-right flow:** Client → Invoker → Command → Receiver
3. **Group related classes:** Các concrete commands gần nhau

### 🔧 Technical Tips
1. **Grid alignment:** Sử dụng grid để căn chỉnh
2. **Consistent spacing:** Khoảng cách đều giữa các elements
3. **Clear labels:** Đặt tên rõ ràng cho relationships
4. **Avoid crossing lines:** Minimize line intersections

### 📝 Documentation Tips
1. **Method signatures:** Include return types và parameters
2. **Attribute types:** Specify data types clearly
3. **Relationship labels:** Name associations clearly
4. **Notes for clarity:** Explain complex concepts

---

Sau khi hoàn thành, bạn sẽ có một UML diagram hoàn chỉnh thể hiện Command Pattern với video editor context, cho thấy rõ ràng cách pattern tách biệt invoker (VideoEditor) khỏi receiver (VideoClip) thông qua command objects, hỗ trợ undo/redo và macro commands.