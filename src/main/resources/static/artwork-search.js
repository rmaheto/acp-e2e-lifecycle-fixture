/* Host API contract: GET /api/artworks/search with optional artist, title, medium,
   minimumPrice and maximumPrice query parameters. Expected response: a JSON array of
   artwork objects with artist, title, medium and price fields. */
(() => {
  const form = document.querySelector("#artwork-search-form");
  const results = document.querySelector("#artwork-results");
  const status = document.querySelector("#search-status");
  const button = document.querySelector("#search-button");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    results.replaceChildren();
    const parameters = new URLSearchParams();
    for (const name of ["artist", "title", "medium", "minimumPrice", "maximumPrice"]) {
      const value = new FormData(form).get(name)?.toString().trim();
      if (value) parameters.set(name, value);
    }

    status.textContent = "Searching artworks…";
    button.disabled = true;
    try {
      const query = parameters.toString();
      const response = await fetch(`/api/artworks/search${query ? `?${query}` : ""}`, {
        headers: { Accept: "application/json" }
      });
      if (!response.ok) throw new Error(`Search request failed (${response.status}).`);
      const artworks = await response.json();
      if (!Array.isArray(artworks)) throw new Error("The search service returned an unexpected response.");
      if (artworks.length === 0) {
        status.textContent = "No artworks matched those details. Try changing or removing a search field.";
        return;
      }
      artworks.forEach((artwork) => results.append(makeArtworkCard(artwork)));
      status.textContent = `${artworks.length} ${artworks.length === 1 ? "artwork" : "artworks"} found.`;
    } catch (error) {
      status.textContent = error instanceof TypeError
        ? "We could not connect to the search service. Please try again later."
        : (error.message || "The search could not be completed. Please try again.");
    } finally {
      button.disabled = false;
    }
  });

  function makeArtworkCard(artwork) {
    const item = document.createElement("li");
    item.className = "result-card";
    const heading = document.createElement("h3");
    heading.textContent = artwork.title || "Untitled artwork";
    item.append(heading);
    addDetail(item, "Artist", artwork.artist);
    addDetail(item, "Medium", artwork.medium);
    if (artwork.price !== null && artwork.price !== undefined && artwork.price !== "") {
      const price = document.createElement("p");
      price.className = "price";
      price.textContent = `Price: ${formatPrice(artwork.price)}`;
      item.append(price);
    }
    return item;
  }

  function addDetail(parent, label, value) {
    if (value === null || value === undefined || value === "") return;
    const detail = document.createElement("p");
    detail.textContent = `${label}: ${value}`;
    parent.append(detail);
  }

  function formatPrice(value) {
    const amount = Number(value);
    return Number.isFinite(amount)
      ? new Intl.NumberFormat(undefined, { style: "currency", currency: "USD" }).format(amount)
      : String(value);
  }
})();
