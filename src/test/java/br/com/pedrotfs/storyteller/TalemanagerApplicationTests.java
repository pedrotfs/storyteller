package br.com.pedrotfs.storyteller;

import br.com.pedrotfs.storyteller.controller.*;
import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.helper.DatabaseTestProfile;
import br.com.pedrotfs.storyteller.repository.*;
import br.com.pedrotfs.storyteller.util.DatabaseCsvDumper;
import br.com.pedrotfs.storyteller.util.DatabaseCsvLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = DatabaseTestProfile.class)
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

	@Autowired
	private DatabaseCsvDumper databaseCsvDumper;

	@Autowired
	private DatabaseCsvLoader databaseCsvLoader;

	private final static String ENTITY_ID = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa";

	private final static String ENTITY_ID_2 = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fe";

	private final static String ENTITY_CHILD_ID = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fc";

	private final static String ENTITY_CHILD_ID_2 = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fd";

	private final static String ENTITY_ID_DUPLICATE = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fb";

	private final static String ENTITY_NAME = "cateano";

	private final static String ENTITY_NAME_UPDATE = "cateano_tale";

	private final static int STATUS_CODE_OK = 200;

	@Test
	void contextLoads() {

		clearDb();

		testDomain();
		testDumpAndRestoreUtils();

		clearDb();
	}

	private void testDomain() {
		testTale();
		testBook();
		testSection();
		testChapter();
		testParagraph();
	}

	private void testTale() {
		testTaleInsert();
		testTaleUpdate();
		testTaleFindBy();
		testTaleFindAll();
		testTaleAddChild();
		testTaleDelChild();
		testTaleDelete();
		testAccountables();
	}

	private void testTaleInsert() {
		ResponseEntity<Tale> result = taleController.upsert(getTaleMessage(ENTITY_ID, ENTITY_NAME, null));

		List<Tale> all = taleRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Tale tale = all.get(0);
		Assertions.assertEquals(tale.getId(), ENTITY_ID);
		Assertions.assertEquals(tale.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), tale);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testTaleUpdate() {
		ResponseEntity<Tale> result = taleController.upsert(getTaleMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null));

		List<Tale> all = taleRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Tale tale = all.get(0);
		Assertions.assertEquals(tale.getId(), ENTITY_ID);
		Assertions.assertEquals(tale.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), tale);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testTaleFindBy() {
		ResponseEntity<Tale> result = taleController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(taleRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), taleRepository.findById(ENTITY_ID).get());
	}

	private void testTaleFindAll() {
		taleController.upsert(getTaleMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME, null));

		List<Tale> all = taleRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Tale tale1 = taleRepository.findById(ENTITY_ID).get();
		Tale tale2 = taleRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(tale1));
		Assertions.assertTrue(all.contains(tale2));
	}

	private void testTaleAddChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Tale> result = taleController.addChild(getTaleMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Tale tale = taleRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(tale.getBooks().size(), 1);
		Assertions.assertEquals(tale.getBooks().get(0), ENTITY_CHILD_ID);
	}

	private void testTaleDelChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Tale> result = taleController.delChild(getTaleMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Tale tale = taleRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(tale.getBooks().size(), 0);
	}

	private void testTaleDelete() {
		ResponseEntity<Tale> result = taleController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(taleController.find(getTaleMessage(ENTITY_ID, ENTITY_NAME, null)).getBody());
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

	private void testBook() {
		testBookInsert();
		testBookUpdate();
		testBookFindBy();
		testBookFindAll();
		testBookAddChild();
		testBookDelChild();
		testBookDelete();
	}

	private void testBookInsert() {
		ResponseEntity<Book> result = bookController.upsert(getBookMessage(ENTITY_ID, ENTITY_NAME, null));
		List<Book> all = bookRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Book entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testBookUpdate() {
		ResponseEntity<Book> result = bookController.upsert(getBookMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null));

		List<Book> all = bookRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Book entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testBookFindBy() {
		ResponseEntity<Book> result = bookController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(bookRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), bookRepository.findById(ENTITY_ID).get());
	}

	private void testBookFindAll() {
		bookController.upsert(getBookMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME, null));

		List<Book> all = bookRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Book entity1 = bookRepository.findById(ENTITY_ID).get();
		Book entity2 = bookRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(entity1));
		Assertions.assertTrue(all.contains(entity2));
	}

	private void testBookAddChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Book> result = bookController.addChild(getBookMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Book entity = bookRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getSections().size(), 1);
		Assertions.assertEquals(entity.getSections().get(0), ENTITY_CHILD_ID);
	}

	private void testBookDelChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Book> result = bookController.delChild(getBookMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Book entity = bookRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getSections().size(), 0);
	}

	private void testBookDelete() {
		ResponseEntity<Book> result = bookController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(bookController.find(getBookMessage(ENTITY_ID, ENTITY_NAME, null)).getBody());
	}

	private String getBookMessage(final String id, final String name, List<String> children) {
		StringBuilder stringBuilder = new StringBuilder("{ " +
				"\"id\":\"" + id + "\"," +
				"\"name\":\"" + name + "\"," +
				"\"title\":\"Caetano's growth tales and fantastic friends\"," +
				"\"imgPath\":\"caetano\"," +
				"\"text\":\"this will be a series of books regarding caetano's experiences\"," +
				"\"orderIndex\":\"1\"," +
				"\"time\":\"5h\"");
		if(children != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"sections\": [ \n");
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

	private void testSection() {
		testSectionInsert();
		testSectionUpdate();
		testSectionFindBy();
		testSectionFindAll();
		testSectionAddChild();
		testSectionDelChild();
		testSectionDelete();
	}

	private void testSectionInsert() {
		ResponseEntity<Section> result = sectionController.upsert(getSectionMessage(ENTITY_ID, ENTITY_NAME, null));
		List<Section> all = sectionRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Section entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testSectionUpdate() {
		ResponseEntity<Section> result = sectionController.upsert(getSectionMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null));

		List<Section> all = sectionRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Section entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testSectionFindBy() {
		ResponseEntity<Section> result = sectionController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(sectionRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), sectionRepository.findById(ENTITY_ID).get());
	}

	private void testSectionFindAll() {
		sectionController.upsert(getSectionMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME, null));

		List<Section> all = sectionRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Section entity1 = sectionRepository.findById(ENTITY_ID).get();
		Section entity2 = sectionRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(entity1));
		Assertions.assertTrue(all.contains(entity2));
	}

	private void testSectionAddChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Section> result = sectionController.addChild(getSectionMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Section entity = sectionRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getChapter().size(), 1);
		Assertions.assertEquals(entity.getChapter().get(0), ENTITY_CHILD_ID);
	}

	private void testSectionDelChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Section> result = sectionController.delChild(getSectionMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Section entity = sectionRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getChapter().size(), 0);
	}

	private void testSectionDelete() {
		ResponseEntity<Section> result = sectionController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(sectionController.find(getSectionMessage(ENTITY_ID, ENTITY_NAME, null)).getBody());
	}

	private String getSectionMessage(final String id, final String name, List<String> children) {
		StringBuilder stringBuilder = new StringBuilder("{ " +
				"\"id\":\"" + id + "\"," +
				"\"name\":\"" + name + "\"," +
				"\"title\":\"Caetano's growth tales and fantastic friends\"," +
				"\"imgPath\":\"caetano\"," +
				"\"text\":\"this will be a series of sections regarding caetano's experiences\"," +
				"\"orderIndex\":\"1\"");
		if(children != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"chapter\": [ \n");
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

	private void testChapter() {
		testChapterInsert();
		testChapterUpdate();
		testChapterFindBy();
		testChapterFindAll();
		testChapterAddChild();
		testChapterDelChild();
		testChapterDelete();
	}

	private void testChapterInsert() {
		ResponseEntity<Chapter> result = chapterController.upsert(getChapterMessage(ENTITY_ID, ENTITY_NAME, null));
		List<Chapter> all = chapterRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Chapter entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testChapterUpdate() {
		ResponseEntity<Chapter> result = chapterController.upsert(getChapterMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null));

		List<Chapter> all = chapterRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Chapter entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testChapterFindBy() {
		ResponseEntity<Chapter> result = chapterController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(chapterRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), chapterRepository.findById(ENTITY_ID).get());
	}

	private void testChapterFindAll() {
		chapterController.upsert(getChapterMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME, null));

		List<Chapter> all = chapterRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Chapter entity1 = chapterRepository.findById(ENTITY_ID).get();
		Chapter entity2 = chapterRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(entity1));
		Assertions.assertTrue(all.contains(entity2));
	}

	private void testChapterAddChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Chapter> result = chapterController.addChild(getChapterMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Chapter entity = chapterRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getParagraphs().size(), 1);
		Assertions.assertEquals(entity.getParagraphs().get(0), ENTITY_CHILD_ID);
	}

	private void testChapterDelChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Chapter> result = chapterController.delChild(getChapterMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Chapter entity = chapterRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getParagraphs().size(), 0);
	}

	private void testChapterDelete() {
		ResponseEntity<Chapter> result = chapterController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(chapterController.find(getChapterMessage(ENTITY_ID, ENTITY_NAME, null)).getBody());
	}

	private String getChapterMessage(final String id, final String name, List<String> children) {
		StringBuilder stringBuilder = new StringBuilder("{ " +
				"\"id\":\"" + id + "\"," +
				"\"name\":\"" + name + "\"," +
				"\"title\":\"Caetano's growth tales and fantastic friends\"," +
				"\"imgPath\":\"caetano\"," +
				"\"text\":\"this will be a series of sections regarding caetano's experiences\"," +
				"\"orderIndex\":\"1\"");
		if(children != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"paragraphs\": [ \n");
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

	private void testParagraph() {
		testParagraphInsert();
		testParagraphUpdate();
		testParagraphFindBy();
		testParagraphFindAll();
		testParagraphAddChild();
		testParagraphDelChild();
		testParagraphDelete();
	}

	private void testParagraphInsert() {
		ResponseEntity<Paragraph> result = paragraphController.upsert(getParagraphMessage(ENTITY_ID, ENTITY_NAME, null));
		List<Paragraph> all = paragraphRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Paragraph entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testParagraphUpdate() {
		ResponseEntity<Paragraph> result = paragraphController.upsert(getParagraphMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null));

		List<Paragraph> all = paragraphRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Paragraph entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testParagraphFindBy() {
		ResponseEntity<Paragraph> result = paragraphController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(paragraphRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), paragraphRepository.findById(ENTITY_ID).get());
	}

	private void testParagraphFindAll() {
		paragraphController.upsert(getParagraphMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME, null));

		List<Paragraph> all = paragraphRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Paragraph entity1 = paragraphRepository.findById(ENTITY_ID).get();
		Paragraph entity2 = paragraphRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(entity1));
		Assertions.assertTrue(all.contains(entity2));
	}

	private void testParagraphAddChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Paragraph> result = paragraphController.addChild(getParagraphMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Paragraph entity = paragraphRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getAccountables().size(), 1);
		Assertions.assertEquals(entity.getAccountables().get(0), ENTITY_CHILD_ID);
	}

	private void testParagraphDelChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Paragraph> result = paragraphController.delChild(getParagraphMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children));
		Paragraph entity = paragraphRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(entity.getAccountables().size(), 0);
	}

	private void testParagraphDelete() {
		ResponseEntity<Paragraph> result = paragraphController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(paragraphController.find(getParagraphMessage(ENTITY_ID, ENTITY_NAME, null)).getBody());
	}

	private String getParagraphMessage(final String id, final String name, List<String> children) {
		StringBuilder stringBuilder = new StringBuilder("{ " +
				"\"id\":\"" + id + "\"," +
				"\"name\":\"" + name + "\"," +
				"\"title\":\"Caetano's growth tales and fantastic friends\"," +
				"\"imgPath\":\"caetano\"," +
				"\"text\":\"this will be a series of sections regarding caetano's experiences\"," +
				"\"orderIndex\":\"1\"");
		if(children != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"accountables\": [ \n");
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

	private void testAccountables() {
		testAccountablesInsert();
		testAccountablesUpdate();
		testAccountablesFindBy();
		testAccountablesFindAll();
		testAccountablesDelete();
	}

	private void testAccountablesInsert() {
		ResponseEntity<Accountables> result = accountableController.upsert(getAccountablesMessage(ENTITY_ID, ENTITY_NAME));
		List<Accountables> all = accountableRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Accountables entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testAccountablesUpdate() {
		ResponseEntity<Accountables> result = accountableController.upsert(getAccountablesMessage(ENTITY_ID, ENTITY_NAME_UPDATE));

		List<Accountables> all = accountableRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Accountables entity = all.get(0);
		Assertions.assertEquals(entity.getId(), ENTITY_ID);
		Assertions.assertEquals(entity.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), entity);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testAccountablesFindBy() {
		ResponseEntity<Accountables> result = accountableController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(accountableRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), accountableRepository.findById(ENTITY_ID).get());
	}

	private void testAccountablesFindAll() {
		accountableController.upsert(getAccountablesMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME));

		List<Accountables> all = accountableRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Accountables entity1 = accountableRepository.findById(ENTITY_ID).get();
		Accountables entity2 = accountableRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(entity1));
		Assertions.assertTrue(all.contains(entity2));
	}

	private void testAccountablesDelete() {
		ResponseEntity<Accountables> result = accountableController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(accountableController.find(getAccountablesMessage(ENTITY_ID, ENTITY_NAME)).getBody());
	}

	private String getAccountablesMessage(final String id, final String name) {
		StringBuilder stringBuilder = new StringBuilder("{ " +
				"\"id\":\"" + id + "\"," +
				"\"name\":\"" + name + "\"," +
				"\"amount\":1," +
				"\"visible\":true");

		stringBuilder.append("\n}");
		return stringBuilder.toString();
	}

	private void testDumpAndRestoreUtils() {
		clearDb();
		testDumpAndRestoreLoadData();

		databaseCsvDumper.dumpAll();
		clearDb();
		databaseCsvLoader.loadAll();

		List<Tale> tales = taleRepository.findAll();
		Assertions.assertFalse(tales.isEmpty());
		Assertions.assertEquals(tales.size(), 1);
		Tale tale = tales.get(0);
		Assertions.assertEquals(tale.getId(), ENTITY_ID);
		Assertions.assertEquals(tale.getName(), ENTITY_NAME);
		Assertions.assertFalse(tale.getBooks().isEmpty());
		Assertions.assertEquals(tale.getBooks().get(0), ENTITY_CHILD_ID);

		List<Book> books = bookRepository.findAll();
		Assertions.assertFalse(books.isEmpty());
		Assertions.assertEquals(books.size(), 1);
		Book book = books.get(0);
		Assertions.assertEquals(book.getId(), ENTITY_ID);
		Assertions.assertEquals(book.getName(), ENTITY_NAME);
		Assertions.assertFalse(book.getSections().isEmpty());
		Assertions.assertEquals(book.getSections().get(0), ENTITY_CHILD_ID);

		List<Section> sections = sectionRepository.findAll();
		Assertions.assertFalse(sections.isEmpty());
		Assertions.assertEquals(sections.size(), 1);
		Section section = sections.get(0);
		Assertions.assertEquals(section.getId(), ENTITY_ID);
		Assertions.assertEquals(section.getName(), ENTITY_NAME);
		Assertions.assertFalse(section.getChapter().isEmpty());
		Assertions.assertEquals(section.getChapter().get(0), ENTITY_CHILD_ID);

		List<Chapter> chapters = chapterRepository.findAll();
		Assertions.assertFalse(chapters.isEmpty());
		Assertions.assertEquals(chapters.size(), 1);
		Chapter chapter = chapters.get(0);
		Assertions.assertEquals(chapter.getId(), ENTITY_ID);
		Assertions.assertEquals(chapter.getName(), ENTITY_NAME);
		Assertions.assertFalse(chapter.getParagraphs().isEmpty());
		Assertions.assertEquals(chapter.getParagraphs().get(0), ENTITY_CHILD_ID);

		List<Paragraph> paragraphs = paragraphRepository.findAll();
		Assertions.assertFalse(paragraphs.isEmpty());
		Assertions.assertEquals(paragraphs.size(), 1);
		Paragraph paragraph = paragraphs.get(0);
		Assertions.assertEquals(paragraph.getId(), ENTITY_ID);
		Assertions.assertEquals(paragraph.getName(), ENTITY_NAME);
		Assertions.assertFalse(paragraph.getAccountables().isEmpty());
		Assertions.assertEquals(paragraph.getAccountables().get(0), ENTITY_CHILD_ID);
		Assertions.assertEquals(paragraph.getAccountables().get(1), ENTITY_CHILD_ID_2);

		List<Accountables> accountables = accountableRepository.findAll();
		Assertions.assertFalse(accountables.isEmpty());
		Assertions.assertEquals(accountables.size(), 2);
		Accountables accountable = accountables.get(0);
		Assertions.assertEquals(accountable.getId(), ENTITY_ID);
		Assertions.assertEquals(accountable.getName(), ENTITY_NAME);
		accountable = accountables.get(1);
		Assertions.assertEquals(accountable.getId(), ENTITY_ID_2);
		Assertions.assertEquals(accountable.getName(), ENTITY_NAME);
	}

	private void testDumpAndRestoreLoadData() {
		taleController.upsert(getTaleMessage(ENTITY_ID, ENTITY_NAME, null));
		taleController.addChild(getTaleMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID)));
		bookController.upsert(getBookMessage(ENTITY_ID, ENTITY_NAME, null));
		bookController.addChild(getBookMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID)));
		sectionController.upsert(getSectionMessage(ENTITY_ID, ENTITY_NAME, null));
		sectionController.addChild(getSectionMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID)));
		chapterController.upsert(getChapterMessage(ENTITY_ID, ENTITY_NAME, null));
		chapterController.addChild(getChapterMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID)));
		paragraphController.upsert(getParagraphMessage(ENTITY_ID, ENTITY_NAME, null));
		paragraphController.addChild(getParagraphMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID)));
		paragraphController.addChild(getParagraphMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID_2)));
		accountableController.upsert(getAccountablesMessage(ENTITY_ID, ENTITY_NAME));
		accountableController.upsert(getAccountablesMessage(ENTITY_ID_2, ENTITY_NAME));
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
