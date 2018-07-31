package com.sorc.content.elasticsearch.video.dao;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.settings.UpdateSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sorc.content.core.pagination.Pagination;
import com.sorc.content.elasticsearch.core.dto.ElasticSearchFilterDataTransfer;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchDAOTest {

	private static ElasticSearchVideoDAO dao;
	private static Logger logger = LoggerFactory.getLogger(ElasticSearchDAOTest.class);
	private static final String INDEX_NAME = "mtod_video";

	private static final Integer ES_ID_MIN = 1;
	private static final Integer ES_ID_MAX = 1;


    ClientConfig clientConfig;
    static JestClient jestClient;
    
	@BeforeClass
	public static void init() {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"testApplicationContext.xml");
		dao = (ElasticSearchVideoDAO) ctx.getBean("elasticSearchVideoDao");

		// start up elasticsearch node
		//startElasticNode();

		//cleanElasticServer();
		//insertElasticSearchVideo(ES_ID_MIN, ES_ID_MAX);

		// Need to add delay so elastic can fully insert all the articles
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static JestClient startElasticNode() {
		 JestClientFactory factory = new JestClientFactory();
		 factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://127.0.0.1:9200")
		                        .multiThreaded(true)
		                        .build());
		 jestClient = factory.getObject();
		 return jestClient;
		 
	}

	//@Test
	public void testSearchFreeTextAllArticlesMatch() throws Exception {

		ElasticSearchFilterDataTransfer esfdt = new ElasticSearchFilterDataTransfer();
		esfdt.setPagination(new Pagination(20, 0));
		esfdt.setIndex(INDEX_NAME);

		Map<String, Object> convertedResults = dao.getDetailList(esfdt);
		System.out.println(convertedResults);
		/*assertTrue(convertedResults.size() == 1);
		assertTrue(convertedResults.get(0).getId().equals(filterTerm));*/
	}

	private static void cleanElasticServer() {
		try {
			String body = "{ \"network.host\" : \"127.0.0.1\" }";

	        UpdateSettings updateSettings = new UpdateSettings.Builder(body).addIndex("mtod_video").build();
	        JestResult result = jestClient.execute(updateSettings);
			//createIndexTemplate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createIndexTemplate()
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(
				"http://localhost:9200/_template/mtod_video");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				ElasticSearchDAOTest.class
						.getResourceAsStream("/json/esVideoTemplate.txt")));
		StringBuilder itemAsJson = new StringBuilder();
		String strLine;
		try {
			while ((strLine = br.readLine()) != null) {
				itemAsJson.append(strLine);
			}
			 PutMapping putMapping = new PutMapping.Builder(
		                INDEX_NAME,
		                null,
		                itemAsJson
		    ).build();
			 JestResult result = jestClient.execute(putMapping);
			 System.out.println(result.isSucceeded());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void insertElasticSearchVideo(Integer id_min,
			Integer id_max) {
		// insert an article in elastic
		try {
			for (int id = id_min; id <= id_max; ++id) {
				HttpPost httpPost = new HttpPost("http://localhost:9200/"
						+ INDEX_NAME + "/" + id);
				StringEntity entity = new StringEntity(getJsonOneItem(
						"esVideo", id));
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
				HttpClient client = new DefaultHttpClient();
				client.execute(httpPost);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getJsonOneItem(String type, Integer id){
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						ElasticSearchDAOTest.class
								.getResourceAsStream("/json/"+type+"-"+id+".txt")));
		StringBuilder itemAsJson = new StringBuilder();
		String strLine;
		// Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null) {
				itemAsJson.append(strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return itemAsJson.toString();
	}
}
