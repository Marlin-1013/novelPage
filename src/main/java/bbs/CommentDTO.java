package bbs;

public class CommentDTO {
	
	private int num;
	private int code;
	private int talker;
	private String comment;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getTalker() {
		return talker;
	}
	public void setTalker(int talker) {
		this.talker = talker;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
