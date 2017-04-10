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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import db.DBManager;
import db.SubCategory;
import db.TopCategory;

public class MainWindow extends  JFrame implements ItemListener{
	
	JPanel  p_west, p_east, p_center;
	JPanel p_up, p_down;
	JTable   table_up, table_down;
	JScrollPane  scroll_up, scroll_down;
	
	// west ����
	Choice  ch_top, ch_sub;
	JTextField  t_name, t_price;
	Canvas  can_west;
	JButton  bt_regist;
	
	// east ����
	Canvas  can_east;
	JTextField  t_name2, t_price2;
	JButton  bt_edit, bt_delete;
	
	DBManager  manager;
	Connection con;
	
	// ����ī�װ��� list
	ArrayList<TopCategory> topList= new ArrayList<TopCategory>();
	ArrayList<SubCategory> subList= new ArrayList<SubCategory>();
	BufferedImage image=null;
	
	public MainWindow() {
		p_west = new JPanel();
		p_center = new JPanel();
		p_east = new JPanel();
		
		p_up = new JPanel();
		p_down = new JPanel();
		
		table_up = new JTable(3,6);
		table_down = new JTable(3,4);
		
		scroll_up = new JScrollPane(table_up);
		scroll_down = new JScrollPane(table_down);
		
		// west ����
		ch_top = new Choice(); 
		ch_sub = new Choice();
		t_name = new JTextField(10); 
		t_price = new JTextField(10);
		
		
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
				g.drawImage((Image)image, 0, 0, 135, 135, this);
			}
		};
		bt_regist = new JButton("���");
		
		// east ����
		can_east = new Canvas();
		t_name2 = new JTextField(10);
		t_price2 = new JTextField(10);
		bt_edit = new JButton("����"); 
		bt_delete = new JButton("����");
		
		// p_west �� ����
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can_west);
		p_west.add(bt_regist);
		
		// p_east �� ����
		p_east.add(can_east);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(bt_edit);
		p_east.add(bt_delete);		
		
		// �� �г��� ����
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
		// �� �гε��� ũ�� ����
		p_west.setPreferredSize(new Dimension(150, 700));
		p_center.setPreferredSize(new Dimension(550, 700));
		p_east.setPreferredSize(new Dimension(150, 700));
		
		ch_top.add("�� ����ī�װ��� ����");
		ch_sub.add("�� ����ī�װ��� ����");
		
		// ������ �׸��� �����ϰ� �� �Ʒ� ����
		p_center.setLayout(new GridLayout(2,1));
		p_center.add(p_up);
		p_center.add(p_down);
		
		// ��ũ�� ����
		p_up.setLayout(new BorderLayout());
		p_down.setLayout(new BorderLayout());
		p_up.add(scroll_up);
		p_down.add(scroll_down);
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_east, BorderLayout.EAST);
		
		// ���̽��� ������ ����
		ch_top.addItemListener(this);
		
		setTitle("Bread");
		setSize(850, 700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		// �ʱ� ����
		init();
		
		// TopCategory ��������
		getTop();
		
	}
	
	// �����ͺ��̽� Ŀ�ؼ� ���
	public void init(){
		manager = DBManager.getInstance();
		con = manager.getConnection();
		System.out.println(con);
	}
	
	// �ֻ��� ī�װ��� ���
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
	
	// ����ī�װ��� ���ϱ�
	// ���ε� ���� : �����˻�, ��ü �˻�, ������ ���ʿ�
	public void getSub(){
		PreparedStatement  pstmt=null;
		ResultSet  rs=null;
		String sql="select * from subcategory where topcategory_id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			// ���ε� ������ ����
			int index = ch_top.getSelectedIndex();
			// ��� ���� �����
			subList.removeAll(subList);
			ch_sub.removeAll();
			ch_sub.add("�� ����ī�װ��� ����");
			if (index-1 >= 0){
				TopCategory  dto=topList.get(index-1);
				pstmt.setInt(1, dto.getToqcategory_id());
				rs = pstmt.executeQuery();
				// ���� ī�װ��� ä���
				while (rs.next()){
					SubCategory  vo = new SubCategory();
					vo.setSubcategory_id(rs.getInt("subcategory_id"));
					vo.setTopcategory_id(rs.getInt("topcategory_id"));
					vo.setSub_name(rs.getString("sub_name"));
					subList.add(vo);
					// sub ī�װ��� choice �� ���
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
		// ���� ī�װ��� ���ϱ�
		getSub();
	}

	public static void main(String[] args) {
		new MainWindow();
	}

}