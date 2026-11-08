(() => {
  const form = document.getElementById('search-form');
  const status = document.getElementById('status');
  const list = document.getElementById('results-list');
  const button = form.querySelector('button[type="submit"]');

  function addText(parent, tag, text, className) {
    const element = document.createElement(tag);
    element.textContent = text;
    if (className) element.className = className;
    parent.appendChild(element);
    return element;
  }

  function render(artworks) {
    list.replaceChildren();
    for (const artwork of artworks) {
      const item = document.createElement('li');
      item.className = 'artwork-card';
      addText(item, 'h3', artwork.title || 'Untitled artwork');
      addText(item, 'p', `Artist: ${artwork.artist || 'Not provided'}`);
      addText(item, 'p', `Medium: ${artwork.medium || 'Not provided'}`);
      addText(item, 'p', `Price: ${artwork.price == null ? 'Not provided' : Number(artwork.price).toLocaleString(undefined, { style: 'currency', currency: 'USD' })}`);
      const available = artwork.available === true;
      addText(item, 'p', available ? 'Available to reserve' : 'Currently reserved', `availability ${available ? 'available' : 'unavailable'}`);
      const reserve = document.createElement('button');
      reserve.type = 'button';
      reserve.className = 'reserve-button';
      reserve.textContent = 'Reserve this artwork';
      reserve.disabled = !available;
      reserve.setAttribute('aria-label', `Reserve ${artwork.title || 'untitled artwork'}`);
      const outcome = addText(item, 'p', '', 'reservation-status');
      outcome.setAttribute('role', 'status');
      outcome.setAttribute('aria-live', 'polite');
      reserve.addEventListener('click', async () => {
        reserve.disabled = true;
        outcome.textContent = 'Submitting reservation…';
        try {
          const response = await fetch('/api/reservations', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
            body: JSON.stringify({ artworkId: artwork.id })
          });
          const data = await response.json();
          if (response.status === 409) {
            outcome.textContent = data.error || 'This artwork was just reserved by someone else.';
            reserve.disabled = true;
            return;
          }
          if (!response.ok) throw new Error(data.error || 'The reservation could not be completed.');
          outcome.textContent = `Reserved until ${new Date(data.expiresAt).toLocaleString()}.`;
          item.querySelector('.availability').textContent = 'Currently reserved';
          item.querySelector('.availability').className = 'availability unavailable';
        } catch (error) {
          outcome.textContent = error instanceof TypeError ? 'Could not connect. Please try again.' : error.message;
          reserve.disabled = false;
        }
      });
      item.appendChild(reserve);
      list.appendChild(item);
    }
  }

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    list.replaceChildren();
    status.textContent = 'Searching artworks…';
    button.disabled = true;
    const params = new URLSearchParams();
    for (const [key, value] of new FormData(form)) {
      if (String(value).trim() !== '') params.set(key, String(value).trim());
    }
    const query = params.toString();
    try {
      const response = await fetch(`/api/artworks/search${query ? `?${query}` : ''}`, { headers: { Accept: 'application/json' } });
      const data = await response.json();
      if (!response.ok) throw new Error(data.error || 'The search could not be completed. Please check your search details.');
      render(data.results || []);
      status.textContent = data.count === 0 ? 'No artworks matched your search. Try different details.' : `${data.count} ${data.count === 1 ? 'artwork' : 'artworks'} found.`;
    } catch (error) {
      list.replaceChildren();
      status.textContent = error instanceof TypeError ? 'We could not connect to the search service. Please try again.' : error.message;
    } finally {
      button.disabled = false;
    }
  });
})();
