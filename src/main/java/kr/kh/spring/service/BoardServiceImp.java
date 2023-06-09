package kr.kh.spring.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.spring.Utils.UploadFileUtils;
import kr.kh.spring.dao.BoardDAO;
import kr.kh.spring.vo.BoardTypeVO;
import kr.kh.spring.vo.BoardVO;
import kr.kh.spring.vo.FileVO;
import kr.kh.spring.vo.LikesVO;
import kr.kh.spring.vo.MemberVO;

@Service
public class BoardServiceImp implements BoardService {
	
	@Autowired
	BoardDAO boardDao;
	
	String uploadPath = "D:\\uploadfiles";
	
	private boolean checkBoard(BoardVO board) {
		//게시글이 없거나, 게시글 제목이 비어있거나, 내용이 비어있으면
		if(board == null ||
				board.getBo_title() == null ||
				board.getBo_title().trim().length() == 0 ||
				board.getBo_content() == null ||
				board.getBo_content().trim().length() == 0)
					return false;
		return true;
	}
			
	private void uploadFiles(MultipartFile [] files,int bo_num) {
		//첨부파일 없을 시
				if(files ==null || files.length == 0 )
					return ;
				//반복문
				for(MultipartFile file : files) {
					if(file == null || file.getOriginalFilename().length()==0)
						continue;
					String fileName = "";
					//첨부파일 서버에 업로드
					try {
						fileName = UploadFileUtils.uploadFile(uploadPath,
								 file.getOriginalFilename(), //파일명
								 file.getBytes()); //실제 파일 데이터
					} catch(Exception e){
						e.printStackTrace();
					}
					
					//첨부파일 객체를 생성
					FileVO fileVo = new FileVO(file.getOriginalFilename(), fileName, bo_num);
					//DAO에게 첨부파일 정보를 주면서 추가하라고 요청
					boardDao.insertFile(fileVo);
				}
	}
	
	
	private void deleteDileList(ArrayList<FileVO> fileList) {
		if(fileList == null || fileList.size() == 0)
			return;
		for(FileVO file : fileList) {
			if(file == null) 
				continue;
			UploadFileUtils.removeFile(uploadPath, file.getFi_name());
			boardDao.deleteFile(file);			
		}
		
	}
	
	@Override
	public ArrayList<BoardTypeVO> getBoardType(int authority){
		//
		
		return boardDao.selectAllBoardType(authority);
		
	}
				
	@Override
	public boolean insertBoard(BoardVO board, MemberVO user, MultipartFile[] files) {
		//회원 정보가 없으면
		if(user == null)
			return false;
		
		//게시글에 빠진 항목이 있으면 false를 리턴
		if(!checkBoard(board))
			return false;
		
		board.setBo_me_id(user.getMe_id());
		
		//게시글 등록
		boardDao.insertBoard(board);
		
		uploadFiles(files, board.getBo_num());

		return true;
	}


	@Override
	public ArrayList<BoardVO> getBoardList() {
		
		return boardDao.selectBoardList();
	}

	@Override
	public BoardVO getBoard(int bo_num, MemberVO user) {
		
		//조회수 증가 (조회수 증가 먼저 다음 게시글가져오기)
		boardDao.updateBoardViews(bo_num);
		//게시글 가져오기
		BoardVO board = boardDao.selectBoard(bo_num);
		//권한확인
		if(board == null)
			return null;
		BoardTypeVO boardType = boardDao.selectBoardType(board.getBo_bt_num());
		//비회원 이상 읽기 가능
		if(boardType.getBt_r_authority() == 0)
				return board;
		//회원 이상인 경우 비회원은 못봄
		if(user == null)
			return null;
		//게시글 읽기 권한이 사용자 권한 이하인 경우만 조회가능
		if(boardType.getBt_r_authority() <= user.getMe_authority())
			return board;
		return null;
	}

	@Override
	public ArrayList<FileVO> getFileList(int bo_num) {
		
		return boardDao.selectFileList(bo_num);
	}

	@Override
	public int updateLikes(MemberVO user, int bo_num, int li_state) {
		// 기존에 추천/비추천을 했는지 확인
		LikesVO likesVo = boardDao.selectLikesById(user.getMe_id(),bo_num);
		//없으면 추가
		if(likesVo==null) {
			//LieksVO 객체를 생성하여 
			likesVo = new LikesVO(li_state, user.getMe_id(), bo_num);
			//DAO에게 전달해서 inseret하라고 시킴
			boardDao.insertLikes(likesVo);
			//bo_num를 리턴
			return li_state;
		}
		
		//있으면 수정
		if(li_state != likesVo.getLi_state()) {
			//현재 상태와 기존 상태가 다르면 => 상태를 바꿔야 한다.
			likesVo.setLi_state(li_state);
			//업데이트
			boardDao.updateLikes(likesVo);
			//bo_num를 리턴
			return li_state;
		}
			//현재 상태와 기존상태가 같으면 => 휘소
			likesVo.setLi_state(0);
			//업데이트
			boardDao.updateLikes(likesVo);
			//0을 리턴
			return 0;

	}

	@Override
	public LikesVO getLikes(int bo_num, MemberVO user) {
		if(user ==null)
			return null;
		
		return boardDao.selectLikesById(user.getMe_id(), bo_num);
	}

	@Override
	public boolean deleteBoard(int bo_num, MemberVO user) {
		if(user == null)
			return false;
		BoardVO board = boardDao.selectBoard(bo_num);
		if(board == null)
			return false;
		//로그인한 사용자와 작성자가 다르면
		if(!board.getBo_me_id().equals(user.getMe_id()))
			return false;
		ArrayList<FileVO> fileList = boardDao.selectFileList(bo_num);
		deleteDileList(fileList);
		return boardDao.deleteBoard(bo_num) != 0;
		
	}

	@Override
	public BoardVO getBoardByWriteAuthority(int bo_num, MemberVO user) {
		
		BoardVO board = boardDao.selectBoard(bo_num);
		
		if(board == null)
			return null;
		
		if(user == null)
			return null;
		
		if(user.getMe_id().equals(board.getBo_me_id()))
			return board;
		return null;
	}


}
