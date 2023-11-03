import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static int cycles = 0;
    public static int heapSize = 0;
    public static Process[] processHeap; // Custom heap structure

    public static void main(String[] args) {
        // ...

        try {
            String filePath = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            int processId = 0;

            // Initialize the heap array
            processHeap = new Process[100]; // Assuming a maximum of 100 processes

            while ((line = reader.readLine()) != null) {
                processId++;
                String[] numbers = line.split(" ");
                int arrivalTime = Integer.parseInt(numbers[0]);
                int runningTime = Integer.parseInt(numbers[1]);
                int priority = Integer.parseInt(numbers[2]);

                Process process = new Process(processId, priority, runningTime, arrivalTime);
                processHeap[heapSize] = process;

                // Increase the heap size
                heapSize++;
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        // Iterate through the heap and add processes with arrival time zero to the heap

        // Extract processes from the heap to get them sorted by priority
        while (heapSize > 0) {
            for (int i = 0; i < heapSize; i++) {
            if (processHeap[i].getArrivalTime() == 0) {
                addToHeap(processHeap[i]);
            }
        }
            Process minPriorityProcess = findMinPriorityProcess();
            boolean processComplete = minPriorityProcess.run();
            if (processComplete){
                extractMinPriorityProcess();
            }
            System.out.println("Process ID: " + minPriorityProcess.getProcessId() + " Priority: " + minPriorityProcess.getPriority());
        }
    }

    public static void addToHeap(Process process) {
        int current = heapSize;
        processHeap[current] = process;
        while (current > 0 && processHeap[current].getPriority() < processHeap[(current - 1) / 2].getPriority()) {
            swap(current, (current - 1) / 2);
            current = (current - 1) / 2;
        }
    }

    public static Process extractMinPriorityProcess() {
        Process minProcess = processHeap[0];
        processHeap[0] = processHeap[heapSize - 1];
        heapSize--;
        heapify(0);
        return minProcess;
    }
    public static Process findMinPriorityProcess() {
    if (heapSize == 0) {
        return null; // Heap is empty
    }
    return processHeap[0];
}

    public static void heapify(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int smallest = index;

        if (left < heapSize && processHeap[left].getPriority() < processHeap[smallest].getPriority()) {
            smallest = left;
        }

        if (right < heapSize && processHeap[right].getPriority() < processHeap[smallest].getPriority()) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapify(smallest);
        }
    }

    public static void swap(int i, int j) {
        Process temp = processHeap[i];
        processHeap[i] = processHeap[j];
        processHeap[j] = temp;
    }
}

class Process {
    private int processId;
    private int priority;
    private int runningTime;
    private int cyclesRun;
    private int arrivalTime;

    public Process(int processId, int priority, int runningTime, int arrivalTime) {
        this.processId = processId;
        this.priority = priority;
        this.runningTime = runningTime;
        this.cyclesRun = 0;
        this.arrivalTime = arrivalTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessId() {
        return processId;
    }

    public boolean run() {
        Main.cycles++;
        cyclesRun++;
        System.out.println("On cycle: " + Main.cycles + " the process " + processId + " ran for " + cyclesRun + " time(s)" + " priority " + priority);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (cyclesRun == runningTime) {
            System.out.println("Process ID: " + processId + " has completed.");
            return true;
        }
        return false;
    }
}
