import java.io.*;
import java.util.*;

/**
 * CS311 Project: Knapsack Problem Solver
 * Implements Greedy and Randomized strategies for Fractional and 0-1 Knapsack problems
 */
public class KnapsackSolver {
    
    // Item class to represent each knapsack item
    static class Item implements Comparable<Item> {
        int id;
        int weight;
        int value;
        double ratio; // value-to-weight ratio
        
        public Item(int id, int weight, int value) {
            this.id = id;
            this.weight = weight;
            this.value = value;
            this.ratio = (weight > 0) ? (double) value / weight : 0;
        }
        
        @Override
        public int compareTo(Item other) {
            return Double.compare(other.ratio, this.ratio); // Descending order
        }
        
        @Override
        public String toString() {
            return String.format("Item[id=%d, w=%d, v=%d, ratio=%.2f]", 
                                id, weight, value, ratio);
        }
    }
    
    // Result class to store algorithm results
    static class Result {
        double totalValue;
        double totalWeight;
        long executionTime; // in nanoseconds
        List<Integer> selectedItems;
        String strategyName;
        
        public Result(String strategyName) {
            this.strategyName = strategyName;
            this.selectedItems = new ArrayList<>();
        }
        
        public double getOptimalityRatio(double optimalValue) {
            return (optimalValue > 0) ? (totalValue / optimalValue) * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("%s: Value=%.2f, Weight=%.2f, Time=%.3f ms",
                                strategyName, totalValue, totalWeight, 
                                executionTime / 1_000_000.0);
        }
    }
    
    // Dataset class to hold problem instance
    static class Dataset {
        int n; // number of items
        int capacity;
        List<Item> items;
        String filename;
        
        public Dataset(String filename) throws IOException {
            this.filename = filename;
            loadData(filename);
        }
        
        private void loadData(String filename) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String[] firstLine = br.readLine().trim().split("\\s+");
            n = Integer.parseInt(firstLine[0]);
            capacity = Integer.parseInt(firstLine[1]);
            
            items = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                String[] line = br.readLine().trim().split("\\s+");
                int weight = Integer.parseInt(line[0]);
                int value = Integer.parseInt(line[1]);
                items.add(new Item(i + 1, weight, value));
            }
            br.close();
        }
        
        public List<Item> getItemsCopy() {
            List<Item> copy = new ArrayList<>();
            for (Item item : items) {
                copy.add(new Item(item.id, item.weight, item.value));
            }
            return copy;
        }
    }
    
    // ==================== FRACTIONAL KNAPSACK ALGORITHMS ====================
    
    /**
     * Greedy Strategy 1: Highest value-to-weight ratio first (Optimal for Fractional)
     */
    public static Result fractionalKnapsackByRatio(Dataset dataset) {
        Result result = new Result("Fractional - Greedy by Ratio");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        // Sort by ratio in descending order
        Collections.sort(items);
        
        double remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (remainingCapacity == 0) break;
            
            if (item.weight <= remainingCapacity) {
                // Take whole item
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            } else {
                // Take fraction of item
                double fraction = remainingCapacity / item.weight;
                result.totalValue += item.value * fraction;
                result.totalWeight += remainingCapacity;
                result.selectedItems.add(item.id);
                remainingCapacity = 0;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    /**
     * Greedy Strategy 2: Highest absolute value first (Fractional)
     */
    public static Result fractionalKnapsackByValue(Dataset dataset) {
        Result result = new Result("Fractional - Greedy by Value");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        // Sort by value in descending order
        items.sort((a, b) -> Integer.compare(b.value, a.value));
        
        double remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (remainingCapacity == 0) break;
            
            if (item.weight <= remainingCapacity) {
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            } else {
                double fraction = remainingCapacity / item.weight;
                result.totalValue += item.value * fraction;
                result.totalWeight += remainingCapacity;
                result.selectedItems.add(item.id);
                remainingCapacity = 0;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    /**
     * Greedy Strategy 3: Lowest weight first (Fractional)
     */
    public static Result fractionalKnapsackByWeight(Dataset dataset) {
        Result result = new Result("Fractional - Greedy by Lowest Weight");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        // Sort by weight in ascending order
        items.sort((a, b) -> Integer.compare(a.weight, b.weight));
        
        double remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (remainingCapacity == 0) break;
            
            if (item.weight <= remainingCapacity) {
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            } else {
                double fraction = remainingCapacity / item.weight;
                result.totalValue += item.value * fraction;
                result.totalWeight += remainingCapacity;
                result.selectedItems.add(item.id);
                remainingCapacity = 0;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    // ==================== 0-1 KNAPSACK ALGORITHMS ====================
    
    /**
     * 0-1 Knapsack - Greedy Strategy 1: Highest value-to-weight ratio first
     */
    public static Result zeroOneKnapsackByRatio(Dataset dataset) {
        Result result = new Result("0-1 Knapsack - Greedy by Ratio");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        Collections.sort(items);
        
        int remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (item.weight <= remainingCapacity) {
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    /**
     * 0-1 Knapsack - Greedy Strategy 2: Highest absolute value first
     */
    public static Result zeroOneKnapsackByValue(Dataset dataset) {
        Result result = new Result("0-1 Knapsack - Greedy by Value");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        items.sort((a, b) -> Integer.compare(b.value, a.value));
        
        int remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (item.weight <= remainingCapacity) {
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    /**
     * 0-1 Knapsack - Greedy Strategy 3: Lowest weight first
     */
    public static Result zeroOneKnapsackByWeight(Dataset dataset) {
        Result result = new Result("0-1 Knapsack - Greedy by Lowest Weight");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        items.sort((a, b) -> Integer.compare(a.weight, b.weight));
        
        int remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (item.weight <= remainingCapacity) {
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    /**
     * Randomized Strategy 1: Random Sampling Approach
     */
    public static Result randomSamplingKnapsack(Dataset dataset, Random rand) {
        Result result = new Result("0-1 Knapsack - Random Sampling");
        List<Item> items = dataset.getItemsCopy();
        
        long startTime = System.nanoTime();
        
        // Shuffle items randomly
        Collections.shuffle(items, rand);
        
        int remainingCapacity = dataset.capacity;
        
        for (Item item : items) {
            if (item.weight <= remainingCapacity) {
                result.totalValue += item.value;
                result.totalWeight += item.weight;
                result.selectedItems.add(item.id);
                remainingCapacity -= item.weight;
            }
        }
        
        result.executionTime = System.nanoTime() - startTime;
        return result;
    }
    
    /**
     * Randomized Strategy 2: Monte Carlo Approximation 1
     * Run multiple trials of random selection, keep the best solution
     */
    public static Result monteCarloKnapsack1(Dataset dataset, int trials) {
        Result bestResult = new Result("0-1 Knapsack - Monte Carlo 1");
        
        long startTime = System.nanoTime();
        Random rand = new Random();
        
        for (int t = 0; t < trials; t++) {
            List<Item> items = dataset.getItemsCopy();
            Collections.shuffle(items, rand);
            
            double trialValue = 0;
            double trialWeight = 0;
            List<Integer> trialItems = new ArrayList<>();
            int remainingCapacity = dataset.capacity;
            
            for (Item item : items) {
                if (item.weight <= remainingCapacity) {
                    trialValue += item.value;
                    trialWeight += item.weight;
                    trialItems.add(item.id);
                    remainingCapacity -= item.weight;
                }
            }
            
            // Keep best solution
            if (trialValue > bestResult.totalValue) {
                bestResult.totalValue = trialValue;
                bestResult.totalWeight = trialWeight;
                bestResult.selectedItems = new ArrayList<>(trialItems);
            }
        }
        
        bestResult.executionTime = System.nanoTime() - startTime;
        return bestResult;
    }
    
    /**
     * Randomized Strategy 3: Monte Carlo Approximation 2
     * Run multiple trials among best value items
     */
    public static Result monteCarloKnapsack2(Dataset dataset, int trials) {
        Result bestResult = new Result("0-1 Knapsack - Monte Carlo 2");
        
        long startTime = System.nanoTime();
        Random rand = new Random();
        
        // Sort by value and take top items
        List<Item> topItems = dataset.getItemsCopy();
        topItems.sort((a, b) -> Integer.compare(b.value, a.value));
        
        // Take top 70% by value
        int topCount = Math.max(1, (int)(topItems.size() * 0.7));
        topItems = topItems.subList(0, topCount);
        
        for (int t = 0; t < trials; t++) {
            List<Item> items = new ArrayList<>(topItems);
            Collections.shuffle(items, rand);
            
            double trialValue = 0;
            double trialWeight = 0;
            List<Integer> trialItems = new ArrayList<>();
            int remainingCapacity = dataset.capacity;
            
            for (Item item : items) {
                if (item.weight <= remainingCapacity) {
                    trialValue += item.value;
                    trialWeight += item.weight;
                    trialItems.add(item.id);
                    remainingCapacity -= item.weight;
                }
            }
            
            if (trialValue > bestResult.totalValue) {
                bestResult.totalValue = trialValue;
                bestResult.totalWeight = trialWeight;
                bestResult.selectedItems = new ArrayList<>(trialItems);
            }
        }
        
        bestResult.executionTime = System.nanoTime() - startTime;
        return bestResult;
    }
    
    // ==================== UTILITY METHODS ====================
    
    public static void printResult(Result result, boolean detailed) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(result.strategyName);
        System.out.println("=".repeat(60));
        System.out.printf("Total Value: %.2f\n", result.totalValue);
        System.out.printf("Total Weight: %.2f\n", result.totalWeight);
        System.out.printf("Execution Time: %.3f ms\n", result.executionTime / 1_000_000.0);
        
        if (detailed) {
            System.out.println("Selected Items: " + result.selectedItems);
        }
    }
    
    public static void runAllAlgorithms(Dataset dataset) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("RUNNING ALL ALGORITHMS ON: " + dataset.filename);
        System.out.println("Dataset: " + dataset.n + " items, Capacity: " + dataset.capacity);
        System.out.println("=".repeat(80));
        
        List<Result> results = new ArrayList<>();
        
        // Fractional Knapsack
        System.out.println("\n>>> FRACTIONAL KNAPSACK ALGORITHMS <<<");
        results.add(fractionalKnapsackByRatio(dataset));
        printResult(results.get(results.size() - 1), false);
        
        results.add(fractionalKnapsackByValue(dataset));
        printResult(results.get(results.size() - 1), false);
        
        results.add(fractionalKnapsackByWeight(dataset));
        printResult(results.get(results.size() - 1), false);
        
        // 0-1 Knapsack Greedy
        System.out.println("\n>>> 0-1 KNAPSACK - GREEDY ALGORITHMS <<<");
        results.add(zeroOneKnapsackByRatio(dataset));
        printResult(results.get(results.size() - 1), false);
        
        results.add(zeroOneKnapsackByValue(dataset));
        printResult(results.get(results.size() - 1), false);
        
        results.add(zeroOneKnapsackByWeight(dataset));
        printResult(results.get(results.size() - 1), false);
        
        // 0-1 Knapsack Randomized
        System.out.println("\n>>> 0-1 KNAPSACK - RANDOMIZED ALGORITHMS <<<");
        results.add(randomSamplingKnapsack(dataset, new Random(42)));
        printResult(results.get(results.size() - 1), false);
        
        results.add(monteCarloKnapsack1(dataset, 1000));
        printResult(results.get(results.size() - 1), false);
        
        results.add(monteCarloKnapsack2(dataset, 1000));
        printResult(results.get(results.size() - 1), false);
        
        // Summary
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SUMMARY - ALL RESULTS");
        System.out.println("=".repeat(80));
        System.out.printf("%-45s %12s %12s %15s\n", 
                         "Algorithm", "Value", "Weight", "Time (ms)");
        System.out.println("-".repeat(80));
        
        for (Result r : results) {
            System.out.printf("%-45s %12.2f %12.2f %15.3f\n", 
                             r.strategyName, r.totalValue, r.totalWeight, 
                             r.executionTime / 1_000_000.0);
        }
    }
    
    // ==================== MAIN MENU ====================
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("CS311 PROJECT: KNAPSACK PROBLEM SOLVER");
            System.out.println("=".repeat(80));
            System.out.println("1. Fractional Knapsack - Greedy by Ratio (Optimal)");
            System.out.println("2. Fractional Knapsack - Greedy by Value");
            System.out.println("3. Fractional Knapsack - Greedy by Lowest Weight");
            System.out.println("4. 0-1 Knapsack - Greedy by Ratio");
            System.out.println("5. 0-1 Knapsack - Greedy by Value");
            System.out.println("6. 0-1 Knapsack - Greedy by Lowest Weight");
            System.out.println("7. 0-1 Knapsack - Random Sampling");
            System.out.println("8. 0-1 Knapsack - Monte Carlo 1 (1000 trials)");
            System.out.println("9. 0-1 Knapsack - Monte Carlo 2 (1000 trials, best items)");
            System.out.println("10. Run ALL Algorithms and Compare");
            System.out.println("0. Exit");
            System.out.println("=".repeat(80));
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (choice == 0) {
                System.out.println("Exiting... Goodbye!");
                break;
            }
            
            System.out.print("\nEnter dataset filename (e.g., data.txt): ");
            String filename = scanner.nextLine();
            
            try {
                Dataset dataset = new Dataset(filename);
                Result result;
                
                switch (choice) {
                    case 1:
                        result = fractionalKnapsackByRatio(dataset);
                        printResult(result, true);
                        break;
                    case 2:
                        result = fractionalKnapsackByValue(dataset);
                        printResult(result, true);
                        break;
                    case 3:
                        result = fractionalKnapsackByWeight(dataset);
                        printResult(result, true);
                        break;
                    case 4:
                        result = zeroOneKnapsackByRatio(dataset);
                        printResult(result, true);
                        break;
                    case 5:
                        result = zeroOneKnapsackByValue(dataset);
                        printResult(result, true);
                        break;
                    case 6:
                        result = zeroOneKnapsackByWeight(dataset);
                        printResult(result, true);
                        break;
                    case 7:
                        result = randomSamplingKnapsack(dataset, new Random());
                        printResult(result, true);
                        break;
                    case 8:
                        result = monteCarloKnapsack1(dataset, 1000);
                        printResult(result, true);
                        break;
                    case 9:
                        result = monteCarloKnapsack2(dataset, 1000);
                        printResult(result, true);
                        break;
                    case 10:
                        runAllAlgorithms(dataset);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
                
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}