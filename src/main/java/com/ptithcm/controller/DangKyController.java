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
import com.ptithcm.entity.LopTinChi;

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
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public String insert(ModelMap model, @RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			NativeQuery query = session.createNativeQuery("EXEC sp_DangKyLopTinChi :maSV, :maLTC");
			query.setParameter("maSV", maSV);
			query.setParameter("maLTC", maLTC);
			query.executeUpdate();
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
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public String delete(ModelMap model, @RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			NativeQuery query = session.createNativeQuery("EXEC sp_HuyDangKyLopTinChi :maSV, :maLTC");
			query.setParameter("maSV", maSV);
			query.setParameter("maLTC", maLTC);
			query.executeUpdate();
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
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> apiRegister(@RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			NativeQuery query = session.createNativeQuery("EXEC sp_DangKyLopTinChi :maSV, :maLTC");
			query.setParameter("maSV", maSV);
			query.setParameter("maLTC", maLTC);
			query.executeUpdate();
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
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> apiCancel(@RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
		Map<String, Object> res = new HashMap<>();
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			NativeQuery query = session.createNativeQuery("EXEC sp_HuyDangKyLopTinChi :maSV, :maLTC");
			query.setParameter("maSV", maSV);
			query.setParameter("maLTC", maLTC);
			query.executeUpdate();
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
