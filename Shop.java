import java.util.function.Supplier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.IntStream;

class Shop {
    protected final List<Server> servers;
    protected final Supplier<Double> serviceTime;
    protected final int qLength;

    Shop(int n, Supplier<Double> serviceTime, int qLength) {
        this.servers = IntStream.rangeClosed(1, n)
        .mapToObj(x -> new Server(x, 0.0, qLength, 0, 0.0)).toList();
        this.serviceTime = serviceTime;
        this.qLength = qLength;
    }

    Shop(List<Server> servers, Supplier<Double> serviceTime, int qLength) {
        this.servers = servers;
        this.serviceTime = serviceTime;
        this.qLength = qLength;
    }

    public Optional<Server> findServer(Customer customer) {
        return this.servers.stream()
        .filter(server -> server.getHasCustomer() != Optional.of(true))
        .filter(server -> server.availTill <= customer.arrivalTime)
        .findFirst();
    }

    public Shop update(Server updatedServer) {
        List<Server> newServers = servers.stream()
            .map(s -> (s.identifier == updatedServer.identifier ? updatedServer : s))
            .toList();
        Shop newShop = new Shop(newServers, this.serviceTime, this.qLength);
        return newShop;
    }

    public Shop updateAftServe(Server server) {
        List<Server> updatedServers = this.servers.stream()
            .map(x -> x.identifier == server.identifier
            ? new Server(
                x.identifier,
                x.availTill,
                x.qLength,
                x.qCustomers == 0 ? 0 : x.qCustomers - 1,
                Optional.of(false), x.eventTime)
            : x)
            .toList();
        return new Shop(updatedServers, this.serviceTime, this.qLength);
    }

    public Shop updateToServe(Server server) {
        List<Server> updatedServers = this.servers.stream()
            .map(s -> s.identifier == server.identifier
            ? new Server(
                s.identifier,
                s.availTill,
                s.qLength,
                s.qCustomers + 1,
                Optional.of(true), s.eventTime)
            : s)
            .toList();
        return new Shop(updatedServers, this.serviceTime, this.qLength);
    }

}


