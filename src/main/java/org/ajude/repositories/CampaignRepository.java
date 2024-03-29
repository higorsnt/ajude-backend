package org.ajude.repositories;

import org.ajude.entities.Campaign;
import org.ajude.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByUrlIdentifier(String urlIdentifier);

    List<Campaign> findByShortNameContainingIgnoreCase(String substring);

    List<Campaign> findTop5ByStatusOrderByDeadlineAsc(Status status);

    @Query(value = "SELECT *, (goal - received) as remaining FROM campaign " +
                   "WHERE status=0 ORDER BY remaining ASC LIMIT 5", nativeQuery = true)
    List<Campaign> findTop5ByStatusOrderByRemaining(Status status);

    @Query(value =  "SELECT *, n.q FROM campaign AS c, (SELECT id, COUNT(id_like) AS q " +
                    "FROM (SELECT * FROM campaign WHERE status = 0) " +
                    "AS t LEFT JOIN like_table as l on t.id = l.id_campaign " +
                    "GROUP BY id) AS n WHERE c.id = n.id ORDER BY q DESC LIMIT 5", nativeQuery = true)
    List<Campaign> findTop5ByStatusOrderByLikes();
}
