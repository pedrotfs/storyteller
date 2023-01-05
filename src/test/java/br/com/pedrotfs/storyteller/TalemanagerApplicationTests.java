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
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
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
	private RegistryController registryController;

	@Autowired
	private AccountableRepository accountableRepository;

	@Autowired
	private RegistryRepository registryRepository;

	@Autowired
	private DatabaseCsvDumper databaseCsvDumper;

	@Autowired
	private DatabaseCsvLoader databaseCsvLoader;

	private final static String ENTITY_ID = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fa";

	private final static String ENTITY_ID_2 = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fe";

	private final static String ENTITY_CHILD_ID = "d04b98f48e8f8bcc15c6ae5ac050801cd6dcfd428fb5f9e65c4e16e7807340fc";

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
		testRegistry();
		testAccountables();
	}

	private void testRegistry() {
		testRegistryInsert();
		testRegistryUpdate();
		testRegistryFindBy();
		testRegistryFindAll();
		testRegistryAddChild();
		testRegistryDelChild();
		testRegistryAddAccountable();
		testRegistryDelAccountable();
		testRegistryDelete();
	}

	private void testRegistryInsert() {
		ResponseEntity<Registry> result = registryController.upsert(getRegistryMessage(ENTITY_ID, ENTITY_NAME, null, null));

		List<Registry> all = registryRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Registry registry = all.get(0);
		Assertions.assertEquals(registry.getId(), ENTITY_ID);
		Assertions.assertEquals(registry.getName(), ENTITY_NAME);

		Assertions.assertEquals(result.getBody(), registry);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testRegistryUpdate() {
		ResponseEntity<Registry> result = registryController.upsert(getRegistryMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null, null));

		List<Registry> all = registryRepository.findAll();
		Assertions.assertEquals(all.size(), 1);

		Registry registry = all.get(0);
		Assertions.assertEquals(registry.getId(), ENTITY_ID);
		Assertions.assertEquals(registry.getName(), ENTITY_NAME_UPDATE);

		Assertions.assertEquals(result.getBody(), registry);
		Assertions.assertEquals(result.getStatusCodeValue(), STATUS_CODE_OK);
	}

	private void testRegistryFindBy() {
		ResponseEntity<Registry> result = registryController.find(ENTITY_ID);
		Assertions.assertNotNull(result.getBody());
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Assertions.assertTrue(registryRepository.findById(ENTITY_ID).isPresent());
		Assertions.assertEquals(result.getBody(), registryRepository.findById(ENTITY_ID).get());
	}

	private void testRegistryFindAll() {
		registryController.upsert(getRegistryMessage(ENTITY_ID_DUPLICATE, ENTITY_NAME, null, null));

		List<Registry> all = registryRepository.findAll();
		Assertions.assertEquals(all.size(), 2);
		Registry registry = registryRepository.findById(ENTITY_ID).get();
		Registry registry2 = registryRepository.findById(ENTITY_ID).get();
		Assertions.assertTrue(all.contains(registry));
		Assertions.assertTrue(all.contains(registry2));
	}

	private void testRegistryAddChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Registry> result = registryController.addChild(getRegistryMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children, null));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Registry registry = registryRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(registry.getChilds().size(), 1);
		Assertions.assertEquals(registry.getChilds().get(0), ENTITY_CHILD_ID);
	}

	private void testRegistryDelChild() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Registry> result = registryController.delChild(getRegistryMessage(ENTITY_ID, ENTITY_NAME_UPDATE, children, null));
		Registry registry = registryRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(registry.getChilds().size(), 0);
	}

	private void testRegistryAddAccountable() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);
		accountableController.upsert(getAccountablesMessage(ENTITY_CHILD_ID, ENTITY_CHILD_ID));
		ResponseEntity<Registry> result = registryController.addAccountable(getRegistryMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null, children));
		Assertions.assertEquals(result.getBody().getId(), ENTITY_ID);
		Registry registry = registryRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(registry.getAccountables().size(), 1);
		Assertions.assertEquals(registry.getAccountables().get(0), ENTITY_CHILD_ID);
	}

	private void testRegistryDelAccountable() {
		List<String> children = new ArrayList<>();
		children.add(ENTITY_CHILD_ID);

		ResponseEntity<Registry> result = registryController.delAccountable(getRegistryMessage(ENTITY_ID, ENTITY_NAME_UPDATE, null, children));
		Registry registry = registryRepository.findById(ENTITY_ID).get();
		Assertions.assertEquals(registry.getAccountables().size(), 0);
		Assertions.assertEquals(0 , accountableRepository.findAll().size());
	}

	private void testRegistryDelete() {
		ResponseEntity<Registry> result = registryController.delete(ENTITY_ID);
		Assertions.assertEquals(ENTITY_ID, result.getBody().getId());
		Assertions.assertNull(registryController.find(getRegistryMessage(ENTITY_ID, ENTITY_NAME, null, null)).getBody());
	}

	private String getRegistryMessage(final String id, final String name, List<String> children, List<String> accountables) {
		StringBuilder stringBuilder = new StringBuilder("{ \n" +
				"    \"id\": \"" + id + "\",\n" +
				"    \"name\": \"" + name + "\",\n" +
				"    \"title\": \"test title\",\n" +
				"    \"imgPath\": \"testtitle01.png\",\n" +
				"    \"text\": \"text is being texted here\",\n" +
				"    \"type\": \"Chapter\",\n" +
				"    \"orderIndex\": \"a\",\n" +
				"    \"owner\": \"Admin\"");
		if(children != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"childs\": [ \n");
			int i = 1;
			for(String child : children) {
				stringBuilder.append("\"").append(child).append("\"");
				if(i++ < children.size()) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append(" ] \n");
		}
		if(accountables != null) {
			stringBuilder.append(", \n");
			stringBuilder.append("\"accountables\": [ \n");
			int i = 1;
			for(String child : accountables) {
				stringBuilder.append("\"").append(child).append("\"");
				if(i++ < accountables.size()) {
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

		List<Registry> registries = registryRepository.findAll();
		Assertions.assertFalse(registries.isEmpty());
		Assertions.assertEquals(registries.size(), 2);
		Registry registry = registries.get(0);
		Assertions.assertEquals(registry.getId(), ENTITY_ID);
		Assertions.assertEquals(registry.getName(), ENTITY_NAME);
		Assertions.assertFalse(registry.getAccountables().isEmpty());
		Assertions.assertEquals(registry.getAccountables().get(0), ENTITY_ID);

		List<Accountables> accountables = accountableRepository.findAll();
		Assertions.assertFalse(accountables.isEmpty());
		Assertions.assertEquals(accountables.size(), 1);
		Accountables accountable = accountables.get(0);
		Assertions.assertEquals(accountable.getId(), ENTITY_ID);
		Assertions.assertEquals(accountable.getName(), ENTITY_NAME);
	}

	private void testDumpAndRestoreLoadData() {

		accountableController.upsert(getAccountablesMessage(ENTITY_ID, ENTITY_NAME));
		registryController.upsert(getRegistryMessage(ENTITY_ID, ENTITY_NAME, null, null));
		registryController.upsert(getRegistryMessage(ENTITY_CHILD_ID, ENTITY_NAME, null, null));
		registryController.addChild(getRegistryMessage(ENTITY_ID, ENTITY_NAME, Collections.singletonList(ENTITY_CHILD_ID), null));
		registryController.addAccountable(getRegistryMessage(ENTITY_ID, ENTITY_NAME, null, Collections.singletonList(ENTITY_ID)));
	}

	private void clearDb() {
		registryRepository.deleteAll();
		accountableRepository.deleteAll();
	}

}
