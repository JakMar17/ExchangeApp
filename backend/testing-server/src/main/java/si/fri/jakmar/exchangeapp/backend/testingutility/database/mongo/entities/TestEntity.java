package si.fri.jakmar.exchangeapp.backend.testingutility.database.mongo.entities;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@Document
public class TestEntity {
    @Id
    private String objectId;

    private String test;
}
