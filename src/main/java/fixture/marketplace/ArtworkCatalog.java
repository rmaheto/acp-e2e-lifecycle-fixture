package fixture.marketplace;

import java.util.List;

/** Supplies artwork records to the search capability. */
public interface ArtworkCatalog {
    List<Artwork> findAll();
}
