# Comparative-Implementation-and-Analysis-of-Greedy-and-Randomized-Knapsack-Algorithms
This project implements and analyzes multiple algorithmic strategies for solving the Fractional Knapsack and 0-1 Knapsack problems. The main objective is to compare Greedy and Randomized approaches in terms of efficiency, execution time, and solution quality across different dataset sizes.
# 🧠 CS311 Project — Comparative Implementation and Analysis of Greedy and Randomized Knapsack Algorithms

## 📘 Overview
This project implements and compares **Greedy** and **Randomized** strategies for solving the **Fractional** and **0-1 Knapsack** problems in Java.  
It aims to analyze algorithm performance based on **execution time**, **solution quality**, and **efficiency** across multiple input sizes.

---

## 🚨 Problem Definition

### 🧩 1. Fractional Knapsack Problem
- Given `n` items, each with weight `wi` and value `vi`, and a knapsack with capacity `W`.
- Items **can be divided** into fractions.
- Objective: Maximize total value without exceeding capacity.

### 🎒 2. 0-1 Knapsack Problem
- Items **cannot be divided**; each item is either included (1) or excluded (0).
- Objective: Maximize total value while staying within weight limit.

---

## 🎯 Project Objectives
1. Implement multiple **Greedy** and **Randomized** algorithms for the Knapsack problem.  
2. Evaluate their performance using synthetic datasets (small, medium, large).  
3. Measure execution time, optimality ratio, and performance trends.  
4. Produce a final report with analysis and comparisons.

---

## ⚙️ Implementation Requirements

### 🧮 A. Greedy Strategies
1. **Strategy 1:** Select items with the highest **value-to-weight ratio** first.  
2. **Strategy 2:** Select items with the highest **absolute value** first.  
3. **Strategy 3:** Select items with the **lowest value** first.

### 🎲 B. Randomized Strategies (0-1 Knapsack)
1. **Random Sampling Approach** – Randomly select items until the weight limit is reached.  
2. **Monte Carlo Approximation 1** – Run multiple random trials, keeping the best result.  
3. **Monte Carlo Approximation 2** – Run random trials focused on high-value items.

---

## 🧱 Project Structure

