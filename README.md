# acp-e2e-lifecycle-fixture

Disposable fixture for the AI Engineering Control Plane end-to-end lifecycle acceptance.
It holds a Java 21 / Maven application and tests.

## Run the artwork search

Start the application with:

```sh
mvn spring-boot:run
```

Open <http://localhost:8080/> for the visitor search and reservation page. Run automated tests with `mvn test`.

## Artwork search API

`GET /api/artworks/search` accepts these optional query parameters:

- `artist`, `title`, `medium`: text to match.
- `minPrice`, `maxPrice`: non-negative decimal price bounds.

Omitted and blank parameters are ignored. All supplied filters are combined with AND. Text matching is a case-insensitive substring match. Price bounds are inclusive. Artworks with a missing text attribute do not match a text filter; artworks with a missing price do not match a price-bound filter. Supplying malformed or negative prices, or a minimum above the maximum, returns HTTP 400 with a JSON `error` message. Each result includes an `available` flag reflecting current reservation availability.

Example request:

```text
GET /api/artworks/search?artist=monet&minPrice=10&maxPrice=1000
```

## Reservations

Visitors can reserve an available artwork for 72 hours. Create a reservation by sending the artwork UUID to `POST /api/reservations`:

```http
Content-Type: application/json

{"artworkId":"11111111-1111-4111-8111-111111111111"}
```

A successful request returns HTTP 201 and the reservation, including its `RESERVED` status, creation time, and expiry time. Unknown artwork IDs return HTTP 400; an artwork with an active reservation returns HTTP 409. An active reservation makes the artwork unavailable until its expiry, exactly 72 hours after creation. Expired reservations no longer block a new reservation. Reservation data is held in memory and does not survive application restarts.

The default provider supplies deterministic in-memory demo artworks so a fresh run can demonstrate search and reservations. This is not a production gallery catalog or current inventory; it is replaceable through the `ArtworkCatalog` provider boundary. No purchasing, catalog editing, or production catalog persistence is included.
