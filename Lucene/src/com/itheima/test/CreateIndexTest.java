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
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.FileSwitchDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.itheima.dao.BookDao;
import com.itheima.dao.impl.BookDaoImpl;
import com.itheima.pojo.Book;

//实现索引流程
public class CreateIndexTest {
	@Test
	public void testCreateIndex() throws Exception {
		//1.采集数据
		BookDao bookDao = new BookDaoImpl();
		List<Book> list = bookDao.queryBookList();
		//2.创建document文档对象
		List<Document> documents = new ArrayList<>();
		for (Book book : list) {
			Document document = new Document();
			//3.创建Field对象,存储数据(参数1:指定域名,指定域值,是否存储)
			Field idField = new StringField("id", book.getId()+"", Store.YES); 
			Field nameField = new TextField("name", book.getName(), Store.YES);
			Field priceField = new TextField("price", book.getPrice().toString(),Store.YES);
			Field picField = new TextField("pic",book.getPic(),Store.YES);
			Field descField = new TextField("desc", book.getDesc(),Store.YES);
			//4.把field加入到文档中
			document.add(idField);
			document.add(nameField);
			document.add(priceField);
			
			document.add(picField);
			document.add(descField);
			//把document放到list中
			documents.add(document);
		}
		//5.创建Anylyzer分词器,分析文档,对文档进行分词.下面这个是lucene中默认的一个分词器(英文分词器)
//		Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
		//6.创建Directory对象,声明索引库的位置.流对象Directory
		Directory directory = FSDirectory.open(new File("E:\\lucene\\lucene_index2"));
		//7.创建IndexWriteConfig对象,写入索引需要的配置
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
		//8.创建IndexWriter写入对象
		IndexWriter indexWriter = new IndexWriter(directory,config);
		//9.写入到索引库,通过IndexWriter添加文档对象document
		for (Document document : documents) {
			indexWriter.addDocument(document);
		}
		//10.释放资源
		indexWriter.close();
	}
}
