package edu.sjsu.cmpe295.repository;

import edu.sjsu.cmpe295.domain.Rating;
import edu.sjsu.cmpe295.domain.RatingPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by BladeInShine on 16/5/4.
 */
public interface RatingRepository extends JpaRepository<Rating,RatingPK> {

    Optional<Rating> findOneByUserIdAndItemId(long userId, long itemId);

}
