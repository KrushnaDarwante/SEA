"""
SEA — Seating Algorithm Stress Test and Anti-Malpractice Verification Benchmark
Simulates and verifies 500+ seat allocations in sub-second time with 0% adjacency collisions.
"""

import time
import random
from collections import defaultdict

def benchmark_seating(total_students=540, hall_capacity=30):
    departments = ["CS", "IT", "AI", "MECH", "CIVIL", "EE"]
    students = []
    
    for i in range(1, total_students + 1):
        dept = departments[(i - 1) % len(departments)]
        students.append({
            "roll": f"2024{dept}{i:03d}",
            "dept": dept,
            "name": f"Student {i}"
        })
    
    start_time = time.time()

    # 1. Group by department
    by_dept = defaultdict(list)
    for s in students:
        by_dept[s["dept"]].append(s)
    
    for d in by_dept:
        random.shuffle(by_dept[d])

    # 2. 2D Anti-Malpractice Grid Allocation with Serpentine & Row Shifting
    num_halls = (total_students + hall_capacity - 1) // hall_capacity
    allocations = []
    rows, cols = 5, 6
    num_depts = len(departments)
    dept_keys = list(by_dept.keys())
    
    violations = 0

    for h in range(1, num_halls + 1):
        hall_name = f"Hall-{h}"
        grid = {}
        for r in range(1, rows + 1):
            # Serpentine offset prevents vertical collisions even when cols == num_depts
            row_offset = (r - 1) * 1 + (h - 1)
            for c in range(1, cols + 1):
                dept_idx = (row_offset + (c - 1)) % num_depts
                target_dept = dept_keys[dept_idx]

                # Pop student from target_dept, or fallback to any available
                if by_dept[target_dept]:
                    student = by_dept[target_dept].pop()
                else:
                    # Find any non-empty department
                    found = False
                    for fallback in dept_keys:
                        if by_dept[fallback]:
                            student = by_dept[fallback].pop()
                            found = True
                            break
                    if not found:
                        break

                # Check horizontal neighbor
                if c > 1 and grid.get((r, c - 1)) == student["dept"]:
                    violations += 1
                
                # Check vertical neighbor
                if r > 1 and grid.get((r - 1, c)) == student["dept"]:
                    violations += 1

                grid[(r, c)] = student["dept"]
                allocations.append({
                    "hall": hall_name,
                    "seat": f"R{r}-C{c}",
                    "roll": student["roll"],
                    "dept": student["dept"]
                })

    duration_ms = (time.time() - start_time) * 1000

    print("=" * 60)
    print("SEA ANTI-MALPRACTICE SEATING BENCHMARK RESULTS")
    print("=" * 60)
    print(f"Total Students Allotted : {len(allocations)}")
    print(f"Total Halls Utilized    : {num_halls}")
    print(f"Execution Latency       : {duration_ms:.2f} ms")
    print(f"Direct Violations (Adj) : {violations}")
    print(f"Anti-Malpractice Success: {((len(allocations) - violations) / len(allocations)) * 100:.2f}%")
    print("=" * 60)
    return duration_ms < 1000 and violations == 0

if __name__ == "__main__":
    benchmark_seating()
