import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static int cycles = 0;
    public static List<Process> processHeap = new ArrayList<>();
    public static List<Process> pendingProcesses = new ArrayList<>(); // Processes with non-zero arrival times

    public static void main(String[] args) {
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

                if (arrivalTime == 0) {
                    // Process with arrival time zero is added directly to the heap
                    addToHeap(process);
                } else {
                    // Processes with non-zero arrival times are stored in a separate list
                    pendingProcesses.add(process);
                }
            }

            reader.close();

            // Main loop to simulate processes
            while (!processHeap.isEmpty() || !pendingProcesses.isEmpty()) {
                // Check if there are pending processes with arrival time zero
                for (Process pendingProcess : new ArrayList<>(pendingProcesses)) {
                    if (pendingProcess.getArrivalTime() == 0) {
                        addToHeap(pendingProcess);
                        pendingProcesses.remove(pendingProcess);
                    }
                }

                // Decrement arrival times for pending processes
                for (Process pendingProcess : new ArrayList<>(pendingProcesses)) {
                    pendingProcess.decrementArrivalTime();
                }

                // Extract and process the top priority process
                if (!processHeap.isEmpty()) {
                    Process minPriorityProcess = processHeap.get(0);
                    boolean processComplete = minPriorityProcess.run();
                    if (processComplete) {
                        extractMinPriorityProcess();
                    }
                    System.out.println("Process ID: " + minPriorityProcess.getProcessId() + " Priority: " + minPriorityProcess.getPriority());
                }

                // Decrement arrival times for processes in the heap
                for (Process heapProcess : new ArrayList<>(processHeap)) {
                    heapProcess.decrementArrivalTime();
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static void addToHeap(Process process) {
        processHeap.add(process);
        heapify();
    }

    public static Process extractMinPriorityProcess() {
        Process minProcess = processHeap.remove(0);
        heapify();
        return minProcess;
    }

    public static void heapify() {
        int size = processHeap.size();
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i, size);
        }
    }

    public static void heapifyDown(int index, int size) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int smallest = index;

        if (left < size && processHeap.get(left).getPriority() < processHeap.get(smallest).getPriority()) {
            smallest = left;
        }

        if (right < size && processHeap.get(right).getPriority() < processHeap.get(smallest).getPriority()) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest, size);
        }
    }

    public static void swap(int i, int j) {
        Process temp = processHeap.get(i);
        processHeap.set(i, processHeap.get(j));
        processHeap.set(j, temp);
    }

    // Rest of your code remains the same.

    public static class Process {
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

        public void decrementArrivalTime() {
            arrivalTime--;
        }

        public boolean run() {
            cycles++;
            cyclesRun++;
            System.out.println("On cycle: " + cycles + " the process " + processId + " ran for " + cyclesRun + " time(s)" + " priority " + priority);
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
}
