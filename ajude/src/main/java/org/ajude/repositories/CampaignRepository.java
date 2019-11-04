package org.ajude.repositories;

import org.ajude.entities.campaigns.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {
}
