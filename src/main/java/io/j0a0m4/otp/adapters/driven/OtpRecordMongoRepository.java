package io.j0a0m4.otp.adapters.driven;

import io.j0a0m4.otp.domain.model.OtpRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRecordMongoRepository extends MongoRepository<OtpRecord, UUID> {

    Optional<OtpRecord> findByUserIdAndOtp(UUID userId, String otp);

}
