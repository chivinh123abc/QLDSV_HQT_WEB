package com.ptithcm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.DangKyId;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.entity.SinhVien;

@Controller
@Transactional
@RequestMapping("/registration")
public class DangKyController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model) {
		Session session = factory.getCurrentSession();
		List<DangKy> registrationList = session.createQuery("FROM DangKy", DangKy.class).list();
		model.addAttribute("registrationList", registrationList);
		return "registration/index";
	}
	
	@RequestMapping(params="btnInsert")
	public String insert(ModelMap model, @RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			// 1. Check Student
			SinhVien sv = session.get(SinhVien.class, maSV);
			if (sv == null) throw new Exception("Sinh viên không tồn tại!");
			if (sv.isDangNghiHoc()) throw new Exception("Sinh viên đang trong trạng thái nghỉ học!");

			// 2. Check Class
			LopTinChi ltc = session.get(LopTinChi.class, maLTC);
			if (ltc == null) throw new Exception("Lớp tín chỉ không tồn tại!");
			if (ltc.isHuyLop()) throw new Exception("Lớp tín chỉ đã bị hủy!");

			// 3. Check Subject Constraint: Cannot register same subject in same semester
			String hqlSubject = "SELECT count(dk) FROM DangKy dk, LopTinChi ltc_ref " +
							   "WHERE dk.maLTC = ltc_ref.maLTC " +
							   "AND upper(trim(dk.maSV)) = upper(trim(:maSV)) " +
							   "AND dk.huyDangKy = false " +
							   "AND upper(trim(ltc_ref.maMH)) = upper(trim(:maMH)) " +
							   "AND ltc_ref.nienKhoa = :nk " +
							   "AND ltc_ref.hocKy = :hk " +
							   "AND dk.maLTC != :currentMaLTC";
			Long countSameSubject = session.createQuery(hqlSubject, Long.class)
					.setParameter("maSV", maSV)
					.setParameter("maMH", ltc.getMaMH())
					.setParameter("nk", ltc.getNienKhoa())
					.setParameter("hk", ltc.getHocKy())
					.setParameter("currentMaLTC", maLTC)
					.uniqueResult();
			
			if (countSameSubject > 0) {
				throw new Exception("Sinh viên đã đăng ký môn học này (" + ltc.getMaMH().trim().toUpperCase() + ") trong học kỳ này rồi!");
			}

			// 4. Check Registration
			DangKyId id = new DangKyId(maLTC, maSV);
			DangKy dk = session.get(DangKy.class, id);
			
			if (dk != null) {
				if (!dk.isHuyDangKy()) {
					throw new Exception("Sinh viên đã đăng ký lớp tín chỉ này rồi!");
				} else {
					dk.setHuyDangKy(false);
					session.merge(dk);
				}
			} else {
				DangKy newDk = new DangKy();
				newDk.setMaLTC(maLTC);
				newDk.setMaSV(maSV);
				newDk.setHuyDangKy(false);
				session.persist(newDk);
			}
			
			t.commit();
			model.addAttribute("message", "Đăng ký thành công");
		} catch (Exception e) {
			t.rollback();
			String errorMsg = e.getMessage();
			if (e.getCause() != null) errorMsg = e.getCause().getMessage();
			model.addAttribute("message", "Lỗi: " + errorMsg);
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="btnUpdate")
	public String update(ModelMap model, DangKy dangKy) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.merge(dangKy);
			t.commit();
			model.addAttribute("message", "Cập nhật thành công");
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="btnDelete")
	public String delete(ModelMap model, @RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			DangKyId id = new DangKyId(maLTC, maSV);
			DangKy dk = session.get(DangKy.class, id);
			if (dk == null || dk.isHuyDangKy()) {
				throw new Exception("Không tìm thấy thông tin đăng ký hoặc đã hủy trước đó!");
			}
			
			dk.setHuyDangKy(true);
			session.merge(dk);
			t.commit();
			model.addAttribute("message", "Đã hủy đăng ký thành công");
		} catch (Exception e) {
			t.rollback();
			String errorMsg = e.getMessage();
			if (e.getCause() != null) errorMsg = e.getCause().getMessage();
			model.addAttribute("message", "Lỗi: " + errorMsg);
		} finally {
			session.close();
		}
		return index(model);
	}

	// --- AJAX API ENDPOINTS ---
	
	@RequestMapping(value="/api/list", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<DangKy> listRegistration() {
		Session session = factory.getCurrentSession();
		return session.createQuery("FROM DangKy", DangKy.class).list();
	}

	@RequestMapping(value="/api/register", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, Object> apiRegister(@RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			SinhVien sv = session.get(SinhVien.class, maSV);
			if (sv == null) throw new Exception("Sinh viên không tồn tại!");
			if (sv.isDangNghiHoc()) throw new Exception("Sinh viên đang trong trạng thái nghỉ học!");

			LopTinChi ltc = session.get(LopTinChi.class, maLTC);
			if (ltc == null) throw new Exception("Lớp tín chỉ không tồn tại!");
			if (ltc.isHuyLop()) throw new Exception("Lớp tín chỉ đã bị hủy!");

			// Check Subject Constraint
			String hqlSubject = "SELECT count(dk) FROM DangKy dk, LopTinChi ltc_ref " +
							   "WHERE dk.maLTC = ltc_ref.maLTC " +
							   "AND upper(trim(dk.maSV)) = upper(trim(:maSV)) " +
							   "AND dk.huyDangKy = false " +
							   "AND upper(trim(ltc_ref.maMH)) = upper(trim(:maMH)) " +
							   "AND ltc_ref.nienKhoa = :nk " +
							   "AND ltc_ref.hocKy = :hk " +
							   "AND dk.maLTC != :currentMaLTC";
			Long countSameSubject = session.createQuery(hqlSubject, Long.class)
					.setParameter("maSV", maSV)
					.setParameter("maMH", ltc.getMaMH())
					.setParameter("nk", ltc.getNienKhoa())
					.setParameter("hk", ltc.getHocKy())
					.setParameter("currentMaLTC", maLTC)
					.uniqueResult();
			
			if (countSameSubject > 0) {
				throw new Exception("Sinh viên đã đăng ký môn học này (" + ltc.getMaMH().trim().toUpperCase() + ") trong học kỳ này rồi!");
			}

			DangKyId id = new DangKyId(maLTC, maSV);
			DangKy dk = session.get(DangKy.class, id);
			
			if (dk != null) {
				if (!dk.isHuyDangKy()) {
					throw new Exception("Sinh viên đã đăng ký lớp tín chỉ này rồi!");
				} else {
					dk.setHuyDangKy(false);
					session.merge(dk);
				}
			} else {
				DangKy newDk = new DangKy();
				newDk.setMaLTC(maLTC);
				newDk.setMaSV(maSV);
				newDk.setHuyDangKy(false);
				session.persist(newDk);
			}
			
			t.commit();
			res.put("status", "success");
			res.put("message", "Đăng ký thành công!");
		} catch (Exception e) {
			if (t != null) t.rollback();
			String errorMsg = e.getMessage();
			if (e.getCause() != null) errorMsg = e.getCause().getMessage();
			res.put("status", "error");
			res.put("message", "Lỗi: " + errorMsg);
		} finally {
			session.close();
		}
		return res;
	}

	@RequestMapping(value="/api/cancel", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, Object> apiCancel(@RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			DangKyId id = new DangKyId(maLTC, maSV);
			DangKy dk = session.get(DangKy.class, id);
			if (dk == null || dk.isHuyDangKy()) {
				throw new Exception("Không tìm thấy thông tin đăng ký hoặc đã hủy trước đó!");
			}
			
			dk.setHuyDangKy(true);
			session.merge(dk);
			t.commit();
			res.put("status", "success");
			res.put("message", "Đã hủy đăng ký thành công!");
		} catch (Exception e) {
			if (t != null) t.rollback();
			String errorMsg = e.getMessage();
			if (e.getCause() != null) errorMsg = e.getCause().getMessage();
			res.put("status", "error");
			res.put("message", "Lỗi: " + errorMsg);
		} finally {
			session.close();
		}
		return res;
	}
	
	@RequestMapping(value="/api/available-classes", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<LopTinChi> availableClasses() {
		Session session = factory.getCurrentSession();
		return session.createQuery("FROM LopTinChi WHERE huyLop = false", LopTinChi.class).list();
	}
}
