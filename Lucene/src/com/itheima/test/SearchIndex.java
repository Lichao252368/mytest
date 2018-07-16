package com.itheima.test;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

//搜索流程
public class SearchIndex {

	@Test
	public void testIndexSearch() throws Exception {
		//创建分词器
//		Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
		//创建索引解析器,第一个参数,默认Field域,第二个参数,分词器
		QueryParser queryParser = new QueryParser("desc",analyzer);
		//1. 创建Query搜索对象
		Query query = queryParser.parse("传智播客");
		
		//2. 创建Directory流对象,声明索引库位置
		Directory directory = FSDirectory.open(new File("e:/lucene/lucene_index2"));
		//3. 创建索引读取对象IndexReader
		IndexReader reader = DirectoryReader.open(directory);
		//	 4. 创建索引搜索对象IndexSearcher
		IndexSearcher searcher = new IndexSearcher(reader);
		//	 5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
		//第一个参数:搜索对象  第二个参数:返回的数据条数,指定查询结果最顶部的n条数据返回
		TopDocs topDocs = searcher.search(query, 5);
//		System.out.println("查询到的数据总条数"+topDocs.totalHits);
		//获取查询结果集
		ScoreDoc[] docs = topDocs.scoreDocs;
		//	 6. 解析结果集(结果集需要的是坐标,是数组形式[(),()])
		for (ScoreDoc scoreDoc : docs) {
			//7.获取每一个坐标中的文档id
			int docId = scoreDoc.doc;
			//8.根据文档ID搜索文档,返回文档对象
			Document doc = searcher.doc(docId);
			//9.打印文档对象
			System.out.println("文档id:"+doc.get("id"));
			System.out.println("文档name:"+doc.get("name"));
			System.out.println("文档price:"+doc.get("price"));
			System.out.println("文档pic:"+doc.get("pic"));
		}
	}
//	 7. 释放资源
}
