package loader.repository;

import loader.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    Boolean existsByVariantName(String variantName);
}