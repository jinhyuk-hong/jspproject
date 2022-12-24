package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
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
	
	public int login(String userID, String userPassword) {
		String SQL = "select userPassword from user where userID= ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; // 로그인 성공
				} else {
					return 0; // 비밀번호 불일치
				}
			}
			return -1; // 아이디 없음
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -2; // DB오류
	}
	
	public int join(User user) {
		String SQL = "insert into user values (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  user.getUserID());
			pstmt.setString(2,  user.getUserPassword());
			pstmt.setString(3,  user.getUserName());
			pstmt.setString(4,  user.getUserGender());
			pstmt.setString(5,  user.getUserEmail());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	// 내정보 가져오기
	public User getUser(String userID){
		String SQL = "select * from user where userID = ? ";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  userID);
			rs = pstmt.executeQuery();
			// select * from user where userID = 'aa';
			if(rs.next()) {
				User user = new User();
				user.setUserID(rs.getString(1));
				user.setUserPassword(rs.getString(2));
				user.setUserName(rs.getString(3));
				user.setUserGender(rs.getString(4));
				user.setUserEmail(rs.getString(5));
				
				return user;
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(User user) {
		String SQL = "update user set userPassword = ?, userName = ?, userGender = ?, userEmail = ? where userID = ?"; //id에 맞는것만 찾아서 바꿈
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  user.getUserPassword());
			pstmt.setString(2,  user.getUserName());
			pstmt.setString(3,  user.getUserGender());
			pstmt.setString(4,  user.getUserEmail());
			pstmt.setString(5,  user.getUserID());
			return pstmt.executeUpdate(); // 0 이상의 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	public int delete(String userID) {
		String SQL = "delete from user where userID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			return pstmt.executeUpdate(); // 0 이상의 결과 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
}
