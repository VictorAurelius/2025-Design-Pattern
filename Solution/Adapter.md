<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

### 3. Mẫu thiết kế Adapter

#### 3.0. Mô tả mẫu Adapter

**Mẫu Adapter** (Adapter Pattern) là một mẫu thiết kế cấu trúc cho phép các giao diện không tương thích có thể làm việc cùng nhau. Adapter hoạt động như một lớp trung gian, chuyển đổi giao diện của một lớp (Adaptee) thành giao diện mà client mong đợi (Target).

**Các thành phần chính:**

- **Target**: Giao diện mà client muốn sử dụng
- **Adaptee**: Lớp có chức năng cần thiết nhưng giao diện không tương thích
- **Adapter**: Lớp trung gian chuyển đổi giao diện
- **Client**: Đối tượng sử dụng Target

**Khi nào sử dụng:**

- Khi cần tích hợp thư viện bên thứ ba có giao diện khác
- Khi muốn tái sử dụng lớp cũ mà không sửa đổi mã nguồn
- Khi cần làm việc với nhiều hệ thống có API khác nhau[^1][^2][^3]


#### 3.1. Mô tả bài toán

Một ứng dụng media player hiện tại chỉ có thể phát file MP3 thông qua giao diện `play(filename)`. Tuy nhiên, người dùng muốn phát cả file MP4 và VLC. Vấn đề là thư viện phát video hiện có (AdvancedMediaPlayer) sử dụng giao diện khác: `playMp4(filename)` và `playVlc(filename)`.

Để ứng dụng có thể phát cả audio và video mà không cần thay đổi code hiện tại, ta sử dụng Adapter Pattern để chuyển đổi giao diện của AdvancedMediaPlayer sang giao diện MediaPlayer mà ứng dụng đang dùng.[^2][^4][^1]

#### 3.2. Cài đặt

Lớp `MediaPlayer` là giao diện mà ứng dụng đang sử dụng (Target).

```java
public interface MediaPlayer {
    void play(String audioType, String filename);
}
```

Lớp `AdvancedMediaPlayer` là thư viện phát video với giao diện khác (Adaptee).

```java
public class AdvancedMediaPlayer {
    public void playMp4(String filename) {
        System.out.println("Playing MP4 file: " + filename);
    }
    
    public void playVlc(String filename) {
        System.out.println("Playing VLC file: " + filename);
    }
}
```

Lớp `MediaAdapter` chuyển đổi giao diện (Adapter).

```java
public class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedPlayer;
    
    public MediaAdapter() {
        this.advancedPlayer = new AdvancedMediaPlayer();
    }
    
    @Override
    public void play(String audioType, String filename) {
        if (audioType.equalsIgnoreCase("mp4")) {
            advancedPlayer.playMp4(filename);
        } else if (audioType.equalsIgnoreCase("vlc")) {
            advancedPlayer.playVlc(filename);
        }
    }
}
```

Lớp `AudioPlayer` sử dụng Adapter để phát cả audio và video.

```java
public class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;
    
    @Override
    public void play(String audioType, String filename) {
        // Phát MP3 trực tiếp
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing MP3 file: " + filename);
        }
        // Sử dụng Adapter để phát MP4 và VLC
        else if (audioType.equalsIgnoreCase("mp4") || 
                 audioType.equalsIgnoreCase("vlc")) {
            mediaAdapter = new MediaAdapter();
            mediaAdapter.play(audioType, filename);
        } else {
            System.out.println("Invalid format: " + audioType);
        }
    }
}
```

Lớp Main chạy chương trình.

```java
public class Main {
    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();
        
        audioPlayer.play("mp3", "song.mp3");
        audioPlayer.play("mp4", "video.mp4");
        audioPlayer.play("vlc", "movie.vlc");
        audioPlayer.play("avi", "clip.avi");
    }
}
```


#### 3.3. Kết quả chạy chương trình

```
Playing MP3 file: song.mp3
Playing MP4 file: video.mp4
Playing VLC file: movie.vlc
Invalid format: avi
```

**Giải thích:**

Trong ví dụ này:

- **MediaPlayer (Target)**: Giao diện đơn giản `play(audioType, filename)` mà ứng dụng sử dụng
- **AdvancedMediaPlayer (Adaptee)**: Thư viện có giao diện khác với hai phương thức riêng biệt `playMp4()` và `playVlc()`
- **MediaAdapter (Adapter)**: Chuyển đổi lời gọi `play()` thành `playMp4()` hoặc `playVlc()` tùy loại file
- **AudioPlayer (Client)**: Sử dụng Adapter để phát nhiều loại file mà không cần biết chi tiết bên trong

Lợi ích: Ứng dụng giữ nguyên giao diện đơn giản nhưng có thể sử dụng thư viện phức tạp bên ngoài mà không cần sửa đổi mã nguồn của thư viện đó.[^4][^1][^2]

<div align="center">⁂</div>

[^1]: https://refactoring.guru/design-patterns/adapter

[^2]: https://www.geeksforgeeks.org/system-design/adapter-pattern/

[^3]: https://stackify.com/design-patterns-explained-adapter-pattern-with-code-examples/

[^4]: https://gpcoder.com/4483-huong-dan-java-design-pattern-adapter/

