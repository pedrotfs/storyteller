package br.com.pedrotfs.storyteller.helper;

import br.com.pedrotfs.storyteller.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoryTellerFeatureTestHelper {

    public static final String ID = "taleId";

    public static final String NAME = "taleName";

    public static final String TITLE = "taleTitle";

    public static final String TEXT = "taleText";

    public static final String OWNER = "taleOwner";

    public static final String TYPE = "Tale";

    public static final String ORDER = "taleOrder";

    public static final String IMAGE = "taleImg";

    public static final String ID_2 = "taleId";

    public static final String ACCOUNTABLE_1_ID = "acc1Id";

    public static final String ACCOUNTABLE_1_NAME = "accountable1";

    public static final Integer ACCOUNTABLE_1_AMOUNT = 1;

    public static final String ACCOUNTABLE_2_ID = "acc2Id";

    public static final String ACCOUNTABLE_2_NAME = "accountable2";

    public static final Integer ACCOUNTABLE_2_AMOUNT = 10;

    public static Registry createTestRegistry() {
        List<String> children = new ArrayList<>();
        children.add(ID_2);
        return new Registry(ID, NAME, TITLE, IMAGE, TEXT, TYPE, ORDER, OWNER, children, Collections.singletonList(ACCOUNTABLE_1_ID));
    }

    public static Accountables createTestAccountable(final String id, final String name, final Integer amount) {
        return new Accountables(id, name, amount, Boolean.TRUE, "title", "icon");
    }
}
