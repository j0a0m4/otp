package io.j0a0m4.otp.adapters.driven;

import io.j0a0m4.otp.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserMongoRepository extends MongoRepository<User, UUID> {

}
