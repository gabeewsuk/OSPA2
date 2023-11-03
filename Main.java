import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static int cycles = 0;
    public static int heapSize = 0;
    public static Process[] processHeap; // Custom heap structure

    public static void main(String[] args) {
        processHeap = new Process[100]; // Assuming a maximum of 100 processes
        
        try {
            String filePath = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            int processId = 0;

            while ((line = reader.readLine()) != null) {
                processId++;
                String[] numbers = line.split(" ");
                int arrivalTime = Integer.parseInt(numbers[0]);
                int runningTime = Integer.parseInt(numbers[1]);
                int priority = Integer.parseInt(numbers[2]);

                Process process = new Process(processId, priority, runningTime, arrivalTime);
                addToHeap(process);
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        while (heapSize > 0) {
            Process currentProcess = processHeap[0]; // Get the top process without removing it
            boolean isCompleted = currentProcess.run();
            if (isCompleted) {
                removeTopProcess();
            }
        }
    }

    public static void addToHeap(Process process) {
        // Add the process to the heap based on your criteria (priority and arrival time)
        processHeap[heapSize] = process;
        int current = heapSize;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (compareProcesses(process, processHeap[parent])) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
        heapSize++;
    }

    public static void removeTopProcess() {
        // Remove the top process from the heap and resort
        if (heapSize == 0) {
            return; // Heap is empty
        }
        heapSize--;
        processHeap[0] = processHeap[heapSize];
        int current = 0;
        while (true) {
            int leftChild = 2 * current + 1;
            int rightChild = 2 * current + 2;
            int smallest = current;
            if (leftChild < heapSize && compareProcesses(processHeap[leftChild], processHeap[smallest])) {
                smallest = leftChild;
            }
            if (rightChild < heapSize && compareProcesses(processHeap[rightChild], processHeap[smallest])) {
                smallest = rightChild;
            }
            if (smallest == current) {
                break;
            }
            swap(current, smallest);
            current = smallest;
        }
    }

    public static void swap(int index1, int index2) {
        Process temp = processHeap[index1];
        processHeap[index1] = processHeap[index2];
        processHeap[index2] = temp;
    }

     public static boolean compareProcesses(Process p1, Process p2) {
    if (p1.getPriority() < p2.getPriority() && p1.getArrivalTime() > p2.getArrivalTime()) {
        return false;
    } else if (p1.getPriority() == p2.getPriority() && p1.getArrivalTime() > p2.getArrivalTime()) {
        return false;
    }
    return true;
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
