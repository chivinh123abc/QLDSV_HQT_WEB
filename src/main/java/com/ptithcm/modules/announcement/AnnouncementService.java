package com.ptithcm.modules.announcement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pusher.rest.Pusher;

import com.ptithcm.entities.ThongBao;
import com.ptithcm.shared.dtos.FindOptions;

@Service
@Transactional
public class AnnouncementService {

    @Autowired
    private AnnouncementDAO thongBaoDAO;

    @Autowired
    private Pusher pusher;

    public List<ThongBao> listThongBao() {
        FindOptions options = new FindOptions();
        options.setOrder(Collections.singletonMap("ngayTao", "DESC"));
        return thongBaoDAO.findAll(Collections.emptyMap(), options);
    }

    public ThongBao getThongBaoById(String id) {
        return thongBaoDAO.findById(id);
    }

    public void saveThongBao(ThongBao thongBao, String mode) throws Exception {
        if ("add".equalsIgnoreCase(mode)) {
            thongBaoDAO.save(thongBao);

            // Trigger realtime notification to students
            try {
                System.out.println("🚀 [Pusher] Đang gửi request lên Cloud...");

                // Tạo payload chứa nhiều thông tin
                Map<String, Object> payload = new HashMap<>();
                payload.put("id", thongBao.getId());
                payload.put("title", thongBao.getTieuDe());

                // HỨNG KẾT QUẢ TRẢ VỀ (Result)
                com.pusher.rest.data.Result result = pusher.trigger("student-channel", "new-announcement", payload);

                // KIỂM TRA MÃ HTTP (200 là thành công)
                if (result.getHttpStatus() == 200) {
                    System.out.println("✅ [Pusher SUCCESS] Đã gửi thông báo thành công!");
                } else {
                    System.err.println("❌ [Pusher API ERROR] Bị Cloud từ chối!");
                    System.err.println("Mã lỗi: " + result.getHttpStatus());
                    System.err.println("Nội dung: " + result.getMessage());
                }

            } catch (Exception pusherEx) {
                // Lỗi này chỉ văng ra khi rớt mạng hoàn toàn (Network Exception)
                System.err.println("❌ [ThongBaoService] Lỗi kết nối mạng Pusher: " + pusherEx.getMessage());
                pusherEx.printStackTrace();
            }
        }
    }

    public void deleteThongBao(String id) throws Exception {
        ThongBao existing = thongBaoDAO.findById(id);
        if (existing != null) {
            thongBaoDAO.delete(existing);
        } else {
            throw new IllegalArgumentException("Không tìm thấy thông báo để xóa!");
        }
    }

    public void markAsRead(String idThongBao, String tenDangNhap) {
        thongBaoDAO.markAsRead(idThongBao, tenDangNhap);
    }

    public int countUnread(String tenDangNhap) {
        return thongBaoDAO.countUnread(tenDangNhap);
    }

    public Map<String, Boolean> getReadStatusMap(String tenDangNhap) {
        return thongBaoDAO.getReadStatusMap(tenDangNhap);
    }
}
