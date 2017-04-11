/*
 * Join 문이란? 
 * - 정규화에 의해 물리적으로 분리된 테이블을 마치 하나의 테이블처럼 보여줄 수 있는 쿼리.
 * 
 * - inner 조인 : 조인 대상이 되는 테이블간 공통적인 레코드만 가져온다.
 *                    우리가 지금까지 해왔던 조인
 *                    주의할 특징) 공통적인 레코드가 아닌 경우, 누락시킨다.
 * - outter 조인 : 조인 대상이 되는 테이블간 공통된 레코드 뿐만 아니라,
 *                      지정한 테이블의 레코드는 일단 무조건 다 가져오는 조인
 */
package com.paris.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import db.DBManager;
import db.SubCategory;
import db.TopCategory;

public class MainWindow extends  JFrame implements ItemListener, ActionListener{
	
	JPanel  p_west, p_east, p_center;
	JPanel p_up, p_down;
	JTable   table_up, table_down;
	JScrollPane  scroll_up, scroll_down;
	
	// west 영역
	Choice  ch_top, ch_sub;
	JTextField  t_name, t_price;
	Canvas  can_west;
	JButton  bt_regist;
	
	// east 영역
	Canvas  can_east;
	JTextField  t_id, t_name2, t_price2;
	JButton  bt_edit, bt_delete;
	
	DBManager  manager;
	Connection con;
	
	// 상위카테고리 list
	ArrayList<TopCategory> topList= new ArrayList<TopCategory>();
	ArrayList<SubCategory> subList= new ArrayList<SubCategory>();
	BufferedImage image=null;
	
	// Table 모델 객체들
	UpModel upModel;
	DownModel  downModel;
	
	JFileChooser  chooser;
	File file;
	
	public MainWindow() {
		p_west = new JPanel();
		p_center = new JPanel();
		p_east = new JPanel();
		
		p_up = new JPanel();
		p_down = new JPanel();
		
		table_up = new JTable();
		table_down = new JTable(3,4);
		
		scroll_up = new JScrollPane(table_up);
		scroll_down = new JScrollPane(table_down);
		
		// west 영역
		ch_top = new Choice(); 
		ch_sub = new Choice();
		t_name = new JTextField(10); 
		t_price = new JTextField(10);
		
		chooser = new JFileChooser("C:/images/");
		
		
		try {
			URL  url=this.getClass().getResource("/default.png");
			image= ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		can_west = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135, 135, this);
			}
		};
		bt_regist = new JButton("등록");
		
		// east 영역
		can_east = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135, 135, this);
			}
		};
		
		t_id = new JTextField(10);
		t_name2 = new JTextField(10);
		t_price2 = new JTextField(10);
		bt_edit = new JButton("수정"); 
		bt_delete = new JButton("삭제");
		
		// p_west 에 부착
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can_west);
		p_west.add(bt_regist);
		
		// p_east 에 부착
		t_id.setEnabled(false);
		p_east.add(t_id);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(can_east);
		p_east.add(bt_edit);
		p_east.add(bt_delete);		
		
		// 각 패널의 색상
		can_west.setBackground(Color.PINK);
		can_east.setBackground(Color.PINK);
		p_west.setBackground(Color.YELLOW);
		p_center.setBackground(Color.GRAY);
		p_up.setBackground(Color.pink);
		p_down.setBackground(Color.CYAN);
		p_east.setBackground(Color.BLUE);
		
		// size
		ch_top.setPreferredSize(new Dimension(135, 40));
		ch_sub.setPreferredSize(new Dimension(135, 40));
		can_west.setPreferredSize(new Dimension(135, 135));
		can_east.setPreferredSize(new Dimension(135, 135));
		// 각 패널들의 크기 지정
		p_west.setPreferredSize(new Dimension(150, 700));
		p_center.setPreferredSize(new Dimension(550, 700));
		p_east.setPreferredSize(new Dimension(150, 700));
		
		ch_top.add("▼ 상위카테고리 선택");
		ch_sub.add("▼ 하위카테고리 선택");
		
		// 센터의 그리드 적용하고 위 아래 구성
		p_center.setLayout(new GridLayout(2,1));
		p_center.add(p_up);
		p_center.add(p_down);
		
		// 스크롤 부착
		p_up.setLayout(new BorderLayout());
		p_down.setLayout(new BorderLayout());
		p_up.add(scroll_up);
		p_down.add(scroll_down);
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_east, BorderLayout.EAST);
		
		// 초이스와 리스너 연결
		ch_top.addItemListener(this);
		bt_regist.addActionListener(this);
		
		// 캐버스에 마우스 리스너 연결
		can_west.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				preView();
			}
		});
		
		// 다운 테이블과 리스너 연결
		table_up.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table_up.getSelectedRow();
				int col=0;
				String subcategory_id = (String)table_up.getValueAt(row, col);
				System.out.println(subcategory_id);
				
				//downModel = new DownModel(con);
				// 구해진 id 를 아래의 모델에 적용하자
				downModel.getList(Integer.parseInt(subcategory_id));
				//table_down.setModel(downModel);
				table_down.updateUI();
				//getDownList(Integer.parseInt(subcategory_id));
			}
		});
		
		table_down.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table_down.getSelectedRow();
				
				// 이차원 백터에 들어 있는 벡터를 얻어오자. 이 백터가 레코드
				Vector vec = downModel.data.get(row);
				System.out.println(vec.get(2));
				
				getDetail(vec);
			}
		} );
		
		setTitle("Bread");
		setSize(850, 700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		// 초기 설정
		init();
		
		// TopCategory 가져오기
		getTop();
		
		// TableModel 얻기
		getUpList();
		
		getDownList();
		
	}
	
	// 데이터베이스 커넥션 얻기
	public void init(){
		manager = DBManager.getInstance();
		con = manager.getConnection();
		System.out.println(con);
	}
	
	// 최상위 카테고리 얻기
	public void getTop(){
		PreparedStatement pstmt=null;
		ResultSet  rs=null;
		String sql="select * from topcategory order by topcategory_id asc ";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				TopCategory dto = new TopCategory();
				dto.setToqcategory_id(rs.getInt("topcategory_id"));
				dto.setTop_name(rs.getString("top_name"));
				topList.add(dto);
				ch_top.add(dto.getTop_name());
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
	
	// 위쪽 Table 데이터 처리
	public void getUpList(){
		System.out.println("getUpList");
		upModel = new UpModel(con);
		table_up.setModel(upModel);
		table_up.updateUI();
	}
	
	public void getDownList(){
		downModel = new DownModel(con);
		//downModel.getList(subcategory_id);
		table_down.setModel(downModel);
		//table_down.updateUI();
	}
	
	// 하위카테고리 구하기
	// 바인드 변수 : 구문검사, 객체 검사, 컴파일 불필요
	public void getSub(){
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		String sql="select * from subcategory where topcategory_id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			// 바인드 변수값 지정
			int index = ch_top.getSelectedIndex();
			// 담기 전에 지우기
			subList.removeAll(subList);
			ch_sub.removeAll();
			//ch_sub.add("▼ 하위카테고리 선택");
			if (index-1 >= 0){
				TopCategory  dto=topList.get(index-1);
				pstmt.setInt(1, dto.getToqcategory_id());
				rs = pstmt.executeQuery();
				// 하위 카테고리 채우기
				while (rs.next()){
					SubCategory  vo = new SubCategory();
					vo.setSubcategory_id(rs.getInt("subcategory_id"));
					vo.setTopcategory_id(rs.getInt("topcategory_id"));
					vo.setSub_name(rs.getString("sub_name"));
					subList.add(vo);
					// sub 카테고리 choice 에 담기
					ch_sub.add(vo.getSub_name());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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

	public void itemStateChanged(ItemEvent e) {
		// 하위 카테고리 구하기
		getSub();
	}
	
	/* 상품 등록 */
	public void regist(){
		PreparedStatement pstmt = null;
		String sql="insert into product (product_id, subcategory_id, product_name, price, img)";
		sql+="values (seq_product.nextval, ?, ?, ?, ?)";
		try {
			pstmt = con.prepareStatement(sql);
			System.out.println(sql);
			// ArrayList 안에 들어 있는 SubCategory DTO 를 추출
			SubCategory vo=subList.get(ch_sub.getSelectedIndex());
			
			// 바인드 변수에 들어갈 값 결정			
			System.out.println("vo.getSubcategory_id() = "+vo.getSubcategory_id());
			pstmt.setInt(1, vo.getSubcategory_id());
			pstmt.setString(2, t_name.getText());
			pstmt.setInt(3, Integer.parseInt(t_price.getText()));
			pstmt.setString(4, file.getName());
			
			
			// excuteUpdate 는 쿼리문 수행후 반영된 레코드의 갯수를 반환해 준다.
			// 따라서 insert 문의 경우 언제나 성공했다면 1건
			// update 1건이상, delete 1건 이상
			// 결론) insert 시 반환값이 0 이라면 insert 실패!			
			int result = pstmt.executeUpdate();
			if (result!=0){
				JOptionPane.showMessageDialog(this, "등록 성공!");
				
				// db를 새롭게 가져와 이차원 벡터 변경
				upModel.getList(); 
				table_up.updateUI();
				
				// 이미지 파일 복사
				copy();
				
			} else {
				JOptionPane.showMessageDialog(this, "등록 실패!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}
	
	// 캔버스에 이미지 반영하기.
	public void preView(){
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION){
			// 캔버스에 이미지 그리자
			file = chooser.getSelectedFile();
			// 얻어진 파일을 기존의 이미지로 대체하여 다시 그리기
			try {
				image = ImageIO.read(file);
				can_west.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 복사 메서드 정의
	public void copy(){
		FileInputStream  fis=null;
		FileOutputStream  fos=null;
		
		try {
			fis = new  FileInputStream(file);
			fos = new FileOutputStream("C:/java_workspace2/BreadProject/data/"+file.getName());
			
			byte[] b=new byte[1024];
			int flag; // -1인지 여부 판단.
			
			while (true){
				flag = fis.read(b);
				//System.out.println("flag="+flag);
				if (flag==-1) break;
				fos.write(b);
			}
			System.out.println("이미지 복사 완료");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos!=null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fis!=null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	// 상세 정보 보여주기
	// 선택한 제품의 상세정보 보여주기
	public void getDetail(Vector vec){
		t_id.setText(vec.get(0).toString());
		t_name2.setText(vec.get(2).toString());
		t_price2.setText(vec.get(3).toString());
		
		try {
			image = ImageIO.read(new File("C:/java_workspace2/BreadProject/data/"+vec.get(4)));
			can_east.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		regist();
	}

	public static void main(String[] args) {
		new MainWindow();
	}

}
