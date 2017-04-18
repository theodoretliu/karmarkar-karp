from sys import argv

if len(argv) != 2:
    exit()

trials = int(argv[1])
pure = []
random = []
hill = []
annealing = []
pp_random = []
pp_hill = []
pp_annealing = []

with open('raw.txt', 'r') as f:
    for i in range(trials):
        pure.append(f.readline())
    for i in range(trials):
        random.append(f.readline())
    for i in range(trials):
        hill.append(f.readline())
    for i in range(trials):
        annealing.append(f.readline())
    for i in range(trials):
        pp_random.append(f.readline())
    for i in range(trials):
        pp_hill.append(f.readline())
    for i in range(trials):
        pp_annealing.append(f.readline())

pure_file = open('pure.txt', 'w')
for i in pure:
    pure_file.write(i)
pure_file.close()

random_file = open('random.txt', 'w')
for i in random:
    random_file.write(i)
random_file.close()

hill_file = open('hill.txt', 'w')
for i in hill:
    hill_file.write(i)
hill_file.close()

annealing_file = open('annealing.txt', 'w')
for i in annealing:
    annealing_file.write(i)
annealing_file.close()

pp_random_file = open('pp_random.txt', 'w')
for i in pp_random:
    pp_random_file.write(i)
pp_random_file.close()

pp_hill_file = open('pp_hill.txt', 'w')
for i in pp_hill:
    pp_hill_file.write(i)
pp_hill_file.close()

pp_annealing_file = open('pp_annealing.txt', 'w')
for i in pp_annealing:
    pp_annealing_file.write(i)
pp_annealing_file.close()
