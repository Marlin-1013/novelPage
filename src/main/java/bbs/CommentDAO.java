package bbs;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import user.MemberDAO;
import user.MemberDTO;
import common.*;

public class CommentDAO extends JDBConnect {
    public CommentDAO() {
    }

    // 검색 조건에 맞는 게시물의 개수를 반환합니다.
    public int selectCount(String name) {
        int totalCount = 0; // 결과(게시물 수)를 담을 변수

        // 게시물 수를 얻어오는 쿼리문 작성
        String query = "SELECT COUNT(*) FROM comment where code = (select num from context where title = ?)";

        try {
            psmt = con.prepareStatement(query);   // 쿼리문 생성
            psmt.setString(1, name);
            rs = psmt.executeQuery();  // 쿼리 실행
            rs.next();  // 커서를 첫 번째 행으로 이동
            totalCount = rs.getInt(1);  // 첫 번째 칼럼 값을 가져옴
        }
        catch (Exception e) {
            System.out.println("게시물 수를 구하는 중 예외 발생");
            e.printStackTrace();
        }

        return totalCount; 
    }
    
    // 검색 조건에 맞는 게시물 목록을 반환합니다.
    public List<CommentDTO> selectList(String input) { 
    	List<CommentDTO> bbs = new Vector<CommentDTO>();  // 결과(게시물 목록)를 담을 변수
        // 쿼리문 템플릿  
        String query = "SELECT * FROM comment ";
            query += "WHERE code = (select num from context where title = ?)";
        
        query += "ORDER BY num";
        
        try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setString(1, input);
            // 쿼리문 실행 
            rs = psmt.executeQuery();
            
            while (rs.next()) {
                // 한 행(게시물 하나)의 데이터를 DTO에 저장
                CommentDTO dto = new CommentDTO();
                dto.setNum(rs.getInt("num"));         		 // 일련번호
                dto.setCode(rs.getInt("code"));     		 // 제목
                dto.setTalker(rs.getInt("talker")); 		 // 작성자
                dto.setComment(rs.getString("comment"));  		// 내용

                // 반환할 결과 목록에 게시물 추가
                bbs.add(dto);
            }
        } 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        
        // 목록 반환
        return bbs;
    }

    // 게시글 데이터를 받아 DB에 추가합니다. 
    public int insertWrite(CommentDTO dto) {
        int result = 0;
        
        try {
            // INSERT 쿼리문 작성 
            String query = "INSERT INTO comment ( "
                         + " code,talker,comment) "
                         + " VALUES ( "
                         + "  ?, ?, ?)";  

            psmt = con.prepareStatement(query);  // 동적 쿼리 
            psmt.setInt(1, dto.getCode());  
            psmt.setInt(2, dto.getTalker());
            psmt.setString(3, dto.getComment());  
            
            result = psmt.executeUpdate(); 
        }
        catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        
        return result;
    }
    
    public CommentDTO commentData(String context, String user, String comment) {
    	CommentDTO dto = new CommentDTO();
    	ContextDAO cdao = new ContextDAO();
    	MemberDAO mdao = new MemberDAO();
    	int code = cdao.searchCode(context);
    	int talker = mdao.searchUserCode(user);
    	dto.setCode(code);
    	dto.setTalker(talker);
    	dto.setComment(comment);
    	
    	return dto;
    }
    
    public int deleteComment(int code) { 
    	int result = 0;        
        String query = "delete from comment where code = ?";
        try {
            // 쿼리문 완성 
            psmt = con.prepareStatement(query);
			psmt.setInt(1, code);
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
        String query = "ALTER TABLE comment AUTO_INCREMENT=1";
        String query2 =  "SET @COUNT = 0";
        String query3 = "UPDATE comment SET comment.num = @COUNT:=@COUNT+1";
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
}
