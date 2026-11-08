# acp-e2e-lifecycle-fixture

Disposable fixture for the AI Engineering Control Plane end-to-end lifecycle acceptance.
It holds a Java 21 / Maven application and tests.

## Run the artwork search

Start the application with:

```sh
mvn spring-boot:run
```

Open <http://localhost:8080/> for the visitor search page. Run automated tests with `mvn test`.

## Artwork search API

`GET /api/artworks/search` accepts these optional query parameters:

- `artist`, `title`, `medium`: text to match.
- `minPrice`, `maxPrice`: non-negative decimal price bounds.

Omitted and blank parameters are ignored. All supplied filters are combined with AND. Text matching is a case-insensitive substring match. Price bounds are inclusive. Artworks with a missing text attribute do not match a text filter; artworks with a missing price do not match a price-bound filter. Supplying malformed or negative prices, or a minimum above the maximum, returns HTTP 400 with a JSON `error` message.

Example request:

```text
GET /api/artworks/search?artist=monet&minPrice=10&maxPrice=1000
```

Example response:

```json
{"results":[{"id":"11111111-1111-4111-8111-111111111111","artist":"Claude Monet","title":"Water Lilies","medium":"Oil on canvas","price":850.00}],"count":1}
```

An empty search returns all catalog entries. `count` is the number of returned results. The API and browser page are served by the same application.

## Demo catalog limitation

The default provider supplies deterministic in-memory demo artworks so a fresh run can demonstrate search. This is not a production gallery catalog or current inventory; it is replaceable through the `ArtworkCatalog` provider boundary. No purchasing, catalog editing, or production catalog persistence is included.
