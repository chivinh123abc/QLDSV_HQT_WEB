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

import com.ptithcm.entity.MonHoc;

@Controller
@Transactional
@RequestMapping("/subject")
public class MonHocController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping()
	public String index(ModelMap model) {
		Session session = factory.getCurrentSession();
		List<MonHoc> monHocList = session.createQuery("FROM MonHoc", MonHoc.class).list();
		model.addAttribute("monHocList", monHocList);
		return "subject/index";
	}
	
	@RequestMapping(params="btnInsert")
	public String insert(ModelMap model, MonHoc monHoc) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.persist(monHoc);
			t.commit();
			model.addAttribute("message", "Đã thêm môn học: " + monHoc.getTenMH());
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="btnUpdate")
	public String update(ModelMap model, MonHoc monHoc) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			session.merge(monHoc);
			t.commit();
			model.addAttribute("message", "Đã cập nhật môn học: " + monHoc.getTenMH());
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Lỗi: " + e.getMessage());
		} finally {
			session.close();
		}
		return index(model);
	}
	
	@RequestMapping(params="btnDelete")
	public String delete(ModelMap model, @RequestParam("maMH") String maMH) {
		Session session = factory.openSession();
		org.hibernate.Transaction t = session.beginTransaction();
		try {
			MonHoc monHoc = session.get(MonHoc.class, maMH);
			if (monHoc != null) {
				session.remove(monHoc);
				t.commit();
				model.addAttribute("message", "Đã xóa môn học: " + maMH);
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
	public String edit(ModelMap model, @RequestParam("maMH") String maMH) {
		Session session = factory.getCurrentSession();
		MonHoc monHoc = session.get(MonHoc.class, maMH);
		model.addAttribute("monHoc", monHoc);
		return index(model);
	}
}
