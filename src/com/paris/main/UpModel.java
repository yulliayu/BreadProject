/*
 * 하위 카테고리와 그 카테고리에 등록된 상품의 수 정보를 제공하는 모델.
 */
package com.paris.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class UpModel extends AbstractTableModel{
	
	Connection con;
	Vector<String> columnName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	
	public UpModel(Connection con) {
		this.con = con;
		getList();
	}
	
	// 목록 가져오기
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		StringBuffer  sql = new StringBuffer();
		sql.append("select s.subcategory_id as subcategory_id, s.sub_name as 카테고리명, count(p.product_id)  as 갯수 ");
		sql.append(" from subcategory s left outer join product p ");
		sql.append(" on    s.subcategory_id = p.subcategory_id ");
		sql.append(" group by s.subcategory_id, s.sub_name");
		sql.append(" order by s.subcategory_id ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			// 백터들을 초기화
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			// 컬럼명 추출
			ResultSetMetaData  meta = rs.getMetaData();
			for (int i=1; i<=meta.getColumnCount(); i++){
				columnName.add(meta.getColumnName(i));  // 컬럼명은 1 부터 시작.
			}
			
			while (rs.next()){
				// 레코드 한건을 vector 에 옮겨 심자.
				// 여기서 백터는 DTO 역할
				Vector vec=new Vector();
				vec.add(rs.getString("subcategory_id"));
				vec.add(rs.getString("카테고리명"));
				vec.add(rs.getString("갯수"));
				
				data.add(vec);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public int getColumnCount() {
		System.out.println("UpModel - getColumnCount = "+columnName.size());
		return columnName.size();
	}
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}

	public int getRowCount() {
		System.out.println("UpModel - getRowCount = "+data.size());
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		Object value = data.get(row).get(col);
		System.out.println("UpModel - getValueAt = "+value);
		return value;
	}

}
