package bit.study.main;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

@RestController
public class VoiceController {
	
	// 해당 언어의 목소리로 읽어주는 서비스 (mp3 파일로 만들어줌)
	@GetMapping("/voice")
	public String sendVoice(String msg,String lang,HttpServletRequest request)
	{
		String jsonData = "";
		
		//mp3파일 업로드할 위치 지정
		String path = request.getSession().getServletContext().getRealPath("/"); //webapp에 저장됨
		
		 String clientId = "5j9ec7irfu";//애플리케이션 클라이언트 아이디값";
	        String clientSecret = "td1w0H4e7KXVGo4b9bBobpJhtRuEYO9yz58pPqzz";//애플리케이션 클라이언트 시크릿값";
	        try {
	            String text = URLEncoder.encode(msg, "UTF-8"); // 13자
	            String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
	            URL url = new URL(apiURL);
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("POST");
	            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
	            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
	            // post request
	            String naraVoice = "";
	            
	            if(lang.equals("ko"))
	            	naraVoice = "nminyoung";
	            else if(lang.equals("en"))
	            	naraVoice = "deriko";
	            else if(lang.equals("ja"))
	            	naraVoice = "meimei";
	            else if(lang.equals("zh-CN"))
	            	naraVoice = "nminyoung";
	            else if(lang.equals("es"))
	            	naraVoice = "carmen";
	            else if(lang.equals("fr"))
	            	naraVoice = "nmeow";
	            
	            String postParams = "speaker="+naraVoice+"&volume=0&speed=0&pitch=0&format=mp3&text=" + text;
	            con.setDoOutput(true);
	            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	            wr.writeBytes(postParams);
	            wr.flush();
	            wr.close();
	            int responseCode = con.getResponseCode();
	            BufferedReader br;
	            if(responseCode==200) { // 정상 호출
	                InputStream is = con.getInputStream();
	                int read = 0;
	                byte[] bytes = new byte[1024];
	                // 랜덤한 이름으로 mp3 파일 생성
	                String tempname = Long.valueOf(new Date().getTime()).toString();
	                File f = new File(path+"/"+tempname + ".mp3"); // webapp에 mp3 파일이 저장
	                f.createNewFile();
	                OutputStream outputStream = new FileOutputStream(f);
	                while ((read =is.read(bytes)) != -1) {
	                    outputStream.write(bytes, 0, read);
	                }
	                is.close();
	                // 저장된 파일명을 반환
	                jsonData = f.getName();
	            } else {  // 오류 발생
	                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	                String inputLine;
	                StringBuffer response = new StringBuffer();
	                while ((inputLine = br.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                br.close();
	                System.out.println(response.toString());
	            }
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		
		
		return jsonData;
	}

}
