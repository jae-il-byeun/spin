package kr.kh.spring.Utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {
	public static boolean removeFile(String uploadPath,String fileName) {
		fileName = fileName.replace("/",File.separator);
		File file = new File(uploadPath + fileName);
		if(file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	//서버에 파일을 업로드하고 업로드된 경로와 파일명이 합쳐진 문자열을 반환 ->최종
	public static String uploadFile(String uploadPath, String originalName, byte[] 	
			fileData)throws Exception{
		//UUID 생성(36개 문자(32개 문자와 4개의 하이픈-)의 고유한 문자열  UUID:(universally unique identifier, 범용 고유 식별자)
		//8-4-4-4-12
		UUID uid = UUID.randomUUID();
		//originalName: 원본 파일명
		//savedName : 서버에 저장될 파일명
		String savedName = uid.toString() +"_" + originalName;
		//savedPath : 서버에 저장할 경로 - 년/월/일 \2023\02\27 로 저장
		//uplodPath: 저장할 서버 풀더 위치 D:\A 로 저장
		//calcPath : 오늘날짜 폴더가 없으면 만들고 있으면 안만든다.
		String savedPath = calcPath(uploadPath);
		//빈 파일 생성 : 서버에 업로드 될 빈파일을 생성
		File target = new File(uploadPath + savedPath, savedName);
		// 복사를 해서 서버에 업로드
		FileCopyUtils.copy(fileData, target);
		//서버에 업로드된 파일의 경로와 파일명
		// /2023/02/27/uuid_flower.jpg =>fi_name으로 들어가는 값
		String uploadFileName = makeIcon(uploadPath, savedPath, savedName);
		return uploadFileName;
	}
	//uploadPath 안에 업로드한 날짜를 기준으로 폴더가 없으면, 생성, 있으면 건너뛰고
	// 날짜 경로를 만들어주는 메소드
	private static String calcPath(String uploadPath) {
		//현재 날짜 정보를 가져옴
		Calendar cal = Calendar.getInstance();
		// \\2023
		//window기반인 경우 \ 리눅스 기반인 경우는 /
		String yearPath = File.separator+cal.get(Calendar.YEAR);
		// \\2023\\02
		String monthPath = yearPath + File.separator 
            + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
					//자릿수 맞추는 메소드
		// \\2023\\02\\27
		String datePath = monthPath + File.separator 
            + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		// \\2023, \\2023\\02, \\2023\\02\\27를 이용하여 폴더를 생성
		makeDir(uploadPath, yearPath, monthPath, datePath);
		// \\2023\\02\\27를 리턴
		return datePath;
 
	}
	//폴더를 만드는 메소드                                    // ... : 가변인자
	private static void makeDir(String uploadPath, String... paths) {
		//DatePath를 기준으로 폴더가 존재하면 폴더를 만들필요가 없기 때문에 만들지 않는다.
		if(new File(paths[paths.length-1]).exists())
			return;
		for(String path : paths) {
			File dirPath = new File(uploadPath + path);
			//경로에 폴더가 존재하지 않으면 폴더를 생성
			if( !dirPath.exists())
				dirPath.mkdir();
		}
	}
	// \를 /로 바꾸는 메소드
	private static String makeIcon(String uploadPath, String path, String fileName)
        	throws Exception{
		// \\2023\\02\\27\\uuid_flower.jpg
		// 	=> //2023//02//27//uuid_flower.jpg
		String iconName =  path + File.separator + fileName;
		return iconName.replace(File.separatorChar, '/');
	}
}
