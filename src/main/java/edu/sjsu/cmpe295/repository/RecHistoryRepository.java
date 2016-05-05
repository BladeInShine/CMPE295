package edu.sjsu.cmpe295.repository;

import edu.sjsu.cmpe295.domain.RecHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RecHistory entity.
 */
public interface RecHistoryRepository extends JpaRepository<RecHistory,Long> {

    @Query("select recHistory from RecHistory recHistory where recHistory.user.login = ?#{principal.username}")
    List<RecHistory> findByUserIsCurrentUser();

    List<RecHistory> findAllByUserIdAndBrandId(long userId, String brandId);

}
