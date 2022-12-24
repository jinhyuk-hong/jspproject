package evaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EvaDAO {

	private Connection conn;
	private ResultSet rs;
	
	public EvaDAO() {
		//UserDAO 객체가 생성될때 바로 데이터베이스 접근하도록 생성자 설정
		try {
			String dbURL = "jdbc:mysql://127.0.0.1:3306/eva";
			String dbID = "root";
			String dbPassword = "0000";
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL,  dbID, dbPassword);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// getDate 현재시간 가져오기
	public String getDate() {
		String SQL = "select now()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // DB 오류
	}
	
	// 다음글
	public int getNext() {
		String SQL = "select evaID from eva order by evaID desc";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	// 글쓰기 정보 -> DB에 추가
	public int write(String evaTitle, String userID, String evaContent) {
		String SQL = "insert into eva values (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, evaTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, evaContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate(); // 0 이상의 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	
	// 글목록
	public ArrayList<Eva> getList(int pageNumber){
		String SQL = "select * from eva where evaID < ? and evaAvailable = 1 order by evaID desc limit 10";
		ArrayList<Eva> list = new ArrayList<Eva>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Eva eva = new Eva();
				eva.setEvaID(rs.getString(1));
				eva.setEvaTitle(rs.getString(2));
				eva.setEvaSubject(rs.getString(3));
				eva.setEvaTeacher(rs.getString(4));
				eva.setEvaDate(rs.getString(5));
				eva.setEvaContent(rs.getString(6));
				eva.setEvaScore(rs.getString(7));
				eva.setEvaAvailable(rs.getString(8));
				
				list.add(eva);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 페이지 존재 유무
	public boolean nextPage(int pageNumber) {
		String SQL = "select * from eva where evaID < ? and evaAvailable = 1 order by evaID desc limit 10";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10); //게시글 갯수 -> 페이지수 계산
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 글 읽기
	public Eva geteva(int evaID) {
		String SQL = "select * from eva where evaID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, evaID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Eva eva = new Eva();
				eva.setEvaID(rs.getString(1));
				eva.setEvaTitle(rs.getString(2));
				eva.setEvaSubject(rs.getString(3));
				eva.setEvaTeacher(rs.getString(4));
				eva.setEvaDate(rs.getString(5));
				eva.setEvaContent(rs.getString(6));
				eva.setEvaScore(rs.getString(7));
				eva.setEvaAvailable(rs.getString(8));
				return eva;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int evaID, String evaTitle, String evaContent) {
		String SQL = "update eva set evaTitle = ?, evaContent = ? where evaID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, evaTitle);
			pstmt.setString(2, evaContent);
			pstmt.setInt(3, evaID);
			return pstmt.executeUpdate(); // 0 이상의 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	public int delete(int evaID) {
		String SQL = "update eva set evaAvailable = 0 where evaID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, evaID);
			return pstmt.executeUpdate(); // 0 이상의 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
}
