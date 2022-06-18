package bbs;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import user.MemberDAO;
import common.*;

public class NovelDAO extends JDBConnect {
	public NovelDAO() {
	}

	// 검색 조건에 맞는 게시물의 개수를 반환합니다.
	public int getNext() {
		String SQL = "SELECT num FROM novel ORDER BY num DESC";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(int unum, String title, String introduce) {
		String SQL = "INSERT INTO novel(unum, title, introduce) VALUES (?, ?, ?)";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, unum);
			pstmt.setString(2, title);
			pstmt.setString(3, introduce);
			
			return pstmt.executeUpdate(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
		
	public ArrayList<NovelDTO> getList(int pageNumber) {
		String SQL = "SELECT * FROM novel WHERE num < ? ORDER BY num DESC LIMIT 10";
		ArrayList<NovelDTO> list = new ArrayList<NovelDTO>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				NovelDTO bbs = new NovelDTO();
				bbs.setNum(rs.getInt(1));
				bbs.setUnum(rs.getInt(2));
				bbs.setTitle(rs.getString(3));
				bbs.setIntroduce(rs.getString(4));
				list.add(bbs);
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) {
		boolean result = false;
		String SQL = "SELECT * FROM novel WHERE num > ?";
		
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public NovelDTO getBbs(int num) {
		String SQL = "SELECT * FROM novel WHERE num = ?";
		
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				NovelDTO bbs = new NovelDTO();
				bbs.setNum(rs.getInt(1));
				bbs.setUnum(rs.getInt(2));
				bbs.setTitle(rs.getString(3));
				bbs.setIntroduce(rs.getString(4));
				return bbs;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int num, String title, String introduce) {
		String SQL = "UPDATE novel SET title = ?, introduce = ? WHERE num =?";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, title);
			pstmt.setString(2, introduce);
			pstmt.setInt(3, num);
			
			return pstmt.executeUpdate(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

	public String searchNovel(String context) {
		String bbs = "";
		// 쿼리문 템플릿
		String query = "SELECT title FROM novel ";
		query += "WHERE num = (SELECT code FROM context WHERE title = ?)";

		query += "ORDER BY num DESC";

		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, context);
			
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				bbs = rs.getString("title");
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		// 목록 반환
		return bbs;
	}
	
	public List<String> searchNovel(int user) {
		List<String> bbs = new Vector<String>();
		// 쿼리문 템플릿
		String query = "SELECT novel.title FROM recent LEFT JOIN novel ON recent.novel = novel.num  WHERE recent.user = ? ORDER BY recent.num desc";

		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setInt(1, user);
			
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				bbs.add(rs.getString("title"));
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}

		// 목록 반환
		return bbs;
	}

	// 게시글 데이터를 받아 DB에 추가합니다.
	public int insertWrite(NovelDTO dto) {
		int result = 0;

		try {
			// INSERT 쿼리문 작성
			String query = "INSERT INTO novel ( " + " unum,title,introduce) " + " VALUES ( " + "  ?, ?, ?)";

			psmt = con.prepareStatement(query); // 동적 쿼리
			psmt.setInt(1, dto.getUnum());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getIntroduce());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
 
		return result;
	}

	// 지정한 게시물을 찾아 내용을 반환합니다.
	public NovelDTO novelData(String unum, String title, String introduce) {
		NovelDTO dto = new NovelDTO();
		MemberDAO cdao = new MemberDAO();
		int user = cdao.searchUserCode(unum);
		dto.setUnum(user);
		dto.setTitle(title);
		dto.setIntroduce(introduce);
		
		return dto;
	}
	
    public int searchCode(String novel) {
    	int code = 0;
    	String query = "select num from novel where title = ?";
    	try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, novel);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while(rs.next()) {
            	code = rs.getInt("num");
            }
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
    	return code;
    }
	
	// 지정한 게시물을 삭제합니다.
	public int deleteNovel(String novel) {
		int result = 0;        
        String query = "delete from novel where title = ? ";
        try {
            ContextDAO dao = new ContextDAO();
            dao.deleteContext(searchCode(novel));
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, novel);
            // 쿼리문 실행 
            result = psmt.executeUpdate();
            numbering();
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        return result; // 결과 반환
	}
	
	public void numbering() {      
        String query = "ALTER TABLE novel AUTO_INCREMENT=1";
        String query2 =  "SET @COUNT = 0";
        String query3 = "UPDATE novel SET novel.num = @COUNT:=@COUNT+1";
        try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
            psmt.executeUpdate();
            psmt = con.prepareStatement(query2);
            psmt.executeUpdate();
            psmt = con.prepareStatement(query3);
            psmt.executeUpdate();
    	} 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
	}
	
//	public void pageCopy() {
//		String query = "INSERT INTO novel(unum, title, introduce) VALUES (3, ?, ?)";
//
//		for (int i = 3; i < 10; i++) {
//			try {
//				String data = "novel" + i;
//				psmt = con.prepareStatement(query);
//				psmt.setString(1, data);
//				psmt.setString(2, data+" introduce");
//				psmt.executeUpdate();
//			} catch (Exception e) {
//				System.out.println("게시물 조회 중 예외 발생");
//				e.printStackTrace();
//			}
//		}
//	}
}
