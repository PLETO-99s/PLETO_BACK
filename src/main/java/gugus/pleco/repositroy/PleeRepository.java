package gugus.pleco.repositroy;

import gugus.pleco.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import gugus.pleco.domain.Plee;

import java.util.List;
import java.util.Optional;

public interface PleeRepository extends JpaRepository<Plee, Long> {
    List<Plee> findByUser(User user);

    Optional<Plee> findByPleeName(String pleeName);
}
