/*
 * 하위 카테고리에 등록된 상품 정보 제공 모델
 */
package com.paris.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DownModel extends AbstractTableModel{
	
	Connection con;
	Vector<String> columnName = new Vector<String>();
	Vector<Vector>  data = new Vector<Vector>();
	
	public DownModel(Connection con) {
		this.con = con;
		
		// 컬럼을 생성시 지정하므로, 다시 생성하지 않아도 데이터 가져옴.
		columnName.add("product_id");
		columnName.add("subcategory_id");
		columnName.add("product_name");
		columnName.add("price");
		columnName.add("img");
	}
	
	// 마우스로 유저가 클릭할때마다 id 값이 바뀌므로
	// 아래의 메서드를 그때마다 호출하면 된다.
	public void getList(int subcategory_id){
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		String sql = "select * from product";
		sql+=" where subcategory_id=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, subcategory_id);
			rs = pstmt.executeQuery();
			
			// 벡터들 초기화
			//columnName.removeAll(columnName);
			data.removeAll(data);
			
			/*
			ResultSetMetaData meta = rs.getMetaData();
			for (int i=1; i<=meta.getColumnCount(); i++){
				columnName.add(meta.getColumnName(i));
			}
			System.out.println("getList 컬럼의 크기는 "+columnName.size());
			*/
			
			while (rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getInt("product_id"));
				vec.add(rs.getInt("subcategory_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("price"));
				vec.add(rs.getString("img"));
				
				data.add(vec);
			}
			System.out.println("getList 레코드의 크기는 "+data.size());
			
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
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}

	public int getColumnCount() {
		System.out.println("컬럼 갯수는 "+columnName.size());
		return columnName.size();
	}

	public int getRowCount() {
		System.out.println("레코드 갯수는 "+data.size());
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		Object value = data.get(row).get(col);
		System.out.println("getValueAt 호출 "+value);
		return value;
	}

}
