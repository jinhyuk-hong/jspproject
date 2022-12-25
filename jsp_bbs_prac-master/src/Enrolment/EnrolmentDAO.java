package Enrolment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



public class EnrolmentDAO {
	private Connection conn;
	private ResultSet rs;
	
	public EnrolmentDAO() {
		//UserDAO 객체가 생성될때 바로 데이터베이스 접근하도록 생성자 설정
		try {
			String dbURL = "jdbc:mysql://localhost:3306/jinhyuk";
			String dbID = "jinhyuk";
			String dbPassword = "12345";
			
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
			String SQL = "select stuID from enrolment order by stuID desc";
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
		public int write(String gradeName,String userID, String stuList) {
			String SQL = "insert into enrolment values (?, ?, ?, ?,?,?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext());
				pstmt.setString(2, gradeName);
				pstmt.setString(3, userID);
				pstmt.setString(4, stuList);
				pstmt.setString(5, getDate());
				pstmt.setInt(6, 1);
				return pstmt.executeUpdate(); // 0 이상의 결과 반환
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // DB 오류
		}
		
		
		// 글목록
		public ArrayList<Enrolment> getList(int pageNumber){
			String SQL = "select * from enrolment where stuID < ? and stuAvailable = 1 order by stuID desc limit 10";
			ArrayList<Enrolment> list = new ArrayList<Enrolment>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					Enrolment ent = new Enrolment();
					ent.setStuID(rs.getString(1));
					ent.setGradeName(rs.getString(2));
					ent.setUserID(rs.getString(3));
					ent.setStuList(rs.getString(4));
					ent.setStuDate(rs.getString(5));
					ent.setStuAvailable(rs.getInt(6));
				
					
					list.add(ent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		// 페이지 존재 유무
		public boolean nextPage(int pageNumber) {
			String SQL = "select * from enrolment where stuID < ? and stuAvailable = 1 order by stuID desc limit 10";
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
		public Enrolment getstu(int evaID) {
			String SQL = "select * from enrolment where stuID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, evaID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					Enrolment ent = new Enrolment();
					ent.setStuID(rs.getString(1));
					ent.setGradeName(rs.getString(2));
					ent.setUserID(rs.getString(3));
					ent.setStuList(rs.getString(4));
					ent.setStuDate(rs.getString(5));
					ent.setStuAvailable(rs.getInt(6));
					
					return ent;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public int update(String stuID, String gradeName, String stuList) {
			String SQL = "update enrolment set gradeName = ?, stuList = ? where stuID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, gradeName);
				pstmt.setString(2, stuList);
				pstmt.setString(3, stuID);
				return pstmt.executeUpdate(); // 0 이상의 결과 반환
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // DB 오류
		}
		
		public int delete(int stuID) {
			String SQL = "delete from enrolment where stuID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, stuID);
				return pstmt.executeUpdate(); // 0 이상의 결과 반환
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // DB 오류
		}
}
