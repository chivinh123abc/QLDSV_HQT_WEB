package com.ptithcm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.entity.MonHoc;

@Controller
@Transactional
@RequestMapping("/credit-class")
public class LopTinChiController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model, HttpSession httpSession) {
		Session session = factory.getCurrentSession();
		
		String sessionRole = (String) httpSession.getAttribute("role");
		String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

		List<LopTinChi> ltcList;
		if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
			ltcList = session.createQuery("FROM LopTinChi WHERE maKhoa = :maKhoa", LopTinChi.class)
					.setParameter("maKhoa", sessionMaKhoa)
					.list();
		} else {
			ltcList = session.createQuery("FROM LopTinChi", LopTinChi.class).list();
		}

		populateCanDelete(session, ltcList);
		
		List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();
		if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
			khoaList = khoaList.stream()
					.filter(k -> k.getMaKhoa().equals(sessionMaKhoa))
					.collect(Collectors.toList());
		}

		List<MonHoc> monHocList = session.createQuery("FROM MonHoc", MonHoc.class).list();
		List<GiangVien> giangVienList;
		if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
			giangVienList = session.createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
					.setParameter("maKhoa", sessionMaKhoa).list();
		} else {
			giangVienList = session.createQuery("FROM GiangVien", GiangVien.class).list();
		}

		model.addAttribute("ltcList", ltcList);
		model.addAttribute("khoaList", khoaList);
		model.addAttribute("monHocList", monHocList);
		model.addAttribute("giangVienList", giangVienList);
		return "credit-class/index";
	}

	@RequestMapping(params = "btnInsert")
	public String insert(ModelMap model, LopTinChi ltc, HttpSession httpSession) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.persist(ltc);
			t.commit();
			model.addAttribute("message", "Đã thêm lớp tín chỉ");
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model, httpSession);
	}

	@RequestMapping(params = "btnUpdate")
	public String update(ModelMap model, LopTinChi ltc, HttpSession httpSession) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.merge(ltc);
			t.commit();
			model.addAttribute("message", "Đã cập nhật lớp tín chỉ");
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model, httpSession);
	}

	@RequestMapping(params = "btnDelete")
	public String delete(ModelMap model, @RequestParam("maLTC") int maLTC, HttpSession httpSession) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			LopTinChi ltc = session.get(LopTinChi.class, maLTC);
			if (ltc != null) {
				session.remove(ltc);
				t.commit();
				model.addAttribute("message", "Đã xóa lớp tín chỉ: " + maLTC);
			}
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model, httpSession);
	}

	// --- AJAX API ENDPOINTS ---

	@RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public LopTinChi getLTC(@RequestParam("maLTC") int maLTC) {
		Session session = factory.getCurrentSession();
		return session.get(LopTinChi.class, maLTC);
	}

	@RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<LopTinChi> listLTC(@RequestParam(value = "maKhoa", required = false) String maKhoa, 
			HttpSession httpSession) {
		Session session = factory.getCurrentSession();
		
		String sessionRole = (String) httpSession.getAttribute("role");
		String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

		if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
			maKhoa = sessionMaKhoa;
		}

		List<LopTinChi> list;
		if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
			list = session.createQuery("FROM LopTinChi WHERE maKhoa = :maKhoa", LopTinChi.class)
					.setParameter("maKhoa", sessionMaKhoa)
					.list();
		} else if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
			list = session.createQuery("FROM LopTinChi", LopTinChi.class).list();
		} else {
			list = session.createQuery("FROM LopTinChi WHERE maKhoa = :maKhoa", LopTinChi.class)
					.setParameter("maKhoa", maKhoa)
					.list();
		}
		populateCanDelete(session, list);
		return list;
	}

	private void populateCanDelete(Session session, List<LopTinChi> list) {
		if (list.isEmpty())
			return;
		List<Integer> ltcWithReg = session.createQuery("SELECT distinct maLTC FROM DangKy", Integer.class).list();
		java.util.Set<Integer> dependentIds = new java.util.HashSet<>(ltcWithReg);

		for (LopTinChi ltc : list) {
			ltc.setCanDelete(!dependentIds.contains(ltc.getMaLTC()));
		}
	}

	@RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> saveLTC(@RequestBody LopTinChi ltc, @RequestParam("mode") String mode) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			if (mode.equals("add")) {
				// Check logical duplicate: nienKhoa, hocKy, maMH, nhom
				String hql = "SELECT COUNT(*) FROM LopTinChi WHERE nienKhoa = :nk AND hocKy = :hk AND maMH = :mh AND nhom = :nh";
				Long count = session.createQuery(hql, Long.class)
						.setParameter("nk", ltc.getNienKhoa())
						.setParameter("hk", ltc.getHocKy())
						.setParameter("mh", ltc.getMaMH())
						.setParameter("nh", ltc.getNhom())
						.uniqueResult();
				if (count > 0) {
					res.put("status", "error");
					res.put("message", "Lớp tín chỉ này đã tồn tại (trùng Niên khóa, Học kỳ, Môn học, Nhóm)!");
					return res;
				}

				// Manual ID generation to fix "Cannot insert NULL into MALTC"
				Integer maxId = session.createQuery("SELECT MAX(maLTC) FROM LopTinChi", Integer.class).uniqueResult();
				ltc.setMaLTC(maxId == null ? 1 : maxId + 1);

				session.persist(ltc);
			} else if (mode.equals("edit")) {
				LopTinChi existing = session.get(LopTinChi.class, ltc.getMaLTC());
				if (existing == null) {
					res.put("status", "error");
					res.put("message", "Không tìm thấy lớp tín chỉ để chỉnh sửa!");
					return res;
				}
				session.merge(ltc);
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
	public Map<String, Object> deleteLTC(@RequestParam("maLTC") int maLTC) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			// Check dependencies: DANGKY
			Long count = session.createQuery("SELECT COUNT(*) FROM DangKy WHERE maLTC = :maLTC", Long.class)
					.setParameter("maLTC", maLTC)
					.uniqueResult();

			if (count > 0) {
				res.put("status", "error");
				res.put("message", "Không thể xóa: Lớp tín chỉ đã có " + count + " sinh viên đăng ký!");
				return res;
			}

			LopTinChi ltc = session.get(LopTinChi.class, maLTC);
			if (ltc != null) {
				session.remove(ltc);
				t.commit();
				res.put("status", "success");
			} else {
				res.put("status", "error");
				res.put("message", "Không tìm thấy lớp tín chỉ để xóa!");
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

	@RequestMapping(value = "/api/monhoc", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<MonHoc> listMonHoc() {
		Session session = factory.getCurrentSession();
		return session.createQuery("FROM MonHoc", MonHoc.class).list();
	}

	@RequestMapping(value = "/api/gv", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<GiangVien> listGiangVien(@RequestParam(value = "maKhoa", required = false) String maKhoa, 
			HttpSession httpSession) {
		Session session = factory.getCurrentSession();
		
		String sessionRole = (String) httpSession.getAttribute("role");
		String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

		if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
			maKhoa = sessionMaKhoa;
		}

		if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
			return session.createQuery("FROM GiangVien", GiangVien.class).list();
		}
		return session.createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
				.setParameter("maKhoa", maKhoa).list();
	}
}
