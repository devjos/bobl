from PlanFit import runAlgorithm

computeSets = [
    # day, ctime,cstop,cline
    [0, 1, 3, 1]#,
#    [0, 1, 3, 1],
#    [0, 1, 4, 1],
#    [0, 1, 5, 1],
#    [0, 1, 6, 1],
#    [0, 1, 3, 2],
#    [0, 1, 6, 3],
#    [0, 1, 6, 4],

#    [0, 2, 2, 1],
#    [0, 2, 3, 1],
#    [0, 2, 4, 1],
#    [0, 2, 5, 1],
#    [0, 2, 6, 1],

#    [0, 2, 2, 1],
#    [0, 2, 3, 2],

#    [0, 3, 2, 1],
#    [0, 3, 3, 1],
#    [0, 3, 4, 1],
#    [0, 3, 5, 1],
#    [0, 3, 6, 1],

#    [0, 3, 2, 3],
#    [0, 3, 3, 3]
]

for x in computeSets:
    runAlgorithm(*x)
