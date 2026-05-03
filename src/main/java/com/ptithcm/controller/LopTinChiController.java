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

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.LopTinChi;

@Controller
@Transactional
@RequestMapping("/credit-class")
public class LopTinChiController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model) {
		Session session = factory.getCurrentSession();
		List<LopTinChi> ltcList = session.createQuery("FROM LopTinChi", LopTinChi.class).list();
		List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();
		model.addAttribute("ltcList", ltcList);
		model.addAttribute("khoaList", khoaList);
		return "credit-class/index";
	}
	
	@RequestMapping(params="btnInsert")
	public String insert(ModelMap model, LopTinChi ltc) {
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
		return index(model);
	}
	
	@RequestMapping(params="btnUpdate")
	public String update(ModelMap model, LopTinChi ltc) {
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
		return index(model);
	}
	
	@RequestMapping(params="btnDelete")
	public String delete(ModelMap model, @RequestParam("maLTC") int maLTC) {
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
		return index(model);
	}
}
