package com.ptithcm.shared.bases;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.ptithcm.entities.base.LuuVetThoiGian;
import com.ptithcm.shared.dtos.FindOptions;
import com.ptithcm.shared.dtos.PaginationDTO;
import com.ptithcm.shared.dtos.PaginationResult;
import com.ptithcm.shared.utils.DateUtil;

/**
 * Lớp cơ sở Abstract cho tất cả các DAO trong hệ thống. Cung cấp các thao tác
 * CRUD cơ bản và các phương thức truy vấn nâng cao (Style TypeORM).
 *
 * @param <T>
 *            Kiểu của thực thể (Entity) - ví dụ: SinhVien, GiangVien
 * @param <ID>
 *            Kiểu dữ liệu của khóa chính (Serializable) - ví dụ: String,
 *            Integer, DangKyId
 */
public abstract class BaseDAO<T, ID extends Serializable> {

    @Autowired
    private SessionFactory sessionFactory;

    private final Class<T> entityClass;
    private final HqlQueryBuilder<T> queryBuilder;

    /**
     * Chức năng: Constructor khởi tạo BaseDAO với Class của Entity cụ thể.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * public class SinhVienDAO extends BaseDAO&lt;SinhVien, String&gt; {
     *     public SinhVienDAO() {
     *         super(SinhVien.class);
     *     }
     * }
     * </pre>
     *
     * @param entityClass
     *            Kiểu Class của Entity mà DAO này quản lý (ví dụ: SinhVien.class)
     */
    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.queryBuilder = new HqlQueryBuilder<>(entityClass);
    }

    /**
     * Chức năng: Lấy Hibernate Session hiện tại từ SessionFactory.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * Session session = getSession();
     * </pre>
     *
     * @return Hibernate Session hiện tại liên kết với Transaction đang hoạt động
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Chức năng: Tìm kiếm và lấy ra toàn bộ các bản ghi của thực thể trong
     * database.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * List&lt;SinhVien&gt; danhSach = sinhVienDAO.findAll();
     * </pre>
     *
     * @return Danh sách chứa toàn bộ các đối tượng thực thể tìm thấy
     */
    public List<T> findAll() {
        return getSession().createQuery("FROM " + entityClass.getName(), entityClass).getResultList();
    }

    /**
     * Chức năng: Tìm kiếm thực thể theo khóa chính (ID).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * SinhVien sv = sinhVienDAO.findById("N15DCCN001");
     * </pre>
     *
     * @param id
     *            Giá trị khóa chính cần tìm kiếm (kiểu ID)
     * @return Đối tượng thực thể tìm thấy, hoặc null nếu không tồn tại
     */
    public T findById(ID id) {
        return getSession().get(entityClass, id);
    }

    /**
     * Chức năng: Thêm mới một thực thể vào database (Insert).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * SinhVien sv = new SinhVien();
     * sv.setMaSV("N15DCCN100");
     * sv.setHo("Nguyễn Văn");
     * sv.setTen("A");
     * sinhVienDAO.save(sv);
     * </pre>
     *
     * @param entity
     *            Đối tượng thực thể cần lưu mới
     */
    public void save(T entity) {
        getSession().persist(entity);
    }

    /**
     * Chức năng: Cập nhật thông tin thực thể vào database (Update/Merge).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * SinhVien sv = sinhVienDAO.findById("N15DCCN001");
     * sv.setDiaChi("TP. Hồ Chí Minh");
     * sinhVienDAO.update(sv);
     * </pre>
     *
     * @param entity
     *            Đối tượng thực thể cần cập nhật
     */
    public void update(T entity) {
        getSession().merge(entity);
    }

    /**
     * Chức năng: Xóa một thực thể khỏi database (Hỗ trợ tự động Soft Delete).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * SinhVien sv = sinhVienDAO.findById("N15DCCN001");
     * if (sv != null) {
     *     sinhVienDAO.delete(sv);
     * }
     * </pre>
     *
     * @param entity
     *            Đối tượng thực thể cần xóa
     */
    public void delete(T entity) {
        // Kiểm tra xem Entity này có kế thừa class LuuVetThoiGian không (xóa mềm)
        if (entity instanceof LuuVetThoiGian) {
            LuuVetThoiGian softDeletableEntity = (LuuVetThoiGian) entity;

            // Cập nhật thời điểm xóa (Múi giờ VN)
            softDeletableEntity.setNgayXoa(DateUtil.nowVn());

            // Biến lệnh Remove thành lệnh Update
            getSession().merge(softDeletableEntity);
        } else {
            // Nếu là bảng bình thường (như bảng trung gian), tiến hành Hard Delete
            getSession().remove(entity);
        }
    }

    /**
     * Chức năng: Xóa thực thể khỏi database dựa trên khóa chính (ID) - TỐI ƯU HÓA
     * HQL.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * sinhVienDAO.deleteById("N15DCCN001");
     * </pre>
     *
     * @param id
     *            Giá trị khóa chính của thực thể cần xóa
     */
    public void deleteById(ID id) {
        if (id == null)
            return;

        // Tự động tìm tên cột khóa chính (ID) của Entity hiện tại bằng Metamodel
        String idName = getSession().getEntityManagerFactory().getMetamodel().entity(entityClass).getId(Object.class)
                .getName();

        String hql;
        MutationQuery query;

        // 1. Nếu là Entity có hỗ trợ Xóa mềm (Soft Delete)
        if (LuuVetThoiGian.class.isAssignableFrom(entityClass)) {
            hql = "UPDATE " + entityClass.getName() + " e SET e.ngayXoa = :ngayXoa WHERE e." + idName + " = :id";
            query = getSession().createMutationQuery(hql).setParameter("ngayXoa", DateUtil.nowVn()).setParameter("id",
                    id);
        }
        // 2. Nếu là Entity bình thường (Hard Delete)
        else {
            hql = "DELETE FROM " + entityClass.getName() + " e WHERE e." + idName + " = :id";
            query = getSession().createMutationQuery(hql).setParameter("id", id);
        }

        // Thực thi ngay lập tức dưới DB (Bỏ qua bước SELECT tốn thời gian)
        query.executeUpdate();
    }

    /**
     * Chức năng: Tìm một đối tượng duy nhất khớp với điều kiện lọc và sắp xếp (Hỗ
     * trợ lazy/eager options).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * Map&lt;String, Object&gt; filter = Map.of("maLop", "D15CQCN01");
     * FindOptions options = new FindOptions(Map.of("ten", "ASC"));
     * SinhVien sv = sinhVienDAO.findOne(filter, options);
     * </pre>
     *
     * @param filter
     *            Bản đồ chứa các cặp trường dữ liệu và giá trị lọc (VD:
     *            Map.of("maLop", "D15CQCN01"))
     * @param options
     *            Cấu hình sắp xếp (FindOptions) hoặc null nếu không cần sắp xếp
     * @return Đối tượng thực thể duy nhất tìm thấy, hoặc null nếu không khớp bản
     *         ghi nào
     */
    public T findOne(Map<String, Object> filter, FindOptions options) {
        String hql = queryBuilder.buildHql("", filter, options).toString();
        Query<T> query = getSession().createQuery(hql, entityClass);
        queryBuilder.bindParameters(query, filter);
        return query.setMaxResults(1).uniqueResult();
    }

    /**
     * Chức năng: Tìm một đối tượng duy nhất khớp với điều kiện lọc.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * Map&lt;String, Object&gt; filter = Map.of("username", "admin");
     * Users user = authDAO.findOne(filter);
     * </pre>
     *
     * @param filter
     *            Bản đồ chứa các cặp trường dữ liệu và giá trị lọc
     * @return Đối tượng thực thể duy nhất tìm thấy, hoặc null nếu không khớp bản
     *         ghi nào
     */
    public T findOne(Map<String, Object> filter) {
        return findOne(filter, null);
    }

    /**
     * Chức năng: Đếm tổng số lượng bản ghi thỏa mãn điều kiện lọc.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * Map&lt;String, Object&gt; filter = Map.of("daNghiHoc", false);
     * long activeStudents = sinhVienDAO.count(filter);
     * </pre>
     *
     * @param filter
     *            Bản đồ chứa các cặp trường dữ liệu và giá trị lọc
     * @return Số lượng bản ghi đếm được (long)
     */
    public long count(Map<String, Object> filter) {
        String hql = queryBuilder.buildHql("SELECT COUNT(e)", filter, null).toString();
        Query<Long> query = getSession().createQuery(hql, Long.class);
        queryBuilder.bindParameters(query, filter);
        return query.uniqueResult() != null ? query.uniqueResult() : 0L;
    }

    /**
     * Chức năng: Truy vấn phân trang dữ liệu kết hợp lọc và sắp xếp động.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * PaginationDTO dto = new PaginationDTO();
     * dto.setLimit(20);
     * dto.setOffset(0);
     * dto.setSort("-ten"); // Sắp xếp theo tên giảm dần (DESC)
     *
     * Map&lt;String, Object&gt; filter = Map.of("maLop", "D15CQCN01");
     *
     * PaginationResult&lt;SinhVien&gt; res = sinhVienDAO.paginate(dto, filter, null);
     * List&lt;SinhVien&gt; trang1 = res.getRows();
     * long tongSoSV = res.getTotal();
     * </pre>
     *
     * @param dto
     *            Đối tượng PaginationDTO chứa limit, offset và sort
     * @param filter
     *            Bản đồ chứa các cặp trường dữ liệu và giá trị lọc
     * @param options
     *            Cấu hình sắp xếp bổ sung (nếu có) hoặc null
     * @return Đối tượng PaginationResult chứa danh sách thực thể phân trang và tổng
     *         số bản ghi
     */
    public PaginationResult<T> paginate(PaginationDTO dto, Map<String, Object> filter, FindOptions options) {
        int limit = dto.getLimit();
        int offset = dto.getOffset();

        long totalCount = count(filter);
        if (totalCount == 0) {
            return new PaginationResult<>(List.of(), 0, limit, offset);
        }

        if (dto.getSort() != null && !dto.getSort().isEmpty()) {
            if (options == null) {
                options = new FindOptions();
            }
            boolean isDesc = dto.getSort().startsWith("-");
            String sortCol = isDesc ? dto.getSort().substring(1) : dto.getSort();
            options.setOrder(Map.of(sortCol, isDesc ? "DESC" : "ASC"));
        }

        String hql = queryBuilder.buildHql("", filter, options).toString();
        Query<T> query = getSession().createQuery(hql, entityClass);
        queryBuilder.bindParameters(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        List<T> data = query.getResultList();

        return new PaginationResult<>(data, totalCount, limit, offset);
    }

    /**
     * Chức năng: Lấy danh sách toàn bộ các đối tượng khớp điều kiện lọc và sắp xếp
     * (Không phân trang).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * Map&lt;String, Object&gt; filter = Map.of("maLop", "D15CQCN01");
     * FindOptions options = new FindOptions(Map.of("ten", "ASC", "ho", "ASC"));
     * List&lt;SinhVien&gt; list = sinhVienDAO.findAll(filter, options);
     * </pre>
     *
     * @param filter
     *            Bản đồ chứa các cặp trường dữ liệu và giá trị lọc
     * @param options
     *            Cấu hình sắp xếp (FindOptions) hoặc null
     * @return Danh sách đối tượng thực thể tìm thấy
     */
    public List<T> findAll(Map<String, Object> filter, FindOptions options) {
        String hql = queryBuilder.buildHql("", filter, options).toString();
        Query<T> query = getSession().createQuery(hql, entityClass);
        queryBuilder.bindParameters(query, filter);
        return query.getResultList();
    }

    /**
     * Chức năng: Tìm kiếm thực thể theo bộ lọc, nếu tìm thấy thì trả về, nếu không
     * tồn tại thì tự động thêm mới vào database.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * Map&lt;String, Object&gt; filter = Map.of("maMH", "MH001");
     * MonHoc mhNew = new MonHoc("MH001", "Cấu trúc dữ liệu", 3);
     * MonHoc mh = monHocDAO.findOneOrCreate(filter, mhNew);
     * </pre>
     *
     * @param filter
     *            Bản đồ chứa các cặp trường dữ liệu và giá trị lọc
     * @param newEntity
     *            Thực thể mới dùng để chèn vào database nếu không tìm thấy bản ghi
     *            cũ
     * @return Đối tượng thực thể cũ đã tồn tại, hoặc thực thể mới vừa chèn vào
     *         database
     */
    public T findOneOrCreate(Map<String, Object> filter, T newEntity) {
        T found = findOne(filter);
        if (found != null) {
            return found;
        }
        getSession().persist(newEntity);
        return newEntity;
    }

    /**
     * Chức năng: Cập nhật hàng loạt một hoặc nhiều trường giá trị theo danh sách
     * khóa chính (ID). Chỉ áp dụng cho thực thể có 1 khóa chính duy nhất. Không
     * dùng cho khóa chính phức hợp.
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * List&lt;String&gt; studentIds = List.of("N15DCCN001", "N15DCCN002", "N15DCCN003");
     * Map&lt;String, Object&gt; updateFields = Map.of("daNghiHoc", true, "diaChi", "Vắng mặt");
     * int rowCount = sinhVienDAO.bulkUpdateByIds(studentIds, updateFields);
     * </pre>
     *
     * @param ids
     *            Danh sách các khóa chính của các bản ghi cần cập nhật
     * @param updateFields
     *            Bản đồ chứa tên thuộc tính và giá trị mới cần cập nhật (SET field
     *            = :val)
     * @return Số lượng dòng dữ liệu bị ảnh hưởng bởi câu lệnh UPDATE (int)
     * @throws UnsupportedOperationException
     *             Nếu thực thể có khóa chính phức hợp (ví dụ: DangKy)
     */
    public int bulkUpdateByIds(List<ID> ids, Map<String, Object> updateFields) {
        if (ids == null || ids.isEmpty() || updateFields == null || updateFields.isEmpty()) {
            return 0;
        }

        java.util.List<String> idNames = new java.util.ArrayList<>();
        for (jakarta.persistence.metamodel.SingularAttribute<? super T, ?> attr : sessionFactory.getMetamodel()
                .entity(entityClass).getSingularAttributes()) {
            if (attr.isId()) {
                idNames.add(attr.getName());
            }
        }

        if (idNames.size() != 1) {
            throw new UnsupportedOperationException(
                    "bulkUpdateByIds không hỗ trợ thực thể có khóa phức hợp. Vui lòng sử dụng saveAll.");
        }

        String idName = idNames.get(0);

        StringBuilder hql = new StringBuilder("UPDATE ").append(entityClass.getName()).append(" e SET ");

        int i = 0;
        for (String key : updateFields.keySet()) {
            if (i > 0) {
                hql.append(", ");
            }
            hql.append("e.").append(key).append(" = :update_").append(key);
            i++;
        }
        hql.append(" WHERE e.").append(idName).append(" IN (:ids)");

        // Dùng createMutationQuery cho các lệnh UPDATE/DELETE trong Hibernate 6
        var query = getSession().createMutationQuery(hql.toString());

        // Bind giá trị update
        for (Map.Entry<String, Object> entry : updateFields.entrySet()) {
            query.setParameter("update_" + entry.getKey(), entry.getValue());
        }
        // Bind danh sách IDs
        query.setParameterList("ids", ids);

        return query.executeUpdate(); // Trả về số dòng bị ảnh hưởng
    }

    /**
     * Chức năng: Lưu hoặc cập nhật hàng loạt một danh sách các thực thể với hiệu
     * năng cao bằng JDBC Batching. Cứ mỗi 50 bản ghi sẽ tự động flush dữ liệu xuống
     * database và clear session cache để tránh tràn RAM (OutOfMemory). Đặc biệt
     * khuyến khích sử dụng thay thế cho bulkUpdate khi cập nhật các thực thể có
     * khóa chính phức hợp (ví dụ: DangKy).
     * <p>
     * Cách dùng:
     * 
     * <pre>
     * List&lt;DangKy&gt; registList = ...; // Danh sách hàng trăm đăng ký điểm thi
     * dangKyDAO.saveAll(registList);
     * </pre>
     *
     * @param entities
     *            Danh sách các đối tượng thực thể cần chèn mới hoặc cập nhật thông
     *            tin
     */
    public void saveAll(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        Session session = getSession();
        int batchSize = 50; // Kích thước lô (Cấu hình chuẩn của Hibernate)

        for (int i = 0; i < entities.size(); i++) {
            // Dùng merge để vừa hỗ trợ Insert (nếu ID null) vừa hỗ trợ Update (nếu có ID)
            session.merge(entities.get(i));

            // Cứ mỗi 50 record thì ép Hibernate đẩy lệnh SQL xuống Database và dọn rác
            if (i > 0 && i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }
        // Đẩy nốt số record lẻ còn lại
        session.flush();
        session.clear();
    }
}
