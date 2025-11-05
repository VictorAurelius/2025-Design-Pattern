# Proxy Design Pattern - Video Streaming Platform

## 1. Mô tả mẫu Proxy

**Proxy Pattern** là một mẫu thiết kế cấu trúc (Structural Design Pattern) cung cấp một **surrogate** (đại diện) hoặc **placeholder** (giữ chỗ) cho một đối tượng khác để **kiểm soát truy cập** đến đối tượng đó. Proxy có cùng interface với đối tượng thực, cho phép nó thay thế đối tượng thực một cách **transparent** (trong suốt).

### Các thành phần chính:

1. **Subject (Interface)**:
   - Định nghĩa common interface cho RealSubject và Proxy
   - Cho phép Proxy có thể thay thế RealSubject
   - Client code sử dụng interface này

2. **RealSubject**:
   - Đối tượng thực sự thực hiện công việc
   - Thường là **expensive** to create/access
   - Contains actual business logic
   - Proxy delegates calls đến RealSubject

3. **Proxy**:
   - Implements cùng interface như RealSubject
   - Maintains **reference** đến RealSubject
   - Controls access to RealSubject
   - **Delegates** requests to RealSubject khi cần
   - Có thể add **additional logic** before/after delegation

### 4 Types of Proxy chính:

**1. Virtual Proxy** (Lazy Initialization):
- Delays creation của expensive object cho đến khi thực sự cần
- Placeholder cho object chưa được tạo
- Saves memory và initialization time
- Example: Load video chỉ khi user clicks play

**2. Protection Proxy** (Access Control):
- Kiểm tra permissions/rights trước khi access object
- Based on user role, subscription status, etc.
- Implements security và authorization
- Example: Premium content chỉ cho paid subscribers

**3. Remote Proxy** (Distributed Systems):
- Local representative của object ở remote location
- Hides complexity of network communication
- Example: RMI stubs, Web service clients

**4. Smart Proxy** (Additional Functionality):
- Adds extra functionality: caching, logging, reference counting
- Transparent enhancements
- Example: Cache frequently accessed data, log access patterns

### Khi nào sử dụng:
- ✅ Khi object **expensive** to create (memory, time, resources)
- ✅ Khi cần **lazy initialization** (defer creation until needed)
- ✅ Khi cần **access control** (security, permissions)
- ✅ Khi cần **add functionality** transparently (logging, caching)
- ✅ Khi object ở **remote location** (hide network complexity)
- ✅ Khi cần **smart reference** (reference counting, copy-on-write)

### Đặc điểm quan trọng:
- **Same Interface**: Proxy và RealSubject implement cùng interface
- **Delegation**: Proxy forwards requests to RealSubject
- **Transparency**: Client không biết đang dùng Proxy hay RealSubject
- **Control**: Proxy controls when và how RealSubject được access
- **Additional Logic**: Proxy có thể add logic before/after delegation
- **Lazy Creation**: Proxy có thể delay creation of RealSubject

---

## 2. Mô tả bài toán

### 🎬 Context Linking (Liên kết với patterns đã học):

Bài toán này **liên kết** với 2 patterns đã học trước:
- **Observer Pattern**: YouTube Channel - kênh video với subscribers
- **Adapter Pattern**: Media Player - playing different video formats

→ Tạo một **mental connection** trong video/media domain để dễ nhớ!

### Bối cảnh:
**Lisa** là Product Manager của **StreamFlix** - một nền tảng streaming video tương tự Netflix/YouTube. Platform có hàng ngàn videos từ nhiều content creators (nhớ lại **YouTube Channel** từ Observer pattern và **Media Players** từ Adapter pattern).

StreamFlix có 2 loại content:
- **Free Videos**: Ai cũng xem được
- **Premium Videos**: Chỉ subscribers trả phí mới xem được

### Vấn đề hiện tại:

**Problem 1: Expensive Object Creation** 💰

Khi user browse video catalog với **1000+ videos**, mỗi video object rất nặng:
- Video file: 100MB - 2GB
- Metadata: title, description, thumbnail
- Streaming URLs, quality options
- Playback statistics

**Cách tiếp cận SAI** (Load everything upfront):
```java
public class VideoLibrary {
    private List<RealVideo> videos = new ArrayList<>();

    public VideoLibrary() {
        // Load ALL 1000 videos upfront!
        for (int i = 1; i <= 1000; i++) {
            videos.add(new RealVideo("video" + i + ".mp4"));
            // Each RealVideo loads actual file → VERY SLOW!
        }
    }
}
// User waits 5 minutes for app to start! 😱
```

**Issues**:
- ❌ **Massive memory usage**: 1000 videos × 500MB average = 500GB memory!
- ❌ **Extremely slow startup**: Load all files takes minutes
- ❌ **Wasted resources**: User chỉ xem 2-3 videos nhưng load 1000
- ❌ **Network bandwidth**: Download all video data upfront
- ❌ **Poor user experience**: App freezes during loading

**Problem 2: No Access Control** 🔒

Không có mechanism để check premium subscriptions:
```java
public class RealVideo {
    public void play() {
        // Anyone can play any video!
        System.out.println("Playing video...");
        // No subscription check!
    }
}
```

**Issues**:
- ❌ Premium content accessible by everyone
- ❌ No revenue from subscriptions
- ❌ Unfair to paying customers
- ❌ Content creators lose money

### Tình huống cụ thể:

**User Story 1: Browsing Videos**
- Lisa opens StreamFlix app
- App displays 1000 video thumbnails
- **Current**: App loads ALL 1000 video files → 5 minutes loading time
- **Expected**: App loads quickly, videos load on-demand

**User Story 2: Watching Free Video**
- Lisa clicks free video "Cooking Tutorial"
- **Current**: Video loads immediately (already loaded)
- **Problem**: But waited 5 minutes at startup!
- **Expected**: Fast startup + video loads when clicked

**User Story 3: Watching Premium Video (No Subscription)**
- Bob (free user) tries to watch premium video "Exclusive Concert"
- **Current**: Video plays (no access control!)
- **Expected**: Access denied message + upgrade prompt

**User Story 4: Watching Premium Video (With Subscription)**
- Lisa (premium subscriber) clicks premium video
- **Expected**: Video plays normally after subscription check

### Sơ đồ vấn đề WITHOUT Proxy:

```
StreamFlix App Startup:
1. Load Video 1 (500MB) ━━━━━ 3 seconds
2. Load Video 2 (600MB) ━━━━━ 3 seconds
3. Load Video 3 (450MB) ━━━━━ 3 seconds
...
1000. Load Video 1000 ━━━━━ 3 seconds
Total: 50 minutes! ❌

User clicks play:
→ Video already loaded ✓
But user waited 50 minutes at startup! ❌
```

---

## 3. Yêu cầu bài toán

### Input:
- StreamFlix platform với 1000+ videos
- 2 loại users: Free users và Premium subscribers
- 2 loại content: Free videos và Premium videos
- Videos are large (100MB - 2GB)
- Metadata: title, description, duration, isPremium flag

### Problem - Vấn đề cần giải quyết:

**1. Expensive Object Creation Issue**:
- Loading all videos upfront wastes enormous resources
- Startup time unacceptable (minutes)
- Most videos never watched by user
- Memory và bandwidth wasted

**2. No Lazy Loading**:
- Videos loaded regardless of whether user watches them
- Cannot defer expensive operations until needed
- Poor scalability (more videos = slower startup)

**3. No Access Control**:
- Premium videos accessible by free users
- No subscription verification
- Revenue loss
- Security issue

**4. No Additional Functionality**:
- No logging of video access
- No caching of frequently watched videos
- No analytics on viewing patterns

### Solution - Proxy Pattern giải quyết:

**1. Introduce Video Interface** (Subject):
```java
public interface Video {
    void display();      // Show thumbnail và info
    void play();         // Play actual video
    String getTitle();
    String getDuration();
}
```

**2. RealVideo Class** (RealSubject - Expensive Object):
- Contains actual video file
- Heavy initialization (load video từ disk/network)
- Real playback functionality
- Only created when truly needed

**3. VideoProxy Class** (Virtual Proxy - Lazy Loading):
- Lightweight placeholder
- Stores only metadata (title, description, isPremium)
- Creates RealVideo **only when play() is called**
- Transparent to client

**4. PremiumVideoProxy Class** (Protection Proxy - Access Control):
- Checks user subscription before allowing play
- Delegates to VideoProxy if authorized
- Shows upgrade message if not authorized

### Architecture:

```
Client → Video (interface)
           ↑
           ├─ VideoProxy (Virtual Proxy)
           │    ↓ (lazy creates)
           │  RealVideo (expensive)
           │
           └─ PremiumVideoProxy (Protection Proxy)
                ↓ (delegates if authorized)
              VideoProxy
```

### Expected Output:

**Scenario 1: App Startup** ⚡
```
Before Proxy: Load 1000 videos → 50 minutes ❌
With Proxy: Create 1000 proxies → 2 seconds ✓

Proxy chỉ stores metadata, không load actual video!
```

**Scenario 2: Playing Free Video** 🎬
```
User clicks "Cooking Tutorial" (free video)
→ VideoProxy.play() called
→ Proxy creates RealVideo (lazy loading)
→ RealVideo loads actual file (3 seconds)
→ Video plays ✓

First play: 3 seconds (load video)
Subsequent plays: Instant (already loaded) ✓
```

**Scenario 3: Playing Premium Video (No Subscription)** 🔒
```
Free user clicks "Exclusive Concert" (premium)
→ PremiumVideoProxy.play() called
→ Check subscription: false
→ Show: "This is premium content. Upgrade to watch!"
→ No video loaded (saves bandwidth) ✓
```

**Scenario 4: Playing Premium Video (With Subscription)** 🌟
```
Premium user clicks "Exclusive Concert"
→ PremiumVideoProxy.play() called
→ Check subscription: true ✓
→ Delegate to VideoProxy
→ VideoProxy creates RealVideo (lazy)
→ Video plays ✓
```

### Performance Comparison:

| Operation | Without Proxy | With Proxy | Improvement |
|-----------|--------------|------------|-------------|
| App Startup (1000 videos) | 50 minutes | 2 seconds | **1500x faster** |
| Memory Usage | 500GB | 50MB | **10,000x less** |
| Bandwidth (startup) | 500GB | 5MB | **100,000x less** |
| Play video (first time) | Instant | 3 seconds | Acceptable |
| Play video (repeat) | Instant | Instant | Same |
| Premium check | No check | Automatic | Secure ✓ |

---

## 4. Hiệu quả của việc sử dụng Proxy Pattern

### Lợi ích trong bài toán này:

#### 1. Lazy Loading với Virtual Proxy ⚡

**Trước (Eager Loading)**:
```java
public class VideoLibrary {
    private List<RealVideo> videos = new ArrayList<>();

    public VideoLibrary() {
        // Load ALL videos upfront
        videos.add(new RealVideo("video1.mp4"));  // 500MB loaded
        videos.add(new RealVideo("video2.mp4"));  // 600MB loaded
        // ... 1000 more videos
        // Total: 500GB memory, 50 minutes loading time
    }
}
```

**Sau (Lazy Loading with Proxy)**:
```java
public class VideoLibrary {
    private List<Video> videos = new ArrayList<>();

    public VideoLibrary() {
        // Create lightweight proxies
        videos.add(new VideoProxy("video1.mp4"));  // Only metadata, instant!
        videos.add(new VideoProxy("video2.mp4"));  // Only metadata, instant!
        // ... 1000 more proxies
        // Total: 50MB memory, 2 seconds loading time
    }
}

public class VideoProxy implements Video {
    private RealVideo realVideo;  // null initially
    private String filename;      // lightweight

    @Override
    public void play() {
        if (realVideo == null) {
            realVideo = new RealVideo(filename);  // Load only when needed!
        }
        realVideo.play();
    }
}
```

**Benefits**:
- ✅ **1500x faster startup**: 50 minutes → 2 seconds
- ✅ **10,000x less memory**: 500GB → 50MB
- ✅ **Scalable**: 10,000 videos still loads in 2 seconds
- ✅ **Better UX**: Users don't wait for unused content

#### 2. Access Control với Protection Proxy 🔒

**Trước (No Access Control)**:
```java
public class RealVideo {
    public void play() {
        // Everyone can play!
        loadVideoFile();
        startPlayback();
    }
}
// Free users can watch premium content! ❌
```

**Sau (With Protection Proxy)**:
```java
public class PremiumVideoProxy implements Video {
    private VideoProxy videoProxy;
    private User currentUser;
    private boolean isPremium;

    @Override
    public void play() {
        if (isPremium && !currentUser.hasSubscription()) {
            System.out.println("⛔ Premium content requires subscription");
            System.out.println("Upgrade to Premium for $9.99/month");
            return;  // Access denied
        }

        // Authorized - delegate to actual video
        if (videoProxy == null) {
            videoProxy = new VideoProxy(filename);
        }
        videoProxy.play();
    }
}
```

**Benefits**:
- ✅ **Security**: Premium content protected
- ✅ **Revenue**: Users must subscribe for premium
- ✅ **Fairness**: Paying customers get exclusive access
- ✅ **Transparent**: No changes to RealVideo code

#### 3. Transparency - Client Không Biết Đang Dùng Proxy 👁️

```java
// Client code - SAME for both Proxy and RealVideo
Video video1 = new VideoProxy("tutorial.mp4");      // Proxy
Video video2 = new RealVideo("documentary.mp4");    // Real

// Both used identically
video1.play();  // Works
video2.play();  // Works

// Client doesn't know or care which is which!
```

#### 4. Multiple Proxy Layers 🎭

```java
// Layer 1: PremiumVideoProxy (access control)
// Layer 2: VideoProxy (lazy loading)
// Layer 3: RealVideo (actual video)

PremiumVideoProxy premium = new PremiumVideoProxy("concert.mp4", user);
premium.play();

// Flow: PremiumProxy → check subscription
//       → VideoProxy → lazy create RealVideo
//       → RealVideo → actual playback
```

### So sánh Before vs After:

| Aspect | Without Proxy | With Proxy (Virtual + Protection) |
|--------|--------------|-----------------------------------|
| Startup Time | 50 minutes | 2 seconds |
| Memory (1000 videos) | 500GB | 50MB |
| Bandwidth (startup) | 500GB | 5MB |
| First Video Play | Instant | 3 seconds (acceptable) |
| Subsequent Plays | Instant | Instant |
| Premium Protection | None ❌ | Enforced ✓ |
| Free User Premium Access | Yes ❌ | Denied ✓ |
| Scalability | Poor | Excellent |
| Code Changes to RealVideo | N/A | None (transparent) |

### Real-World Performance Example:

**Netflix/YouTube Scenario**:
```
User opens app with 10,000 videos
Without Proxy:
- Load 10,000 videos × 500MB = 5TB memory
- Load time: 8+ hours
- Result: App unusable ❌

With Proxy:
- Create 10,000 proxies with metadata
- Memory: 100MB
- Load time: 5 seconds
- Result: Instant browsing ✓

User watches 3 videos:
- Only 3 videos actually loaded (1.5GB)
- Saved: 4.9985TB not loaded!
- Bandwidth saved: 99.97%
```

### Trade-offs và Nhược điểm:

#### ⚠️ Nhược điểm cần lưu ý:

**1. Additional Indirection**:
- Extra layer between client và real object
- Slight overhead (usually negligible)
- First access có delay (lazy loading)

**2. Complexity**:
- More classes to maintain (Proxy classes)
- Multiple proxy layers có thể confusing
- Debugging harder (more layers)

**3. First Access Delay**:
```java
videoProxy.play();  // First time: 3 seconds (load video)
videoProxy.play();  // Second time: Instant

// Tradeoff: Slow first access vs fast startup
```

**4. Memory Eventually Same**:
- After user watches all videos, memory same as without proxy
- But this rarely happens (users watch ~1% of catalog)

**5. Proxy vs RealSubject Synchronization**:
- Nếu RealVideo được updated, Proxy có thể outdated
- Need mechanism to refresh/invalidate proxy

### Khi nào KHÔNG nên dùng Proxy:

❌ **Object không expensive**: Nếu tạo object rất nhanh và nhẹ → overhead không worth it
❌ **Always need object**: Nếu luôn cần object ngay → lazy loading vô dụng
❌ **No access control needed**: Virtual proxy only makes sense cho expensive objects
❌ **Simple application**: Cho small app với vài objects → thêm complexity không cần thiết
❌ **Real-time performance critical**: Extra indirection có thể không chấp nhận được

### Best Practices:

✅ **Use Virtual Proxy for expensive objects**: Images, videos, large documents
✅ **Use Protection Proxy for security**: Premium content, sensitive data
✅ **Keep proxy lightweight**: Chỉ store minimal data (metadata)
✅ **Same interface**: Proxy và RealSubject must implement same interface
✅ **Lazy creation**: Create RealSubject only when truly needed
✅ **Document proxy type**: Clarify whether Virtual, Protection, Remote, or Smart
✅ **Consider caching**: Proxy có thể cache results (Smart Proxy)
✅ **Handle errors**: What if RealSubject creation fails?

---

## 5. Cài đặt

### Video Interface (Subject):

```java
public interface Video {
	void display();
	void play();
	String getTitle();
	String getDuration();
}
```

### RealVideo (RealSubject - Expensive Object):

```java
public class RealVideo implements Video {

	private String filename;
	private String title;
	private String duration;

	public RealVideo(String filename) {
		this.filename = filename;
		loadVideoFromDisk();  // Expensive operation!
	}

	private void loadVideoFromDisk() {
		// Simulate expensive loading operation
		System.out.println("   [RealVideo] 📁 Loading video from disk: " + filename);
		System.out.println("   [RealVideo] ⏳ Loading...");

		try {
			Thread.sleep(2000);  // Simulate 2 second load time
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Parse filename to get title
		this.title = filename.replace(".mp4", "").replace("_", " ");
		this.duration = "10:45";  // Simulated duration

		System.out.println("   [RealVideo] ✓ Video loaded successfully!");
		System.out.println("   [RealVideo] 💾 Video size: ~500MB");
	}

	@Override
	public void display() {
		System.out.println("\n┌────────────────────────────────────┐");
		System.out.println("│ 🎬 " + title);
		System.out.println("│ ⏱️  Duration: " + duration);
		System.out.println("│ 📊 Quality: 1080p");
		System.out.println("└────────────────────────────────────┘");
	}

	@Override
	public void play() {
		System.out.println("\n▶️  [RealVideo] Playing video: " + title);
		System.out.println("   [RealVideo] Buffering: ████████████ 100%");
		System.out.println("   [RealVideo] 🎵 Audio: ON | 🎞️  Video: ON");
		System.out.println("   [RealVideo] Now playing at 1080p...");
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDuration() {
		return duration;
	}
}
```

### VideoProxy (Virtual Proxy - Lazy Loading):

```java
public class VideoProxy implements Video {

	private RealVideo realVideo;  // Reference to real video (null initially)
	private String filename;
	private String title;
	private String duration;
	private boolean isPremium;

	public VideoProxy(String filename, boolean isPremium) {
		this.filename = filename;
		this.isPremium = isPremium;

		// Only store lightweight metadata
		this.title = filename.replace(".mp4", "").replace("_", " ");
		this.duration = "10:45";

		System.out.println("[VideoProxy] ⚡ Proxy created for: " + title + " (lightweight)");
	}

	@Override
	public void display() {
		// Can display without loading actual video!
		System.out.println("\n┌────────────────────────────────────┐");
		System.out.println("│ 🎬 " + title);
		System.out.println("│ ⏱️  Duration: " + duration);
		if (isPremium) {
			System.out.println("│ 👑 PREMIUM");
		} else {
			System.out.println("│ 🆓 FREE");
		}
		System.out.println("└────────────────────────────────────┘");
	}

	@Override
	public void play() {
		System.out.println("\n[VideoProxy] 🎬 Play requested for: " + title);

		// Lazy loading: create RealVideo only when needed
		if (realVideo == null) {
			System.out.println("[VideoProxy] 🔄 Lazy loading: Creating RealVideo...");
			realVideo = new RealVideo(filename);
		} else {
			System.out.println("[VideoProxy] ♻️  Using cached RealVideo");
		}

		// Delegate to real video
		realVideo.play();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDuration() {
		return duration;
	}

	public boolean isPremium() {
		return isPremium;
	}
}
```

### User Class (for Protection Proxy):

```java
public class User {

	private String name;
	private boolean hasSubscription;

	public User(String name, boolean hasSubscription) {
		this.name = name;
		this.hasSubscription = hasSubscription;
	}

	public String getName() {
		return name;
	}

	public boolean hasSubscription() {
		return hasSubscription;
	}

	public void subscribe() {
		this.hasSubscription = true;
		System.out.println("\n✨ " + name + " subscribed to Premium!");
	}
}
```

### PremiumVideoProxy (Protection Proxy - Access Control):

```java
public class PremiumVideoProxy implements Video {

	private VideoProxy videoProxy;
	private String filename;
	private User currentUser;
	private String title;

	public PremiumVideoProxy(String filename, User user) {
		this.filename = filename;
		this.currentUser = user;
		this.title = filename.replace(".mp4", "").replace("_", " ");

		System.out.println("[PremiumProxy] 🔒 Protection proxy created for premium content: " + title);
	}

	@Override
	public void display() {
		// Can always display preview
		System.out.println("\n┌────────────────────────────────────┐");
		System.out.println("│ 🎬 " + title);
		System.out.println("│ ⏱️  Duration: 10:45");
		System.out.println("│ 👑 PREMIUM CONTENT");
		if (!currentUser.hasSubscription()) {
			System.out.println("│ 🔒 Subscribe to watch");
		}
		System.out.println("└────────────────────────────────────┘");
	}

	@Override
	public void play() {
		System.out.println("\n[PremiumProxy] 🔐 Checking access rights...");
		System.out.println("[PremiumProxy] User: " + currentUser.getName());
		System.out.println("[PremiumProxy] Has subscription: " + currentUser.hasSubscription());

		if (!currentUser.hasSubscription()) {
			// Access denied
			System.out.println("\n╔════════════════════════════════════════╗");
			System.out.println("║  ⛔ ACCESS DENIED                     ║");
			System.out.println("║                                        ║");
			System.out.println("║  This is premium content.              ║");
			System.out.println("║  Upgrade to Premium to watch!          ║");
			System.out.println("║                                        ║");
			System.out.println("║  💎 Premium: $9.99/month               ║");
			System.out.println("║  ✓ Unlimited premium videos            ║");
			System.out.println("║  ✓ Ad-free experience                  ║");
			System.out.println("║  ✓ 4K quality                          ║");
			System.out.println("╚════════════════════════════════════════╝");
			return;
		}

		// Access granted - delegate to VideoProxy
		System.out.println("[PremiumProxy] ✅ Access granted!");

		if (videoProxy == null) {
			videoProxy = new VideoProxy(filename, true);
		}

		videoProxy.play();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDuration() {
		return "10:45";
	}
}
```

### ProxyDemo (Client):

```java
import java.util.ArrayList;
import java.util.List;

public class ProxyDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════╗");
		System.out.println("║        PROXY PATTERN DEMO                     ║");
		System.out.println("║        StreamFlix Video Platform              ║");
		System.out.println("║  (Linked: Observer + Adapter patterns)        ║");
		System.out.println("╚════════════════════════════════════════════════╝");

		// Create users
		User freeUser = new User("Bob", false);
		User premiumUser = new User("Lisa", true);

		System.out.println("\n👤 Users:");
		System.out.println("   Bob - Free user");
		System.out.println("   Lisa - Premium subscriber");

		// Test 1: Loading Video Library with Proxies (Fast!)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 1: App Startup - Loading Video Library");
		System.out.println("═══════════════════════════════════════════════");

		long startTime = System.currentTimeMillis();

		List<Video> videoLibrary = new ArrayList<>();
		videoLibrary.add(new VideoProxy("Cooking_Tutorial.mp4", false));
		videoLibrary.add(new VideoProxy("Tech_Review.mp4", false));
		videoLibrary.add(new VideoProxy("Travel_Vlog.mp4", false));
		videoLibrary.add(new PremiumVideoProxy("Exclusive_Concert.mp4", freeUser));
		videoLibrary.add(new PremiumVideoProxy("Masterclass_Series.mp4", freeUser));

		long endTime = System.currentTimeMillis();

		System.out.println("\n✓ Video library loaded!");
		System.out.println("   Videos in catalog: " + videoLibrary.size());
		System.out.println("   Loading time: " + (endTime - startTime) + "ms");
		System.out.println("   Memory used: ~5MB (only proxies)");
		System.out.println("\n💡 Without proxy: Would take 10+ seconds and 2.5GB memory!");

		// Test 2: Browsing Videos (Display without loading)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 2: Browsing Video Catalog");
		System.out.println("═══════════════════════════════════════════════");

		System.out.println("\n📋 Available videos:");
		for (Video video : videoLibrary) {
			video.display();
		}

		// Test 3: Playing Free Video (Lazy Loading)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 3: Free User Plays Free Video");
		System.out.println("═══════════════════════════════════════════════");

		Video cookingTutorial = videoLibrary.get(0);
		System.out.println("\n👤 Bob clicks on: " + cookingTutorial.getTitle());
		cookingTutorial.play();  // First time: loads video (2 seconds)

		System.out.println("\n\n--- Playing Same Video Again ---");
		cookingTutorial.play();  // Second time: instant (cached)

		// Test 4: Free User tries Premium Video (Access Denied)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 4: Free User Tries Premium Video");
		System.out.println("═══════════════════════════════════════════════");

		Video exclusiveConcert = videoLibrary.get(3);
		System.out.println("\n👤 Bob (free user) clicks on: " + exclusiveConcert.getTitle());
		exclusiveConcert.play();  // Access denied!

		// Test 5: Free User Subscribes
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 5: User Subscribes to Premium");
		System.out.println("═══════════════════════════════════════════════");

		freeUser.subscribe();

		// Test 6: Now Premium User can watch Premium Video
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 6: Premium User Watches Premium Video");
		System.out.println("═══════════════════════════════════════════════");

		// Need to create new proxy with updated user
		Video premiumConcert = new PremiumVideoProxy("Exclusive_Concert.mp4", freeUser);
		System.out.println("\n👤 Bob (now premium) clicks on: " + premiumConcert.getTitle());
		premiumConcert.play();  // Access granted!

		// Test 7: Premium User (Lisa) watches Premium Video
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 7: Another Premium User");
		System.out.println("═══════════════════════════════════════════════");

		Video masterclass = new PremiumVideoProxy("Masterclass_Series.mp4", premiumUser);
		System.out.println("\n👤 Lisa (premium) clicks on: " + masterclass.getTitle());
		masterclass.play();

		// Summary
		System.out.println("\n\n╔════════════════════════════════════════════════╗");
		System.out.println("║              SUMMARY                          ║");
		System.out.println("╚════════════════════════════════════════════════╝");
		System.out.println("✓ Virtual Proxy: Lazy loading videos on demand");
		System.out.println("✓ Protection Proxy: Access control for premium content");
		System.out.println("✓ Fast startup: 5ms vs 10+ seconds without proxy");
		System.out.println("✓ Memory efficient: 5MB vs 2.5GB without proxy");
		System.out.println("✓ Transparent: Same interface for all videos");
		System.out.println("✓ Secure: Premium content protected");
		System.out.println("\n🎬 Context Link: Video platform like YouTube (Observer)");
		System.out.println("   using media players (Adapter) with smart loading!");
		System.out.println("════════════════════════════════════════════════");
	}
}
```

---

## 6. Kết quả chạy chương trình

```
╔════════════════════════════════════════════════╗
║        PROXY PATTERN DEMO                     ║
║        StreamFlix Video Platform              ║
║  (Linked: Observer + Adapter patterns)        ║
╚════════════════════════════════════════════════╝

👤 Users:
   Bob - Free user
   Lisa - Premium subscriber


═══════════════════════════════════════════════
TEST 1: App Startup - Loading Video Library
═══════════════════════════════════════════════
[VideoProxy] ⚡ Proxy created for: Cooking Tutorial (lightweight)
[VideoProxy] ⚡ Proxy created for: Tech Review (lightweight)
[VideoProxy] ⚡ Proxy created for: Travel Vlog (lightweight)
[PremiumProxy] 🔒 Protection proxy created for premium content: Exclusive Concert
[PremiumProxy] 🔒 Protection proxy created for premium content: Masterclass Series

✓ Video library loaded!
   Videos in catalog: 5
   Loading time: 15ms
   Memory used: ~5MB (only proxies)

💡 Without proxy: Would take 10+ seconds and 2.5GB memory!


═══════════════════════════════════════════════
TEST 2: Browsing Video Catalog
═══════════════════════════════════════════════

📋 Available videos:

┌────────────────────────────────────┐
│ 🎬 Cooking Tutorial
│ ⏱️  Duration: 10:45
│ 🆓 FREE
└────────────────────────────────────┘

┌────────────────────────────────────┐
│ 🎬 Tech Review
│ ⏱️  Duration: 10:45
│ 🆓 FREE
└────────────────────────────────────┘

┌────────────────────────────────────┐
│ 🎬 Travel Vlog
│ ⏱️  Duration: 10:45
│ 🆓 FREE
└────────────────────────────────────┘

┌────────────────────────────────────┐
│ 🎬 Exclusive Concert
│ ⏱️  Duration: 10:45
│ 👑 PREMIUM CONTENT
│ 🔒 Subscribe to watch
└────────────────────────────────────┘

┌────────────────────────────────────┐
│ 🎬 Masterclass Series
│ ⏱️  Duration: 10:45
│ 👑 PREMIUM CONTENT
│ 🔒 Subscribe to watch
└────────────────────────────────────┘


═══════════════════════════════════════════════
TEST 3: Free User Plays Free Video
═══════════════════════════════════════════════

👤 Bob clicks on: Cooking Tutorial

[VideoProxy] 🎬 Play requested for: Cooking Tutorial
[VideoProxy] 🔄 Lazy loading: Creating RealVideo...
   [RealVideo] 📁 Loading video from disk: Cooking_Tutorial.mp4
   [RealVideo] ⏳ Loading...
   [RealVideo] ✓ Video loaded successfully!
   [RealVideo] 💾 Video size: ~500MB

▶️  [RealVideo] Playing video: Cooking Tutorial
   [RealVideo] Buffering: ████████████ 100%
   [RealVideo] 🎵 Audio: ON | 🎞️  Video: ON
   [RealVideo] Now playing at 1080p...


--- Playing Same Video Again ---

[VideoProxy] 🎬 Play requested for: Cooking Tutorial
[VideoProxy] ♻️  Using cached RealVideo

▶️  [RealVideo] Playing video: Cooking Tutorial
   [RealVideo] Buffering: ████████████ 100%
   [RealVideo] 🎵 Audio: ON | 🎞️  Video: ON
   [RealVideo] Now playing at 1080p...


═══════════════════════════════════════════════
TEST 4: Free User Tries Premium Video
═══════════════════════════════════════════════

👤 Bob (free user) clicks on: Exclusive Concert

[PremiumProxy] 🔐 Checking access rights...
[PremiumProxy] User: Bob
[PremiumProxy] Has subscription: false

╔════════════════════════════════════════╗
║  ⛔ ACCESS DENIED                     ║
║                                        ║
║  This is premium content.              ║
║  Upgrade to Premium to watch!          ║
║                                        ║
║  💎 Premium: $9.99/month               ║
║  ✓ Unlimited premium videos            ║
║  ✓ Ad-free experience                  ║
║  ✓ 4K quality                          ║
╚════════════════════════════════════════╝


═══════════════════════════════════════════════
TEST 5: User Subscribes to Premium
═══════════════════════════════════════════════

✨ Bob subscribed to Premium!


═══════════════════════════════════════════════
TEST 6: Premium User Watches Premium Video
═══════════════════════════════════════════════
[PremiumProxy] 🔒 Protection proxy created for premium content: Exclusive Concert

👤 Bob (now premium) clicks on: Exclusive Concert

[PremiumProxy] 🔐 Checking access rights...
[PremiumProxy] User: Bob
[PremiumProxy] Has subscription: true
[PremiumProxy] ✅ Access granted!
[VideoProxy] ⚡ Proxy created for: Exclusive Concert (lightweight)

[VideoProxy] 🎬 Play requested for: Exclusive Concert
[VideoProxy] 🔄 Lazy loading: Creating RealVideo...
   [RealVideo] 📁 Loading video from disk: Exclusive_Concert.mp4
   [RealVideo] ⏳ Loading...
   [RealVideo] ✓ Video loaded successfully!
   [RealVideo] 💾 Video size: ~500MB

▶️  [RealVideo] Playing video: Exclusive Concert
   [RealVideo] Buffering: ████████████ 100%
   [RealVideo] 🎵 Audio: ON | 🎞️  Video: ON
   [RealVideo] Now playing at 1080p...


═══════════════════════════════════════════════
TEST 7: Another Premium User
═══════════════════════════════════════════════
[PremiumProxy] 🔒 Protection proxy created for premium content: Masterclass Series

👤 Lisa (premium) clicks on: Masterclass Series

[PremiumProxy] 🔐 Checking access rights...
[PremiumProxy] User: Lisa
[PremiumProxy] Has subscription: true
[PremiumProxy] ✅ Access granted!
[VideoProxy] ⚡ Proxy created for: Masterclass Series (lightweight)

[VideoProxy] 🎬 Play requested for: Masterclass Series
[VideoProxy] 🔄 Lazy loading: Creating RealVideo...
   [RealVideo] 📁 Loading video from disk: Masterclass_Series.mp4
   [RealVideo] ⏳ Loading...
   [RealVideo] ✓ Video loaded successfully!
   [RealVideo] 💾 Video size: ~500MB

▶️  [RealVideo] Playing video: Masterclass Series
   [RealVideo] Buffering: ████████████ 100%
   [RealVideo] 🎵 Audio: ON | 🎞️  Video: ON
   [RealVideo] Now playing at 1080p...


╔════════════════════════════════════════════════╗
║              SUMMARY                          ║
╚════════════════════════════════════════════════╝
✓ Virtual Proxy: Lazy loading videos on demand
✓ Protection Proxy: Access control for premium content
✓ Fast startup: 5ms vs 10+ seconds without proxy
✓ Memory efficient: 5MB vs 2.5GB without proxy
✓ Transparent: Same interface for all videos
✓ Secure: Premium content protected

🎬 Context Link: Video platform like YouTube (Observer)
   using media players (Adapter) with smart loading!
════════════════════════════════════════════════
```

### Giải thích cách pattern hoạt động:

**1. App Startup (TEST 1)**:
- Created 5 Video proxies in 15ms
- Only metadata stored (filename, title, duration)
- **NO actual videos loaded** → extremely fast!
- Without proxy: Would load all 5 videos = 10+ seconds

**2. Browsing Catalog (TEST 2)**:
- Can display all videos immediately
- `display()` method doesn't need RealVideo
- User sees thumbnails và info instantly

**3. Playing Free Video (TEST 3)**:
- **First time**: VideoProxy creates RealVideo (lazy loading - 2 seconds)
- RealVideo loads actual file from disk
- **Second time**: Uses cached RealVideo (instant!)
- Demonstrates Virtual Proxy benefits

**4. Free User + Premium Video (TEST 4)**:
- PremiumProxy checks subscription: **false**
- Access denied immediately
- **No video loaded** → saves bandwidth!
- Shows upgrade prompt

**5. User Subscribes (TEST 5)**:
- User object updated with subscription
- Now can access premium content

**6. Premium User + Premium Video (TEST 6)**:
- PremiumProxy checks subscription: **true**
- Access granted → delegates to VideoProxy
- VideoProxy lazy loads RealVideo
- Video plays successfully

**7. Multiple Proxy Layers**:
```
Client → PremiumVideoProxy (access control)
           ↓ if authorized
         VideoProxy (lazy loading)
           ↓ if not loaded
         RealVideo (actual playback)
```

### Key Benefits Demonstrated:

✅ **Fast Startup**: 15ms vs 10+ seconds (670x faster)
✅ **Memory Efficient**: 5MB vs 2.5GB (500x less)
✅ **Lazy Loading**: Videos loaded only when played
✅ **Access Control**: Premium content protected
✅ **Caching**: Played videos cached for instant replay
✅ **Transparency**: Same interface for all video types
✅ **Scalability**: Works with 1000+ videos

### Context Link:
This connects perfectly with:
- **Observer Pattern** (YouTube Channel): Platform for video distribution
- **Adapter Pattern** (Media Player): Playing videos with different formats

All three patterns work in the **video/media domain** → easier to remember! 🎬

---

## 7. Sơ đồ UML

### Class Diagram:

```
┌─────────────────────────────┐
│    <<interface>>            │
│         Video               │
├─────────────────────────────┤
│ + display()                 │
│ + play()                    │
│ + getTitle(): String        │
│ + getDuration(): String     │
└─────────────────────────────┘
            △
            │ implements
    ┌───────┼───────┬─────────┐
    │       │       │         │
┌─────────┐┌──────┐┌────────┐┌──────────┐
│RealVideo││Video ││Premium ││User      │
│         ││Proxy ││Video   ││          │
│         ││      ││Proxy   ││          │
└─────────┘└──────┘└────────┘└──────────┘
            │
            │ creates (lazy)
            ↓
         RealVideo
```

### Detailed Component Description:

**1. Video Interface (Subject)**:
```java
interface Video {
    + display(): void
    + play(): void
    + getTitle(): String
    + getDuration(): String
}
```
- Common interface cho tất cả video types
- Client code uses this interface
- Enables transparency (client doesn't know proxy vs real)

**2. RealVideo (RealSubject)**:
- **Fields**:
  - `filename: String`
  - `title: String`
  - `duration: String`
- **Methods**:
  - `RealVideo(filename)`: Constructor - **expensive operation!** (2 seconds)
  - `loadVideoFromDisk()`: Private method loads actual video file
  - `display()`: Show video information
  - `play()`: Actual video playback
- **Characteristics**:
  - Heavy object (~500MB)
  - Expensive to create
  - Contains actual video data

**3. VideoProxy (Virtual Proxy)**:
- **Fields**:
  - `realVideo: RealVideo` - Reference (null initially)
  - `filename: String` - Lightweight metadata
  - `title: String` - Lightweight metadata
  - `duration: String` - Lightweight metadata
  - `isPremium: boolean` - Content type flag
- **Methods**:
  - `VideoProxy(filename, isPremium)`: Constructor - **instant!** (only stores metadata)
  - `display()`: Can display without loading RealVideo
  - `play()`: Lazy creates RealVideo if null, then delegates
  - `getTitle()`, `getDuration()`: Return metadata
- **Characteristics**:
  - Lightweight placeholder (~1KB)
  - Lazy initialization
  - Caches RealVideo after first creation

**4. PremiumVideoProxy (Protection Proxy)**:
- **Fields**:
  - `videoProxy: VideoProxy` - Reference to Virtual Proxy
  - `filename: String` - Video file name
  - `currentUser: User` - User requesting access
  - `title: String` - Metadata
- **Methods**:
  - `PremiumVideoProxy(filename, user)`: Constructor with user context
  - `display()`: Always allowed (preview)
  - `play()`: **Checks subscription first!**
    - If not subscribed → show upgrade message
    - If subscribed → delegate to VideoProxy
- **Characteristics**:
  - Access control layer
  - Wraps VideoProxy
  - Security enforcement

**5. User Class** (Supporting):
- **Fields**:
  - `name: String`
  - `hasSubscription: boolean`
- **Methods**:
  - `getName()`: Get user name
  - `hasSubscription()`: Check subscription status
  - `subscribe()`: Upgrade to premium

**6. ProxyDemo (Client)**:
- Creates video library with proxies
- Demonstrates lazy loading
- Demonstrates access control
- Shows performance benefits

### Relationships:

**Implements**:
- `RealVideo` implements `Video`
- `VideoProxy` implements `Video`
- `PremiumVideoProxy` implements `Video`

**Aggregation** (Has-A):
- `VideoProxy` → `RealVideo` (creates lazily, caches)
- `PremiumVideoProxy` → `VideoProxy` (delegates if authorized)
- `PremiumVideoProxy` → `User` (checks permissions)

**Dependencies**:
- `ProxyDemo` uses `Video` interface
- `ProxyDemo` creates `VideoProxy` and `PremiumVideoProxy`

### Interaction Flow:

**Scenario 1: Free Video with Virtual Proxy**
```
1. Client creates VideoProxy("video.mp4")
   → Proxy stores only filename (instant)

2. Client calls proxy.display()
   → Proxy displays metadata (no RealVideo needed)

3. Client calls proxy.play()
   → Proxy checks: realVideo == null?
   → Yes → Create RealVideo (2 seconds)
   → Delegate: realVideo.play()

4. Client calls proxy.play() again
   → Proxy checks: realVideo == null?
   → No → Use cached RealVideo (instant)
   → Delegate: realVideo.play()
```

**Scenario 2: Premium Video with Protection Proxy**
```
1. Client creates PremiumVideoProxy("premium.mp4", user)
   → Protection Proxy stores filename + user

2. Client calls premiumProxy.play()
   → PremiumProxy checks: user.hasSubscription()?

   If false:
   → Show "Access Denied" message
   → Return (no video loaded)

   If true:
   → Create VideoProxy if needed
   → Call videoProxy.play()
   → VideoProxy lazily creates RealVideo
   → Video plays
```

### Multiple Proxy Layers:

```
Client
  ↓ calls play()
PremiumVideoProxy (Protection Proxy)
  ↓ check subscription
  ├─ Access Denied → Stop
  └─ Access Granted → Delegate
     ↓
VideoProxy (Virtual Proxy)
  ↓ lazy create if needed
  ├─ realVideo == null → Create RealVideo
  └─ realVideo != null → Use cached
     ↓
RealVideo (Actual Object)
  ↓ actual work
Play video
```

### UML Notes:
- **Transparency**: All classes implement same Video interface
- **Lazy Loading**: VideoProxy creates RealVideo only when play() called
- **Access Control**: PremiumVideoProxy checks before delegating
- **Caching**: VideoProxy keeps RealVideo reference after creation
- **Delegation**: Proxies forward calls to wrapped objects

### Comparison Table:

| Class | Memory | Creation Time | When Created | Purpose |
|-------|--------|---------------|--------------|---------|
| RealVideo | 500MB | 2 seconds | When first play() | Actual video |
| VideoProxy | 1KB | Instant | At startup | Lazy loading |
| PremiumVideoProxy | 1KB | Instant | At startup | Access control |

---

## 8. Tổng kết

### Kết luận về bài toán:

**StreamFlix Video Streaming Platform** là ví dụ hoàn hảo của **Proxy Pattern** trong thực tế, giải quyết vấn đề:

✅ **Expensive Object Creation**: Videos là objects rất nặng (500MB - 2GB)
✅ **Lazy Loading**: Load videos chỉ khi thực sự cần (Virtual Proxy)
✅ **Access Control**: Protect premium content với subscription check (Protection Proxy)
✅ **Performance**: App startup 670x faster (10s → 15ms)
✅ **Memory Efficiency**: 500x less memory (2.5GB → 5MB)
✅ **Transparency**: Client code không biết đang dùng Proxy

### 🎬 Context Linking (Mental Connection):

Bài toán này **liên kết** với 2 patterns đã học:
1. **Observer Pattern** (YouTube Channel): Platform distributing videos to subscribers
2. **Adapter Pattern** (Media Player): Playing different video formats

→ Tất cả 3 patterns trong **video/media domain** → **easier to remember!**

**Memory Anchor**:
"Video Platform = Observer (subscribers) + Adapter (players) + Proxy (smart loading)"

### Ứng dụng thực tế của Proxy Pattern:

**1. Video/Image Platforms** 🎬:
- **Netflix, YouTube, Spotify**: Lazy load videos/songs
- **Instagram, Pinterest**: Virtual proxy cho images
- **Twitch**: Lazy load live streams

**2. Document Viewers** 📄:
- **Google Docs, Office 365**: Load documents on demand
- **PDF Readers**: Load pages lazily
- **E-book Readers**: Virtual proxy for book chapters

**3. Database Access** 💾:
- **ORM Frameworks (Hibernate)**: Lazy loading relationships
- **Connection Pools**: Proxy for database connections
- **Lazy Collections**: Load data only when accessed

**4. Security Systems** 🔒:
- **Authentication Proxies**: Check credentials before access
- **API Gateways**: Protection proxy for backend services
- **Firewalls**: Proxy for network access control

**5. Caching Systems** ⚡:
- **CDNs**: Cache proxy for web content
- **Reverse Proxies (Nginx)**: Cache responses
- **Browser Proxies**: Cache web pages

**6. Remote Systems** 🌐:
- **RMI, RPC**: Remote proxy hiding network complexity
- **Web Services**: SOAP/REST client proxies
- **Distributed Objects**: CORBA, .NET Remoting

### Khi nào nên dùng Proxy Pattern:

✅ **Nên dùng khi**:
- Object expensive to create (memory, time, I/O)
- Need lazy initialization to improve performance
- Need access control (security, permissions)
- Object is remote (hide network complexity)
- Need add functionality transparently (logging, caching)
- Want to defer expensive operations

❌ **KHÔNG nên dùng khi**:
- Object very lightweight (proxy overhead not worth it)
- Always need object immediately (lazy loading useless)
- No access control needed
- Simple application (added complexity not justified)
- Real-time critical (proxy indirection unacceptable)

### Proxy vs Decorator Pattern:

| Aspect | Proxy | Decorator |
|--------|-------|-----------|
| **Purpose** | Control access, lazy loading | Add responsibilities |
| **Focus** | Manage object lifecycle | Extend functionality |
| **Creation** | Proxy creates/manages RealSubject | Decorator wraps existing object |
| **Intent** | Placeholder/surrogate | Enhancement |
| **Transparency** | Yes - same interface | Yes - same interface |
| **Example** | Lazy load video | Add scrollbar to window |
| **When** | Need control | Need extend |

**Key Distinction**:
- **Proxy**: "I control access to object"
- **Decorator**: "I add features to object"

### Types of Proxy - Quick Reference:

| Type | Purpose | Example | When to Use |
|------|---------|---------|-------------|
| **Virtual Proxy** | Lazy loading | Load video when play | Expensive to create |
| **Protection Proxy** | Access control | Premium content check | Need security |
| **Remote Proxy** | Hide remote object | RMI stub | Distributed system |
| **Smart Proxy** | Additional logic | Cache, logging | Need enhancements |

### Key Takeaways:

🎯 **Proxy Pattern giải quyết**:
- Expensive object creation → Lazy loading
- Uncontrolled access → Access control
- Remote complexity → Local representative
- Missing functionality → Transparent enhancements

⚠️ **Cần lưu ý**:
- Extra indirection (slight overhead)
- First access has delay (lazy loading tradeoff)
- More classes to maintain
- Proxy-RealSubject synchronization

💡 **Best Practices**:
- Keep proxy lightweight
- Same interface for transparency
- Document proxy type clearly
- Consider caching in proxy
- Handle creation failures
- Use factory for multiple proxy types

### Real-World Impact:

**Netflix Example**:
- Catalog: 10,000 videos
- Without Proxy: Load all → 50 minutes startup, 5TB memory
- With Proxy: Create proxies → 5 seconds startup, 100MB memory
- User watches 3 videos → Only 3 loaded (1.5GB)
- **Bandwidth saved**: 99.97%!

### Tương lai và mở rộng:

**Có thể extend system này với**:
1. **Smart Caching Proxy**: Cache frequently watched videos
2. **Logging Proxy**: Track viewing analytics
3. **Bandwidth Proxy**: Adjust quality based on connection
4. **Multi-language Proxy**: Lazy load subtitle tracks
5. **Thumbnail Proxy**: Separate proxy for thumbnails
6. **CDN Integration**: Remote proxy for distributed delivery

**Integration với patterns khác**:
- **Factory**: Create appropriate proxy type
- **Strategy**: Different loading strategies
- **Observer**: Notify when video loaded
- **Decorator**: Add features to proxies

Proxy Pattern là fundamental pattern cho modern applications, đặc biệt trong **video streaming**, **document management**, và **distributed systems**. Hiểu rõ pattern này giúp bạn build **performant**, **secure**, và **scalable** systems! 🚀
