package de.hpi.parser.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class ParsedOfferRepository implements IParsedOfferRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void save(ParsedOffer parsedOffer) {
        getMongoTemplate().save(parsedOffer, Long.toString(parsedOffer.getShopID()));
    }

}
