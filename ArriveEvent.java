import java.util.Optional;

class ArriveEvent extends Event {

    ArriveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
    
    public Optional<Pair<Event, Shop>> next(Shop shop) {
        return shop.findServer(super.customer)
            .flatMap(server -> Optional.of(new Pair<Event, Shop>(
                new ServeEvent(super.customer, server, super.eventTime),
                shop
            )))
            .or(() -> shop.servers.stream()
                .filter(server -> server.isAvail())
                .findFirst()
                .<Pair<Event, Shop>>map(server -> new Pair<Event, Shop>(
                    new WaitEvent(super.customer, server.updateQ().updateEventTime(super.customer),
                                  super.eventTime, false, server.qCustomers + 1),
                    shop.updateToServe(server.updateEventTime(super.customer)))
            )
            .or(() -> Optional.of(new Pair<Event, Shop>(
                new LeaveEvent(super.customer, super.eventTime),
                shop
            ))));
    }

    @Override
    public String toString() {
        return String.format("%.3f",super.eventTime) + " " + super.customer.toString() + " arrives";
    }

    public String name() {
        return "Arrive";
    }

}
