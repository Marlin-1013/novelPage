package bbs;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import user.MemberDAO;
import common.*;

public class ContextDAO extends JDBConnect {
	public ContextDAO() {
	}

	// 검색 조건에 맞는 게시물의 개수를 반환합니다.
	public int getNext(int code) {
		String SQL = "SELECT num FROM (select ROWNUM() AS row, code,  title, context, visitcount from context where context.code = ?) co"
					+ " ORDER BY row DESC";
		try {
			psmt = con.prepareStatement(SQL);
			psmt.setInt(1, code);
			rs = psmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("row") + 1;
			}
			return 1; // 첫 번째 게시물인 경우
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(int code, String title, String context) {
		String SQL = "INSERT INTO context(code, title, context, visitcount) VALUES (?, ?, ?, 0)";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, code);
			pstmt.setString(2, title);
			pstmt.setString(3, context);
			
			return pstmt.executeUpdate(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
		
	public ArrayList<ContextDTO> getList(int pageNumber, int bsnum) {
		String SQL = "SELECT * FROM (select ROWNUM() AS row, code,  title, context, visitcount from context where context.code = ?) co" +
					" WHERE row > ? ORDER BY row LIMIT 10";
		ArrayList<ContextDTO> list = new ArrayList<ContextDTO>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, bsnum);
			pstmt.setInt(2, (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			int row = 1;
			while (rs.next()) {
				ContextDTO bbs = new ContextDTO();
				bbs.setNum(rs.getInt(1));
				bbs.setCode(rs.getInt(2));
				bbs.setTitle(rs.getString(3));
				bbs.setContext(rs.getString(4));
				bbs.setVisitcount(rs.getInt(5));
				bbs.setRownum(row);
				list.add(bbs);
				row++;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<ContextDTO> getList(int num) {
		String SQL = "SELECT * FROM context WHERE code = ?";
		
		ArrayList<ContextDTO> list = new ArrayList<ContextDTO>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			int row = 1;
			while (rs.next()) {
				ContextDTO bbs = new ContextDTO();
				bbs.setNum(rs.getInt(1));
				bbs.setCode(rs.getInt(2));
				bbs.setTitle(rs.getString(3));
				bbs.setContext(rs.getString(4));
				bbs.setVisitcount(rs.getInt(5));
				bbs.setRownum(row);
				list.add(bbs);
				row++;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber, int bsnum) {
		boolean result = false;
		String SQL = "SELECT * FROM (select ROWNUM() AS row, code,  title, context, visitcount from context where context.code = ?) co"
					+" WHERE row > ?";
		
		try {
			psmt = con.prepareStatement(SQL);
			psmt.setInt(1, bsnum);
			psmt.setInt(2, (pageNumber -1) * 10);
			rs = psmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public ContextDTO getBbs(int num) {
		String SQL = "SELECT * FROM context WHERE num = ?";
		
		try {
			psmt = con.prepareStatement(SQL);
			psmt.setInt(1, num);
			rs = psmt.executeQuery();
			if (rs.next()) {
				ContextDTO bbs = new ContextDTO();
				bbs.setNum(rs.getInt(1));
				bbs.setCode(rs.getInt(2));
				bbs.setTitle(rs.getString(3));
				bbs.setContext(rs.getString(4));
				bbs.setVisitcount(rs.getInt(5));
				return bbs;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ContextDTO getBbs(int code, int num) {
		String SQL = "SELECT * FROM (select ROWNUM() AS row, code,  title, context, visitcount from context where context.code = ?) co"
					+" WHERE row = ?";
		
		try {
			psmt = con.prepareStatement(SQL);
			psmt.setInt(1, code);
			psmt.setInt(2, num);
			rs = psmt.executeQuery();
			if (rs.next()) {
				ContextDTO bbs = new ContextDTO();
				bbs.setNum(rs.getInt(1));
				bbs.setCode(rs.getInt(2));
				bbs.setTitle(rs.getString(3));
				bbs.setContext(rs.getString(4));
				bbs.setVisitcount(rs.getInt(5));
				return bbs;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int code, int num, String title, String context) {
		String SQL = "UPDATE context SET title = ?, context = ? WHERE num =?";
		try {
			int cnum = checkNum(code, num);
			psmt = con.prepareStatement(SQL);
			psmt.setString(1, title);
			psmt.setString(2, context);
			psmt.setInt(3, cnum);
			
			return psmt.executeUpdate(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int delete(int code, int num) {
		String SQL = "DELETE FROM context WHERE num =?";
		try {
			int cnum = checkNum(code, num);
			psmt = con.prepareStatement(SQL);
			psmt.setInt(1, cnum);
			
			int result = psmt.executeUpdate();
			new CommentDAO().deleteComment(cnum);
			numbering();
			return result;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

	// 게시글 데이터를 받아 DB에 추가합니다.
	public int insertWrite(ContextDTO dto) {
		int result = 0;

		try {
			// INSERT 쿼리문 작성
			String query = "INSERT INTO context ( " + " code,title,context,visitcount) " + " VALUES ( "
					+ "  ?, ?, ?, 0)";

			psmt = con.prepareStatement(query); // 동적 쿼리
			psmt.setInt(1, dto.getCode());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getContext());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}

		return result;
	}

	// 지정한 게시물을 수정합니다.
	public int updateEdit(ContextDTO dto) {
		int result = 0;

		try {
			// 쿼리문 템플릿
			String query = "UPDATE context SET " + " title=?, context=? " + " WHERE num=?";

			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContext());
			psmt.setInt(3, dto.getNum());

			// 쿼리문 실행
			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생");
			e.printStackTrace();
		}

		return result; // 결과 반환
	}

	// 지정한 게시물을 찾아 내용을 반환합니다.
	public ContextDTO contextData(String novel, String title, String context) {
		ContextDTO dto = new ContextDTO();
		NovelDAO cdao = new NovelDAO();
		int code = cdao.searchCode(novel);

		// 쿼리문 준비
		String query = "SELECT num " + " FROM context " + " WHERE title=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, title); // 인파라미터를 일련번호로 설정
			rs = psmt.executeQuery(); // 쿼리 실행

			// 결과 처리
			if (rs.next()) {
				dto.setNum(rs.getInt("num"));
			}
			dto.setCode(code);
			dto.setTitle(title);
			dto.setContext(context);
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	// 지정한 게시물의 조회수를 1 증가시킵니다.
	public void updateVisitCount(String title) {
		// 쿼리문 준비
		String query = "UPDATE context SET visitcount=visitcount+1 WHERE title=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, title);

			// 쿼리문 실행
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
	}

	public int searchCode(String context) {
		int code = 0;
		String query = "select num from context where title = ?";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, context);
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				code = rs.getInt("num");
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return code;
	}
	
	public int checkNum(int code, int num) {
		int result = 0;
		String query = "select num FROM (SELECT num, ROWNUM() row FROM context WHERE CODE=?) co WHERE co.row=?";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
			psmt.setInt(2, num);
			rs = psmt.executeQuery();
			rs.next();
			result = rs.getInt("num");
			} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}

	// 지정한 게시물을 삭제합니다.
	public int deleteContext(String context) {
		int result = 0;
		String query = "delete from context where title = ? ";
		try {
			CommentDAO dao = new CommentDAO();
			dao.deleteComment(searchCode(context));
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, context);
			// 쿼리문 실행
			result = psmt.executeUpdate();
			numbering();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result; // 결과 반환
	}

	public List<Integer> deleteList(int code) {
		List<Integer> result = new Vector<Integer>();
		String query = "select num from context where code = ?";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
			// 쿼리문 실행
			rs = psmt.executeQuery();

			while (rs.next()) {
				result.add(rs.getInt("num"));
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result; // 결과 반환
	}

	public int deleteContext(int code) {
		int result = 0;
		String query = "delete from context where code = ?";
		try {
			CommentDAO dao = new CommentDAO();
			List<Integer> cList = deleteList(code);
			for (int i : cList) {
				dao.deleteComment(i);
			}
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
			// 쿼리문 실행
			result = psmt.executeUpdate();
			numbering();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		return result; // 결과 반환
	}

	public void numbering() {
		String query = "ALTER TABLE context AUTO_INCREMENT=1";
		String query2 = "SET @COUNT = 0";
		String query3 = "UPDATE context SET context.num = @COUNT:=@COUNT+1";
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.executeUpdate();
			psmt = con.prepareStatement(query2);
			psmt.executeUpdate();
			psmt = con.prepareStatement(query3);
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
	}
	
//	public void pageCopy() {
//		String query = "INSERT INTO context(code, title, context, visitcount) VALUES (4, ?, ?, 0)";
//
//		for (int i = 4; i < 20; i++) {
//			try {
//				String data = "page" + i;
//				psmt = con.prepareStatement(query);
//				psmt.setString(1, data);
//				psmt.setString(2, data);
//				psmt.executeUpdate();
//			} catch (Exception e) {
//				System.out.println("게시물 조회 중 예외 발생");
//				e.printStackTrace();
//			}
//		}
//	}
	
}