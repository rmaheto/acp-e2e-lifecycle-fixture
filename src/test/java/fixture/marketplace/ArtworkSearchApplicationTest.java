package fixture.marketplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtworkSearchApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchesAndReturnsDocumentedJsonShape() throws Exception {
        mockMvc.perform(get("/api/artworks/search").param("artist", "mOnEt").param("minPrice", "850").param("maxPrice", "850"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].id").value("11111111-1111-4111-8111-111111111111"))
                .andExpect(jsonPath("$.results[0].artist").value("Claude Monet"))
                .andExpect(jsonPath("$.results[0].title").value("Water Lilies"))
                .andExpect(jsonPath("$.results[0].medium").value("Oil on canvas"))
                .andExpect(jsonPath("$.results[0].price").value(850.00));
    }

    @Test
    void malformedAndInvertedPriceRangesAreClientErrors() throws Exception {
        mockMvc.perform(get("/api/artworks/search").param("minPrice", "not-a-price"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists());
        mockMvc.perform(get("/api/artworks/search").param("minPrice", "20").param("maxPrice", "10"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists());
        mockMvc.perform(get("/api/artworks/search").param("minPrice", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void servesVisitorPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Find an artwork")))
                .andExpect(content().string(containsString("search-form")));
    }
}
