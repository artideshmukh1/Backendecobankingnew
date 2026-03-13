package com.ecobank.core.Repository;

import com.ecobank.core.Enums.KycStatus;
import com.ecobank.core.models.Player;
import com.ecobank.core.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByKycStatus(KycStatus kycStatus);

    boolean existsByName(String name);

    Optional<Player> findByContactEmail(String contactEmail);

    boolean existsByContactEmail(String contactEmail);
}

