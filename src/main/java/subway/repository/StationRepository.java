package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Station;

interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String name); // (1)
}