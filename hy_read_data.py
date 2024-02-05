file = open(r"C:\Users\ganha\PythonProjects\Y3SEM2\SC3020-DB-SYS-PRINCIPLE\SC3020\src\data.tsv", 'r')
record = {}
record["tconst"] = []
record["avgRating"] = []
record["numVotes"] = []

next(file)
for line in file:
    record_ = line.rstrip().split("\t")
    record["tconst"].append(record_[0])
    record["avgRating"].append(float(record_[1]))
    record["numVotes"].append(int(record_[2]))

print(f"Number of records: {len(record['tconst'])}")
print(f"Length of primary key: {len(record['tconst'][0])}")
print(f"Max Average Rating: {max(record['avgRating'])}\tMin Average Rating: {min(record['avgRating'])}")
print(f"Max number of votes: {max(record['numVotes'])}\tMin number of votes: {min(record['numVotes'])}")
file.close()