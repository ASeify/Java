package identifyingtopn;

class Edge {

    Node source, destination;
    float weight, value;
    String id, duration, count;

    // Constructor
    Edge(String id, Node sourceNode, Node destinationNode, float edgeWeight,
            float value, String duration, String count) {
        this.id = id;
        this.source = sourceNode;
        this.destination = destinationNode;
        this.weight = edgeWeight;
        this.value = value;
        this.duration = duration;
        this.count = count;
    }

    // Methods
    public void updateInfo(Node sourceNode, Node destinationNode, float edgeWeight) {
        this.source = sourceNode;
        this.destination = destinationNode;
        this.weight = edgeWeight;
    }

    public void updateSource(Node sourceUpdate) {
        this.source = sourceUpdate;
    }

    public void updateDestination(Node destinationUpdate) {
        this.destination = destinationUpdate;
    }

    public void updateWeight(float weightUpdate) {
        this.weight = weightUpdate;
    }

    public void updateValue(float valueUpdate) {
        this.value = valueUpdate;
    }

    public void updateDuration(String durationUpdate) {
        this.duration = durationUpdate;
    }

    public void updateCount(String countUpdate) {
        this.count = countUpdate;
    }

}
