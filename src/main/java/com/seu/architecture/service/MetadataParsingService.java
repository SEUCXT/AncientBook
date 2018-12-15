package com.seu.architecture.service;

import com.seu.architecture.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 解析元数据，书本的登记表
 */
public interface MetadataParsingService {

    List<Book> parseBookXssf(String type, InputStream is) throws IOException;

    List<Book> parseAncientBookXssf(InputStream is) throws IOException;

    List<Book> parseAncientBookHssf(InputStream is) throws IOException;

    List<Book> parseRepublicBookXssf(InputStream is) throws IOException;

    List<Book> parseRepublicBookHssf(InputStream is) throws IOException;

    List<Book> parseCharacteristicBookXssf(InputStream is) throws IOException;

    List<Book> parseCharacteristicBookHssf(InputStream is) throws IOException;

    List<User> parseUsersXssf(InputStream is) throws IOException;

    List<User> parseUsersHssf(InputStream is) throws IOException;
}

