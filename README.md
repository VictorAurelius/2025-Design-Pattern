# 📚 Java Design Patterns - Comprehensive Learning Project

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Design Patterns](https://img.shields.io/badge/Design_Patterns-23-blue?style=for-the-badge)](https://en.wikipedia.org/wiki/Design_Patterns)
[![BlueJ](https://img.shields.io/badge/BlueJ-IDE-green?style=for-the-badge)](https://www.bluej.org/)

## 🎯 Project Overview

This project is a comprehensive collection of **23 Design Patterns** implemented in Java, demonstrating best practices in object-oriented software design. Each pattern is implemented as a standalone example with real-world business scenarios, complete with UML diagrams, detailed documentation, and executable demonstrations.

### 📊 Project Statistics

- **Total Patterns**: 23 Design Patterns
- **Categories**: Creational (5), Structural (7), Behavioral (11)
- **Implementation**: Pure Java with BlueJ IDE support
- **Documentation**: Full requirement specs + UML diagrams for each pattern

---

## 📖 Design Patterns Catalog

### 🏗️ Structural Patterns (7 Patterns)

#### **1. Adapter Pattern** 🔌
📁 `1-Adapter-DP/`

**Problem**: Legacy temperature sensors use Fahrenheit, but the new Smart Monitoring System requires Celsius readings. Direct integration is incompatible.

**Context**: A warehouse monitoring system needs to integrate legacy Fahrenheit sensors with a modern Celsius-based monitoring platform without modifying existing sensor hardware or software.

**Solution**: Create an adapter that converts `LegacyTemperatureSensor` (Fahrenheit) to `TemperatureReading` interface (Celsius).

**Key Classes**:
- `TemperatureReading` (Target interface)
- `LegacyTemperatureSensor` (Adaptee)
- `TemperatureSensorAdapter` (Adapter)
- `FahrenheitSensor` (Concrete Adaptee)

**Real-World Impact**: Enables integration of existing hardware worth $50,000+ without replacement costs.

---

#### **2. Facade Pattern** 🎭
📁 `2-Facade-DP/`

**Problem**: E-commerce order processing requires coordinating multiple subsystems (inventory, payment, shipping, notification), making client code complex and tightly coupled.

**Context**: Placing an order involves 4 subsystems with 12+ method calls. Developers waste time understanding subsystem interactions, leading to errors and code duplication.

**Solution**: `OrderFacade` provides simplified methods (`placeOrder()`, `cancelOrder()`) that orchestrate all subsystem interactions.

**Key Classes**:
- `OrderFacade` (Facade)
- `InventorySystem`, `PaymentProcessor`, `ShippingService`, `NotificationService` (Subsystems)

**Real-World Impact**: Reduces order placement code from 50 lines to 5 lines. 90% reduction in integration bugs.

---

#### **3. Composite Pattern** 🌳
📁 `3-Composite-DP/`

**Problem**: Project management systems need to handle both individual tasks and projects (containing multiple tasks) uniformly, but treating them differently causes code duplication.

**Context**: Developers manually check if an item is a task or project before performing operations (calculate hours, display hierarchy), violating DRY principle.

**Solution**: Define a common `Task` interface for both `TaskItem` (leaf) and `Project` (composite), enabling uniform tree traversal.

**Key Classes**:
- `Task` (Component interface)
- `TaskItem` (Leaf)
- `Project` (Composite)

**Real-World Impact**: Enables recursive operations on hierarchies. Used in file systems, organizational charts, UI component trees.

---

#### **4. Bridge Pattern** 🌉
📁 `4-Bridge-DP/`

**Problem**: Extending device types AND remote control types creates class explosion (Smart TV + Basic Remote, Smart TV + Advanced Remote, Old TV + Basic Remote = 6 classes for 2×3 combinations).

**Context**: Home entertainment systems with M device types and N remote types would require M×N subclasses without Bridge pattern.

**Solution**: Separate device abstraction from remote implementation using composition instead of inheritance.

**Key Classes**:
- `Device` (Implementor interface)
- `RemoteControl` (Abstraction)
- `TV`, `Radio` (Concrete Implementors)
- `AdvancedRemote` (Refined Abstraction)

**Real-World Impact**: Reduces classes from M×N to M+N. Enables independent evolution of abstractions and implementations.

---

#### **5. Proxy Pattern** 🛡️
📁 `8-Proxy-DP/`

**Problem**: Video streaming requires expensive resource loading, access control for premium content, and caching for frequently watched videos.

**Context**: Loading all video data upfront wastes bandwidth. Need lazy loading, access control, and performance optimization without modifying video loading logic.

**Solution**: `ProxyVideo` controls access to `RealVideo`, implementing lazy loading, access control, and caching transparently.

**Key Classes**:
- `Video` (Subject interface)
- `RealVideo` (Real Subject)
- `ProxyVideo` (Proxy with lazy loading + access control + caching)

**Real-World Impact**: Reduces bandwidth by 70%, enforces subscription tiers, improves response time by 5x for cached content.

---

#### **6. Flyweight Pattern** 🪶
📁 `10-Flyweight-DP/`

**Problem**: Video player UI has 9,000+ icon instances consuming 90MB RAM (10KB per icon), causing performance issues on low-end devices.

**Context**: Same icon images (play, pause, stop) duplicated thousands of times across UI, with only position/size varying.

**Solution**: Share intrinsic state (icon image) via `IconFactory`, store extrinsic state (position, size) in `IconContext`.

**Key Classes**:
- `Icon` (Flyweight interface)
- `ConcreteIcon` (Flyweight with intrinsic state)
- `IconFactory` (manages flyweight pool)
- `IconContext` (stores extrinsic state)

**Real-World Impact**: Reduces memory from 90MB to 30KB (99.97% reduction). Enables smooth UI on resource-constrained devices.

---

#### **7. Decorator Pattern** 🎨
📁 `21-Decorator-DP/`

**Problem**: Adding features to video streams (subtitles, ads, watermark, analytics) via inheritance creates class explosion (2^N classes for N features).

**Context**: StreamFlix needs to add/remove stream enhancements dynamically at runtime. Inheritance approach requires 64 classes for 6 features.

**Solution**: Wrap `VideoStream` with decorators that add functionality transparently while maintaining the same interface.

**Key Classes**:
- `VideoStream` (Component interface)
- `BasicVideoStream` (Concrete Component)
- `StreamDecorator` (Decorator base)
- `SubtitleDecorator`, `AdvertisementDecorator`, `WatermarkDecorator`, etc.

**Real-World Impact**: Reduces classes from 64 to 8. Enables runtime composition of features based on user subscription tier.

---

### 🏭 Creational Patterns (5 Patterns)

#### **8. Singleton Pattern** 👤
📁 `5-Singleton-DP/`

**Problem**: Database connection pool, logging service, or configuration manager should have exactly one instance to coordinate resource access.

**Context**: Multiple instances of configuration manager cause inconsistent settings. Multiple database pools waste connections.

**Solution**: Private constructor + static instance + lazy/eager initialization ensures single instance.

**Key Classes**:
- `Singleton` (ensures single instance)
- Variations: Lazy, Eager, Thread-Safe, Enum-based

**Real-World Impact**: Prevents resource waste, ensures consistency, controls access to shared resources.

---

#### **9. Builder Pattern** 🔨
📁 `11-Builder-DP/`

**Problem**: Video upload configuration has 12+ parameters (quality, privacy, subtitles, thumbnails, etc.), making constructors unwieldy and error-prone.

**Context**: Constructors with many parameters are hard to read, prone to argument order mistakes, and don't handle optional parameters well.

**Solution**: `VideoUploadBuilder` provides fluent API for step-by-step object construction with optional parameters.

**Key Classes**:
- `VideoUploadConfig` (Product)
- `VideoUploadBuilder` (Builder)
- `Director` (optional, for common configurations)

**Real-World Impact**: Improves code readability, prevents parameter order errors, handles optional parameters elegantly.

---

#### **10. Factory Method Pattern** 🏭
📁 `12-Factory-Method-DP/`

**Problem**: Video export system needs to create different format exporters (MP4, AVI, MKV, WebM) without hardcoding creation logic.

**Context**: Direct instantiation (`new MP4Exporter()`) makes code rigid. Adding new formats requires modifying client code.

**Solution**: Define `VideoExporter` abstract class with factory method `createExporter()`, let subclasses decide concrete type.

**Key Classes**:
- `VideoExporter` (Creator with factory method)
- `MP4Exporter`, `AVIExporter`, `MKVExporter`, `WebMExporter` (Concrete Products)

**Real-World Impact**: Adding new export format requires zero changes to client code. Follows Open/Closed Principle.

---

#### **11. Abstract Factory Pattern** 🏭🏭
📁 `13-Abstract-Factory-DP/`

**Problem**: Video player UI needs consistent theme families (Light/Dark) where all components (buttons, sliders, progress bars) must match.

**Context**: Mixing light button with dark slider breaks visual consistency. Need to ensure all UI components belong to same theme family.

**Solution**: `UIFactory` interface creates families of related objects (`Button`, `Slider`, `ProgressBar`) with `LightThemeFactory` and `DarkThemeFactory`.

**Key Classes**:
- `UIFactory` (Abstract Factory interface)
- `LightThemeFactory`, `DarkThemeFactory` (Concrete Factories)
- `Button`, `Slider`, `ProgressBar` (Product interfaces)

**Real-World Impact**: Guarantees theme consistency, makes switching themes trivial (change one factory).

---

#### **12. Prototype Pattern** 🐑
📁 `14-Prototype-DP/`

**Problem**: Creating video upload configurations from scratch is time-consuming when users often need similar settings.

**Context**: Content creators upload 100+ videos with similar configurations. Manually setting 12 parameters per video wastes time.

**Solution**: Clone existing `VideoUploadConfig` templates instead of creating new instances. Modify only what differs.

**Key Classes**:
- `Prototype` (interface with `clone()`)
- `VideoUploadConfig` (implements Cloneable)
- `TemplateManager` (manages template library)

**Real-World Impact**: Reduces upload configuration time from 2 minutes to 10 seconds. Enables template marketplace.

---

### ⚙️ Behavioral Patterns (11 Patterns)

#### **13. Observer Pattern** 👀
📁 `6-Observer-DP/`

**Problem**: YouTube channels need to notify thousands of subscribers when new videos are uploaded without tight coupling.

**Context**: Direct notification (calling each subscriber's method) creates tight coupling and doesn't support dynamic subscribe/unsubscribe.

**Solution**: `YouTubeChannel` (Subject) maintains observer list, automatically notifies all `Subscriber` observers when state changes.

**Key Classes**:
- `Channel` (Subject interface)
- `YouTubeChannel` (Concrete Subject)
- `Subscriber` (Observer interface)
- `EmailSubscriber`, `MobileAppSubscriber`, `SMSSubscriber` (Concrete Observers)

**Real-World Impact**: Supports millions of subscribers, dynamic subscribe/unsubscribe, multiple notification channels (email, SMS, push).

---

#### **14. Mediator Pattern** 🤝
📁 `7-Mediator-DP/`

**Problem**: Chat room participants communicating directly creates N*(N-1) connections. Adding/removing users affects all other users.

**Context**: Direct peer-to-peer communication is complex, hard to maintain, and doesn't support centralized logging or moderation.

**Solution**: `ChatRoom` mediator centralizes communication. Users send messages to mediator, which routes to appropriate recipients.

**Key Classes**:
- `ChatMediator` (Mediator interface)
- `ChatRoom` (Concrete Mediator)
- `User` (Colleague)

**Real-World Impact**: Reduces connections from N² to N, enables centralized logging/moderation, simplifies user management.

---

#### **15. Chain of Responsibility Pattern** ⛓️
📁 `9-ChainOfResponsibility-DP/`

**Problem**: Content moderation system needs to apply multiple filters (profanity, spam, adult content) without hardcoding filter order.

**Context**: Adding new filters or changing filter order requires modifying core processing logic. Filter logic is scattered across codebase.

**Solution**: Each filter is a handler in chain. Request passes through chain until handled or end of chain reached.

**Key Classes**:
- `ContentFilter` (Handler interface)
- `ProfanityFilter`, `SpamFilter`, `AdultContentFilter` (Concrete Handlers)
- `ModerationPipeline` (Client that builds chain)

**Real-World Impact**: Enables dynamic filter composition, A/B testing of filter orders, plugin architecture for new filters.

---

#### **16. Memento Pattern** 💾
📁 `15-Memento-DP/`

**Problem**: Video editor needs multi-level undo/redo functionality without exposing internal editor state or using manual state management.

**Context**: Tracking every edit operation manually is error-prone. Exposing editor internals violates encapsulation.

**Solution**: `VideoEditor` creates `EditorMemento` snapshots, `EditorHistory` stores mementos for undo/redo.

**Key Classes**:
- `VideoEditor` (Originator)
- `EditorMemento` (Memento - encapsulates state)
- `EditorHistory` (Caretaker - manages mementos)

**Real-World Impact**: Enables unlimited undo/redo levels, supports editor snapshots for recovery, maintains encapsulation.

---

#### **17. Template Method Pattern** 📋
📁 `16-Template-Method-DP/`

**Problem**: Video processing pipeline (validate → preprocess → encode → optimize → save → notify) has 67% code duplication across Standard, Premium, and Live processors.

**Context**: Processing steps are mostly the same, only encoding and optimization vary. Copy-paste approach causes maintenance issues.

**Solution**: `VideoProcessor` abstract class defines template method with fixed algorithm structure, subclasses override variable steps.

**Key Classes**:
- `VideoProcessor` (Abstract class with template method)
- `StandardVideoProcessor`, `PremiumVideoProcessor`, `LiveStreamProcessor` (Concrete classes)

**Real-World Impact**: Reduces duplication from 67% to 0%, ensures consistent processing pipeline, enables easy addition of new processor types.

---

#### **18. State Pattern** 🔄
📁 `17-State-DP/`

**Problem**: Video player state management using boolean flags creates invalid states (e.g., `isPlaying=true` AND `isPaused=true`) and if-else spaghetti code.

**Context**: 4 boolean flags create 16 possible combinations, only 4 are valid. State-specific behavior scattered across if-else blocks.

**Solution**: Extract each state into separate class implementing `PlayerState` interface. `VideoPlayer` delegates behavior to current state object.

**Key Classes**:
- `PlayerState` (State interface)
- `VideoPlayer` (Context)
- `StoppedState`, `PlayingState`, `PausedState`, `BufferingState` (Concrete States)

**Real-World Impact**: Eliminates invalid states, simplifies state transitions, makes adding new states trivial.

---

#### **19. Strategy Pattern** 🎯
📁 `18-Strategy-DP/`

**Problem**: Video compression algorithm selection uses hardcoded if-else statements, violating Open/Closed Principle.

**Context**: Adding new compression algorithm (AV1, VP9, H.265) requires modifying core compression code and retesting entire module.

**Solution**: Encapsulate each algorithm in separate strategy class implementing `CompressionStrategy`. `VideoCompressor` uses strategy interface.

**Key Classes**:
- `CompressionStrategy` (Strategy interface)
- `VideoCompressor` (Context)
- `H264Strategy`, `H265Strategy`, `VP9Strategy`, `AV1Strategy` (Concrete Strategies)

**Real-World Impact**: Enables runtime algorithm selection, A/B testing of compression quality, plugin architecture for new codecs.

---

#### **20. Command Pattern** 🎬
📁 `19-Command-DP/`

**Problem**: Video editor operations (add filter, adjust brightness, trim) are tightly coupled with no undo/redo support.

**Context**: Direct method calls can't be undone. Need to support command queueing, logging, macro recording.

**Solution**: Encapsulate each operation as `Command` object with `execute()` and `undo()` methods.

**Key Classes**:
- `Command` (Command interface)
- `VideoClip` (Receiver)
- `VideoEditor` (Invoker)
- `AddFilterCommand`, `AdjustBrightnessCommand`, `TrimCommand`, `MacroCommand` (Concrete Commands)

**Real-World Impact**: Enables unlimited undo/redo, command logging for crash recovery, macro recording for batch operations.

---

#### **21. Interpreter Pattern** 🔍
📁 `20-Interpreter-DP/`

**Problem**: Video search limited to basic filters, cannot handle complex query expressions like "duration > 10 AND (category = 'tutorial' OR views > 1000)".

**Context**: Adding each new query combination requires new code. Complex queries require nested if-else blocks.

**Solution**: Define grammar for search queries, implement interpreter for expression tree evaluation.

**Key Classes**:
- `Expression` (Abstract Expression)
- `Video` (Context)
- `GreaterThanExpression`, `AndExpression`, `OrExpression` (Concrete Expressions)
- `QueryParser` (builds expression tree)

**Real-World Impact**: Enables SQL-like video search, user-defined filters, advanced analytics queries.

---

#### **22. Iterator Pattern** 🔁
📁 `22-Iterator-DP/`

**Problem**: Different collection types (array, linked list, tree) require different traversal code, exposing internal structure to clients.

**Context**: Clients need to know if playlist uses array or linked list to iterate. Changing collection type breaks client code.

**Solution**: `VideoIterator` interface provides uniform iteration. Collections provide iterator without exposing internal structure.

**Key Classes**:
- `VideoIterator` (Iterator interface)
- `VideoCollection` (Aggregate interface)
- `ArrayVideoCollection`, `LinkedListVideoCollection` (Concrete Aggregates)
- `ArrayVideoIterator`, `FilteredIterator`, `ShuffledIterator` (Concrete Iterators)

**Real-World Impact**: Enables collection-agnostic code, supports multiple iteration strategies (filtered, shuffled, reversed).

---

#### **23. Visitor Pattern** 🚶
📁 `23-Visitor-DP/`

**Problem**: Operations (JSON export, XML export, analytics, quality check) scattered across entity classes violate Single Responsibility Principle. Adding new export format requires modifying all entity classes.

**Context**: StreamFlix needs to export video data (VideoFile, Playlist, Category) to 7 different formats. Without Visitor: 3 classes × 7 formats = 21 methods. With Visitor: 7 visitor classes.

**Solution**: Define `VideoVisitor` interface with `visit()` methods for each element type. Elements accept visitors via double dispatch.

**Key Classes**:
- `VideoElement` (Element interface with `accept()`)
- `VideoVisitor` (Visitor interface with `visit()` methods)
- `VideoFile`, `Playlist`, `Category` (Concrete Elements)
- `JSONExportVisitor`, `XMLExportVisitor`, `DurationCalculatorVisitor`, `QualityCheckVisitor` (Concrete Visitors)

**Real-World Impact**: Adding new operation requires 1 visitor class (2 hours) instead of modifying 3+ entity classes (1 week). 96% cost reduction.

---

## 🗺️ Pattern Relationships & Context Mapping

### When to Use Which Pattern?

#### **Problem: Incompatible Interfaces**
→ **Use Adapter Pattern** (#1)
- Example: Integrate Fahrenheit sensors with Celsius monitoring system

#### **Problem: Complex Subsystem Coordination**
→ **Use Facade Pattern** (#2)
- Example: Simplify e-commerce order processing (inventory + payment + shipping + notification)

#### **Problem: Tree Hierarchies with Uniform Treatment**
→ **Use Composite Pattern** (#3)
- Example: Task management (tasks and projects), file systems, org charts

#### **Problem: Orthogonal Class Hierarchies**
→ **Use Bridge Pattern** (#4)
- Example: M device types × N remote controls = M+N classes instead of M×N

#### **Problem: Expensive Object Creation or Access Control**
→ **Use Proxy Pattern** (#5)
- Example: Video streaming with lazy loading, access control, caching

#### **Problem: Many Objects with Shared State**
→ **Use Flyweight Pattern** (#6)
- Example: 9,000 UI icons sharing same images (99.97% memory reduction)

#### **Problem: Dynamic Feature Composition**
→ **Use Decorator Pattern** (#7)
- Example: Video stream enhancements (subtitles, ads, watermark) without class explosion

#### **Problem: Need Exactly One Instance**
→ **Use Singleton Pattern** (#8)
- Example: Database connection pool, configuration manager, logger

#### **Problem: Complex Object Construction**
→ **Use Builder Pattern** (#9)
- Example: Video upload config with 12+ parameters

#### **Problem: Creating Objects without Specifying Exact Class**
→ **Use Factory Method Pattern** (#10)
- Example: Video export format selection (MP4, AVI, MKV, WebM)

#### **Problem: Creating Families of Related Objects**
→ **Use Abstract Factory Pattern** (#11)
- Example: UI themes (light/dark) with consistent components (button, slider, progress bar)

#### **Problem: Clone Expensive Objects Instead of Create**
→ **Use Prototype Pattern** (#12)
- Example: Video upload templates (clone + modify faster than create from scratch)

#### **Problem: One-to-Many Dependency with Auto-Notify**
→ **Use Observer Pattern** (#13)
- Example: YouTube channel notifying thousands of subscribers

#### **Problem: Complex Communication Between Many Objects**
→ **Use Mediator Pattern** (#14)
- Example: Chat room centralizing user-to-user communication

#### **Problem: Sequential Processing with Variable Handler**
→ **Use Chain of Responsibility Pattern** (#15)
- Example: Content moderation filters (profanity → spam → adult content)

#### **Problem: Undo/Redo Functionality**
→ **Use Memento Pattern** (#16)
- Example: Video editor with multi-level undo/redo

#### **Problem: Algorithm with Fixed Steps but Variable Implementation**
→ **Use Template Method Pattern** (#17)
- Example: Video processing pipeline (validate → encode → optimize → save)

#### **Problem: State-Dependent Behavior**
→ **Use State Pattern** (#18)
- Example: Video player states (stopped, playing, paused, buffering)

#### **Problem: Interchangeable Algorithms**
→ **Use Strategy Pattern** (#19)
- Example: Video compression algorithms (H.264, H.265, VP9, AV1)

#### **Problem: Operations as First-Class Objects**
→ **Use Command Pattern** (#20)
- Example: Video editor operations with undo/redo support

#### **Problem: Evaluate Expressions in Custom Language**
→ **Use Interpreter Pattern** (#21)
- Example: Video search query language ("duration > 10 AND category = 'tutorial'")

#### **Problem: Uniform Traversal of Different Collections**
→ **Use Iterator Pattern** (#22)
- Example: Iterate videos regardless of array/linked list implementation

#### **Problem: Operations on Object Structure**
→ **Use Visitor Pattern** (#23)
- Example: Export/analytics operations on video entities without modifying them

---

## 🔗 Pattern Combinations (Real-World Scenarios)

### Scenario 1: Video Streaming Platform (StreamFlix)
**Used Patterns:**
1. **Proxy** (#5): Lazy loading, access control, caching for videos
2. **Decorator** (#7): Dynamic stream enhancements (subtitles, ads, watermark)
3. **Observer** (#13): Notify subscribers about new uploads
4. **Strategy** (#19): Compression algorithm selection
5. **Visitor** (#23): Multi-format export and analytics
6. **Composite** (#3): Video playlists and categories hierarchy
7. **Flyweight** (#6): UI icon memory optimization

### Scenario 2: E-Commerce System
**Used Patterns:**
1. **Facade** (#2): Order processing (inventory + payment + shipping)
2. **Singleton** (#8): Database connection pool
3. **Factory Method** (#10): Create different payment processors
4. **Observer** (#13): Order status notifications
5. **Chain of Responsibility** (#15): Request validation (auth → inventory → payment)
6. **Command** (#20): Order operations with undo/cancellation

### Scenario 3: Video Editor Application
**Used Patterns:**
1. **Memento** (#16): Undo/redo for editing operations
2. **Command** (#20): Editing operations as objects
3. **Composite** (#3): Video timeline with nested clips
4. **Template Method** (#17): Export pipeline (validate → encode → save)
5. **Strategy** (#19): Compression algorithm selection
6. **Builder** (#9): Export configuration builder

### Scenario 4: UI Framework
**Used Patterns:**
1. **Abstract Factory** (#11): Theme families (light/dark)
2. **Composite** (#3): UI component tree (panels, buttons, etc.)
3. **Decorator** (#7): Component decorators (scrollable, bordered, shadowed)
4. **Observer** (#13): Event handling (button clicks)
5. **Flyweight** (#6): Icon and font sharing
6. **Iterator** (#22): Traverse UI component tree

---

## 📁 Project Structure

```
2025-Design-Pattern/
│
├── README.md                          # This file
│
├── Scripts/                           # Requirements and planning documents
│   ├── req-1.md                      # Adapter Pattern requirements
│   ├── req-2.md                      # Facade Pattern requirements
│   ├── ...
│   └── req-23.md                     # Visitor Pattern requirements
│
├── Solution/                          # Solution documents and lectures
│   ├── Adapter.md
│   ├── Facade.md
│   └── ...
│
├── Code-Sample/                       # Reference implementations
│   ├── AdapterPattern-Project/
│   ├── FacadePattern-Project/
│   └── ...
│
├── 1-Adapter-DP/                     # Pattern implementations
│   ├── *.java                        # Source files
│   └── package.bluej                 # UML diagram
│
├── 2-Facade-DP/
├── 3-Composite-DP/
├── 4-Bridge-DP/
├── 5-Singleton-DP/
├── 6-Observer-DP/
├── 7-Mediator-DP/
├── 8-Proxy-DP/
├── 9-ChainOfResponsibility-DP/
├── 10-Flyweight-DP/
├── 11-Builder-DP/
├── 12-Factory-Method-DP/
├── 13-Abstract-Factory-DP/
├── 14-Prototype-DP/
├── 15-Memento-DP/
├── 16-Template-Method-DP/
├── 17-State-DP/
├── 18-Strategy-DP/
├── 19-Command-DP/
├── 20-Interpreter-DP/
├── 21-Decorator-DP/
├── 22-Iterator-DP/
└── 23-Visitor-DP/
```

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **BlueJ IDE** (recommended) or any Java IDE (IntelliJ IDEA, Eclipse, VS Code)
- Basic understanding of Object-Oriented Programming (OOP)

### Running Examples

#### Option 1: Using BlueJ (Recommended)

1. Open BlueJ
2. Select `Project` → `Open Project`
3. Navigate to a pattern folder (e.g., `1-Adapter-DP/`)
4. Right-click on the Demo/Main class
5. Select `void main(String[] args)`
6. View output in Terminal window

#### Option 2: Using Command Line

```bash
# Navigate to pattern directory
cd 1-Adapter-DP/

# Compile all Java files
javac *.java

# Run the demo class
java MonitoringSystem
```

#### Option 3: Using IDE (IntelliJ IDEA / Eclipse)

1. Import project folder
2. Locate Demo/Main class
3. Right-click → Run

---

## 📚 Learning Path

### For Beginners

**Week 1-2: Structural Patterns (Easy)**
1. Adapter (#1) - Easiest, great starting point
2. Facade (#2) - Simplification concept
3. Proxy (#5) - Understand delegation

**Week 3-4: Creational Patterns (Medium)**
4. Singleton (#8) - Simplest creational pattern
5. Builder (#9) - Practical for configuration objects
6. Factory Method (#10) - Foundation for factories

**Week 5-6: Behavioral Patterns (Easy-Medium)**
7. Observer (#13) - Very common, easy to understand
8. Strategy (#19) - Algorithm encapsulation
9. Template Method (#17) - Code reuse through inheritance

**Week 7-8: Advanced Structural Patterns**
10. Composite (#3) - Tree structures
11. Decorator (#7) - Dynamic feature addition
12. Bridge (#4) - Separation of concerns

**Week 9-10: Advanced Behavioral Patterns**
13. Command (#20) - Operation objects with undo
14. State (#18) - State machines
15. Chain of Responsibility (#15) - Request processing pipeline

**Week 11-12: Expert-Level Patterns**
16. Visitor (#23) - Complex but powerful
17. Interpreter (#21) - Language processing
18. Memento (#16) - State management
19. Mediator (#14) - Object communication
20. Iterator (#22) - Collection traversal
21. Flyweight (#6) - Memory optimization
22. Abstract Factory (#11) - Family creation
23. Prototype (#12) - Object cloning

---

## 🎓 Key Concepts

### SOLID Principles Applied

- **Single Responsibility Principle (SRP)**: Visitor (#23), Command (#20)
- **Open/Closed Principle (OCP)**: Strategy (#19), Decorator (#7), Factory Method (#10)
- **Liskov Substitution Principle (LSP)**: All structural patterns
- **Interface Segregation Principle (ISP)**: Adapter (#1), Bridge (#4)
- **Dependency Inversion Principle (DIP)**: Abstract Factory (#11), Factory Method (#10)

### Gang of Four (GoF) Categories

**Creational Patterns** (5): Object creation mechanisms
- Singleton, Builder, Factory Method, Abstract Factory, Prototype

**Structural Patterns** (7): Class and object composition
- Adapter, Facade, Composite, Bridge, Proxy, Flyweight, Decorator

**Behavioral Patterns** (11): Interaction patterns between objects
- Observer, Mediator, Chain of Responsibility, Memento, Template Method, State, Strategy, Command, Interpreter, Iterator, Visitor

---

## 💡 Best Practices

### When to Use Design Patterns

✅ **DO Use Patterns When:**
- Problem matches pattern's intent
- Pattern simplifies complexity
- Code becomes more maintainable
- Team understands the pattern

❌ **DON'T Use Patterns When:**
- Problem is simple (don't over-engineer)
- Pattern adds unnecessary complexity
- Team is unfamiliar (training needed first)
- Requirements are unclear

### Common Anti-Patterns to Avoid

1. **Singleton Overuse**: Not everything should be singleton
2. **Pattern Obsession**: Don't force patterns where they don't fit
3. **Premature Optimization**: Use patterns when needed, not "just in case"
4. **Mixing Patterns Incorrectly**: Understand pattern combinations

---

## 📖 Resources

### Official Documentation
- [Gang of Four (GoF) Design Patterns Book](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Source Making - Design Patterns](https://sourcemaking.com/design_patterns)

### Video Tutorials
- [Christopher Okhravi - Design Patterns Playlist](https://www.youtube.com/playlist?list=PLrhzvIcii6GNjpARdnO4ueTUAVR9eMBpc)
- [Derek Banas - Design Patterns Playlist](https://www.youtube.com/playlist?list=PLF206E906175C7E07)

### Java-Specific Resources
- [Java Design Patterns - GitHub](https://github.com/iluwatar/java-design-patterns)
- [Baeldung - Design Patterns in Java](https://www.baeldung.com/design-patterns-series)

---

## 🤝 Contributing

This is a learning project. Suggestions for improvements are welcome:

1. **Add New Examples**: Implement patterns in different domains
2. **Improve Documentation**: Enhance explanations with diagrams
3. **Code Reviews**: Suggest better implementations
4. **Bug Fixes**: Report issues with existing code

---

## 📜 License

This project is for educational purposes. Code samples are based on Gang of Four design patterns, which are public knowledge.

---

## 🎯 Learning Outcomes

After completing this project, you will be able to:

✅ Recognize 23 design patterns in real-world code
✅ Apply appropriate patterns to solve design problems
✅ Evaluate trade-offs between different patterns
✅ Implement patterns in Java following best practices
✅ Combine multiple patterns for complex scenarios
✅ Explain pattern benefits using concrete examples
✅ Refactor code using design patterns
✅ Make informed architectural decisions

---

## 🌟 Acknowledgments

- **Gang of Four** for creating the seminal Design Patterns book
- **BlueJ Team** for the excellent educational IDE
- **Open Source Community** for reference implementations
- **Students and Educators** who make design patterns accessible to all

---

## 📞 Contact & Feedback

For questions, suggestions, or feedback about this project:
- Open an issue in the project repository
- Contribute improvements via pull requests
- Share your learning experience

---

**Happy Learning! 🚀**

*"Design patterns are not about code reuse, they are about knowledge reuse."*
— Gang of Four

---

**Last Updated**: November 2025
**Patterns Implemented**: 23/23 ✅
**Status**: Complete 🎉
