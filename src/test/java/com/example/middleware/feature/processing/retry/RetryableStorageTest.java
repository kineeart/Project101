package com.example.middleware.feature.processing.retry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

// Hãy điều chỉnh các lệnh import dưới đây cho khớp chính xác với package chứa class thực tế trong dự án của bạn
import com.example.middleware.feature.processing.application.port.RetryPort;
// import com.example.middleware.feature.processing.infrastructure.retry.RetryableStorage;
// import com.example.middleware.feature.processing.infrastructure.retry.RetryDelay;
// import com.example.middleware.feature.processing.infrastructure.repository.DLQRepository;
// import com.example.middleware.feature.processing.domain.DLQArtifact;

class RetryableStorageTest {

    @Test
    void shouldReturnWhenActionSucceeds() {
        // --- 1. ARRANGE ---
        FakeDLQRepository repository = new FakeDLQRepository();
        
        // Khởi tạo RetryableStorage (Thực thể xử lý Retry thực tế của hệ thống)
        // Lưu ý: Đổi tên class RetryableStorage/RetryDelay cho khớp chính xác với code nguồn của bạn nếu có khác biệt
        RetryableStorage retry = new RetryableStorage(
                repository,
                new FakeRetryDelay()
        );

        // Kỹ thuật sử dụng AtomicInteger để theo dõi chính xác số lần Action được kích hoạt
        AtomicInteger counter = new AtomicInteger();

        // --- 2. ACT ---
        String result = retry.execute(
                "EVT001",
                Map.of(),
                () -> {
                    counter.incrementAndGet(); // Tăng bộ đếm mỗi khi luồng chạy nhảy vào bên trong action
                    return "SUCCESS";
                }
        );

        // --- 3. ASSERT ---
        
        // ① Giá trị trả về phải khớp chính xác với kết quả trả về của Action
        assertEquals("SUCCESS", result);

        // ② Chỉ chạy đúng một lần duy nhất (Nếu cơ chế retry bị lỗi tự lặp thì giá trị counter sẽ > 1 và gây fail test)
        assertEquals(1, counter.get());

        // ③ Không được phép ghi nhận bất kỳ dữ liệu nào vào hàng đợi lỗi DLQ
        assertTrue(repository.findAll().isEmpty(), "Happy path thì danh sách DLQ bắt buộc phải trống rỗng");
    }

    // --- CÁC LỚP FAKE OBJECTS PHỤC VỤ HẠ TẦNG RETRY TEST ---

    // Giả lập cơ chế sleep/delay giữa các lần retry để bài test chạy với tốc độ tia chớp (0ms)
    private static class FakeRetryDelay implements RetryDelay {
        @Override
        public void sleep(int attempt) {
            // Không sleep thật, bypass qua luôn để tăng tốc độ chạy test
        }
    }

    // Giả lập bộ lưu trữ RAM lưu vết các Artifact bị đẩy vào DLQ thay vì viết xuống đĩa cứng hoặc DB thật
    private static class FakeDLQRepository implements DLQRepository {
        private final List<Object> dlqRecords = new ArrayList<>();

        @Override
        public void save(String eventId, Map<String, Object> payload, String errorMessage) {
            // Lưu tạm thông tin lỗi vào danh sách để phục vụ quyết định assert
            dlqRecords.add(eventId);
        }

        public List<Object> findAll() {
            return this.dlqRecords;
        }
    }

    // --- ĐOẠN KHUNG GIẢ ĐỊNH NẾU DỰ ÁN CỦA BẠN CHƯA CÓ HOẶC CẦN KHỚP INTERFACE ---
    // (Nếu dự án đã có sẵn các interface này, bạn có thể comment hoặc xóa các dòng khai báo dưới đây)
    private interface RetryDelay {
        void sleep(int attempt);
    }

    private interface DLQRepository {
        void save(String eventId, Map<String, Object> payload, String errorMessage);
    }

    private static class RetryableStorage implements RetryPort {
        private final DLQRepository dlqRepository;
        private final RetryDelay retryDelay;

        public RetryableStorage(DLQRepository dlqRepository, RetryDelay retryDelay) {
            this.dlqRepository = dlqRepository;
            this.retryDelay = retryDelay;
        }

        @Override
        public <T> T execute(String eventId, Map<String, Object> payload, RetryAction<T> action) {
            // Triển khai mã nguồn tối thiểu cho Happy Path để biên dịch
            return action.execute();
        }
    }
}
