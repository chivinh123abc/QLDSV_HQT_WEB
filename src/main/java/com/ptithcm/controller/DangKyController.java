package com.ptithcm.controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.DangKyId;

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
	public String insert(ModelMap model, DangKy dangKy) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.persist(dangKy);
			t.commit();
			model.addAttribute("message", "Đăng ký thành công");
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
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
			if (dk != null) {
				session.remove(dk);
				t.commit();
				model.addAttribute("message", "Đã xóa đăng ký");
			}
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
}
