import java.util.Optional;

class ServeEvent extends Event {
    private final Server server;

    ServeEvent(Customer customer, Server server, double eventTime) {
        super(customer, eventTime);
        this.server = server;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
    
    public Optional<Pair<Event, Shop>> next(Shop shop) {
        double serviceTime = shop.serviceTime.get();
        Server updatedServer = server.serve(super.customer, serviceTime)
                                     .updateEventTime(super.customer)
                                     .mq();

        Server newServer = new Server(updatedServer.identifier, updatedServer.availTill,
                                      updatedServer.qLength, updatedServer.qCustomers,
                                      updatedServer.getHasCustomer(), updatedServer.eventTime);
        Shop newShop = shop.update(updatedServer);
        return Optional.of(new Pair<Event, Shop>(new DoneEvent(super.customer, 
                           updatedServer.availTill), newShop));
    }

    @Override
    public String toString() {
        return String.format("%.3f",super.eventTime) + " " + super.customer
            .toString() + " serves by " + this.server
                .toString();
    }

    public String name() {
        return "Serve";
    }
}
