# 📱 MinLish Mobile

**MinLish Mobile** là ứng dụng Android hỗ trợ học và ghi nhớ từ vựng thông minh, được xây dựng dựa trên thuật toán lặp lại ngắt quãng (Spaced Repetition) **SM-2**. Ứng dụng tập trung vào trải nghiệm người dùng mượt mà, hỗ trợ chế độ Offline-first và được phát triển bằng các công nghệ Android hiện đại nhất.

---

## ✨ Tính năng nổi bật

- 🔐 **Xác thực bảo mật:** Đăng nhập / Đăng ký truyền thống và hỗ trợ **Google Sign-In**. Token được mã hóa và tự động làm mới (refresh) khi hết hạn.
- 📚 **Quản lý từ vựng:** Tạo, sửa, xóa và clone các bộ từ vựng (Vocab Sets) cá nhân.
- 🧠 **Học tập thông minh:** Ôn tập qua Flashcard kết hợp thuật toán **SM-2** giúp tối ưu hóa thời gian ghi nhớ.
- 🌍 **Khám phá (Explore):** Tìm kiếm và thêm các bộ từ vựng có sẵn từ cộng đồng.
- 📶 **Offline-First:** Hỗ trợ học tập khi không có mạng. Dữ liệu được tự động đồng bộ ngầm (background sync) khi có kết nối Internet.

---

## 🛠️ Công nghệ & Tech Stack

Dự án được xây dựng 100% bằng **Kotlin**, áp dụng các tiêu chuẩn cao nhất của Android development hiện nay:

| Lĩnh vực | Công nghệ sử dụng |
| :--- | :--- |
| **Giao diện (UI)** | Jetpack Compose, Material 3 |
| **Kiến trúc** | Clean Architecture, MVVM |
| **Dependency Injection** | Hilt (Dagger) |
| **Mạng (Network)** | Retrofit, OkHttp (kèm Interceptor cho JWT) |
| **Cơ sở dữ liệu** | Room Database |
| **Lưu trữ bảo mật** | EncryptedSharedPreferences |
| **Bất đồng bộ** | Kotlin Coroutines, Flow |
| **Chạy ngầm (Background)**| WorkManager (Đồng bộ dữ liệu) |

---

## 🏗️ Kiến trúc dự án (Clean Architecture)

Dự án được tổ chức chặt chẽ theo mô hình **Clean Architecture**, chia thành 3 tầng (layers) độc lập, giúp dễ dàng bảo trì, mở rộng và viết Unit Test:

1. **Presentation Layer (`presentation/`)**
    - Chứa giao diện người dùng (UI) viết bằng Jetpack Compose.
    - Các `ViewModel` nhận dữ liệu từ UseCases và cập nhật UI thông qua StateFlow.
    - Bao gồm các màn hình: Auth, Dashboard, Vocab Management, Learning (Flashcard), Explore, Profile.
2. **Domain Layer (`domain/`)**
    - Trung tâm của ứng dụng, chứa các Model thuần túy (không phụ thuộc Android).
    - Chứa các `UseCase` đóng gói logic nghiệp vụ (VD: `CalculateSM2UseCase`, `LoginUseCase`).
3. **Data Layer (`data/`)**
    - `remote/`: Gọi API, xử lý Interceptor (Auth, Refresh Token).
    - `local/`: Lưu trữ cache bằng Room và lưu token bảo mật.
    - `repository/`: Nguồn dữ liệu duy nhất (Single Source of Truth), quyết định lấy data từ mạng hay local.
    - `sync/`: Xử lý đồng bộ dữ liệu ngầm bằng WorkManager.

---

## 🚀 Bắt đầu (Getting Started)

### Yêu cầu hệ thống
- **Android Studio:** Ladybug hoặc mới hơn.
- **SDK:**
    - Min SDK: 24 (Android 7.0)
    - Target SDK: 34 (Android 14)
- **JDK:** 17 trở lên.

### Cài đặt & Cấu hình

1. Clone repository này về máy:
   ```bash
   git clone https://github.com/MinLish-Ecosystem/minlish-frontend-mobile.git
   cd minlish-frontend-mobile

2. Mở dự án bằng Android Studio.
3. Cấu hình Backend URL:
Tạo hoặc cập nhật file **local.properties** ở thư mục gốc (hoặc file config tương ứng của dự án) để trỏ đến đúng Base URL của API:
    ```bash
    # Ví dụ
    API_BASE_URL="https://api.minlish.com/v1/"