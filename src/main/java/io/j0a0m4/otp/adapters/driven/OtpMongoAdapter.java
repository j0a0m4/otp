package io.j0a0m4.otp.adapters.driven;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpRecord;
import io.j0a0m4.otp.domain.model.OtpStatus;
import io.j0a0m4.otp.domain.ports.driven.OtpRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class OtpMongoAdapter implements OtpRepository {

    private final OtpRecordMongoRepository otpRepository;

    private final UserMongoRepository userRepository;

    public OtpMongoAdapter(
            final OtpRecordMongoRepository otpRepository,
            final UserMongoRepository userRepository
    ) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Otp> findBy(final UUID userId, final String otp) {
        return otpRepository.findByUserIdAndOtp(userId, otp)
                            .map(OtpRecord::toEntity);
    }

    @Override
    public Optional<Otp> saveIfPresent(final UUID userId, final Otp otp) {
        final var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        otpRepository.save(new OtpRecord(userId, otp));

        return Optional.of(otp);
    }

    @Override
    public Optional<Otp> updateStatusWith(UUID userId, Otp otp, OtpStatus updatedStatus) {
        return otpRepository
                .findByUserIdAndOtp(userId, otp.otp())
                .map(record -> record.with(updatedStatus))
                .map(otpRepository::save)
                .map(OtpRecord::toEntity);
    }

}
