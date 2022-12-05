package com.droptheclothes.api.utility;

import java.time.format.DateTimeFormatter;

public class BusinessConstants {

    public static final int CLOTHING_BIN_SEARCH_RADIUS = 500;

    public static final int MAX_IMAGE_COUNT = 3;

    public static final String REPORT_IMAGE_DIRECTORY = "report/";

    public static final String ARTICLE_IMAGE_DIRECTORY = "article/";

    public static final String DELETED_COMMENT = "삭제된 댓글입니다.";

    public static final String DELETED_MARK = "_deleted_";

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final int MAX_REPORT_COUNT = 3;
}
