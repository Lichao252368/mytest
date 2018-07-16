package com.itheima.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.itheima.dao.BookDao;
import com.itheima.pojo.Book;



public class BookDaoImpl implements BookDao{

	@Override
	public List<Book> queryBookList() {
		// 数据库连接
		List<Book> list = new ArrayList<Book>();
		try {
			//1.加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.创建连接对象
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene","root","root");
			//3.sql语句
			String sql = "select * from book";
			//4.获取执行平台
			PreparedStatement ps = conn.prepareStatement(sql);
			//5.获取结果集
			ResultSet resultSet = ps.executeQuery();
			//6.结果集解析
			while(resultSet.next()) {
				Book book = new Book();
				book.setId(resultSet.getInt("id"));
				book.setName(resultSet.getString("name"));
				book.setPic(resultSet.getString("pic"));
				book.setPrice(resultSet.getFloat("price"));
				book.setDesc(resultSet.getString("desc"));
				list.add(book);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
