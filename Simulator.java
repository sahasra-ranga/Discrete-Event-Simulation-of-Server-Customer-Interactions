import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;

class Simulator {
    private final int servers;
    private final int qMaxCust;
    private final Supplier<Double> supp;
    private final List<Pair<Integer, Double>> arrivals;
    private static final int INDEXNUM = 9;
    private static final double CONSTANT = 1000.0;
    private static final double SMALL = + 0.0000001;

    Simulator(int servers, int qMaxCust, Supplier<Double> supp, int num,
        List<Pair<Integer, Double>> arrivals) {
        this.servers = servers;
        this.qMaxCust = qMaxCust;
        this.supp = supp;
        this.arrivals = arrivals;
    }

    public Pair<String, String> run() {
        PQ<Event> pq = new PQ<>();

        for (Pair<Integer, Double> p : arrivals) {
            pq = pq.add(new ArriveEvent(new Customer(p.t(), p.u()), p.u()));
        }

        State oldState = new State(pq, new Shop(servers, supp, qMaxCust));

        List<State> states = Stream.iterate(
                Optional.of(oldState),
                new java.util.function.Predicate<Optional<State>>() {
                    public boolean test(Optional<State> o) {
                        return o.isPresent();
                    }
                },
                new java.util.function.UnaryOperator<Optional<State>>() {
                    public Optional<State> apply(Optional<State> o) {
                        State curr = o.get(); 
                        Pair<Optional<Event>, PQ<Event>> polled = curr.pq.poll();
                        Optional<Event> event = polled.t();
                        PQ<Event> nextPQ = polled.u();

                        if (event.isEmpty()) {
                            return Optional.empty();
                        }
                        Event e = event.get();
                        String out = e.toString();

                        if (e.isTerminal()) {
                            return Optional.of(curr.with(out, nextPQ, curr.shop));
                        }

                        Pair<Event, Shop> result = e.next(curr.shop).get();
                        PQ<Event> updatedPQ = nextPQ.add(result.t());
                        return Optional.of(curr.with(out, updatedPQ, result.u()));
                    }
                })
            .map(new java.util.function.Function<Optional<State>, State>() {
                public State apply(Optional<State> o) {
                    return o.get();
                }
            })
            .toList();

        List<String> output = states.stream()
            .map(new java.util.function.Function<State, String>() {
                public String apply(State s) {
                    return s.toString();
                }
            })
            .filter(new java.util.function.Predicate<String>() {
                public boolean test(String s) {
                    return !s.isEmpty();
                }
            })
            .toList();

        int served = (int) output.stream()
            .filter(new java.util.function.Predicate<String>() {
                public boolean test(String s) {
                    return s.contains("serves");
                }
            }).count();

        int left = (int) output.stream()
            .filter(new java.util.function.Predicate<String>() {
                public boolean test(String s) {
                    return s.contains("leaves");
                }
            }).count();
        
        double totalWait = output.stream()
            .filter(s -> s.contains("serves"))
            .mapToDouble(s -> {
                double serveTime = Double.parseDouble(s.substring(0, s.indexOf(" ")));

                int custIndex = s.indexOf("customer") + INDEXNUM;
                int endCustId = s.indexOf(" ", custIndex);
                int customerId = Integer.parseInt(s.substring(custIndex, endCustId));
                double arrivalTime = output.stream()
                    .filter(line -> line.contains("arrives") && 
                     line.contains("customer " + customerId))
                    .map(line -> Double.parseDouble(line.substring(0, line.indexOf(" "))))
                    .findFirst()
                    .orElse(0.0);  

                return serveTime - arrivalTime;
            })
            .sum();

        double avgWait = served == 0 ? 0.0 : Math.round((totalWait / served) * 
                                             CONSTANT + SMALL) / CONSTANT;

        String log = String.join("\n", output);
        String stats = String.format("[%.3f %d %d]", avgWait, served, left);

        return new Pair<>(log, stats);
    }
}
