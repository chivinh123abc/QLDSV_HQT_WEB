package com.ptithcm.controller;

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

@Controller
@Transactional
@RequestMapping("/class")
public class LopController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model, @RequestParam(value="maKhoa", required=false) String maKhoa) {
		Session session = factory.getCurrentSession();
		List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();
		
		List<Lop> lopList;
		if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
			Query<Lop> query = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class);
			query.setParameter("maKhoa", maKhoa);
			lopList = query.list();
		} else {
			lopList = session.createQuery("FROM Lop", Lop.class).list();
		}
		
		model.addAttribute("lopList", lopList);
		model.addAttribute("khoaList", khoaList);
		model.addAttribute("maKhoa", maKhoa);
		return "class/index";
	}

	// --- AJAX API ENDPOINTS ---
	
	@RequestMapping(value="/api/get", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Lop getClass(@RequestParam("maLop") String maLop) {
		Session session = factory.getCurrentSession();
		return session.get(Lop.class, maLop);
	}

	@RequestMapping(value="/api/list", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Lop> listClasses(@RequestParam(value="maKhoa", required=false) String maKhoa) {
		Session session = factory.getCurrentSession();
		if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
			return session.createQuery("FROM Lop", Lop.class).list();
		}
		Query<Lop> query = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class);
		query.setParameter("maKhoa", maKhoa);
		return query.list();
	}

	@RequestMapping(value="/api/save", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, Object> saveClass(@RequestBody Lop lop, @RequestParam("mode") String mode) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			Lop existing = session.get(Lop.class, lop.getMaLop());
			if (mode.equals("add")) {
				if (existing != null) {
					res.put("status", "error");
					res.put("message", "Mã lớp [" + lop.getMaLop() + "] đã tồn tại!");
					return res;
				}
				session.persist(lop);
			} else if (mode.equals("edit")) {
				if (existing == null) {
					res.put("status", "error");
					res.put("message", "Không tìm thấy lớp để chỉnh sửa!");
					return res;
				}
				session.merge(lop);
			}
			t.commit();
			res.put("status", "success");
		} catch (Exception e) {
			if (t != null) t.rollback();
			res.put("status", "error");
			res.put("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return res;
	}

	@RequestMapping(value="/api/delete", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, Object> deleteClass(@RequestParam("maLop") String maLop) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			// Check dependencies: SINHVIEN
			Long count = session.createQuery("SELECT COUNT(*) FROM SinhVien WHERE maLop = :maLop", Long.class)
					.setParameter("maLop", maLop)
					.uniqueResult();
			
			if (count > 0) {
				res.put("status", "error");
				res.put("message", "Không thể xóa: Lớp đang có " + count + " sinh viên!");
				return res;
			}

			Lop lop = session.get(Lop.class, maLop);
			if (lop != null) {
				session.remove(lop);
				t.commit();
				res.put("status", "success");
			} else {
				res.put("status", "error");
				res.put("message", "Không tìm thấy lớp để xóa!");
			}
		} catch (Exception e) {
			if (t != null) t.rollback();
			res.put("status", "error");
			res.put("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return res;
	}
}
