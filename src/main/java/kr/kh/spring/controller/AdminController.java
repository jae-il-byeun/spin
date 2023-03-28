package kr.kh.spring.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.kh.spring.service.AdminService;
import kr.kh.spring.vo.BoardTypeVO;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(value="/admin/board/type/list")
	public ModelAndView boardTypeList(ModelAndView mv) {
		//admin서비스에게 모든 게시글 타입을 가져오라고 요청
		System.out.println(mv);
		ArrayList<BoardTypeVO> list = adminService.getBoardType();
		mv.addObject("list",list);
		mv.setViewName("/admin/boardTypeList");
		return mv;
	}
	
	@RequestMapping(value="/admin/board/type/insert", method=RequestMethod.POST)
	public ModelAndView boardTypeInsert(ModelAndView mv, BoardTypeVO bt) {
		boolean res = adminService.insertBoardType(bt);
		mv.setViewName("redirect:/admin/board/type/list");
		return mv;
	}
	@RequestMapping(value="/admin/board/type/update", method=RequestMethod.POST)
	public ModelAndView boardTypeUpdate(ModelAndView mv, BoardTypeVO bt) {
		System.out.println(bt);
		boolean res = adminService.updateBoardType(bt);
		mv.setViewName("redirect:/admin/board/type/list");
		
		return mv;
	}
	@RequestMapping(value="/admin/board/type/delete", method=RequestMethod.POST)
	public ModelAndView boardTypeDelete(ModelAndView mv, BoardTypeVO bt) {
		//일반적으로 객체를 설정해도 객체가 안만들어지지만 스프링 컴트롤쪽은 기본생성자를 만들어서 넘겨주기 때문에 만들수 있다.
		boolean res = adminService.deleteBoardType(bt.getBt_num());
		
		mv.setViewName("redirect:/admin/board/type/list");
		
		return mv;
	}
}
