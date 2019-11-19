package dapp.dapp.repository;

import dapp.dapp.domain.BCTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BCTransactionRepository extends MongoRepository<BCTransaction, String> {

    BCTransaction findByTransactionId(String transactionId);
}
