# Mẫu Thiết Kế Decorator (Decorator Pattern)

## Định Nghĩa
Mẫu Decorator cho phép thêm chức năng mới vào một đối tượng một cách động mà không cần thay đổi cấu trúc của nó. Mẫu này tạo ra một lớp decorator bao bọc lớp gốc và cung cấp chức năng bổ sung trong khi vẫn giữ nguyên chữ ký phương thức của lớp đó.

## Khái Niệm Cốt Lõi

### Vấn Đề Cần Giải Quyết
- **Bùng nổ lớp (Class Explosion)**: Khi có nhiều tính năng có thể kết hợp với nhau, việc tạo subclass cho mọi tổ hợp sẽ dẫn đến hàng trăm lớp
- **Không linh hoạt**: Không thể thêm/bỏ tính năng trong runtime
- **Vi phạm nguyên lý Open/Closed**: Phải sửa đổi code hiện có để thêm tính năng mới

### Giải Pháp Decorator
- Tạo các **decorator** nhỏ, mỗi cái chỉ chịu trách nhiệm cho 1 tính năng
- **Bao bọc (wrap)** các đối tượng để thêm chức năng
- Cho phép **kết hợp** các decorator với nhau tại runtime

## Cấu Trúc Pattern

### 1. Component Interface
```java
public interface VideoStream {
    String play();
    int getBitrate();
    String getDescription();
}
```

### 2. Concrete Component (Thành phần cụ thể)
```java
public class BasicVideoStream implements VideoStream {
    public String play() {
        return "Playing basic 480p video stream";
    }
    
    public int getBitrate() {
        return 500; // 500 kbps
    }
    
    public String getDescription() {
        return "Basic Stream (480p, 500 kbps)";
    }
}
```

### 3. Abstract Decorator (Decorator trừu tượng)
```java
public abstract class StreamDecorator implements VideoStream {
    protected VideoStream wrappedStream;
    
    public StreamDecorator(VideoStream stream) {
        this.wrappedStream = stream;
    }
    
    // Ủy quyền mặc định cho stream được bao bọc
    public String play() {
        return wrappedStream.play();
    }
    
    public int getBitrate() {
        return wrappedStream.getBitrate();
    }
    
    public String getDescription() {
        return wrappedStream.getDescription();
    }
}
```

### 4. Concrete Decorator (Decorator cụ thể)
```java
public class SubtitleDecorator extends StreamDecorator {
    private String language;
    
    public SubtitleDecorator(VideoStream stream, String language) {
        super(stream);
        this.language = language;
    }
    
    @Override
    public String play() {
        return wrappedStream.play() + " + Subtitles (" + language + ")";
    }
    
    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 100; // +100 kbps cho subtitles
    }
    
    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Subtitles (" + language + ")";
    }
}
```

## Ví Dụ Thực Tế: Nền Tảng Video StreamFlix

### Kịch Bản 1: Người Dùng Miễn Phí vs Premium

#### Người dùng miễn phí (có quảng cáo)
```java
VideoStream freeStream = new BasicVideoStream();
freeStream = new AdvertisementDecorator(freeStream, "pre-roll,mid-roll");

// Kết quả:
// - Chất lượng: 480p (500 kbps)
// - Có quảng cáo trước và giữa video
// - Tổng băng thông: 700 kbps
```

#### Người dùng Premium (HD + phụ đề)
```java
VideoStream premiumStream = new HDVideoStream();
premiumStream = new SubtitleDecorator(premiumStream, "EN");

// Kết quả:
// - Chất lượng: 1080p (2500 kbps)
// - Có phụ đề tiếng Anh
// - Tổng băng thông: 2600 kbps
```

### Kịch Bản 2: Khách Hàng Doanh Nghiệp (Nhiều Tính Năng)

```java
// TechCorp cần: HD + đa ngôn ngữ + watermark + analytics + DRM
VideoStream businessStream = new HDVideoStream();
businessStream = new SubtitleDecorator(businessStream, "EN");
businessStream = new SubtitleDecorator(businessStream, "ES");
businessStream = new SubtitleDecorator(businessStream, "FR");
businessStream = new WatermarkDecorator(businessStream, "TechCorp Confidential", "top-right");
businessStream = new AnalyticsDecorator(businessStream, "cybersecurity_training");
businessStream = new DRMDecorator(businessStream, "enterprise_license");

// Kết quả:
// - Chất lượng: 1080p
// - 3 ngôn ngữ phụ đề
// - Watermark công ty
// - Tracking analytics
// - Bảo mật DRM
// - Tổng băng thông: 2980 kbps
```

### Kịch Bản 3: Thay Đổi Tính Năng Theo Thời Gian Thực

```java
// 10:00 AM - Tại nhà (WiFi): HD + Phụ đề
VideoStream stream = new HDVideoStream();
stream = new SubtitleDecorator(stream, "EN");

// 10:30 AM - Ra ngoài (4G): Giảm chất lượng
stream = new BasicVideoStream();
stream = new SubtitleDecorator(stream, "EN");
stream = new QualityAdjustmentDecorator(stream, "auto");

// 11:00 AM - Vùng sóng kém (3G): Chỉ video cơ bản
stream = new BasicVideoStream();
stream = new QualityAdjustmentDecorator(stream, "aggressive");

// 12:00 PM - Văn phòng (WiFi): Trở lại chất lượng cao nhất
stream = new UHDVideoStream();
stream = new SubtitleDecorator(stream, "EN");
```

## Các Ví Dụ Decorator Cụ Thể

### 1. Advertisement Decorator (Quảng Cáo)
```java
public class AdvertisementDecorator extends StreamDecorator {
    private String adTypes;
    
    @Override
    public String play() {
        String content = wrappedStream.play();
        if (adTypes.contains("pre-roll")) {
            content = "[Pre-roll ad] " + content;
        }
        if (adTypes.contains("post-roll")) {
            content = content + " [Post-roll ad]";
        }
        return content;
    }
    
    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 200; // +200 kbps cho ads
    }
}
```

### 2. Watermark Decorator (Watermark)
```java
public class WatermarkDecorator extends StreamDecorator {
    private String text;
    private String position;
    
    @Override
    public String play() {
        return wrappedStream.play() + " + Watermark [" + text + " @ " + position + "]";
    }
    
    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 50; // +50 kbps cho watermark
    }
}
```

### 3. Analytics Decorator (Phân Tích)
```java
public class AnalyticsDecorator extends StreamDecorator {
    private String trackingId;
    
    @Override
    public String play() {
        // Track viewing analytics
        System.out.println("📊 Analytics: User started watching [" + trackingId + "]");
        return wrappedStream.play() + " + Analytics tracking";
    }
    
    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 30; // +30 kbps cho analytics
    }
}
```

### 4. DRM Decorator (Bảo Mật)
```java
public class DRMDecorator extends StreamDecorator {
    private String licenseKey;
    
    @Override
    public String play() {
        // Verify DRM license
        System.out.println("🔐 DRM: Validating license [" + licenseKey + "]");
        return wrappedStream.play() + " + DRM protected";
    }
    
    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 100; // +100 kbps cho encryption
    }
}
```

## Luồng Thực Thi (Execution Flow)

Khi có chuỗi decorator: `AdvertisementDecorator → WatermarkDecorator → SubtitleDecorator → BasicVideoStream`

### Gọi method play():
1. `AdvertisementDecorator.play()` thực hiện:
   - Thêm "[Pre-roll ad]" 
   - Gọi `wrappedStream.play()` (WatermarkDecorator)
2. `WatermarkDecorator.play()` thực hiện:
   - Gọi `wrappedStream.play()` (SubtitleDecorator)
   - Thêm watermark
3. `SubtitleDecorator.play()` thực hiện:
   - Gọi `wrappedStream.play()` (BasicVideoStream)
   - Thêm phụ đề
4. `BasicVideoStream.play()` trả về nội dung cơ bản

### Gọi method getBitrate():
1. `AdvertisementDecorator.getBitrate()` = wrappedStream.getBitrate() + 200
2. `WatermarkDecorator.getBitrate()` = wrappedStream.getBitrate() + 50  
3. `SubtitleDecorator.getBitrate()` = wrappedStream.getBitrate() + 100
4. `BasicVideoStream.getBitrate()` = 500

**Kết quả cuối:** 500 + 100 + 50 + 200 = 850 kbps

## Ưu Điểm Của Decorator Pattern

### 1. ✅ Tránh Bùng Nổ Lớp
- **Trước:** 100+ lớp cho mọi tổ hợp tính năng
- **Sau:** 3 base stream + 6 decorator = vô số tổ hợp
- **Tiết kiệm:** 90% code

### 2. ✅ Nguyên Lý Open/Closed
- Thêm tính năng mới = Tạo 1 decorator mới
- Không cần sửa đổi code hiện có
- Không ảnh hưởng đến chức năng cũ

### 3. ✅ Linh Hoạt Runtime
- Thêm/bỏ tính năng trong lúc chạy
- Thích ứng với context (WiFi vs 4G)
- Cá nhân hóa theo user

### 4. ✅ Single Responsibility
- Mỗi decorator chỉ lo 1 tính năng
- Code sạch, dễ maintain
- Dễ test riêng biệt

### 5. ✅ Transparent Wrapping
- Client code không biết object có được decorate hay không
- Cùng interface → dễ thay thế

## So Sánh Trước/Sau Áp Dụng Pattern

| Tiêu Chí | Trước Decorator | Sau Decorator | Cải Thiện |
|----------|----------------|---------------|-----------|
| **Số lớp** | 100+ classes | 10 classes | 90% giảm |
| **Tổ hợp** | Giới hạn | Vô hạn | ∞ linh hoạt |
| **Thêm tính năng** | 2 tuần | 2 giờ | 95% nhanh hơn |
| **Chi phí maintain** | Cao | Thấp | 80% giảm |

## Khi Nào Sử Dụng Decorator Pattern

### ✅ NÊN DÙNG KHI:
- Cần thêm chức năng cho object động
- Subclassing sẽ gây bùng nổ lớp  
- Các tính năng có thể kết hợp nhiều cách
- Muốn thêm/bỏ tính năng tại runtime

### ✅ TRƯỜNG HỢP THỰC TẾ:
- **Java I/O:** `BufferedReader(FileReader(file))`
- **GUI:** borders, scrollbars, shadows
- **Web:** middleware, filters, interceptors
- **Media:** filters, effects, codecs

### ❌ KHÔNG NÊN DÙNG KHI:
- Chỉ cần 1 tổ hợp duy nhất (dùng inheritance)
- Các tính năng phụ thuộc lẫn nhau (dùng Strategy)
- Performance rất quan trọng (overhead của chain)

## Ví Dụ Mở Rộng: Thêm Tính Năng Mới

Khi có yêu cầu mới "Quality Adjustment":

### Cách Truyền Thống:
```
❌ Sửa 50+ lớp hiện có
❌ Tạo 100+ lớp tổ hợp mới  
❌ Risk phá vỡ chức năng cũ
❌ 2 tuần development
❌ 500+ test case
```

### Với Decorator Pattern:
```java
✅ Tạo 1 lớp: QualityAdjustmentDecorator

public class QualityAdjustmentDecorator extends StreamDecorator {
    private String mode; // "auto", "aggressive"
    
    public QualityAdjustmentDecorator(VideoStream stream, String mode) {
        super(stream);
        this.mode = mode;
    }
    
    @Override
    public int getBitrate() {
        int baseBitrate = wrappedStream.getBitrate();
        if ("aggressive".equals(mode)) {
            return baseBitrate * 60 / 100; // Giảm 40%
        }
        return baseBitrate * 80 / 100; // Giảm 20%
    }
    
    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Quality Adjust (" + mode + ")";
    }
}

// Sử dụng ngay với tất cả stream hiện có:
VideoStream stream = new HDVideoStream();
stream = new SubtitleDecorator(stream, "EN");
stream = new QualityAdjustmentDecorator(stream, "auto");
```

**Kết quả:**
- ✅ 2 giờ thay vì 2 tuần
- ✅ 0 bug cho code cũ
- ✅ Hoạt động với tất cả tổ hợp hiện có

## Kết Luận

**Decorator Pattern giúp:**

> "Thêm chức năng cho object một cách động mà không làm bùng nổ số lớng"

Thay vì tạo subclass cho mọi tổ hợp tính năng, ta bao bọc object bằng các decorator nhỏ, mỗi cái thêm 1 tính năng. Điều này tránh class explosion và cho phép tổ hợp runtime.

**Key Takeaway:** Decorator = Composition over Inheritance + Runtime Flexibility
