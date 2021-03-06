package bbs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BbsDao {
	private Connection conn;
	private java.sql.PreparedStatement pstmt;
	private ResultSet rs;
	
	public BbsDao() {
		String driver="com.mysql.jdbc.Oracle";
		String url="jdbc:mysql://localhost:3306/xe?&useSSL=false";
		String user="scott";
		String password="tiger";
		
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Date getDate() {
		String sql="select now()";
		try {
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getDate(1);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getNext() {
		String sql="select bbsId from bbs order by id desc";
		try {
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int write(String bbsTitle,int num,String bbsContent) {
		String sql="Insert into bbs values (?,?,?,?,?,?)";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setInt(3, num);
			pstmt.setDate(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public ArrayList<BbsDto> getList(int pageNum) throws SQLException{
		String sql="select * from bbs where bbsId < ? order by bbsID desc Limit 10";
		ArrayList<BbsDto> list=new ArrayList<BbsDto>();
		pstmt=conn.prepareStatement(sql);
		pstmt.setInt(1, getNext()-(pageNum-1)*10);
		rs=pstmt.executeQuery();
		while(rs.next()) {
			BbsDto dto=new BbsDto();
			dto.setBbsID(rs.getInt("bbsId"));
		}
		return list;
	}
}
