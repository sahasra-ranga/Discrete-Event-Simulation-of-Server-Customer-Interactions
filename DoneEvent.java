import java.util.Optional;

class DoneEvent extends Event {

    DoneEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    public Optional<Pair<Event,Shop>> next(Shop shop) {
        return Optional.of(new Pair<>(new DoneEvent(super.customer, super.eventTime), shop));
    }

    @Override
    public String toString() {
        return String.format("%.3f",super.eventTime) + " " + super.customer.toString() + " done";
    }

    public String name() {
        return "Done";
    }
}

