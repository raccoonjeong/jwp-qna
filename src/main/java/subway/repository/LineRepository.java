package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Line;
import subway.domain.Station;

interface LineRepository extends JpaRepository<Line, Long> {
    Line findByName(String name); // (1)
}