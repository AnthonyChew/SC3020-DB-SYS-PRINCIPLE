
def getData():
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

def checkRangedData():
    bruteforcepath = "./BruteForce.txt"
    bplustreepath = "./BPlusTree.txt"
    bf = open(bruteforcepath, 'r')
    bpt = open(bplustreepath, 'r')
    bfTconst = []
    bptTconst = []
    for line in bf:
        bfTconst.append(line.rstrip())
    
    for line in bpt:
        bptTconst.append(line.rstrip())
    
    bfTconst.sort()
    bptTconst.sort()
    if bfTconst == bptTconst:
        print("Same.")
    else:
        print("Not same.")

    bf.close()
    bpt.close()

if __name__ == "__main__":
    checkRangedData()