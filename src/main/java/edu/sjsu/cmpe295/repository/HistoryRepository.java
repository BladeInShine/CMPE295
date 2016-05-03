package edu.sjsu.cmpe295.repository;

import edu.sjsu.cmpe295.domain.History;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the History entity.
 */
public interface HistoryRepository extends JpaRepository<History,Long> {

    @Query("select history from History history where history.user.login = ?#{principal.username}")
    List<History> findByUserIsCurrentUser();

    List<History> findByUserId(long userid);
}
