package com.jeffersonvilla.HabitsTracker.service.implementations;

import static com.jeffersonvilla.HabitsTracker.service.messages.MessageConstants.INVALID_VERIFICATION_TOKEN;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jeffersonvilla.HabitsTracker.exceptions.VerificationTokenNotExistsException;
import com.jeffersonvilla.HabitsTracker.model.User;
import com.jeffersonvilla.HabitsTracker.model.VerificationToken;
import com.jeffersonvilla.HabitsTracker.repository.VerificationTokenRepo;
import com.jeffersonvilla.HabitsTracker.service.interfaces.VerificationTokenService;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{

    private final VerificationTokenRepo verificationTokenRepo;

    public VerificationTokenServiceImpl(VerificationTokenRepo verificationTokenRepo){
        this.verificationTokenRepo = verificationTokenRepo;
    }

    @Override
    public VerificationToken generateToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken emailVerfication = new VerificationToken();
        emailVerfication.setToken(token);
        emailVerfication.setUser(user);

        return verificationTokenRepo.save(emailVerfication);
    }

    @Override
    public VerificationToken verifyToken(String token) {

        if(verificationTokenRepo.existsByToken(token)){
            return verificationTokenRepo.findByToken(token).get();
        }

        throw new VerificationTokenNotExistsException(INVALID_VERIFICATION_TOKEN);
    }
    
}
