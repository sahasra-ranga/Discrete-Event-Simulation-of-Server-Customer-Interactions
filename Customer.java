class Customer {
    protected final int identifier;
    protected final double arrivalTime;

    Customer(int identifier, double arrivalTime) {
        this.identifier = identifier;
        this.arrivalTime = arrivalTime;
    } 

    public boolean canBeServed(double time) {
        return time <= this.arrivalTime;
    }

    public double serveTill(double serviceTime) {
        return serviceTime + this.arrivalTime;
    }

    @Override
    public String toString() {
        return "customer " + this.identifier;
    }
}

