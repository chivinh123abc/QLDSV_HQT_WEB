package com.ptithcm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import com.ptithcm.entity.SinhVien;

@Controller
@Transactional
@RequestMapping("/student")
public class SinhVienController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model, 
			@RequestParam(value = "maLop", required = false) String maLop,
			@RequestParam(value = "maKhoa", required = false) String maKhoa) {
		Session session = factory.getCurrentSession();

		// Fetch All Departments
		List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();

		// Fetch Classes (Filter if maKhoa is provided)
		List<Lop> lopList;
		if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
			lopList = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class)
					.setParameter("maKhoa", maKhoa)
					.list();
		} else {
			lopList = session.createQuery("FROM Lop", Lop.class).list();
		}

		List<SinhVien> filteredList = new ArrayList<>();
		if (maLop != null && !maLop.isEmpty()) {
			Query<SinhVien> querySV = session.createQuery("FROM SinhVien WHERE maLop = :maLop", SinhVien.class);
			querySV.setParameter("maLop", maLop);
			filteredList = querySV.list();
		}

		model.addAttribute("lopList", lopList);
		model.addAttribute("khoaList", khoaList);
		model.addAttribute("sinhVienList", filteredList);
		model.addAttribute("maLop", maLop);
		model.addAttribute("maKhoa", maKhoa);
		return "student/index";
	}

	@RequestMapping(params = "btnInsert")
	public String insert(ModelMap model, SinhVien sinhVien) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.persist(sinhVien);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "redirect:/student?maLop=" + sinhVien.getMaLop() + "&showModal=true";
	}

	@RequestMapping(params = "btnUpdate")
	public String update(ModelMap model, SinhVien sinhVien) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.merge(sinhVien);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "redirect:/student?maLop=" + sinhVien.getMaLop() + "&showModal=true";
	}

	@RequestMapping(params = "btnDelete")
	public String delete(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			SinhVien sv = session.get(SinhVien.class, maSV);
			if (sv != null)
				session.remove(sv);
			t.commit();
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return "redirect:/student?maLop=" + maLop;
	}

	@RequestMapping(params = "lnkEdit")
	public String edit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop) {
		Session session = factory.getCurrentSession();
		SinhVien sv = session.get(SinhVien.class, maSV);
		model.addAttribute("sinhVien", sv);
		return index(model, maLop, null);
	}

	@RequestMapping(params = "lnkDelete")
	public String deleteInit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop) {
		Session session = factory.getCurrentSession();
		SinhVien sv = session.get(SinhVien.class, maSV);
		model.addAttribute("sinhVien", sv);
		return index(model, maLop, null);
	}

	// --- AJAX API ENDPOINTS ---

	@RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public SinhVien getStudent(@RequestParam("maSV") String maSV) {
		Session session = factory.getCurrentSession();
		return session.get(SinhVien.class, maSV);
	}

	@RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<SinhVien> listStudents(@RequestParam("maLop") String maLop) {
		Session session = factory.getCurrentSession();
		Query<SinhVien> query = session.createQuery("FROM SinhVien WHERE maLop = :maLop", SinhVien.class);
		query.setParameter("maLop", maLop);
		return query.list();
	}

	@RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> saveStudent(@RequestBody SinhVien sv, @RequestParam("mode") String mode) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			SinhVien existing = session.get(SinhVien.class, sv.getMaSV());
			if (mode.equals("add")) {
				if (existing != null) {
					res.put("status", "error");
					res.put("message", "Mã sinh viên [" + sv.getMaSV() + "] đã tồn tại!");
					return res;
				}
				session.persist(sv);
			} else if (mode.equals("edit")) {
				if (existing == null) {
					res.put("status", "error");
					res.put("message", "Không tìm thấy sinh viên để chỉnh sửa!");
					return res;
				}
				session.merge(sv);
			}

			t.commit();
			res.put("status", "success");
		} catch (Exception e) {
			if (t != null)
				t.rollback();
			res.put("status", "error");
			res.put("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return res;
	}

	@RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> deleteStudent(@RequestParam("maSV") String maSV) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			// Check dependencies: DANGKY
			Long count = session.createQuery("SELECT COUNT(*) FROM DangKy WHERE maSV = :maSV", Long.class)
					.setParameter("maSV", maSV)
					.uniqueResult();

			if (count > 0) {
				res.put("status", "error");
				res.put("message", "Không thể xóa: Sinh viên đã có " + count + " bản ghi đăng ký môn học!");
				return res;
			}

			SinhVien sv = session.get(SinhVien.class, maSV);
			if (sv != null) {
				session.remove(sv);
				t.commit();
				res.put("status", "success");
			} else {
				res.put("status", "error");
				res.put("message", "Không tìm thấy sinh viên để xóa!");
			}
		} catch (Exception e) {
			if (t != null)
				t.rollback();
			res.put("status", "error");
			res.put("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return res;
	}

	@RequestMapping(value = "/api/classes", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Lop> listClasses(@RequestParam(value = "maKhoa", required = false) String maKhoa) {
		Session session = factory.getCurrentSession();
		if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
			return session.createQuery("FROM Lop", Lop.class).list();
		}
		Query<Lop> query = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class);
		query.setParameter("maKhoa", maKhoa);
		return query.list();
	}
}
