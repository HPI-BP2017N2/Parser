package de.hpi.parser.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParsedOfferRepository extends MongoRepository<ParsedOffer, Long> {

}
