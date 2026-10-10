"""
SEA — Seat Exam Allotment
Bulk Seed Data Generator for 500+ Students & Multi-Hall Scale Testing
Matoshri College of Engineering and Research Centre, Eklahare, Nashik
Domain: @matoshriengg.edu.in
"""

import random
import sys

DEPARTMENTS = [
    "Computer Engineering",
    "Artificial Intelligence & Data Science",
    "Mechanical",
    "Civil",
    "Electrical",
    "IT",
    "Electronics & Telecommunication"
]

DEPT_CODE_MAP = {
    "Computer Engineering": "CS",
    "Artificial Intelligence & Data Science": "AI",
    "Mechanical": "ME",
    "Civil": "CE",
    "Electrical": "EE",
    "IT": "IT",
    "Electronics & Telecommunication": "ETC"
}

FIRST_NAMES = [
    "Aarav", "Vivaan", "Aditya", "Vihaan", "Arjun", "Sai", "Reyansh", "Ayaan", "Krishna", "Ishaan",
    "Shaurya", "Atharva", "Advik", "Pranav", "Advaith", "Dhruv", "Kabir", "Rohan", "Ananya", "Diya",
    "Saanvi", "Ira", "Myra", "Aadhya", "Aarohi", "Pari", "Anika", "Navya", "Riya", "Purva",
    "Prashant", "Tanmay", "Gaurav", "Sneha", "Pooja", "Anjali", "Sayali", "Neha", "Priya", "Rahul"
]
LAST_NAMES = [
    "Sharma", "Patel", "Verma", "Rao", "Kumar", "Singh", "Joshi", "Mehta", "Deshmukh", "Patil",
    "Kulkarni", "Shinde", "Bhalerao", "Kadam", "More", "Chaudhari", "Anarthe", "Dukare", "Darwante", "Jadhav"
]

def generate_students(count=520):
    students = []
    # Seed deterministic generator for reproducibility
    random.seed(42)
    for i in range(1, count + 1):
        dept = DEPARTMENTS[(i - 1) % len(DEPARTMENTS)]
        dept_code = DEPT_CODE_MAP[dept]
        roll_no = f"2026{dept_code}{i:03d}"
        first = random.choice(FIRST_NAMES)
        last = random.choice(LAST_NAMES)
        name = f"{first} {last}"
        year = 2
        semester = 4
        email = f"{first.lower()}.{last.lower()}{i}@matoshriengg.edu.in"
        phone = f"98{random.randint(10000000, 99999999)}"
        students.append((i, roll_no, name, dept, year, semester, email, phone))
    return students

def generate_sql_file(filename="students_500_seed.sql", count=520):
    students = generate_students(count)
    with open(filename, "w", encoding="utf-8") as f:
        f.write("-- =======================================================================\n")
        f.write("-- SEA: SEAT EXAM ALLOTMENT SYSTEM\n")
        f.write("-- Matoshri College of Engineering and Research Centre, Eklahare, Nashik\n")
        f.write("-- Auto-generated 500+ Students Seed for Anti-Malpractice Testing\n")
        f.write("-- Domain: @matoshriengg.edu.in | Roll Format: 2026[DEPT][ID]\n")
        f.write("-- =======================================================================\n")
        f.write("USE `sea_db`;\n\n")
        f.write("INSERT INTO `students` (`id`, `roll_no`, `name`, `department`, `year`, `semester`, `email`, `phone`) VALUES\n")
        
        lines = []
        for s in students:
            line = f"({s[0]}, '{s[1]}', '{s[2]}', '{s[3]}', {s[4]}, {s[5]}, '{s[6]}', '{s[7]}')"
            lines.append(line)
        
        f.write(",\n".join(lines))
        f.write(";\n")
    print(f"[SUCCESS] Generated {count} student records in {filename}")

if __name__ == "__main__":
    generate_sql_file()
