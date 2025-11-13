# Hướng Dẫn Vẽ UML Command Pattern - Layout Hoàn Chỉnh

## 1. LAYOUT DIAGRAM HOÀN CHỈNH

```
                    COMMAND PATTERN - VIDEO EDITOR UML DIAGRAM
                    
┌─────────────────────┐      ┌─────────────────────┐      ┌─────────────────────┐
│     <<interface>>   │      │     VideoEditor     │      │      VideoClip      │
│       Command       │      │     (Invoker)       │      │     (Receiver)      │
├─────────────────────┤      ├─────────────────────┤      ├─────────────────────┤
│ + execute(): void   │      │ - video: VideoClip  │      │ - filename: String  │
│ + undo(): void      │◄─────┤ - undoStack: Stack  │────► │ - duration: String  │
│ + getDescription()  │      │ - redoStack: Stack  │      │ - brightness: int   │
│   : String          │      │ - history: List     │      │ - contrast: int     │
└─────────────────────┘      │ - MAX_HISTORY: int  │      │ - filter: String    │
           ▲                 ├─────────────────────┤      │ - textOverlays:List │
           │                 │ + executeCommand()  │      │ - volume: int       │
           │                 │ + undo(): void      │      ├─────────────────────┤
           │                 │ + redo(): void      │      │ + applyFilter()     │
           │                 │ + canUndo(): bool   │      │ + setBrightness()   │
           │                 │ + canRedo(): bool   │      │ + setContrast()     │
┌──────────┴────┐            │ + showHistory()     │      │ + addTextOverlay()  │
│               │            └─────────────────────┘      │ + removeTextOverlay()│
│               │                                         │ + setVolume()       │
▼               ▼                                         │ + showState()       │
┌─────────────────────┐      ┌─────────────────────┐      └─────────────────────┘
│   AddTextCommand    │      │AdjustBrightnessCmd  │                ▲
├─────────────────────┤      ├─────────────────────┤                │
│ - video: VideoClip  │──────┤ - video: VideoClip  │────────────────┘
│ - text: String      │      │ - adjustment: int   │
│ - x: int            │      │ - prevBrightness:int│
│ - y: int            │      ├─────────────────────┤
│ - overlayIndex: int │      │ + execute(): void   │
├─────────────────────┤      │ + undo(): void      │
│ + execute(): void   │      │ + getDescription()  │
│ + undo(): void      │      └─────────────────────┘
│ + getDescription()  │
└─────────────────────┘
           ▲
           │
           │
┌─────────────────────┐
│    MacroCommand     │
├─────────────────────┤
│ - commands: List    │◆─────┐
│ - name: String      │      │
├─────────────────────┤      │
│ + addCommand()      │      │
│ + removeCommand()   │      │  (Composition)
│ + execute(): void   │      │  Contains multiple
│ + undo(): void      │      │  Command objects
│ + getDescription()  │      │
└─────────────────────┘      │
           │                 │
           └─────────────────┘

           
LEGEND:
═══════

◄─────  Association (uses)
  │     Realization (implements)
  ▲
  
◆─────  Composition (contains)

────►   Association (uses)

commands *   (multiplicity many)
video 1      (multiplicity one)
```

## 2. RELATIONSHIPS MAPPING (Các Đường Nối Chi Tiết)

### A. REALIZATION (IMPLEMENTS) - 3 đường nối
```
AddTextCommand          ──────────────────┐
                                          │
AdjustBrightnessCommand ──────────────────┼──► Command
                                          │   <<interface>>
MacroCommand           ──────────────────┘

Kiểu đường: ──────────────────► (nét liền, mũi tên tam giác trống)
```

### B. ASSOCIATION (USES) - 4 đường nối
```
1. VideoEditor ────────► Command
   Role: commands
   Multiplicity: *

2. VideoEditor ────────► VideoClip
   Role: video
   Multiplicity: 1

3. AddTextCommand ────────► VideoClip
   Role: video
   Multiplicity: 1

4. AdjustBrightnessCommand ────────► VideoClip
   Role: video
   Multiplicity: 1

Kiểu đường: ────────► (nét liền, mũi tên đơn)
```

### C. COMPOSITION (CONTAINS) - 1 đường nối
```
MacroCommand ◆────────► Command
   Role: commands
   Multiplicity: *

Kiểu đường: ◆────────► (nét liền, hình thoi đặc, mũi tên đơn)
```

## 3. DETAILED LAYOUT STRUCTURE

### Tầng 1 (Top Layer):
```
┌─Command─┐    ┌─VideoEditor─┐    ┌─VideoClip─┐
│Interface│    │ (Invoker)   │    │(Receiver) │
└─────────┘    └─────────────┘    └───────────┘
     │              │                   ▲
     │              │                   │
     │              ▼                   │
     │         [Association]            │
     │                                  │
```

### Tầng 2 (Middle Layer):
```
     │                                  │
     │                                  │
     ▼                                  │
┌─────────────────────────┐             │
│ Concrete Commands Layer │             │
│                         │             │
│  ┌─AddTextCommand─┐     │─────────────┘
│  │               │     │
│  └───────────────┘     │
│                        │
│  ┌─AdjustBrightness─┐  │
│  │     Command      │  │
│  └──────────────────┘  │
└─────────────────────────┘
```

### Tầng 3 (Bottom Layer):
```
┌─MacroCommand─┐
│             │◆
│ (Composite) │ ┐
└─────────────┘ │ Composition
                │ relationship
       ┌────────┘
       │
       ▼
   [Back to Command Interface]
```

## 4. CONNECTION POINTS (Điểm Kết Nối)

### From Command Interface:
- **To AddTextCommand**: Bottom center → Top center
- **To AdjustBrightnessCommand**: Bottom center → Top center
- **To MacroCommand**: Bottom center → Top center

### From VideoEditor:
- **To Command**: Left center → Right center
- **To VideoClip**: Right center → Left center

### From Concrete Commands:
- **AddTextCommand to VideoClip**: Right center → Bottom left
- **AdjustBrightnessCommand to VideoClip**: Right center → Bottom center

### From MacroCommand:
- **To Command Interface**: Top center → Left bottom (composition diamond)

## 5. VISUAL HIERARCHY

```
LEVEL 1: Core Abstractions
┌─────────┐
│Command  │ (Interface - Blue)
│Interface│
└─────────┘

LEVEL 2: Pattern Participants
┌─────────┐    ┌─────────┐
│VideoEditor   │VideoClip│
│(Orange) │    │ (Pink)  │
└─────────┘    └─────────┘

LEVEL 3: Concrete Implementations
┌─────────┐ ┌─────────┐ ┌─────────┐
│AddText  │ │Adjust   │ │Macro    │
│Command  │ │Bright   │ │Command  │
│(Green)  │ │Command  │ │(Yellow) │
│         │ │(Green)  │ │         │
└─────────┘ └─────────┘ └─────────┘
```

## 6. COMPLETE WIRING DIAGRAM

```
                [1] Realization
    ┌───────────────▲───────────────┐
    │               │               │
    │               │               │
AddTextCmd    AdjustBrightCmd    MacroCmd
    │               │               ║
    │[3] Association│               ║ [5] Composition
    │               │               ║
    └───────┐       └───────┐       ▼
            │               │    Command
            │               │   Interface
            │               │       ▲
            │               │       │
            │               │       │[2] Association
            │               │       │
            ▼               ▼       │
         VideoClip ◄──────────VideoEditor
                   [4] Association

Legend:
[1] 3x Realization arrows
[2] 1x Association (VideoEditor → Command)
[3] 2x Association (Commands → VideoClip)
[4] 1x Association (VideoEditor → VideoClip)
[5] 1x Composition (MacroCommand ◆→ Command)

Total: 8 connections
```

## 7. STEP-BY-STEP DRAWING ORDER

### Bước 1: Vẽ Classes (6 boxes)
1. Command Interface (top-left)
2. VideoEditor (top-center)
3. VideoClip (top-right)
4. AddTextCommand (bottom-left)
5. AdjustBrightnessCommand (bottom-left, dưới AddTextCommand)
6. MacroCommand (bottom-left, dưới AdjustBrightnessCommand)

### Bước 2: Vẽ Realization (3 arrows up)
7. AddTextCommand ──────────► Command
8. AdjustBrightnessCommand ──────────► Command
9. MacroCommand ──────────► Command

### Bước 3: Vẽ Association (4 arrows horizontal/diagonal)
10. VideoEditor ────────► Command [commands *]
11. VideoEditor ────────► VideoClip [video 1]
12. AddTextCommand ────────► VideoClip [video 1]
13. AdjustBrightnessCommand ────────► VideoClip [video 1]

### Bước 4: Vẽ Composition (1 diamond arrow)
14. MacroCommand ◆────────► Command [commands *]

**Total: 6 classes + 8 relationships = 14 elements**

---

## Đường Nối Chi Tiết

### 1. Realization (Implements) - Đường Kế Thừa Interface

#### AddTextCommand implements Command:
- **Toolbox**: Chọn **Realization**
- **Từ**: AddTextCommand
- **Đến**: Command interface
- **Kiểu đường**: Đường liền nét ——————————
- **Mũi tên**: Tam giác trống ◁
- **Màu**: Đen

#### AdjustBrightnessCommand implements Command:
- **Tương tự**: AdjustBrightnessCommand → Command
- **Đường nối**: ——————————◁

#### MacroCommand implements Command:
- **Tương tự**: MacroCommand → Command  
- **Đường nối**: ——————————◁

### 2. Association - VideoEditor uses Command

#### VideoEditor → Command:
- **Toolbox**: Chọn **Association**
- **Từ**: VideoEditor
- **Đến**: Command interface
- **Kiểu đường**: Đường liền nét ——————————
- **Mũi tên**: Mũi tên đơn →
- **Label**: `commands *` (ở phía Command)
- **Multiplicity**: `*` (many)

### 3. Association - VideoEditor uses VideoClip  

#### VideoEditor → VideoClip:
- **Từ**: VideoEditor
- **Đến**: VideoClip
- **Kiểu đường**: Đường liền nét ——————————
- **Mũi tên**: Mũi tên đơn →
- **Label**: `video 1` (ở phía VideoClip)
- **Multiplicity**: `1`

### 4. Association - Commands use VideoClip

#### AddTextCommand → VideoClip:
- **Từ**: AddTextCommand
- **Đến**: VideoClip
- **Kiểu đường**: Đường liền nét ——————————
- **Mũi tên**: Mũi tên đơn →
- **Label**: `video 1`

#### AdjustBrightnessCommand → VideoClip:
- **Tương tự**: AdjustBrightnessCommand → VideoClip
- **Label**: `video 1`

### 5. Composition - MacroCommand contains Commands

#### MacroCommand ◆→ Command:
- **Toolbox**: Chọn **Composition**
- **Từ**: MacroCommand  
- **Đến**: Command interface
- **Kiểu đường**: Đường liền nét ——————————
- **Hình thoi**: Đặc, màu đen ♦ (ở phía MacroCommand)
- **Label**: `commands *` (ở phía Command)
- **Multiplicity**: `*`

---

## Cách Vẽ Từng Loại Đường Nối

### Realization (Kế thừa Interface):
1. Chọn **Realization** từ Toolbox
2. Click vào class con (AddTextCommand)
3. Kéo đến interface cha (Command)
4. **Kết quả**: ——————————◁

### Association (Quan hệ sử dụng):
1. Chọn **Association** từ Toolbox
2. Click vào class sử dụng (VideoEditor)
3. Kéo đến class được sử dụng (Command)
4. **Cấu hình Properties**:
   - End2 Role: `commands`
   - End2 Multiplicity: `*`
5. **Kết quả**: ——————————→ commands *

### Composition (Quan hệ chứa đựng):
1. Chọn **Composition** từ Toolbox
2. Click vào class chứa (MacroCommand)
3. Kéo đến class được chứa (Command)
4. **Kết quả**: ♦——————————→ commands *

---

## Thứ Tự Vẽ Đường Nối

### Bước 1: Vẽ Realization trước (3 đường)
1. AddTextCommand → Command
2. AdjustBrightnessCommand → Command  
3. MacroCommand → Command

### Bước 2: Vẽ Association từ VideoEditor (2 đường)
4. VideoEditor → Command
5. VideoEditor → VideoClip

### Bước 3: Vẽ Association từ Commands đến VideoClip (2 đường)
6. AddTextCommand → VideoClip
7. AdjustBrightnessCommand → VideoClip

### Bước 4: Vẽ Composition cuối cùng (1 đường)
8. MacroCommand ◆→ Command

---

## Tips Layout Hiệu Quả

### 🎯 Positioning Strategy:
- **Interface ở trên cùng**: Command interface làm gốc
- **Invoker ở giữa**: VideoEditor ở vị trí trung tâm
- **Receiver ở phải**: VideoClip tách biệt
- **Concrete Commands ở dưới**: Implementations ở dưới interface

### 🔧 Spacing và Alignment:
1. **Uniform spacing**: 100-150px giữa các classes
2. **Grid alignment**: Sử dụng grid để căn chỉnh chính xác
3. **Minimize crossing**: Tránh đường nối chéo nhau
4. **Clear sight lines**: Đường nối thẳng, không quanh co

### 📐 Practical Steps:
1. **Đặt Command interface trước** (anchor point)
2. **VideoEditor và VideoClip** trên cùng hàng với Command
3. **Concrete Commands** xuống hàng dưới
4. **MacroCommand** ở vị trí đặc biệt (vừa implement vừa contain)

---

## Checklist Đường Nối

### ✅ Realization Arrows (3 đường):
- [ ] AddTextCommand ——————————◁ Command
- [ ] AdjustBrightnessCommand ——————————◁ Command  
- [ ] MacroCommand ——————————◁ Command

### ✅ Association Lines (4 đường):
- [ ] VideoEditor ——————————→ Command [commands *]
- [ ] VideoEditor ——————————→ VideoClip [video 1]
- [ ] AddTextCommand ——————————→ VideoClip [video 1]
- [ ] AdjustBrightnessCommand ——————————→ VideoClip [video 1]

### ✅ Composition Line (1 đường):
- [ ] MacroCommand ♦——————————→ Command [commands *]

**Tổng cộng: 8 đường nối**

---

Với layout và đường nối này, bạn sẽ có một UML diagram rõ ràng thể hiện Command Pattern với tất cả relationships chính xác.