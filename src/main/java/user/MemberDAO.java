package user;

import common.*;

public class MemberDAO extends JDBConnect {
    // 명시한 데이터베이스로의 연결이 완료된 MemberDAO 객체를 생성합니다.
	public MemberDAO() {
		super();
	}
	
    public MemberDAO(String drv, String url, String id, String pw) {
        super(drv, url, id, pw);
    }
    
    //로그인
    public int login(String uid, String upw) {
		String SQL = "SELECT upw FROM user WHERE uid = ?";
		try {
			psmt = con.prepareStatement(SQL);
			psmt.setString(1, uid);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				if (rs.getString(1).equals(upw)) {
					return 1; // 로그인 성공
				} else {
					return 0; // 비밀번호 불일치
				}
			} 
			return -1; // 아이디가 없음
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return -2; // 데이터베이스 오류
	}
    
    public int join(MemberDTO user) {
		String SQL = "INSERT INTO user(uid, upw, uname) VALUES (?, ?, ?)";
		try {
			psmt = con.prepareStatement(SQL);
			psmt.setString(1, user.getUid());
			psmt.setString(2, user.getUpw());
			psmt.setString(3, user.getUname());
			
			return psmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
    
    public String pwSearch(String name, String id) {
    	String query = "SELECT * FROM user WHERE uid=?";
    	try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("uname").equals(name)) {
					
					return rs.getString("upw");
				}
				else return "2";
			}
			else return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "3";
		}	
    }
    
    public String searchUser(String name) {
    	String query = "SELECT * FROM user WHERE num= (select unum from novel where title = ?)";
    	try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, name);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("uname");
			}
			else return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "3";
		}	
    }
    
    public String searchUser(int num) {
    	String query = "SELECT * FROM user WHERE num= (select unum from novel where num = ?)";
    	try {
			psmt = con.prepareStatement(query);
			psmt.setInt(1, num);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("uname");
			}
			else return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "3";
		}	
    }
    
    public int searchUserCode(String user) {
    	int talker = 0;
    	String query = "select num from user where uname = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, user);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	talker = rs.getInt("num");
            }
    	} 
        catch (Exception e) {
            System.out.println("유저 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return talker;
    }
    
    public int searchUcode(String id) {
    	int talker = 0;
    	String query = "select num from user where uid = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, id);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	talker = rs.getInt("num");
            }
    	} 
        catch (Exception e) {
            System.out.println("유저 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return talker;
    }
    
    public String checkUser(int code) {
    	String talker = "";
    	String query = "select uname from user where num = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	talker = rs.getString("uname");
            }
    	} 
        catch (Exception e) {
            System.out.println("유저 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return talker;
    }
    
    public String checkUserId(String name) {
    	String talker = "";
    	String query = "select uid from user where uname = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, name);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	talker = rs.getString("uid");
            }
    	} 
        catch (Exception e) {
            System.out.println("유저 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return talker;
    }
    
    public String checkUsername(String id) {
    	String talker = "";
    	String query = "select uname from user where uid = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, id);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	talker = rs.getString("uname");
            }
    	} 
        catch (Exception e) {
            System.out.println("유저 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return talker;
    }
}
