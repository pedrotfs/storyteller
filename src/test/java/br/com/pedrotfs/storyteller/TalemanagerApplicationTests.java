package br.com.pedrotfs.storyteller;

import br.com.pedrotfs.storyteller.controller.*;
import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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

	private final static String TALE_ID = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa";

	private final static String TALE_CHILD_ID = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fc";

	private final static String TALE_ID_DUPLICATE = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fb";

	private final static String TALE_NAME = "cateano";

	private final static String TALE_NAME_UPDATE = "cateano_tale";

	private final static int STATUS_CODE_OK = 200;

	@Test
	void contextLoads() {
		clearDb();

		testTale();

		clearDb();
	}

	private void testTale() {
		testTaleInsert();
		testTaleUpdate();
		testTaleFindBy();
		testTaleFindAll();
		testTaleAddChild();
		testTaleDelChild();
		testTaleDelete();
	}

	private void testTaleInsert() {
		ResponseEntity<Tale> result = taleController.upsert(getTaleMessage(TALE_ID, TALE_NAME, null));

		List<Tale> all = taleRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Tale tale = all.get(0);
		Assertions.assertEquals(tale.getId(), TALE_ID);
		Assertions.assertEquals(tale.getName(), TALE_NAME);

		Assertions.assertEquals(result.getBody(), tale);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testTaleUpdate() {
		ResponseEntity<Tale> result = taleController.upsert(getTaleMessage(TALE_ID, TALE_NAME_UPDATE, null));

		List<Tale> all = taleRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Tale tale = all.get(0);
		Assertions.assertEquals(tale.getId(), TALE_ID);
		Assertions.assertEquals(tale.getName(), TALE_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), tale);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testTaleFindBy() {
		ResponseEntity<Tale> result = taleController.find(TALE_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), TALE_ID);
		Assertions.assertTrue(taleRepository.findById(TALE_ID).isPresent());
		Assertions.assertEquals(result.getBody(), taleRepository.findById(TALE_ID).get());
	}

	private void testTaleFindAll() {
		taleController.upsert(getTaleMessage(TALE_ID_DUPLICATE, TALE_NAME, null));

		List<Tale> all = taleRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Tale tale1 = taleRepository.findById(TALE_ID).get();
		Tale tale2 = taleRepository.findById(TALE_ID).get();
		Assertions.assertTrue(all.contains(tale1));
		Assertions.assertTrue(all.contains(tale2));
	}

	private void testTaleAddChild() {
		List<String> children = new ArrayList<>();
		children.add(TALE_CHILD_ID);

		ResponseEntity<Tale> result = taleController.addChild(getTaleMessage(TALE_ID, TALE_NAME_UPDATE, children));
		Assertions.assertEquals(result.getBody().getId(), TALE_ID);
		Tale tale = taleRepository.findById(TALE_ID).get();
		Assertions.assertEquals(tale.getBooks().size(), 1);
		Assertions.assertEquals(tale.getBooks().get(0), TALE_CHILD_ID);
	}

	private void testTaleDelChild() {
		List<String> children = new ArrayList<>();
		children.add(TALE_CHILD_ID);

		ResponseEntity<Tale> result = taleController.delChild(getTaleMessage(TALE_ID, TALE_NAME_UPDATE, children));
		Tale tale = taleRepository.findById(TALE_ID).get();
		Assertions.assertEquals(tale.getBooks().size(), 0);
	}

	private void testTaleDelete() {
		ResponseEntity<Tale> result = taleController.delete(getTaleMessage(TALE_ID, TALE_NAME, null));
		Assertions.assertEquals(TALE_ID, result.getBody().getId());
		Assertions.assertNull(taleController.find(getTaleMessage(TALE_ID, TALE_NAME, null)).getBody());
	}

	private String getTaleMessage(final String id, final String name, List<String> children) {
		StringBuilder stringBuilder = new StringBuilder("{ " +
				"\"id\":\"" + id + "\"," +
				"\"name\":\"" + name + "\"," +
				"\"title\":\"Caetano's growth tales and fantastic friends\"," +
				"\"imgPath\":\"caetano\"," +
				"\"text\":\"this will be a series of books regarding caetano's experiences\"," +
				"\"owner\":\"pedro.silva\"");
		if(children != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"books\": [ \n");
			int i = 1;
			for(String child : children) {
				stringBuilder.append("\"").append(child).append("\"");
				if(i++ < children.size()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append(" ] \n");
		}
		stringBuilder.append("\n}");
		return stringBuilder.toString();
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
