package kr.kh.spring.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.kh.spring.service.MemberService;
import kr.kh.spring.vo.MemberOKVO;
import kr.kh.spring.vo.MemberVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping(value = {"/home","/"}) 
	//메인화면과 홈화면은 다른의미 나오는 화면 자체는 같을 수 있어도 기능은 다른 의미이다.
	//그러므로 홈이랑 메인이랑 두개다 될 수 있게 구현해야한다. url 또는 홈페이지 경로를 적어야한다.
	public ModelAndView home(ModelAndView mv) {
		mv.setViewName("/main/home");
		System.out.println(mv);
		return mv;
	}
	@RequestMapping(value = "/signup", method=RequestMethod.GET)
	public ModelAndView signup(ModelAndView mv) {
		mv.setViewName("/member/signup");
		return mv;
	}
	@RequestMapping(value = "/signup", method=RequestMethod.POST)
	public ModelAndView signupPost(ModelAndView mv, MemberVO member) {
		boolean isSignup = memberService.signup(member);
		if(isSignup) {
			
			//아이디가 주어지면 주어진 아이디의 인증 번호를 발급하고, 
			//발급한 인증 번호를 DB에 저장하고, 이메일로 인증 번호가 있는 링크를 전송하는 기능
			memberService.emailAuthentication(member.getMe_id(), member.getMe_email());
			mv.setViewName("redirect:/");
		}else {
			mv.setViewName("redirect:/signup");
		}
		return mv;
	}
	
	@RequestMapping(value = "/email", method=RequestMethod.GET)
	public ModelAndView email(ModelAndView mv,MemberOKVO mok) {
		//System.out.println("인증 정보 : " + mok); 코딩이 잘못되서 가능한 코딩인지 아니니지 파악함
		if(memberService.emailAuthenticationConfirm(mok)) {
			//System.out.println("인증 성공");
		}else{
			//System.out.println("인증 실패");
		}
		mv.setViewName("redirect:/");
		return mv;
	}
	
	@RequestMapping(value = "/login", method=RequestMethod.GET)
	public ModelAndView login(ModelAndView mv) {
		
		mv.setViewName("/member/login");
		return mv;
	}
	@RequestMapping(value = "/login", method=RequestMethod.POST)
	public ModelAndView loginPost(ModelAndView mv,MemberVO member) {
		MemberVO user = memberService.login(member);
	
		mv.addObject("user",user);
		if(user != null)
			mv.setViewName("redirect:/");
		else
			mv.setViewName("redirect:/login");
	
		return mv;
	}
	@RequestMapping(value = "/logout", method=RequestMethod.GET)
	public ModelAndView logout(ModelAndView mv,
			HttpSession session,
			HttpServletResponse response) throws IOException {
		MemberVO user = (MemberVO)session.getAttribute("user");//객체로 받기 때문에 자동 형변환이 안되어서 MemberVO로 지정해줘야함
		if(user != null) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('로그아웃되었습니다.');location.href='/spring/'</script>");
			out.flush();
		}
		//세션에 있는 회원정보를 삭제
		session.removeAttribute("user");
		mv.setViewName("redirect:/");
		return mv;
	}
	
	@RequestMapping(value = "/ex1")
	public ModelAndView ex1(ModelAndView mv,String name,Integer age) {
		System.out.println("예제1 - 화면에서 전달한 이름 : " + name);
		System.out.println("예제1 - 화면에서 전달한 나이 : " + age);
		mv.setViewName("/main/ex1");
		return mv;
	}
	
	@RequestMapping(value = "/ex2")
	public ModelAndView ex2(ModelAndView mv,String name,Integer age) {
		System.out.println("예제2 - 화면에서 전달한 이름 : " + name);
		System.out.println("예제2 - 화면에서 전달한 나이 : " + age);
		mv.setViewName("/main/ex2");
		return mv;
	}
	
	@RequestMapping(value = "/ex3")
	public ModelAndView ex3(ModelAndView mv) {
		
		mv.setViewName("/main/ex3");
		return mv;
	}
	@RequestMapping(value = "/ex3{name}/{age}")
	public ModelAndView exNameAge3(ModelAndView mv,@PathVariable("name")String name,@PathVariable("age")int age) {
		System.out.println("예제3 - 화면에서 전달한 이름 : " + name);
		System.out.println("예제3 - 화면에서 전달한 나이 : " + age);
		mv.setViewName("/main/ex3");
		return mv;
	}
	@RequestMapping(value = "/ex4")//
	public ModelAndView ex4(ModelAndView mv) {
		/*서버에서 화면으로 이름과 나이를 전송
		 * 화면에서 호출할 이름(변수명)과 값을 지정
		 * addObject(메소드)를 통해서 전달
		 * */
		mv.addObject("name","Emma");
		mv.addObject("age","20");
		mv.setViewName("/main/ex4");
		return mv;
	}
	
	/*
	@RequestMapping(value = "/ex5")//
	public ModelAndView ex5(ModelAndView mv,String num) {
		String name= memberService.getNameByNum(num);
		mv.addObject("name",name);
		mv.addObject("num",num);
		mv.setViewName("/main/ex5");
		return mv;
	}
	*/
	//404인 경우 representation은 url이 없는거 다른건 jsp가 없는 경우 오류가 난다.
	//405인 경우 타입 즉 POST 나 GET이 맞지 않는 경우 발생
	//500인 경우 sql이나 script오류인 경우가 있다. 일단 오류난 원인을 잘봐야한다. 오타일 확률이 높다.
	//8080인 경우 cmd창->netstat -ano->로컬ip에 8080을 찾고 -> PID 값을 -> 작업관리자-세부정보 -> 같은 PID값을 찾고 -> 끝내기를 한다. 먼저 실행되던 서버가 안끝났기 때문에 서버를 강제종료하는 느낌
	//baseLayout 오류가 뜨면 오류창 맨 밑에 근본원인 첫번째 줄을 확인하면 된다.
	
	/*1일차
	@RequestMapping(value = "/"/*, method = RequestMethod.GET)
	//GET인지 POST에 따라 처리하는 파일이 달라진다. GET은 GET POST는 POST만 처리 하고 
	// GET을 많이 쓰고(URL에 원하는 변수들을 넣어줄 수 있다.) method를 지우면 둘다 쓸수 있다.
	public ModelAndView home(ModelAndView mv, String name,Integer age) {
		// ?매개변수가 안들어가면 null로 반환 꼭 안들어갈 수 있으면 레퍼클래스를 사용(Integer) #int는 널을 받아들이지 못하므로 에러가 난다.
		//홈페이지 운영에는 래퍼클래스를 사용하는 것이 안정적이다.
		//많은 양의 데이터 URL에 보여지면 안되는 정보들을 있을 때 POST를 사용한다.
		mv.addObject("name1",name);
		//(name1,name) 화면에서 불릴 이름 과 불러올 이름
		mv.addObject("age",age);
		
		mv.setViewName("home");
		return mv;
	}
	@RequestMapping(value = "/board/{num}")//경로를 넘겨주고 싶을 때는 {}를 사용한다.
	public ModelAndView board(ModelAndView mv,@PathVariable("num")Integer num1) {
		System.out.println("게시글 번호 : " + num1);
		mv.setViewName("home");
		return mv;
	}
	
	@RequestMapping(value = "/test")
	public ModelAndView test(ModelAndView mv,InfoVO info) {//여러가지 세트로 보내줄 때는 VO를 쓴다.
		mv.addObject("info1",info);
		mv.setViewName("home2");
		return mv;
		//URL 입력 값
		//http://localhost:8080/spring/test?name=Herrypotter&num=21 
	}
	@RequestMapping(value = "/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView mv,login id,login passWord) {
		mv.addObject("login",id);
		mv.addObject("login",passWord);
		mv.setViewName("login");
		return mv;

	}
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	public ModelAndView loginPost(ModelAndView mv,String id, String pw) {
		System.out.println(id);
		System.out.println(pw);
		mv.setViewName("login");
		return mv;
	}
	*/
}
/*
 * MVC
 * M: model 데이터를 주고 받을 때 쓰는 여갛ㄹ
 * V: view jsp들로 화면을 보여주는 역할
 * C: controller 
 * 
 * dispatcherServlet url을 처리할 수 있을지 없을지를 판단하는 역할
 * 
 * */
 