package br.com.pedrotfs.storyteller.helper;

import br.com.pedrotfs.storyteller.domain.*;

import java.util.ArrayList;
import java.util.List;

public class StoryTellerFeatureTestHelper {

    public static final String TALE_ID = "taleId";

    public static final String TALE_NAME = "taleName";

    public static final String TALE_TITLE = "taleTitle";

    public static final String TALE_TEXT = "taleText";

    public static final String TALE_OWNER = "taleOwner";

    public static final String BOOK_ID = "BookId";

    public static final String BOOK_NAME = "bookName";

    public static final String BOOK_TITLE = "bookTitle";

    public static final String BOOK_TEXT = "bookText";

    public static final String BOOK_TIME = "bookTime";

    public static final String BOOK_ORDER = "bookOrder";

    public static final String SECTION_ID = "sectionId";

    public static final String SECTION_NAME = "sectionName";

    public static final String SECTION_TITLE = "sectionTitle";

    public static final String SECTION_TEXT = "sectionText";

    public static final String SECTION_ORDER = "sectionOrder";

    public static final String CHAPTER_ID = "chapterId";

    public static final String CHAPTER_NAME = "chapterName";

    public static final String CHAPTER_TITLE = "chapterTitle";

    public static final String CHAPTER_TEXT = "chapterText";

    public static final String CHAPTER_ORDER = "chapterOrder";

    public static final String PARAGRAPH_1__ID = "paragraph1Id";

    public static final String PARAGRAPH_1__NAME = "paragraph1Name";

    public static final String PARAGRAPH_1__TITLE = "paragraph1Title";

    public static final String PARAGRAPH_1__TEXT = "paragraph1Text";

    public static final String PARAGRAPH_1__ORDER = "paragraph1Order";

    public static final String PARAGRAPH_2__ID = "paragraph2Id";

    public static final String PARAGRAPH_2__NAME = "paragraph2Name";

    public static final String PARAGRAPH_2__TITLE = "paragraph2Title";

    public static final String PARAGRAPH_2__TEXT = "paragraph2Text";

    public static final String PARAGRAPH_2__ORDER = "paragraph2Order";

    public static final String ACCOUNTABLE_1_ID = "acc1Id";

    public static final String ACCOUNTABLE_1_NAME = "accountable1";

    public static final Integer ACCOUNTABLE_1_AMOUNT = 1;

    public static final String ACCOUNTABLE_2_ID = "acc2Id";

    public static final String ACCOUNTABLE_2_NAME = "accountable2";

    public static final Integer ACCOUNTABLE_2_AMOUNT = 10;

    public static Tale createTestTale() {
        List<String> children = new ArrayList<>();
        children.add(BOOK_ID);
        return new Tale(TALE_NAME, TALE_ID, TALE_TITLE, TALE_TITLE + "/", TALE_TEXT, children, TALE_OWNER);
    }

    public static Book createTestBook() {
        List<String> children = new ArrayList<>();
        children.add(SECTION_ID);
        return new Book(BOOK_NAME, BOOK_ID, BOOK_TITLE, BOOK_TITLE + "/", BOOK_TEXT, children, BOOK_TIME, BOOK_ORDER);
    }

    public static Section createTestSection() {
        List<String> children = new ArrayList<>();
        children.add(CHAPTER_ID);
        return new Section(SECTION_NAME, SECTION_ID, SECTION_TITLE, SECTION_TITLE + "/", SECTION_TEXT, children, SECTION_ORDER);
    }

    public static Chapter createTestChapter() {
        List<String> children = new ArrayList<>();
        children.add(PARAGRAPH_1__ID);
        children.add(PARAGRAPH_2__ID);
        return new Chapter(CHAPTER_NAME, CHAPTER_ID, CHAPTER_TITLE, CHAPTER_TITLE + "/", CHAPTER_TEXT, children, CHAPTER_ORDER);
    }

    public static Paragraph createTestParagraph(final String name, final String id, final String text, final String title, final String order) {
        List<String> children = new ArrayList<>();
        Paragraph paragraph = new Paragraph(name, id, title, title + "/", text, children, order);
        if(id.equals(PARAGRAPH_2__ID)) {
            paragraph.getAccountables().add(ACCOUNTABLE_1_ID);
            paragraph.getAccountables().add(ACCOUNTABLE_2_ID);
        }
        return paragraph;
    }

    public static Accountables createTestAccountable(final String id, final String name, final Integer amount) {
        return new Accountables(id, name, amount, Boolean.TRUE, "title", "icon");
    }
}
