# HƯỚNG DẪN GIẢI THÍCH CODE - DESIGN PATTERNS

**Giải thích chi tiết code của 3 mẫu: Flyweight, Factory Method, Command**

---

## 1. FLYWEIGHT PATTERN - TIẾT KIỆM BỘ NHỚ

### 🎯 Mục tiêu: Chia sẻ objects để tiết kiệm memory khi có hàng ngàn objects tương tự

### A. INTERFACE FLYWEIGHT - [`VideoIcon`](10-Flyweight-DP/VideoIcon.java:1)

```java
public interface VideoIcon {
    void render(int x, int y, String videoTitle);  // ← Vẽ icon ở vị trí (x,y)
    String getIconType();                          // ← Loại icon (play, pause...)
    int getIconSize();                            // ← Kích thước icon
}
```

**Giải thích:**
- `render(x, y, title)`: **Extrinsic state** - thông tin thay đổi (vị trí, title)
- `getIconType()`, `getIconSize()`: **Intrinsic state** - thông tin cố định được chia sẻ

### B. CONCRETE FLYWEIGHT - Ví dụ `PlayIcon`

```java
public class PlayIcon implements VideoIcon {
    private String iconType = "PLAY";        // ← Intrinsic: không đổi
    private int iconSize = 24;               // ← Intrinsic: không đổi
    
    @Override
    public void render(int x, int y, String videoTitle) {  // ← Extrinsic: thay đổi
        System.out.println("▶️ Rendering PLAY icon for: " + videoTitle + " at (" + x + ", " + y + ")");
    }
}
```

**Giải thích các hàm:**
- **Constructor**: Khởi tạo intrinsic state (iconType, iconSize)
- **render()**: Nhận extrinsic state (x, y, title) để vẽ icon cụ thể
- **getIconType()/getIconSize()**: Trả về intrinsic state được chia sẻ

### C. FLYWEIGHT FACTORY - [`IconFactory`](10-Flyweight-DP/IconFactory.java:1) ⭐

```java
public class IconFactory {
    // Object pool - stores flyweight objects
    private static Map<String, VideoIcon> iconPool = new HashMap<>();
    private static int createdCount = 0;
    private static int reusedCount = 0;
    
    public static synchronized VideoIcon getIcon(String iconType) {
        VideoIcon icon = iconPool.get(iconType);  // ← Kiểm tra pool trước
        
        if (icon == null) {                       // ← Chưa có trong pool
            System.out.println("🆕 Creating NEW flyweight: " + iconType);
            
            switch (iconType.toLowerCase()) {     // ← Tạo flyweight mới
                case "play":   icon = new PlayIcon(); break;
                case "pause":  icon = new PauseIcon(); break;
                case "like":   icon = new LikeIcon(); break;
                case "share":  icon = new ShareIcon(); break;
            }
            
            iconPool.put(iconType, icon);         // ← Lưu vào pool
            createdCount++;
            
        } else {                                  // ← Đã có trong pool
            System.out.println("♻️ Reusing EXISTING flyweight: " + iconType);
            reusedCount++;
        }
        
        return icon;                              // ← Trả về flyweight
    }
}
```

**Giải thích từng hàm:**

1. **`getIcon(String iconType)`** - HÀM CHÍNH:
   - **Input**: Loại icon cần lấy ("play", "pause", etc.)
   - **Logic**: Kiểm tra pool → Tạo mới nếu chưa có → Reuse nếu đã có
   - **Output**: VideoIcon flyweight

2. **`iconPool.get(iconType)`**: Kiểm tra xem đã có flyweight này chưa

3. **Switch statement**: Factory pattern để tạo concrete flyweight

4. **`iconPool.put(iconType, icon)`**: Lưu flyweight vào pool để reuse

### D. CÁCH SỬ DỤNG FLYWEIGHT

```java
// Client code
VideoIcon playIcon1 = IconFactory.getIcon("play");    // ← Tạo mới
VideoIcon playIcon2 = IconFactory.getIcon("play");    // ← Reuse!

// Cùng 1 object trong memory!
// playIcon1 == playIcon2  ← true

// Sử dụng với extrinsic state khác nhau
playIcon1.render(100, 50, "Video 1");                 // ← Vị trí khác
playIcon2.render(200, 150, "Video 2");                // ← Title khác
```

**Lợi ích:**
- 1000 videos chỉ cần 4 flyweight objects thay vì 1000 objects
- Tiết kiệm 99.6% memory

---

## 2. FACTORY METHOD PATTERN - TẠO OBJECTS LINH HOẠT

### 🎯 Mục tiêu: Tạo objects mà không cần biết class cụ thể, dễ mở rộng

### A. PRODUCT INTERFACE - [`VideoExporter`](12-Factory-Method-DP/VideoExporter.java:1)

```java
public interface VideoExporter {
    void export(String videoPath);              // ← Export video
    String getFormat();                         // ← Định dạng (MP4, AVI...)
    int getBitrate();                          // ← Chất lượng
    String getCodec();                         // ← Codec nén
    String getFileExtension();                 // ← Extension file
}
```

**Giải thích:** Interface chung cho tất cả concrete exporters

### B. CONCRETE PRODUCT - Ví dụ `MP4Exporter`

```java
public class MP4Exporter implements VideoExporter {
    @Override
    public void export(String videoPath) {
        System.out.println("Exporting to MP4 format...");
        System.out.println("Codec: " + getCodec());
        System.out.println("Bitrate: " + getBitrate() + " kbps");
        System.out.println("Output: " + videoPath.replace(".raw", getFileExtension()));
    }
    
    @Override public String getFormat() { return "MP4"; }
    @Override public int getBitrate() { return 5000; }
    @Override public String getCodec() { return "H.264"; }
    @Override public String getFileExtension() { return ".mp4"; }
}
```

**Giải thích các hàm:**
- **export()**: Thực hiện export với logic riêng của MP4
- **getFormat()/getBitrate()/getCodec()**: Trả về thông tin cấu hình MP4

### C. ABSTRACT CREATOR - [`ExporterFactory`](12-Factory-Method-DP/ExporterFactory.java:1) ⭐

```java
public abstract class ExporterFactory {
    
    // FACTORY METHOD (abstract) - subclasses override this
    public abstract VideoExporter createExporter();    // ← Hàm factory chính
    
    // TEMPLATE METHOD - defines the export workflow
    public void exportVideo(String videoPath) {        // ← Template sử dụng factory
        // Step 1: Get the appropriate exporter using factory method
        VideoExporter exporter = createExporter();     // ← Gọi factory method
        
        // Step 2: Use the exporter polymorphically
        exporter.export(videoPath);                    // ← Sử dụng product
    }
    
    // Convenience static method for factory selection
    public static ExporterFactory getFactory(String format) {  // ← Utility method
        switch (format.toUpperCase()) {
            case "MP4":  return new MP4ExporterFactory();
            case "AVI":  return new AVIExporterFactory();
            case "MOV":  return new MOVExporterFactory();
            case "WEBM": return new WebMExporterFactory();
            default: throw new IllegalArgumentException("Unknown format: " + format);
        }
    }
}
```

**Giải thích từng hàm:**

1. **`createExporter()`** - FACTORY METHOD:
   - **Abstract**: Subclass phải override
   - **Mục đích**: Tạo concrete product (VideoExporter)
   - **Polymorphism**: Mỗi subclass tạo product khác nhau

2. **`exportVideo(String videoPath)`** - TEMPLATE METHOD:
   - **Workflow**: Lấy exporter → Export video
   - **Sử dụng factory method**: `createExporter()`
   - **High-level logic**: Client không cần biết loại exporter

3. **`getFactory(String format)`** - UTILITY:
   - **Input**: Format string ("MP4", "AVI"...)
   - **Output**: Concrete factory tương ứng
   - **Convenience**: Giúp client code sạch hơn

### D. CONCRETE CREATOR - [`MP4ExporterFactory`](12-Factory-Method-DP/MP4ExporterFactory.java:1)

```java
public class MP4ExporterFactory extends ExporterFactory {
    @Override
    public VideoExporter createExporter() {           // ← Implement factory method
        System.out.println("[Factory] Creating MP4 Exporter...");
        return new MP4Exporter();                      // ← Tạo concrete product
    }
}
```

**Giải thích:**
- **Override factory method**: Tạo MP4Exporter cụ thể
- **Encapsulation**: Client không cần `new MP4Exporter()`

### E. CÁCH SỬ DỤNG FACTORY METHOD

```java
// Cách 1: Trực tiếp với concrete factory
ExporterFactory factory = new MP4ExporterFactory();   // ← Tạo factory
factory.exportVideo("video.raw");                     // ← Export qua template method

// Cách 2: Sử dụng utility method (khuyến khích)
ExporterFactory factory = ExporterFactory.getFactory("MP4");  // ← Factory selection
factory.exportVideo("video.raw");                            // ← Template method
```

**Flow diagram:**
```
Client → ExporterFactory.getFactory("MP4") → MP4ExporterFactory
      → exportVideo() → createExporter() → new MP4Exporter()
      → exporter.export() → MP4 specific logic
```

---

## 3. COMMAND PATTERN - ĐÓNG GÓI OPERATIONS

### 🎯 Mục tiêu: Biến operations thành objects để hỗ trợ undo/redo, logging, queuing

### A. COMMAND INTERFACE - [`Command`](19-Command-DP/Command.java:1)

```java
public interface Command {
    void execute();                    // ← Thực hiện operation
    void undo();                      // ← Hoàn tác operation  
    String getDescription();          // ← Mô tả cho history
}
```

**Giải thích:** Interface chung cho tất cả commands

### B. RECEIVER - [`VideoClip`](19-Command-DP/VideoClip.java:1)

```java
public class VideoClip {
    private String filename;
    private String duration;           // Format: "MM:SS"
    private int brightness;            // 0-200, default 100
    private List<String> textOverlays; // Text overlays with positions
    
    // Operations that commands will call
    public void addTextOverlay(String text, int x, int y) {    // ← Receiver method
        String overlay = text + " at (" + x + ", " + y + ")";
        textOverlays.add(overlay);
    }
    
    public void removeTextOverlay(int index) {                 // ← Receiver method
        if (index >= 0 && index < textOverlays.size()) {
            textOverlays.remove(index);
        }
    }
    
    public void setBrightness(int value) {                     // ← Receiver method
        if (value < 0) value = 0;
        if (value > 200) value = 200;
        this.brightness = value;
    }
    
    public int getBrightness() { return brightness; }          // ← Getter
    public int getTextOverlayCount() { return textOverlays.size(); }
}
```

**Giải thích các hàm:**
- **addTextOverlay()**: Thêm text overlay vào video
- **removeTextOverlay()**: Xóa text overlay theo index  
- **setBrightness()**: Điều chỉnh độ sáng với validation
- **Getters**: Lấy thông tin hiện tại cho commands

### C. CONCRETE COMMAND - [`AddTextCommand`](19-Command-DP/AddTextCommand.java:1) ⭐

```java
public class AddTextCommand implements Command {
    private VideoClip video;       // ← Reference to receiver
    private String text;           // ← Command parameters
    private int x;
    private int y;
    private int overlayIndex;      // ← For undo (state to restore)
    
    public AddTextCommand(VideoClip video, String text, int x, int y) {
        this.video = video;        // ← Store receiver
        this.text = text;          // ← Store parameters
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void execute() {
        // Store index where text will be added (for undo)
        overlayIndex = video.getTextOverlayCount();    // ← Save state BEFORE
        
        // Add text overlay
        video.addTextOverlay(text, x, y);              // ← Delegate to receiver
        
        // Feedback
        System.out.println("Added text: \"" + text + "\" at (" + x + ", " + y + ")");
    }
    
    @Override
    public void undo() {
        // Remove the text overlay at stored index
        video.removeTextOverlay(overlayIndex);         // ← Restore state
        System.out.println("Removed text: \"" + text + "\"");
    }
    
    @Override
    public String getDescription() {
        return "Add Text: \"" + text + "\" at (" + x + ", " + y + ")";
    }
}
```

**Giải thích từng hàm:**

1. **Constructor**: 
   - Store receiver (VideoClip)
   - Store command parameters (text, x, y)
   - Không thực hiện operation ngay!

2. **execute()**:
   - **Save state**: `overlayIndex = video.getTextOverlayCount()` 
   - **Do operation**: `video.addTextOverlay(text, x, y)`
   - **Feedback**: Print confirmation

3. **undo()**:
   - **Restore state**: `video.removeTextOverlay(overlayIndex)`
   - **Reverse operation**: Xóa text overlay đã thêm

4. **getDescription()**: Mô tả human-readable cho history display

### D. ANOTHER CONCRETE COMMAND - [`AdjustBrightnessCommand`](19-Command-DP/AdjustBrightnessCommand.java:1)

```java
public class AdjustBrightnessCommand implements Command {
    private VideoClip video;           // ← Receiver
    private int adjustment;            // ← Parameter: how much to adjust
    private int previousBrightness;    // ← For undo: previous value
    
    public AdjustBrightnessCommand(VideoClip video, int adjustment) {
        this.video = video;
        this.adjustment = adjustment;  // ← +20, -10, etc.
    }
    
    @Override
    public void execute() {
        // Save current state for undo
        previousBrightness = video.getBrightness();    // ← Save BEFORE
        
        // Calculate new brightness
        int newBrightness = previousBrightness + adjustment;
        
        // Apply new brightness
        video.setBrightness(newBrightness);            // ← Delegate to receiver
        
        // Feedback
        System.out.println("Brightness: " + previousBrightness + " → " + newBrightness);
    }
    
    @Override
    public void undo() {
        // Restore previous brightness
        video.setBrightness(previousBrightness);       // ← Restore saved value
        System.out.println("Brightness restored to: " + previousBrightness);
    }
    
    @Override
    public String getDescription() {
        return "Adjust Brightness (" + (adjustment >= 0 ? "+" : "") + adjustment + ")";
    }
}
```

**Khác biệt với AddTextCommand:**
- **State restoration strategy**: Restore value thay vì remove by index
- **Relative operation**: Adjustment (+/-) thay vì absolute value

### E. INVOKER - [`VideoEditor`](19-Command-DP/VideoEditor.java:1) ⭐

```java
public class VideoEditor {
    private VideoClip video;                           // ← Receiver reference
    private Stack<Command> undoStack;                  // ← Commands có thể undo
    private Stack<Command> redoStack;                  // ← Commands có thể redo
    private List<Command> history;                     // ← All commands (display)
    
    public VideoEditor(VideoClip video) {
        this.video = video;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.history = new ArrayList<>();
    }
    
    public void executeCommand(Command command) {       // ← HÀM CHÍNH
        // Execute the command
        command.execute();                              // ← Delegate to command
        
        // Add to undo stack
        undoStack.push(command);                        // ← Store for undo
        
        // Clear redo stack (standard behavior)
        redoStack.clear();                              // ← New command clears redo
        
        // Add to history
        history.add(command);                           // ← Track for display
        
        // Confirmation
        System.out.println("Executed: " + command.getDescription());
    }
    
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo");
            return;
        }
        
        // Pop from undo stack
        Command command = undoStack.pop();              // ← Get last command
        
        // Undo the command
        System.out.println("Undoing: " + command.getDescription());
        command.undo();                                 // ← Delegate to command
        
        // Push to redo stack
        redoStack.push(command);                        // ← Store for redo
    }
    
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo");
            return;
        }
        
        // Pop from redo stack
        Command command = redoStack.pop();              // ← Get last undone
        
        // Re-execute the command
        System.out.println("Redoing: " + command.getDescription());
        command.execute();                              // ← Execute again
        
        // Push back to undo stack
        undoStack.push(command);                        // ← Back to undo stack
    }
}
```

**Giải thích từng hàm:**

1. **executeCommand(Command command)** - CORE:
   - **Execute**: `command.execute()`
   - **Store for undo**: `undoStack.push(command)`
   - **Clear redo**: `redoStack.clear()`
   - **Track history**: `history.add(command)`

2. **undo()**:
   - **Pop**: `undoStack.pop()`
   - **Reverse**: `command.undo()`
   - **Store for redo**: `redoStack.push(command)`

3. **redo()**:
   - **Pop**: `redoStack.pop()`
   - **Re-execute**: `command.execute()`
   - **Store for undo**: `undoStack.push(command)`

### F. CÁCH SỬ DỤNG COMMAND PATTERN

```java
// Setup
VideoClip video = new VideoClip("video.mp4", "5:30");
VideoEditor editor = new VideoEditor(video);

// Execute commands
Command addText = new AddTextCommand(video, "Subscribe!", 100, 50);
editor.executeCommand(addText);           // ← Execute + store for undo

Command brightness = new AdjustBrightnessCommand(video, 20);
editor.executeCommand(brightness);        // ← Execute + store for undo

// Undo/Redo
editor.undo();                           // ← Undo brightness (last command)
editor.undo();                           // ← Undo text
editor.redo();                           // ← Redo text
```

**Flow diagram:**
```
Client → VideoEditor.executeCommand(command)
      → command.execute() → VideoClip.addTextOverlay()
      → undoStack.push(command)
      
Client → VideoEditor.undo()
      → undoStack.pop() → command.undo() → VideoClip.removeTextOverlay()
      → redoStack.push(command)
```

---

## 🔗 LIÊN KẾT GIỮA CÁC THÀNH PHẦN

### FLYWEIGHT:
```
Client → IconFactory.getIcon() → Check pool → Create/Reuse → Return flyweight
Client → flyweight.render(x, y, title) → Use with extrinsic state
```

### FACTORY METHOD:
```
Client → ExporterFactory.getFactory() → Concrete factory
Client → factory.exportVideo() → factory.createExporter() → Concrete product
Client → product.export() → Specific export logic
```

### COMMAND:
```
Client → new Command(receiver, params) → Store receiver + params
Client → editor.executeCommand(command) → command.execute() → receiver.operation()
Client → editor.undo() → command.undo() → receiver.reverse_operation()
```

**Key takeaways:**
- **Flyweight**: Share intrinsic state, pass extrinsic state
- **Factory Method**: Abstract creation, concrete implementation
- **Command**: Encapsulate request, enable undo/redo

---

**Lưu ý**: Code examples dựa trên implementation thực tế trong project, không phải pseudo-code!