package com.ptithcm.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Transactional
public class HomeController {

	@Autowired
	private SessionFactory factory;

	@RequestMapping({"/", "/index"})
	public String index(ModelMap model) {
		Session session = factory.getCurrentSession();
		
		Long studentCount = session.createQuery("SELECT COUNT(*) FROM SinhVien", Long.class).uniqueResult();
		Long classCount = session.createQuery("SELECT COUNT(*) FROM Lop", Long.class).uniqueResult();
		Long subjectCount = session.createQuery("SELECT COUNT(*) FROM MonHoc", Long.class).uniqueResult();
		Long creditClassCount = session.createQuery("SELECT COUNT(*) FROM LopTinChi", Long.class).uniqueResult();
		
		model.addAttribute("studentCount", studentCount);
		model.addAttribute("classCount", classCount);
		model.addAttribute("subjectCount", subjectCount);
		model.addAttribute("creditClassCount", creditClassCount);
		
		return "index";
	}
}
