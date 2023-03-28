	package kr.kh.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kr.kh.spring.service.BoardService;
import kr.kh.spring.vo.BoardTypeVO;
import kr.kh.spring.vo.BoardVO;
import kr.kh.spring.vo.FileVO;
import kr.kh.spring.vo.LikesVO;
import kr.kh.spring.vo.MemberVO;
import utils.MessageUtils;

@Controller
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@RequestMapping(value = "/Board/list", method=RequestMethod.GET)
	public ModelAndView boardList(ModelAndView mv) {
		
		// 우선 전체 게시글을 가져오는 코드로 작성하고 추후에 페이지네이션 및 검색 기능을 적용
		ArrayList<BoardVO> list = boardService.getBoardList();
		mv.addObject("list",list);
		mv.setViewName("/Board/list");
		return mv;
	}
	@RequestMapping(value = "/Board/insert", method=RequestMethod.GET)
	public ModelAndView boardInsert(ModelAndView mv,HttpServletRequest request) {
												//세션에서 회원 정보를 가져옴(게시판을 가져오기 위해서)
												//=> 쓰기 권한이 있는 게시판을 가져오기 위한 자업
		MemberVO user = (MemberVO)request.getSession().getAttribute("user");
		//사용자 권한에 맞는 게시판들을 가져옴
		ArrayList<BoardTypeVO> btList = boardService.getBoardType(user.getMe_authority());
		
		mv.addObject("btList",btList);//게시글                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                전달
		
		//작성할 타입이 없으면 작성 페이지로 갈 필요가 없어서 게시글 리스트로 이동	
		if(btList.size() == 0) {
			mv.setViewName("redirect:/Board/list"); //리다이렉트
		}else
			mv.setViewName("/Board/insert");//화면
		
		
		return mv;
	}
	@RequestMapping(value = "/Board/insert", method=RequestMethod.POST)
	public ModelAndView boardInsertPost(ModelAndView mv,BoardVO board,HttpSession session, MultipartFile []files) {//inser.jep와 이름이 같아야한다. 첨부파일 넣기니까 첨부파일이니까 첨부파일과 이름이 같아야한다.
		System.out.println(board);
		//세션에 있는 회원정보 가져옴 작성자에 넣기 위해서
		MemberVO user = (MemberVO)session.getAttribute("user"); //유저가 닐인 경우는 로그인을 안해서 전부 껐다가 다시 킬것
		//게시글 정보와 회원 정보를 이용하여 게시글 등록하라고 시킴
		boardService.insertBoard(board,user,files);
		mv.setViewName("redirect:/Board/list");
		return mv;
	}
	@RequestMapping(value = "/Board/detail/{bo_num}", method=RequestMethod.GET)
	public ModelAndView boardDetail(ModelAndView mv,@PathVariable("bo_num") int bo_num, HttpSession session, HttpServletResponse response) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		BoardVO board = boardService.getBoard(bo_num,user);
		ArrayList<FileVO> files = boardService.getFileList(bo_num);
		LikesVO likesVo = boardService.getLikes(bo_num,user);
		mv.addObject("board",board);
		mv.addObject("files",files);
		mv.addObject("likes",likesVo);
		if(board == null) {
			MessageUtils.alertAndMovePage(response,
					"삭제된 게시글이거나 조회 권한이 없습니다.",
					"/spring", "/Board/list");
			mv.setViewName("redirec:/Board/list");
		}else {
			mv.setViewName("/Board/detail");
		}
		return mv;
	}
	
	@ResponseBody//리턴값을 화면에 바로 보내주는 역할
	@RequestMapping(value = "/Board/like/{li_state}/{bo_num}", method=RequestMethod.GET)
	public Map<String, Object> boardLike(HttpSession session, @PathVariable("li_state")int li_state,@PathVariable("bo_num")int bo_num){
		HashMap<String, Object> map = new HashMap<String, Object>();
		//res - 1 : 추천, -1 : 비추천 : 0이면 취소
		MemberVO user = (MemberVO)session.getAttribute("user");
		int res = boardService.updateLikes(user, bo_num, li_state);
		map.put("res", res);
		return map;
	}
	
	@RequestMapping(value = "/Board/delete/{bo_num}", method=RequestMethod.GET)
	public ModelAndView boardDelete(ModelAndView mv,HttpSession session, @PathVariable("bo_num")int bo_num, HttpServletResponse response){
		
		//세션에 있는 회원정보 가져옴, 작성자와 아이디가 같은지 확인하려고
		MemberVO user = (MemberVO)session.getAttribute("user");
		boolean res = boardService.deleteBoard(bo_num, user);
		if(res) {
			MessageUtils.alertAndMovePage(response, "게시글을 삭제했습니다.", "/spring", "/Board/list");
		
		}else {
			MessageUtils.alertAndMovePage(response, "작성자가 아니거나 이미 삭제된 게시글입니다.", "/spring", "/Board/detail/"+bo_num);
		}
		return mv;
	}
	@RequestMapping(value = "/Board/update/{bo_num}", method=RequestMethod.GET)
	public ModelAndView boardUpdate(ModelAndView mv,HttpSession session, @PathVariable("bo_num")int bo_num, HttpServletResponse response){
		
		//세션에 있는 회원정보 가져옴, 작성자와 아이디가 같은지 확인하려고
		MemberVO user = (MemberVO)session.getAttribute("user");
		BoardVO board = boardService.getBoardByWriteAuthority(bo_num, user);
		if(board == null) {
			MessageUtils.alertAndMovePage(response, "작성자가 아니거나 이미 존재하지 않은 게시글입니다.", "/spring", "/Board/list");
		
		}else {
			mv.addObject("board",board);
			ArrayList<BoardTypeVO> btList = boardService.getBoardType(user.getMe_authority());
			mv.addObject("btList",btList);
			if(btList.size() == 0) {
				MessageUtils.alertAndMovePage(response, "권한이 없어서 작성할 수 있는 게시판이 없습니다.", "/spring", "/Board/list");
			}else
				mv.setViewName("/Board/update");
		}
		return mv;
	}
}
