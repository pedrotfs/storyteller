package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.pedrotfs.storyteller.helper.StoryTellerFeatureTestHelper.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultDatabaseCsvLoaderTest {

    @InjectMocks
    private DefaultDatabaseCsvLoader defaultDatabaseCsvLoader;

    @Mock
    private RegistryService registryService;

    @Mock
    private AccountableService accountableService;

    private final Registry registry = createTestRegistry();

    private final Accountables accountables1 = createTestAccountable(ACCOUNTABLE_1_ID, ACCOUNTABLE_1_NAME, ACCOUNTABLE_1_AMOUNT);

    private final Accountables accountables2 = createTestAccountable(ACCOUNTABLE_2_ID, ACCOUNTABLE_2_NAME, ACCOUNTABLE_2_AMOUNT);

    private AutoCloseable closeable;

    @BeforeEach
    private void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        doReturn(registry).when(registryService).upsert(registry);
        doReturn(accountables1).when(accountableService).upsert(accountables1);
        doReturn(accountables2).when(accountableService).upsert(accountables2);

        defaultDatabaseCsvLoader.setPath("src/main/resources/dbDump/");
    }

    @Test
    public void testDump() {
        defaultDatabaseCsvLoader.loadAll();

        verify(registryService).upsert(registry);
        verify(accountableService).upsert(accountables1);
        verify(accountableService).upsert(accountables2);
    }

    @AfterEach
    private void cleanUp() throws Exception {
        closeable.close();
    }

}