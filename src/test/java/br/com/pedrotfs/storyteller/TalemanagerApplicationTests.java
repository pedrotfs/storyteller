package br.com.pedrotfs.storyteller;

import br.com.pedrotfs.storyteller.controller.*;
import br.com.pedrotfs.storyteller.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class TalemanagerApplicationTests {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private AccountableController accountableController;

	@Autowired
	private BookController bookController;

	@Autowired
	private ChapterController chapterController;

	@Autowired
	private ParagraphController paragraphController;

	@Autowired
	private SectionController sectionController;

	@Autowired
	private TaleController taleController;

	@Autowired
	private AccountableRepository accountableRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private ParagraphRepository paragraphRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private TaleRepository taleRepository;

	private final static String TALE_MESSAGE =
			"{ " +
					"\"id\":\"d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa\"," +
					"\"name\":\"cateano\"," +
					"\"title\":\"Caetano's growth tales and fantastic friends\"," +
					"\"imgPath\":\"caetano\"," +
					"\"text\":\"this will be a series of books regarding caetano's experiences\"," +
					"\"owner\":\"pedro.silva\"," +
			"}";



	@Test
	void contextLoads() {
		clearDb();

		//taleController.upsert(TALE_MESSAGE);

		clearDb();
	}

	private void clearDb() {
		taleRepository.deleteAll();
		bookRepository.deleteAll();
		sectionRepository.deleteAll();
		chapterRepository.deleteAll();
		paragraphRepository.deleteAll();
		accountableRepository.deleteAll();
	}

}
