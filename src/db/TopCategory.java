/*
 * topcategory 레코드 1건을 담는 VO 객체
 */
package db;

public class TopCategory {
	
	private int  toqcategory_id;
	private String top_name;
	
	public TopCategory() {
		// TODO Auto-generated constructor stub
	}

	public int getToqcategory_id() {
		return toqcategory_id;
	}

	public void setToqcategory_id(int toqcategory_id) {
		this.toqcategory_id = toqcategory_id;
	}

	public String getTop_name() {
		return top_name;
	}

	public void setTop_name(String top_name) {
		this.top_name = top_name;
	}

	
	

}
