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
public class SearchIndex2 {
	
	@Test
	public void testIndexSearch() throws Exception {
		//1.创建分词器对象
		Analyzer analyzer = new StandardAnalyzer();
//		Analyzer analyzer = new IKAnalyzer();
		//2.创建一个搜索解析器对象,两个参数:1.指定默认搜索的域名 2.分词器对象
		QueryParser queryParser = new QueryParser("name",analyzer);
		//3.根据解析器对象创建查询条件对象
		//参数说明:创建条件查询对象query需要指定搜索的域名
		//如果在此指定搜索的域名的话,那么就不用根据上面创建解析器的域名搜索了
		//如果没有指定搜索域名那么就根据上面设置的默认的搜索域名来搜索
		//4.设置查询条件
		Query query = queryParser.parse("编思想");
//		Query query = queryParser.parse("name:编程思想");
		//5创建指定索引库地址的流对象Directory
		Directory directory = FSDirectory.open(new File("e:/lucene/lucene_index"));
		//6.创建读取索引读取对象
		IndexReader indexReader = DirectoryReader.open(directory);
		//7.创建搜索对象:indexSearcher 需要读取索引库对象
		IndexSearcher indexSeacher = new IndexSearcher(indexReader);
		//8.执行查询,返回结果集
		TopDocs topDocs = indexSeacher.search(query, 2);
		//打印结果集总记录数
		System.out.println(topDocs.totalHits);
		//9.结果集需要的是坐标,是数组形式[(),()]
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//10.遍历坐标数据
		for (ScoreDoc scoreDoc : scoreDocs) {
			//11.获取每一个坐标中的文档id
			int docID = scoreDoc.doc;
			//12.根据文档id搜索文档,返回文档对象
			Document doc = indexSeacher.doc(docID);
			//13.打印文档数据
			System.out.println("文档id:"+doc.get("id"));
			System.out.println("文档name:"+doc.get("name"));
			System.out.println("文档price:"+doc.get("price"));
			System.out.println("文档pic:"+doc.get("pic"));
		}
	}
}
