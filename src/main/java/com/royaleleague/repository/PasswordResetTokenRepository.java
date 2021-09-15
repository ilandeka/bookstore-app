package com.royaleleague.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.royaleleague.domain.User;
import com.royaleleague.domain.security.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	PasswordResetToken findByToken(String token);
	
	PasswordResetToken findByUser(User user);
	
	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);
	
	// Used to delete all expired tokens
	@Modifying
	@Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate <= ?1")
	void deleteAllExpiredSince(Date now);
}
