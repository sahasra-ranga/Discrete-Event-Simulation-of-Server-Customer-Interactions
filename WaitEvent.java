import java.util.Optional;

class WaitEvent extends Event {
    protected final Server server;
    protected final boolean repeat;
    protected final int qPosition;

    WaitEvent(Customer customer, Server server, double eventTime, boolean repeat, int qPosition) {
        super(customer, eventTime);
        this.server = server;
        this.repeat = repeat;
        this.qPosition = qPosition;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
    
    public Optional<Pair<Event, Shop>> next(Shop shop) {
        return shop.servers.stream()
            .filter(s -> s.identifier == this.server.identifier)
            .findFirst()
            .flatMap(currentServer -> {
                if (this.qPosition == 1) {
                    Server updatedServer = new Server(
                        currentServer.identifier,
                        currentServer.availTill, 
                        this.server.qLength,
                        this.server.qCustomers, 
                        Optional.of(true), 
                        currentServer.availTill
                    );

                    Event serveEvent = new ServeEvent(
                        new Customer(super.customer.identifier, currentServer.availTill),
                        updatedServer,
                        currentServer.availTill
                    );

                    return Optional.of(new Pair<>(serveEvent, shop.update(updatedServer)));
                } else {
                    int newQPosition = this.qPosition - 1;
                    Event nextWaitEvent = new WaitEvent(
                        new Customer(super.customer.identifier, currentServer.availTill),
                        currentServer,
                        currentServer.availTill,
                        true,
                        newQPosition
                    );
                    return Optional.of(new Pair<>(nextWaitEvent, shop));
                }
            });
    }
    
    @Override
    public String toString() {
        if (this.repeat == false) {
            return String.format("%.3f", super.eventTime) + " " + super.customer.toString() 
                + " waits at " + server.toString();
        } else {
            return "";
        }
    }

    public String name() {
        return "Wait";
    }
}
