package loader.repository;

import loader.entity.Variant;
import loader.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    Boolean existsByVariantName(String variantName);
    Set<Variant> findAllByVariantOwnerAndVariantLanguage(long variantOwner, Language variantLanguage);
}