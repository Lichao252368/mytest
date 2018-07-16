package com.itheima.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.itheima.dao.BookDao;
import com.itheima.dao.impl.BookDaoImpl;
import com.itheima.pojo.Book;



//实现索引流程
public class CreateIndexTest2 {
	@Test
	public void testIndexCreate() throws IOException {
		//1.采集数据
		BookDao bookDao = new BookDaoImpl();
		List<Book> bookList = bookDao.queryBookList();
		//2.创建document文档对象
		List<Document> documents = new ArrayList<Document>();
		for (Book book : bookList) {
			Document document = new Document();
			//3.创建field字段,存储数据
			Field idField = new TextField("id", book.getId()+"", Store.YES);
			Field nameField = new TextField("name", book.getName(), Store.YES);
			Field priceField = new TextField("price", book.getPrice()+"", Store.YES);
			Field picField = new TextField("pic", book.getPic(), Store.YES);
			Field descField = new TextField("desc", book.getDesc(), Store.YES);
			//4.将字段(域)添加到文档对象
			document.add(idField);
			document.add(nameField);
			document.add(priceField);
			document.add(picField);
			document.add(descField);
			//5.将文档对象添加到文档集合
			documents.add(document);
		}
		//6.创建Analyzer分词器
		Analyzer analyzer = new StandardAnalyzer();
//		Analyzer analyzer = new IKAnalyzer();
		//7.创建directory对象,声明索引库位置
		Directory directory = FSDirectory.open(new File("e:/lucene/lucene_index"));
		//8.创建IndexWriterConfig对象,写入索引需要的配置
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
		//9.创建IndexWriter写入对象
		IndexWriter indexWriter = new IndexWriter(directory, config);
		//10.写入到索引库,通过IndexWriter添加文档对象Document
		for (Document document : documents) {
			indexWriter.addDocument(document);
		}
		//11.释放资源
		indexWriter.close();
		
	}
}
