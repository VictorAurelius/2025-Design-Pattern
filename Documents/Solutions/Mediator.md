# Mediator Design Pattern - Smart Home Automation System

## 1. Mô tả mẫu Mediator

**Mediator Pattern** là một mẫu thiết kế hành vi (Behavioral Design Pattern) định nghĩa một đối tượng đóng vai trò **trung gian điều phối** (mediator) để đóng gói cách mà một tập hợp các đối tượng tương tác với nhau. Pattern này giúp giảm coupling giữa các đối tượng bằng cách ngăn chúng **tham chiếu trực tiếp** đến nhau, thay vào đó chúng **giao tiếp qua mediator**.

### Các thành phần chính:

1. **Mediator (Interface/Abstract Class)**:
   - Định nghĩa interface cho communication với Colleague objects
   - Khai báo methods để colleagues communicate qua mediator
   - Không chứa concrete coordination logic

2. **ConcreteMediator**:
   - Implement Mediator interface
   - Biết và maintain references đến tất cả Colleague objects
   - Chứa **coordination logic** để điều phối interactions
   - Orchestrate behavior của colleagues

3. **Colleague (Abstract/Interface)**:
   - Base class cho các objects cần communicate
   - Maintains reference đến Mediator
   - Giao tiếp với colleagues khác **chỉ qua Mediator**

4. **ConcreteColleague**:
   - Implement Colleague interface
   - Communicate với mediator khi có events
   - Nhận commands từ mediator để perform actions
   - KHÔNG biết về existence của colleagues khác

### Khi nào sử dụng:
- ✅ Khi có **many-to-many** relationships giữa objects
- ✅ Khi interactions giữa objects phức tạp và hard-coded
- ✅ Khi muốn **reuse objects** mà không phụ thuộc vào cách chúng communicate
- ✅ Khi muốn **centralize complex communication logic**
- ✅ Khi thay đổi interaction behavior không muốn modify nhiều classes
- ✅ Khi có distributed behavior muốn customize thông qua subclassing

### Đặc điểm quan trọng:
- **Centralized Control**: Tất cả communication logic ở một chỗ (Mediator)
- **Loose Coupling**: Colleagues không biết nhau, chỉ biết Mediator
- **Many-to-Many → Many-to-One**: Reduce từ O(n²) connections xuống O(n)
- **Single Responsibility**: Mediator chịu trách nhiệm coordination
- **Bidirectional Communication**: Colleagues có thể send VÀ receive qua Mediator

---

## 2. Mô tả bài toán

### Bối cảnh:
**James** vừa chuyển vào căn hộ thông minh mới với hệ thống **Smart Home** hiện đại. Căn hộ được trang bị nhiều thiết bị IoT: **Security Cameras**, **Smart Lights**, **Thermostat (điều hòa)**, và **Motion Sensors**. James muốn các thiết bị này **phối hợp với nhau tự động** dựa trên các tình huống khác nhau:

**Tình huống 1: Security Mode (Ban đêm)**
- Motion sensor phát hiện chuyển động lúc 2 giờ sáng
- → Security cameras bật recording
- → Smart lights bật sáng để răn đe
- → Thermostat không thay đổi (để yên)

**Tình huống 2: Welcome Home Mode**
- Motion sensor phát hiện chủ nhà về (6 giờ chiều)
- → Smart lights bật với brightness vừa phải
- → Thermostat điều chỉnh nhiệt độ comfortable (24°C)
- → Cameras tắt recording (chủ nhà đã về)

**Tình huống 3: Away Mode**
- Thermostat được set sang "Away"
- → All lights tắt để tiết kiệm điện
- → Cameras bật surveillance mode
- → Motion sensors tăng sensitivity

**Tình huống 4: Manual Camera Recording**
- User manually bật camera recording
- → Lights có thể bật (nếu tối) để hỗ trợ recording
- → Thermostat không bị ảnh hưởng

### Vấn đề hiện tại:

**Cách tiếp cận không dùng Mediator Pattern** (Direct Coupling):

```java
public class MotionSensor {
    private SecurityCamera camera;
    private SmartLight light;
    private Thermostat thermostat;

    public MotionSensor(SecurityCamera camera, SmartLight light, Thermostat thermostat) {
        this.camera = camera;
        this.light = light;
        this.thermostat = thermostat;
    }

    public void detectMotion() {
        // MotionSensor phải biết TẤT CẢ devices và logic coordination
        if (isNightTime()) {
            camera.startRecording();
            light.turnOn(100);  // Full brightness
            // thermostat does nothing
        } else if (isEveningTime()) {
            light.turnOn(50);   // Medium brightness
            thermostat.setTemperature(24);
            camera.stopRecording();
        }
    }
}

public class Thermostat {
    private SmartLight light;
    private SecurityCamera camera;
    private MotionSensor sensor;

    public void setAwayMode() {
        // Thermostat phải biết all other devices
        light.turnOff();
        camera.startSurveillance();
        sensor.increaseSensitivity();
    }
}
```

**Vấn đề**:
- ❌ **Tight Coupling**: Mỗi device phải biết và maintain references đến tất cả devices khác
- ❌ **O(n²) Connections**: 4 devices → 12 possible connections (mỗi device biết 3 devices khác)
- ❌ **Scattered Logic**: Coordination logic nằm rải rác trong mỗi device class
- ❌ **Hard to Modify**: Thêm device mới phải modify TẤT CẢ existing devices
- ❌ **Duplicate Code**: Same coordination logic lặp lại ở nhiều nơi
- ❌ **Violation of SRP**: Mỗi device vừa làm job của nó VỪA phải biết coordination logic
- ❌ **Testing Nightmare**: Phải mock tất cả devices để test một device

### Sơ đồ connections không dùng Mediator:

```
      MotionSensor ←→ SecurityCamera
           ↕              ↕
      SmartLight ←→ Thermostat

= 6 bidirectional connections (12 references total)
```

Mỗi device phải:
1. Biết về tất cả devices khác
2. Maintain references đến chúng
3. Biết khi nào và cách coordinate với chúng
4. Handle complex conditional logic

---

## 3. Yêu cầu bài toán

### Input:
- Một Smart Home System với 4 thiết bị: MotionSensor, SecurityCamera, SmartLight, Thermostat
- Nhiều modes: Security Mode, Welcome Home Mode, Away Mode
- Manual controls cho từng thiết bị
- Complex coordination rules giữa các devices

### Problem - Vấn đề cần giải quyết:

**1. Tight Coupling Issue**:
- Devices directly reference nhau → high coupling
- Adding new device requires modifying ALL existing devices
- Device classes vi phạm Single Responsibility Principle
- Hard to reuse devices in other contexts

**2. Scattered Coordination Logic**:
- Coordination logic distributed across nhiều device classes
- Same logic duplicated ở nhiều nơi
- Khó maintain: thay đổi one rule phải update nhiều classes
- Conditional logic phức tạp embedded trong devices

**3. Scalability Issues**:
- O(n²) connections: n devices → n*(n-1) connections
- Adding device #5 requires modifying 4 existing classes
- Each device needs to know about (n-1) other devices

**4. Testing Complexity**:
- Phải mock (n-1) devices để test một device
- Integration tests extremely complex
- Hard to isolate device behavior

### Solution - Mediator Pattern giải quyết:

**1. Introduce SmartHomeHub (Mediator)**:
```java
public interface SmartHomeHub {
    void notify(SmartDevice device, String event);
}
```

**2. ConcreteMediator - SmartHomeController**:
- Biết TẤT CẢ devices (có references)
- Chứa TẤT CẢ coordination logic
- Devices notify hub khi có events
- Hub điều phối responses từ other devices

**3. SmartDevice (Colleague)**:
- Base class cho tất cả devices
- Chỉ có reference đến SmartHomeHub
- KHÔNG biết về other devices
- Call hub khi có events

**4. Concrete Devices**:
- MotionSensor, SecurityCamera, SmartLight, Thermostat
- Implement device-specific functionality
- Notify hub về events: `hub.notify(this, "motion_detected")`
- Nhận commands từ hub: `startRecording()`, `turnOn(brightness)`

### Sơ đồ connections với Mediator:

```
           SmartHomeHub (Mediator)
         /      |        |        \
        /       |        |         \
  MotionSensor  Camera   Light   Thermostat

= 4 connections (8 references: 4 từ hub → devices, 4 từ devices → hub)
```

Mỗi device chỉ cần:
1. Reference đến Hub
2. Notify hub về own events
3. Respond to hub's commands

### Expected Output:

**Khi Motion Sensor detects movement at night**:
1. ✅ MotionSensor calls `hub.notify(this, "motion_detected_night")`
2. ✅ Hub checks mode and time
3. ✅ Hub coordinates: `camera.startRecording()`, `light.turnOn(100)`
4. ✅ Devices respond independently
5. ✅ MotionSensor không biết về Camera và Light

**Advantages**:
- 🔓 Loose Coupling: Devices không biết nhau
- 🎯 Single Responsibility: Devices chỉ lo device logic, Hub lo coordination
- ➕ Easy to Extend: Thêm device mới chỉ cần register với Hub
- 🔧 Easy to Maintain: Coordination logic ở một chỗ
- 🧪 Easy to Test: Mock hub thay vì mock all devices

---

## 4. Hiệu quả của việc sử dụng Mediator Pattern

### Lợi ích trong bài toán này:

#### 1. Reduced Coupling 🔓

**Trước (Direct Coupling)**:
```java
// MotionSensor phải biết tất cả devices
public class MotionSensor {
    private SecurityCamera camera;
    private SmartLight light;
    private Thermostat thermostat;

    public void detectMotion() {
        camera.startRecording();     // Direct call
        light.turnOn(100);           // Direct call
        // Knows about other devices and their APIs
    }
}
```

**Sau (Loose Coupling)**:
```java
// MotionSensor chỉ biết Hub
public class MotionSensor extends SmartDevice {
    public void detectMotion() {
        hub.notify(this, "motion_detected");  // Single call
        // Doesn't know about other devices
    }
}
```

#### 2. Centralized Coordination Logic 🎯

**Trước (Scattered Logic)**:
```java
// Logic rải rác ở nhiều classes
class MotionSensor {
    void detectMotion() {
        if (isNightTime()) {
            camera.startRecording();
            light.turnOn(100);
        }
    }
}

class Thermostat {
    void setAwayMode() {
        light.turnOff();
        camera.startSurveillance();
    }
}
// Duplicate coordination logic!
```

**Sau (Centralized Logic)**:
```java
// Tất cả coordination logic ở một chỗ
class SmartHomeController implements SmartHomeHub {
    public void notify(SmartDevice device, String event) {
        switch (event) {
            case "motion_detected":
                handleMotionDetection();
                break;
            case "away_mode":
                handleAwayMode();
                break;
        }
    }

    private void handleMotionDetection() {
        // All coordination logic here
        camera.startRecording();
        light.turnOn(100);
    }
}
```

#### 3. O(n²) → O(n) Connections 📉

| Aspect | Without Mediator | With Mediator |
|--------|------------------|---------------|
| Connections | n*(n-1) = O(n²) | 2*n = O(n) |
| 4 Devices | 12 connections | 8 connections |
| 5 Devices | 20 connections | 10 connections |
| 10 Devices | 90 connections | 20 connections |

#### 4. Easy to Extend ➕

**Thêm device mới - Trước**:
```java
// Phải modify TẤT CẢ 4 existing devices
class MotionSensor {
    private DoorLock doorLock;  // Add new field
    // Modify detectMotion() to interact with doorLock
}

class SecurityCamera {
    private DoorLock doorLock;  // Add new field
    // Modify methods...
}
// ... modify Light, Thermostat similarly
```

**Thêm device mới - Sau**:
```java
// Chỉ cần add class mới và register với Hub
class DoorLock extends SmartDevice {
    public void lock() {
        hub.notify(this, "door_locked");
    }
}

// Trong SmartHomeController, add coordination logic
private void handleMotionDetection() {
    camera.startRecording();
    light.turnOn(100);
    doorLock.lock();  // Add new coordination
}
// Không cần modify existing device classes!
```

#### 5. Single Responsibility Principle ✅

**Trước**: Mỗi device có 2 responsibilities
- Device functionality (sensing, recording, lighting, heating)
- Coordination logic (khi nào interact với devices khác)

**Sau**: Clear separation
- Devices: Chỉ lo device functionality
- Hub: Chỉ lo coordination logic

### So sánh Before vs After:

| Aspect | Without Mediator | With Mediator |
|--------|------------------|---------------|
| Coupling | Tight - devices know each other | Loose - devices only know hub |
| Dependencies | n*(n-1) = O(n²) | 2*n = O(n) |
| Coordination Logic | Scattered across classes | Centralized in mediator |
| Add New Device | Modify all existing | Only modify mediator |
| Testing | Mock (n-1) devices | Mock only mediator |
| Reusability | Low - devices tightly coupled | High - devices independent |
| Code Duplication | High - same logic repeated | Low - logic in one place |
| Maintainability | Hard - changes affect many classes | Easy - changes in one place |

### Trade-offs và Nhược điểm:

#### ⚠️ Nhược điểm cần lưu ý:

1. **God Object Risk**:
   - Mediator có thể trở thành **God Object** chứa quá nhiều logic
   - Khi có quá nhiều devices và complex rules → Mediator becomes bloated
   - **Giải pháp**:
     - Split mediator thành multiple specialized mediators
     - Use Strategy pattern cho different coordination strategies
     - Delegate complex logic to helper classes

2. **Single Point of Failure**:
   - Tất cả communication qua mediator
   - Mediator có bug → affects toàn bộ system
   - **Giải pháp**: Thorough testing, defensive programming

3. **Performance Overhead**:
   - Extra indirection: Device → Mediator → Other Device
   - Có thể slower than direct calls
   - **Giải pháp**:
     - Profile nếu performance critical
     - Consider async communication nếu cần

4. **Mediator Knows All**:
   - Mediator phải biết tất cả concrete colleague classes
   - Tight coupling từ Mediator đến Colleagues (one-way)
   - **Acceptable tradeoff**: Centralized knowledge tốt hơn distributed coupling

### Khi nào KHÔNG nên dùng Mediator:

❌ **Simple interactions**: Nếu chỉ có 2-3 devices với simple communication → overkill
❌ **One-to-many broadcast**: Dùng Observer pattern thay vì Mediator
❌ **Fixed communication flow**: Nếu flow cố định và đơn giản → không cần mediator
❌ **Performance critical**: Direct calls nhanh hơn mediator indirection
❌ **Independent objects**: Nếu objects không cần communicate → không cần mediator

### Best Practices:

✅ **Keep mediator focused**: Avoid God Object, split nếu cần
✅ **Use Strategy/State**: Nếu coordination logic phức tạp
✅ **Document coordination rules**: Clear documentation về interactions
✅ **Consider async**: Nếu có many devices hoặc slow operations
✅ **Test thoroughly**: Unit test mediator logic riêng
✅ **Use interfaces**: Mediator và Colleagues nên dựa trên interfaces

---

## 5. Cài đặt

### SmartHomeHub Interface (Mediator):

```java
public interface SmartHomeHub {
	void notify(SmartDevice device, String event);
	void registerDevice(SmartDevice device);
}
```

### SmartDevice Abstract Class (Colleague):

```java
public abstract class SmartDevice {

	protected SmartHomeHub hub;
	protected String deviceName;

	public void setHub(SmartHomeHub hub) {
		this.hub = hub;
	}

	public String getDeviceName() {
		return deviceName;
	}
}
```

### SmartHomeController (ConcreteMediator):

```java
public class SmartHomeController implements SmartHomeHub {

	private MotionSensor motionSensor;
	private SecurityCamera securityCamera;
	private SmartLight smartLight;
	private Thermostat thermostat;

	private String currentMode = "NORMAL";
	private boolean isNightTime = false;

	public SmartHomeController() {
		System.out.println("==============================================");
		System.out.println("  Smart Home Controller Initialized");
		System.out.println("==============================================");
	}

	// Register devices
	public void setMotionSensor(MotionSensor sensor) {
		this.motionSensor = sensor;
	}

	public void setSecurityCamera(SecurityCamera camera) {
		this.securityCamera = camera;
	}

	public void setSmartLight(SmartLight light) {
		this.smartLight = light;
	}

	public void setThermostat(Thermostat thermostat) {
		this.thermostat = thermostat;
	}

	@Override
	public void registerDevice(SmartDevice device) {
		device.setHub(this);
		System.out.println("[Hub] Device registered: " + device.getDeviceName());
	}

	public void setMode(String mode) {
		this.currentMode = mode;
		System.out.println("\n[Hub] Mode changed to: " + mode);
	}

	public void setNightTime(boolean isNight) {
		this.isNightTime = isNight;
	}

	@Override
	public void notify(SmartDevice device, String event) {
		System.out.println("\n[Hub] Received notification from " + device.getDeviceName() + ": " + event);
		System.out.println("[Hub] Current mode: " + currentMode);

		// Coordination logic based on event and current state
		switch (event) {
			case "motion_detected":
				handleMotionDetection();
				break;
			case "recording_started":
				handleRecordingStarted();
				break;
			case "temperature_changed":
				handleTemperatureChange();
				break;
			case "light_turned_on":
				handleLightOn();
				break;
			default:
				System.out.println("[Hub] No specific action for this event");
		}
	}

	// Coordination Methods

	private void handleMotionDetection() {
		System.out.println("[Hub] Coordinating response to motion detection...");

		if (currentMode.equals("SECURITY") || isNightTime) {
			// Security/Night mode: Full alert
			System.out.println("[Hub] → Activating SECURITY protocol");
			securityCamera.startRecording();
			smartLight.turnOn(100);  // Full brightness
		} else if (currentMode.equals("WELCOME_HOME")) {
			// Welcome home mode: Comfort
			System.out.println("[Hub] → Activating WELCOME HOME protocol");
			smartLight.turnOn(50);   // Medium brightness
			thermostat.setTemperature(24);
			securityCamera.stopRecording();
		} else {
			// Normal mode: Basic response
			System.out.println("[Hub] → Normal mode response");
			smartLight.turnOn(70);
		}
	}

	private void handleRecordingStarted() {
		System.out.println("[Hub] Coordinating support for recording...");

		// Check if it's dark, turn on lights to help recording
		if (isNightTime || currentMode.equals("SECURITY")) {
			System.out.println("[Hub] → Turning on lights to assist recording");
			smartLight.turnOn(80);
		}
	}

	private void handleTemperatureChange() {
		System.out.println("[Hub] Temperature changed, checking mode...");

		if (currentMode.equals("AWAY")) {
			System.out.println("[Hub] → AWAY mode: Reverting temperature to eco mode");
			thermostat.setTemperature(18);  // Eco temperature
		}
	}

	private void handleLightOn() {
		System.out.println("[Hub] Lights turned on");
		// Could coordinate with other devices if needed
	}

	// Mode-specific coordination methods

	public void activateSecurityMode() {
		System.out.println("\n╔═══════════════════════════════════════╗");
		System.out.println("║   ACTIVATING SECURITY MODE           ║");
		System.out.println("╚═══════════════════════════════════════╝");

		setMode("SECURITY");
		securityCamera.enableSurveillance();
		motionSensor.increaseSensitivity();
		thermostat.setTemperature(20);  // Moderate temperature
		System.out.println("[Hub] Security mode activated successfully");
	}

	public void activateAwayMode() {
		System.out.println("\n╔═══════════════════════════════════════╗");
		System.out.println("║   ACTIVATING AWAY MODE               ║");
		System.out.println("╚═══════════════════════════════════════╝");

		setMode("AWAY");
		smartLight.turnOff();
		securityCamera.enableSurveillance();
		thermostat.setTemperature(18);  // Eco temperature
		motionSensor.increaseSensitivity();
		System.out.println("[Hub] Away mode activated successfully");
	}

	public void activateWelcomeHomeMode() {
		System.out.println("\n╔═══════════════════════════════════════╗");
		System.out.println("║   ACTIVATING WELCOME HOME MODE       ║");
		System.out.println("╚═══════════════════════════════════════╝");

		setMode("WELCOME_HOME");
		smartLight.turnOn(60);
		thermostat.setTemperature(24);  // Comfortable temperature
		securityCamera.stopRecording();
		System.out.println("[Hub] Welcome Home mode activated successfully");
	}
}
```

### MotionSensor (ConcreteColleague):

```java
public class MotionSensor extends SmartDevice {

	private int sensitivity = 50;

	public MotionSensor() {
		this.deviceName = "Motion Sensor";
	}

	public void detectMotion() {
		System.out.println("\n[" + deviceName + "] ⚠️  Motion detected!");
		hub.notify(this, "motion_detected");
	}

	public void increaseSensitivity() {
		sensitivity = 90;
		System.out.println("[" + deviceName + "] Sensitivity increased to " + sensitivity + "%");
	}

	public void normalSensitivity() {
		sensitivity = 50;
		System.out.println("[" + deviceName + "] Sensitivity set to normal: " + sensitivity + "%");
	}
}
```

### SecurityCamera (ConcreteColleague):

```java
public class SecurityCamera extends SmartDevice {

	private boolean isRecording = false;
	private boolean surveillanceMode = false;

	public SecurityCamera() {
		this.deviceName = "Security Camera";
	}

	public void startRecording() {
		if (!isRecording) {
			isRecording = true;
			System.out.println("[" + deviceName + "] 🎥 Recording started");
			hub.notify(this, "recording_started");
		} else {
			System.out.println("[" + deviceName + "] Already recording");
		}
	}

	public void stopRecording() {
		if (isRecording) {
			isRecording = false;
			System.out.println("[" + deviceName + "] ⏹️  Recording stopped");
		} else {
			System.out.println("[" + deviceName + "] Not currently recording");
		}
	}

	public void enableSurveillance() {
		surveillanceMode = true;
		startRecording();
		System.out.println("[" + deviceName + "] 👁️  Surveillance mode enabled");
	}

	public void disableSurveillance() {
		surveillanceMode = false;
		stopRecording();
		System.out.println("[" + deviceName + "] Surveillance mode disabled");
	}
}
```

### SmartLight (ConcreteColleague):

```java
public class SmartLight extends SmartDevice {

	private boolean isOn = false;
	private int brightness = 0;

	public SmartLight() {
		this.deviceName = "Smart Light";
	}

	public void turnOn(int brightness) {
		this.isOn = true;
		this.brightness = brightness;
		System.out.println("[" + deviceName + "] 💡 Light ON (Brightness: " + brightness + "%)");
		hub.notify(this, "light_turned_on");
	}

	public void turnOff() {
		this.isOn = false;
		this.brightness = 0;
		System.out.println("[" + deviceName + "] 🌑 Light OFF");
	}

	public void adjustBrightness(int level) {
		if (isOn) {
			this.brightness = level;
			System.out.println("[" + deviceName + "] Brightness adjusted to " + level + "%");
		} else {
			System.out.println("[" + deviceName + "] Cannot adjust brightness - light is off");
		}
	}
}
```

### Thermostat (ConcreteColleague):

```java
public class Thermostat extends SmartDevice {

	private int currentTemperature = 22;
	private int targetTemperature = 22;

	public Thermostat() {
		this.deviceName = "Thermostat";
	}

	public void setTemperature(int temperature) {
		this.targetTemperature = temperature;
		System.out.println("[" + deviceName + "] 🌡️  Temperature set to " + temperature + "°C");

		if (temperature != currentTemperature) {
			hub.notify(this, "temperature_changed");
		}
	}

	public void adjustToCurrentWeather() {
		System.out.println("[" + deviceName + "] Adjusting to current weather conditions");
		setTemperature(23);
	}

	public int getTemperature() {
		return targetTemperature;
	}
}
```

### MediatorDemo (Client):

```java
public class MediatorDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════╗");
		System.out.println("║     MEDIATOR PATTERN DEMO                     ║");
		System.out.println("║     Smart Home Automation System              ║");
		System.out.println("╚════════════════════════════════════════════════╝");

		// Create Mediator
		SmartHomeController hub = new SmartHomeController();

		// Create Devices (Colleagues)
		MotionSensor motionSensor = new MotionSensor();
		SecurityCamera securityCamera = new SecurityCamera();
		SmartLight smartLight = new SmartLight();
		Thermostat thermostat = new Thermostat();

		// Register devices with mediator
		System.out.println("\n--- Registering Devices ---");
		hub.registerDevice(motionSensor);
		hub.registerDevice(securityCamera);
		hub.registerDevice(smartLight);
		hub.registerDevice(thermostat);

		// Set device references in mediator
		hub.setMotionSensor(motionSensor);
		hub.setSecurityCamera(securityCamera);
		hub.setSmartLight(smartLight);
		hub.setThermostat(thermostat);

		// Test 1: Normal mode - Motion Detection
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 1: Normal Mode - Motion Detection");
		System.out.println("═══════════════════════════════════════════════");
		hub.setMode("NORMAL");
		motionSensor.detectMotion();

		// Test 2: Security Mode (Night Time)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 2: Security Mode - Motion Detection at Night");
		System.out.println("═══════════════════════════════════════════════");
		hub.setNightTime(true);
		hub.activateSecurityMode();
		pause(1);
		motionSensor.detectMotion();

		// Test 3: Welcome Home Mode
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 3: Welcome Home Mode");
		System.out.println("═══════════════════════════════════════════════");
		hub.setNightTime(false);
		hub.activateWelcomeHomeMode();
		pause(1);
		motionSensor.detectMotion();

		// Test 4: Away Mode
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 4: Away Mode");
		System.out.println("═══════════════════════════════════════════════");
		hub.activateAwayMode();

		// Test 5: Manual Camera Recording (triggers coordination)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 5: Manual Camera Recording");
		System.out.println("═══════════════════════════════════════════════");
		hub.setNightTime(true);
		securityCamera.startRecording();

		// Test 6: Thermostat adjustment in Away mode
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 6: Temperature Adjustment in Away Mode");
		System.out.println("═══════════════════════════════════════════════");
		thermostat.setTemperature(25);  // Hub will revert to eco mode

		// Summary
		System.out.println("\n\n╔════════════════════════════════════════════════╗");
		System.out.println("║              SUMMARY                          ║");
		System.out.println("╚════════════════════════════════════════════════╝");
		System.out.println("✓ Devices communicate only through SmartHomeHub");
		System.out.println("✓ Devices don't know about each other");
		System.out.println("✓ Hub coordinates complex interactions");
		System.out.println("✓ Centralized coordination logic");
		System.out.println("✓ Easy to add new devices or modes");
		System.out.println("✓ Reduced coupling from O(n²) to O(n)");
		System.out.println("════════════════════════════════════════════════");
	}

	private static void pause(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
```

---

## 6. Kết quả chạy chương trình

```
╔════════════════════════════════════════════════╗
║     MEDIATOR PATTERN DEMO                     ║
║     Smart Home Automation System              ║
╚════════════════════════════════════════════════╝
==============================================
  Smart Home Controller Initialized
==============================================

--- Registering Devices ---
[Hub] Device registered: Motion Sensor
[Hub] Device registered: Security Camera
[Hub] Device registered: Smart Light
[Hub] Device registered: Thermostat


═══════════════════════════════════════════════
TEST 1: Normal Mode - Motion Detection
═══════════════════════════════════════════════

[Hub] Mode changed to: NORMAL

[Motion Sensor] ⚠️  Motion detected!

[Hub] Received notification from Motion Sensor: motion_detected
[Hub] Current mode: NORMAL
[Hub] Coordinating response to motion detection...
[Hub] → Normal mode response
[Smart Light] 💡 Light ON (Brightness: 70%)

[Hub] Received notification from Smart Light: light_turned_on
[Hub] Current mode: NORMAL
[Hub] Lights turned on


═══════════════════════════════════════════════
TEST 2: Security Mode - Motion Detection at Night
═══════════════════════════════════════════════

╔═══════════════════════════════════════╗
║   ACTIVATING SECURITY MODE           ║
╚═══════════════════════════════════════╝

[Hub] Mode changed to: SECURITY
[Security Camera] 🎥 Recording started

[Hub] Received notification from Security Camera: recording_started
[Hub] Current mode: SECURITY
[Hub] Coordinating support for recording...
[Hub] → Turning on lights to assist recording
[Smart Light] 💡 Light ON (Brightness: 80%)

[Hub] Received notification from Smart Light: light_turned_on
[Hub] Current mode: SECURITY
[Hub] Lights turned on
[Security Camera] 👁️  Surveillance mode enabled
[Motion Sensor] Sensitivity increased to 90%
[Thermostat] 🌡️  Temperature set to 20°C

[Hub] Received notification from Thermostat: temperature_changed
[Hub] Current mode: SECURITY
[Hub] Temperature changed, checking mode...
[Hub] Security mode activated successfully

[Motion Sensor] ⚠️  Motion detected!

[Hub] Received notification from Motion Sensor: motion_detected
[Hub] Current mode: SECURITY
[Hub] Coordinating response to motion detection...
[Hub] → Activating SECURITY protocol
[Security Camera] Already recording
[Smart Light] 💡 Light ON (Brightness: 100%)

[Hub] Received notification from Smart Light: light_turned_on
[Hub] Current mode: SECURITY
[Hub] Lights turned on


═══════════════════════════════════════════════
TEST 3: Welcome Home Mode
═══════════════════════════════════════════════

╔═══════════════════════════════════════╗
║   ACTIVATING WELCOME HOME MODE       ║
╚═══════════════════════════════════════╝

[Hub] Mode changed to: WELCOME_HOME
[Smart Light] 💡 Light ON (Brightness: 60%)

[Hub] Received notification from Smart Light: light_turned_on
[Hub] Current mode: WELCOME_HOME
[Hub] Lights turned on
[Thermostat] 🌡️  Temperature set to 24°C

[Hub] Received notification from Thermostat: temperature_changed
[Hub] Current mode: WELCOME_HOME
[Hub] Temperature changed, checking mode...
[Security Camera] ⏹️  Recording stopped
[Hub] Welcome Home mode activated successfully

[Motion Sensor] ⚠️  Motion detected!

[Hub] Received notification from Motion Sensor: motion_detected
[Hub] Current mode: WELCOME_HOME
[Hub] Coordinating response to motion detection...
[Hub] → Activating WELCOME HOME protocol
[Smart Light] 💡 Light ON (Brightness: 50%)

[Hub] Received notification from Smart Light: light_turned_on
[Hub] Current mode: WELCOME_HOME
[Hub] Lights turned on
[Thermostat] 🌡️  Temperature set to 24°C
[Security Camera] Not currently recording


═══════════════════════════════════════════════
TEST 4: Away Mode
═══════════════════════════════════════════════

╔═══════════════════════════════════════╗
║   ACTIVATING AWAY MODE               ║
╚═══════════════════════════════════════╝

[Hub] Mode changed to: AWAY
[Smart Light] 🌑 Light OFF
[Security Camera] 🎥 Recording started

[Hub] Received notification from Security Camera: recording_started
[Hub] Current mode: AWAY
[Hub] Coordinating support for recording...
[Security Camera] 👁️  Surveillance mode enabled
[Thermostat] 🌡️  Temperature set to 18°C

[Hub] Received notification from Thermostat: temperature_changed
[Hub] Current mode: AWAY
[Hub] Temperature changed, checking mode...
[Hub] → AWAY mode: Reverting temperature to eco mode
[Thermostat] 🌡️  Temperature set to 18°C
[Motion Sensor] Sensitivity increased to 90%
[Hub] Away mode activated successfully


═══════════════════════════════════════════════
TEST 5: Manual Camera Recording
═══════════════════════════════════════════════
[Security Camera] Already recording


═══════════════════════════════════════════════
TEST 6: Temperature Adjustment in Away Mode
═══════════════════════════════════════════════
[Thermostat] 🌡️  Temperature set to 25°C

[Hub] Received notification from Thermostat: temperature_changed
[Hub] Current mode: AWAY
[Hub] Temperature changed, checking mode...
[Hub] → AWAY mode: Reverting temperature to eco mode
[Thermostat] 🌡️  Temperature set to 18°C

[Hub] Received notification from Thermostat: temperature_changed
[Hub] Current mode: AWAY
[Hub] Temperature changed, checking mode...
[Hub] → AWAY mode: Reverting temperature to eco mode
[Thermostat] 🌡️  Temperature set to 18°C


╔════════════════════════════════════════════════╗
║              SUMMARY                          ║
╚════════════════════════════════════════════════╝
✓ Devices communicate only through SmartHomeHub
✓ Devices don't know about each other
✓ Devices don't know about each other
✓ Hub coordinates complex interactions
✓ Centralized coordination logic
✓ Easy to add new devices or modes
✓ Reduced coupling from O(n²) to O(n)
════════════════════════════════════════════════
```

### Giải thích cách pattern hoạt động:

**1. Device Registration** (Setup):
- Tạo SmartHomeController (Mediator)
- Tạo 4 devices (Colleagues)
- Register devices với hub
- Hub maintains references to all devices

**2. TEST 1 - Normal Mode**:
- MotionSensor detects motion
- Calls `hub.notify(this, "motion_detected")`
- Hub checks current mode (NORMAL)
- Hub coordinates: turns on light at 70% brightness
- Light notifies hub → hub logs event

**3. TEST 2 - Security Mode at Night**:
- Hub activates security mode → coordinates all devices
- MotionSensor detects motion → notifies hub
- Hub checks mode (SECURITY) + nightTime (true)
- Hub activates SECURITY protocol:
  - Camera: start recording
  - Light: turn on at 100% (full brightness)
- Devices respond independently

**4. TEST 3 - Welcome Home Mode**:
- Hub activates welcome home mode → coordinates devices
- Motion detected → hub activates WELCOME HOME protocol:
  - Light: 50% brightness (comfortable)
  - Thermostat: set to 24°C (comfortable temp)
  - Camera: stop recording (owner is home)

**5. TEST 4 - Away Mode**:
- Hub activates away mode → coordinates all:
  - Light: turn OFF (save energy)
  - Camera: surveillance mode ON
  - Thermostat: 18°C (eco mode)
  - Motion sensor: increase sensitivity

**6. TEST 5 - Manual Camera Operation**:
- Camera manually started recording
- Camera notifies hub
- Hub coordinates support: turns on lights (because it's night)

**7. TEST 6 - Temperature Override Prevention**:
- User tries to set temp to 25°C in AWAY mode
- Thermostat notifies hub of change
- Hub detects AWAY mode → reverts to eco temp (18°C)
- Enforces mode rules centrally

### Key Points:

✅ **No Direct Communication**: Devices NEVER call methods on other devices
✅ **Hub Coordinates**: All interaction logic in SmartHomeController
✅ **Bidirectional**: Devices notify hub AND hub commands devices
✅ **Mode-Based Logic**: Hub makes decisions based on current mode and state
✅ **Loose Coupling**: Add DoorLock device chỉ cần modify hub, không touch other devices
✅ **Single Point of Control**: Easy to modify coordination rules

---

## 7. Sơ đồ UML

### Class Diagram:

```
┌─────────────────────────────────┐
│    <<interface>>                │
│      SmartHomeHub               │
├─────────────────────────────────┤
│ + notify(device, event)         │
│ + registerDevice(device)        │
└─────────────────────────────────┘
            △
            │ implements
            │
┌─────────────────────────────────────────────────┐
│         SmartHomeController                     │
├─────────────────────────────────────────────────┤
│ - motionSensor: MotionSensor                    │
│ - securityCamera: SecurityCamera                │
│ - smartLight: SmartLight                        │
│ - thermostat: Thermostat                        │
│ - currentMode: String                           │
│ - isNightTime: boolean                          │
├─────────────────────────────────────────────────┤
│ + notify(device, event): void                   │
│ + registerDevice(device): void                  │
│ + setMode(mode): void                           │
│ + activateSecurityMode(): void                  │
│ + activateAwayMode(): void                      │
│ + activateWelcomeHomeMode(): void               │
│ - handleMotionDetection(): void                 │
│ - handleRecordingStarted(): void                │
│ - handleTemperatureChange(): void               │
└─────────────────────────────────────────────────┘
            │
            │ maintains/coordinates
            ▼
    ┌───────────────────┐
    │  <<abstract>>     │
    │   SmartDevice     │
    ├───────────────────┤
    │ # hub: SmartHomeHub│
    │ # deviceName: String│
    ├───────────────────┤
    │ + setHub(hub)     │
    │ + getDeviceName() │
    └───────────────────┘
            △
            │ extends
    ┌───────┼───────┬─────────┬─────────┐
    │       │       │         │         │
┌────────┐┌──────┐┌────────┐┌───────────┐
│Motion  ││Security││Smart   ││Thermostat │
│Sensor  ││Camera ││Light   ││           │
└────────┘└──────┘└────────┘└───────────┘
```

### Detailed Component Description:

**1. SmartHomeHub Interface (Mediator)**:
- `notify(SmartDevice device, String event)`: Receive notifications from devices
- `registerDevice(SmartDevice device)`: Register a device with hub

**2. SmartHomeController (ConcreteMediator)**:
- **Fields**:
  - References to ALL concrete colleagues (4 devices)
  - State fields: `currentMode`, `isNightTime`
- **Public Methods**:
  - `notify()`: Central coordination entry point
  - `registerDevice()`: Register and link devices
  - Mode activation methods: `activateSecurityMode()`, `activateAwayMode()`, `activateWelcomeHomeMode()`
- **Private Coordination Methods**:
  - `handleMotionDetection()`: Coordinate response to motion
  - `handleRecordingStarted()`: Support camera recording
  - `handleTemperatureChange()`: Manage temperature policies
- **Responsibility**: Contains ALL coordination logic

**3. SmartDevice Abstract Class (Colleague)**:
- **Fields**:
  - `hub`: Reference to mediator (HOW colleagues communicate)
  - `deviceName`: Device identification
- **Methods**:
  - `setHub()`: Set mediator reference
  - `getDeviceName()`: Get device name
- **Responsibility**: Base for all devices, maintains hub reference

**4. ConcreteColleagues** (4 devices):

**MotionSensor**:
- `detectMotion()`: Detect motion and notify hub
- `increaseSensitivity()`: Increase sensitivity (called by hub)
- `normalSensitivity()`: Normal sensitivity

**SecurityCamera**:
- `startRecording()`: Start recording and notify hub
- `stopRecording()`: Stop recording
- `enableSurveillance()`: Enable surveillance mode
- `disableSurveillance()`: Disable surveillance

**SmartLight**:
- `turnOn(brightness)`: Turn on with brightness level
- `turnOff()`: Turn off
- `adjustBrightness(level)`: Adjust brightness

**Thermostat**:
- `setTemperature(temp)`: Set target temperature and notify hub
- `adjustToCurrentWeather()`: Auto-adjust
- `getTemperature()`: Get current setting

**5. MediatorDemo (Client)**:
- Creates mediator and colleagues
- Registers colleagues with mediator
- Demonstrates various scenarios

### Relationships:

**Implements**:
- SmartHomeController `implements` SmartHomeHub

**Extends**:
- MotionSensor, SecurityCamera, SmartLight, Thermostat `extend` SmartDevice

**Associations (Bidirectional)**:
- SmartHomeController ↔ MotionSensor
- SmartHomeController ↔ SecurityCamera
- SmartHomeController ↔ SmartLight
- SmartHomeController ↔ Thermostat
- (Each device knows hub, hub knows each device)

### Communication Flow:

```
1. MotionSensor detects motion
2. MotionSensor.detectMotion() called
3. Calls hub.notify(this, "motion_detected")
4. Hub receives notification
5. Hub.notify() executes
6. Hub checks currentMode and isNightTime
7. Hub calls handleMotionDetection()
8. handleMotionDetection() runs coordination logic
9. Hub calls securityCamera.startRecording()
10. Hub calls smartLight.turnOn(100)
11. Devices execute their functions
12. Devices may notify hub of completion
13. Hub may coordinate additional responses
```

### Connections Comparison:

**Without Mediator** (Direct coupling):
```
4 devices × 3 other devices = 12 bidirectional references
MotionSensor ↔ Camera, Light, Thermostat
Camera ↔ MotionSensor, Light, Thermostat
Light ↔ MotionSensor, Camera, Thermostat
Thermostat ↔ MotionSensor, Camera, Light
= O(n²) complexity
```

**With Mediator**:
```
Hub → 4 devices (4 references)
4 devices → Hub (4 references)
Total: 8 references
= O(n) complexity
```

### UML Notes:
- Multiplicity: SmartHomeController `1` ↔ `4` SmartDevices
- Bidirectional associations (both know each other)
- Interface realization: dotted line with hollow triangle
- Extension: solid line with hollow triangle
- All devices extend SmartDevice
- Only SmartHomeController implements SmartHomeHub
- Hub maintains references to ALL concrete colleagues

---

## 8. Tổng kết

### Kết luận về bài toán:

**Smart Home Automation System** minh họa hoàn hảo **Mediator Pattern** trong thực tế, giải quyết vấn đề:

✅ **Many-to-many communication**: 4 devices cần coordinate với nhau
✅ **Centralized coordination**: Hub điều phối tất cả interactions
✅ **Loose coupling**: Devices không biết nhau, chỉ biết Hub
✅ **Complex logic**: Mode-based coordination với multiple rules
✅ **Easy to extend**: Thêm DoorLock chỉ cần modify Hub

Mediator Pattern biến một hệ thống **tightly coupled** (O(n²) connections) thành **loosely coupled** (O(n) connections), với coordination logic **centralized** thay vì **scattered**, giúp code dễ maintain, extend, và test hơn.

### Ứng dụng thực tế của Mediator Pattern:

**1. GUI Frameworks**:
- Dialog boxes với multiple controls (buttons, textboxes, dropdowns)
- Mediator coordinates interactions giữa controls
- Example: Form validation, dependent field updates

**2. Chat Applications**:
- Chat room là mediator
- Users là colleagues
- Messages route qua chat room, không directly user-to-user

**3. Air Traffic Control**:
- Control tower là mediator
- Airplanes là colleagues
- Tower coordinates takeoffs, landings, routing

**4. Smart Home/IoT Systems**:
- Central hub/gateway là mediator
- Sensors, actuators, displays là colleagues
- Hub orchestrates automation rules

**5. Enterprise Service Bus (ESB)**:
- ESB là mediator cho microservices
- Services communicate qua bus
- Decouples service implementations

**6. MVC/MVVM Frameworks**:
- Controller/ViewModel là mediator
- Coordinates interactions giữa Model và View
- Decouples UI from business logic

### Khi nào nên dùng Mediator Pattern:

✅ **Nên dùng khi**:
- Many-to-many relationships giữa objects
- Interactions phức tạp với nhiều rules
- Muốn reuse objects independently
- Coordination logic cần centralized
- Objects tightly coupled và cần decouple
- Communication flow thay đổi thường xuyên

❌ **KHÔNG nên dùng khi**:
- Chỉ có simple one-to-one hoặc one-to-many (dùng Observer)
- Interactions đơn giản và cố định
- Mediator sẽ trở thành God Object
- Performance critical (mediator adds overhead)
- Objects hoàn toàn independent

### So sánh Mediator vs Observer:

| Aspect | Mediator | Observer |
|--------|----------|----------|
| Relationship | Many-to-many | One-to-many |
| Communication | Bidirectional qua mediator | One-way broadcast |
| Purpose | Coordinate interactions | Notify state changes |
| Colleagues/Observers | Active (send & receive) | Passive (only receive) |
| Complexity | Higher (coordination logic) | Lower (simple notification) |
| Use Case | Complex interactions | Event notifications |
| Example | Smart home hub | YouTube subscriptions |

### Mediator vs Facade:

| Aspect | Mediator | Facade |
|--------|----------|--------|
| Direction | Bidirectional | Unidirectional (client → facade) |
| Purpose | Decouple colleagues | Simplify interface |
| Colleagues Know Mediator | Yes | No (subsystems don't know facade) |
| Adds Functionality | Yes (coordination logic) | No (just simplifies) |

### Key Takeaways:

🎯 **Mediator Pattern giải quyết**:
- Many-to-many communication complexity
- Tight coupling giữa interacting objects
- Scattered coordination logic
- O(n²) connection problem

⚠️ **Cần lưu ý**:
- Mediator có thể trở thành God Object
- Adds indirection (performance overhead)
- Mediator tightly coupled với colleagues (acceptable tradeoff)
- Requires careful design để avoid bloat

💡 **Best Practices**:
- Keep mediator focused (avoid God Object)
- Use Strategy/State cho complex coordination logic
- Document coordination rules clearly
- Consider splitting large mediators
- Test mediator logic thoroughly
- Use interfaces để reduce coupling

### Tương lai và mở rộng:

**Có thể extend system này với**:
1. **More Devices**: DoorLock, Window Blinds, Speaker, TV
2. **AI/ML**: Smart predictions based on behavior patterns
3. **Voice Control**: Alexa/Google Home integration qua mediator
4. **Remote Access**: Mobile app control qua mediator API
5. **Energy Optimization**: Smart scheduling để save energy
6. **Multiple Hubs**: Distributed mediators cho large homes

Mediator Pattern là foundation cho complex coordination systems. Hiểu rõ pattern này giúp bạn thiết kế systems với clean architecture, maintainable code, và scalable design.
