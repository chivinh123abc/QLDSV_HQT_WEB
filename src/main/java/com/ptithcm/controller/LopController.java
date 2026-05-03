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
import com.ptithcm.entity.Lop;

@Controller
@Transactional
@RequestMapping("/class")
public class LopController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model) {
		Session session = factory.getCurrentSession();
		List<Lop> lopList = session.createQuery("FROM Lop", Lop.class).list();
		List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();
		model.addAttribute("lopList", lopList);
		model.addAttribute("khoaList", khoaList);
		return "class/index";
	}
	
	@RequestMapping(params="btnInsert")
	public String insert(ModelMap model, Lop lop) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.persist(lop);
			t.commit();
			model.addAttribute("message", "Đã thêm lớp: " + lop.getTenLop());
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="btnUpdate")
	public String update(ModelMap model, Lop lop) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.merge(lop);
			t.commit();
			model.addAttribute("message", "Đã cập nhật lớp: " + lop.getTenLop());
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="btnDelete")
	public String delete(ModelMap model, @RequestParam("maLop") String maLop) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			Lop lop = session.get(Lop.class, maLop);
			if (lop != null) {
				session.remove(lop);
				t.commit();
				model.addAttribute("message", "Đã xóa lớp: " + maLop);
			}
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="lnkEdit")
	public String edit(ModelMap model, @RequestParam("maLop") String maLop) {
		Session session = factory.getCurrentSession();
		Lop lop = session.get(Lop.class, maLop);
		model.addAttribute("lop", lop);
		return index(model);
	}
}
