import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static int cycles = 0;
    public static int arrivalTime = 0;
    public static int heapSize = 0;

    public static void main(String[] args) {
        try {
            String filePath = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            Process[] processes = new Process[100];
            int processId = 0;

            while ((line = reader.readLine()) != null) {
                processId++;
                String[] numbers = line.split(" ");
                arrivalTime = Integer.parseInt(numbers[0]);
                int runningTime = Integer.parseInt(numbers[1]);
                int priority = Integer.parseInt(numbers[2]);

                Process process = new Process(processId, priority, runningTime);
                CompletableFuture<Void> processFuture = CompletableFuture.runAsync(() -> process.run());
                line = reader.readLine();
                if (line != null) {
                    arrivalTime = Integer.parseInt(line.split(" ")[0]);
                }

                processes[heapSize] = process;
                heapSize++;
                if (cycles == 0) {
                    heapifyUp(processes, heapSize - 1);
                } else {
                    Process minProcess = extractMin(processes, heapSize);
                    CompletableFuture<Void> minProcessFuture = CompletableFuture.runAsync(() -> minProcess.run());
                    minProcessFuture.get();
                }

                processFuture.get();
            }

            reader.close();

            while (heapSize > 0) {
                Process minProcess = extractMin(processes, heapSize);
                heapSize--;
                CompletableFuture<Void> processFuture = CompletableFuture.runAsync(() -> minProcess.run());
                processFuture.get();
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static void heapifyUp(Process[] processes, int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (processes[index].compareTo(processes[parentIndex]) < 0) {
                swap(processes, index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private static Process extractMin(Process[] processes, int size) {
        if (size <= 0) {
            return null;
        }

        Process min = processes[0];
        processes[0] = processes[size - 1];
        heapifyDown(processes, size - 1, 0);
        return min;
    }

    private static void heapifyDown(Process[] processes, int size, int index) {
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < size && processes[left].compareTo(processes[smallest]) < 0) {
            smallest = left;
        }

        if (right < size && processes[right].compareTo(processes[smallest]) < 0) {
            smallest = right;
        }

        if (smallest != index) {
            swap(processes, index, smallest);
            heapifyDown(processes, size, smallest);
        }
    }

    private static void swap(Process[] processes, int i, int j) {
        Process temp = processes[i];
        processes[i] = processes[j];
        processes[j] = temp;
    }
}

class Process implements Comparable<Process> {
    private int processId;
    private int priority;
    private int runningTime;
    private int cyclesRun;

    public Process(int processId, int priority, int runningTime) {
        this.processId = processId;
        this.priority = priority;
        this.runningTime = runningTime;
        this.cyclesRun = 0;
    }

    public void run() {
        Main.cycles++;
        while (cyclesRun < runningTime) {
            cyclesRun++;
            Main.cycles++;
            System.out.println("On cycle: " + Main.cycles + " the process " + processId + " ran for " + cyclesRun + " time(s)" + " priority " + priority);
            //System.out.println("main.arrival " + Main.arrivalTime+ "   main.cycles "+Main.cycles);
            if (Main.arrivalTime == Main.cycles) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (runningTime == cyclesRun) {
            System.out.println("Process ID: " + processId + " has completed.");
        }
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.priority, other.priority);
    }
}
