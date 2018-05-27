package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.persistence.ParsedOffer;

import java.util.concurrent.CompletableFuture;

public interface IParserService {

    CompletableFuture<ParsedOffer> extractData(CrawledPage crawledPage);
}
