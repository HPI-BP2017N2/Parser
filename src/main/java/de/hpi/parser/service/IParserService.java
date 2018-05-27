package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;

public interface IParserService {

    void extractData(CrawledPage crawledPage);
}
