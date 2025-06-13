import java.util.Optional;
import java.util.List;
import java.util.stream.Stream;

class Server {
    protected final int identifier;
    protected final double availTill; //availTill means availFrom, messed up phrasing
    protected final int qLength;
    protected final int qCustomers;
    protected final Optional<Boolean> hasCustomer;
    protected final double eventTime;

    Server(int identifier, double availTill, int qLength, int qCustomers, 
           double eventTime) {
        this.identifier = identifier;
        this.availTill = availTill;
        this.qLength = qLength;
        this.hasCustomer = Optional.empty();
        this.qCustomers = qCustomers;
        this.eventTime = eventTime;
    }

    Server(int identifier, double availTill, int qLength, int qCustomers,
           Optional<Boolean> hasCustomer, double eventTime) {
        this.identifier = identifier;
        this.availTill = availTill;
        this.qLength = qLength;
        this.hasCustomer = hasCustomer;
        this.qCustomers = qCustomers;
        this.eventTime = eventTime;
    }

    public Server serve(Customer customer, double time) {
        return new Server(this.identifier, customer.serveTill(time), this.qLength,
                          this.qCustomers, Optional.of(true), this.eventTime);
    }

    public boolean canServe(Customer customer) {
        return this.availTill >= customer.arrivalTime;
    }

    @Override
    public String toString() {
        return "server " + this.identifier;
    }

    public boolean isAvail() {
        return this.qCustomers < this.qLength;
    }

    public Optional<Boolean> getHasCustomer() {
        return this.hasCustomer
            .map(h -> h)
            .or(() -> Optional.of(false));
    }

    public Server updateQ() {
        return new Server(this.identifier, this.availTill, this.qLength,
                          this.qCustomers + 1, this.hasCustomer, this.eventTime);
    }

    public Server mq() {
        if (this.qCustomers >= 1) {
            return new Server(this.identifier, this.availTill, this.qLength,
                          this.qCustomers - 1, this.hasCustomer, this.eventTime);
        } else {
            return this;
        }
    }

    public Server updateEventTime(Customer customer) {
        return new Server(this.identifier, this.availTill, this.qLength,
                          this.qCustomers, this.hasCustomer, customer.arrivalTime);
    }

}
