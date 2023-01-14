import difflib
import filecmp
import os
from os.path import isfile, join

from numpy import diff

PATH_TO_SERVER = "Server\\FinalProjectEkt_Server\\src\\"
PATH_TO_CLIENT = "Client\\finalProjectEkt_Client\\src\\"

lines=0
for path, subdirs, files in os.walk(PATH_TO_CLIENT):
    for name in files:
        #print()
        t = os.path.join(path, name)
        #print(t)
        if t[-4:]== 'java':
            #print("FUCK!")
            lines += sum(1 for line in open(t))

for path, subdirs, files in os.walk(PATH_TO_SERVER):
    for name in files:
        #print()
        t = os.path.join(path, name)
        #print(t)
        if t[-4:]== 'java' and "OCSF" not in t:
            #print("FUCK!")
            lines += sum(1 for line in open(t))

print("FUCKING COCK SUCKING TOTAL LOC=",lines)
