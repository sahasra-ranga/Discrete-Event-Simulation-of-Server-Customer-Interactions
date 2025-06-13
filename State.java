import java.util.Optional;

class State {
    protected final PQ<Event> pq;
    protected final Shop shop;
    protected final String str;

    State(PQ<Event> pq, Shop shop) {
        this.pq = pq;
        this.shop = shop;
        this.str = "";
    }

    State(PQ<Event> pq, Shop shop, String str) {
        this.pq = pq;
        this.shop = shop;
        this.str = str;
    }

    public boolean isEmpty() {
        return this.pq.isEmpty() && this.str.isEmpty();
    }

    @Override
    public String toString() {
        return this.str;
    }

    public State with(String log, PQ<Event> newPQ, Shop newShop) {
        return new State(newPQ, newShop, log);
    }

    public Optional<State> next() {
        Pair<Optional<Event>, PQ<Event>> polled = this.pq.poll();
        Optional<Event> curr = polled.t();
        PQ<Event> nextPq = polled.u();

        return curr.flatMap(event -> {
            String newStr = event.toString();
            if (event.isTerminal()) {
                return Optional.of(new State(nextPq, this.shop, newStr));
            }

            Optional<State> result = event.next(this.shop)
                .flatMap(pair -> {
                    Event nextEvent = pair.t();
                    Shop newShop = pair.u();
                    PQ<Event> updatedPq = nextPq.add(nextEvent);
                    return Optional.of(new State(updatedPq, newShop, newStr));
                })
                .or(() -> Optional.empty());

            return result;
        });
    }
}
